package io.github.jmcleodfoss.pst;

/**	The BTree class is the base for the block, node, heap-on-node, and sub-node B-tree classes. */
abstract class BTree extends ReadOnlyTreeModel implements BTreeNode, TreeCustomNodeText {

	/**	Logger for debugging BTree-derived classes */
	protected static java.util.logging.Logger logger = Debug.getLogger("io.github.jmcleodfoss.pst.BTree");

	/**	The Context class allows information about the current root node to be passed to child nodes without having to save
	*	it in the BTree itself. */
	protected static abstract class Context<I extends BTree, L extends BTreeLeaf> {

		/**	The data container holding the information read in. */
		protected DataContainer dc;

		/**	Hold the stream, FileChannel, and header information necessary for reading the B-tree. */
		protected PSTFile pstFile;

		/**	The base class constructor reads any metadata requested and saves the input stream and file format.
		*
		*	@param	pstFile	The PST {@link Header}, data stream, etc.
		*	@param	fields	The data fields to read in.
		*
		*	@throws java.io.IOException	There was a problem reading the PST file.
		*/
		protected Context(PSTFile pstFile, final DataDefinition[]... fields)
		throws
			java.io.IOException
		{
			dc = new DataContainer();
			for (DataDefinition[] f : fields)
				dc.read(pstFile.mbb, f);
	
			this.pstFile = pstFile;
		}

		/**	Obtain a data stream from which entries may be read.
		*
		*	@return	A ByteBuffer object from which leaf and node entries may be read.
		*
		*	@throws java.io.IOException	There was a problem reading the PST file.
		*/
		protected abstract java.nio.ByteBuffer entryDataStream()
		throws
			java.io.IOException;

		/**	Determine whether the current node is a leaf node or an intermediate node.
		*
		*	@return	true if the current node is a leaf node, false if it is an intermediate node.
		*/
		protected abstract boolean isLeafNode();

		/**	Obtain the size of an entry and ignored padding bytes in the input stream.
		*
		*	@return	The size of an entry in type of B-Tree
		*/
		protected abstract int getEntrySize();

		/**	Obtain the number of entries which can be read from the input stream.
		*
		*	@return	The number of entries available in the input entry stream.
		*/
		protected abstract int getNumEntries();

		/**	Create an intermediate node from the data stream.
		*
		*	@param	byteBuffer	The data stream from which to read the intermediate node information.
		*
		*	@return	A B-tree object containing the entry and all its children.
		*/
		protected abstract I intermediateNodeFactory(java.nio.ByteBuffer byteBuffer)
		throws
			java.io.IOException;

		/**	Create a leaf node from the data stream.
		*
		*	@param	byteBuffer	The data stream from which to read the leaf node information.
		*
		*	@return	A leaf node object containing the leaf data.
		*/
		protected abstract L leafNodeFactory(java.nio.ByteBuffer byteBuffer)
		throws
			java.io.IOException;
	
		/**	Convenience method to provide file format.
		*
		*	@return	true if the PST file being processed is a Unicode PST file, false if it is ANSE.
		*/
		protected boolean unicode()
		{
			return pstFile.unicode();
		}
	}

	/**	The Iterator class allows iteration through the leaves of the B-tree.
	*/
	private class Iterator implements java.util.Iterator<BTreeNode> {

		/**	The index of the next child to return the leaf of. */
		private int nextChild;

		/**	The iterator for the current child node's leaves. */
		private Iterator childIterator;

		/**	Construct a B-tree iterator object. */
		Iterator()
		{
			nextChild = 0;
			childIterator = nextChild < children.length && children[nextChild] instanceof BTree ? ((BTree)children[nextChild++]).iterator() : null;
		}

		/**	Indicate whether the "next" function will return anything.
		*
		*	@return	true if there is another leaf to return, false otherwise.
		*/
		public boolean hasNext()
		{
			if (childIterator != null && childIterator.hasNext())
				return true;

			if (nextChild < children.length) {
				if (children[nextChild] instanceof BTree)
					return ((BTree)children[nextChild]).children.length > 0;
				else
					return true;
			}

			return false;
		}

		/**	Provide the next leaf of the B-tree.
		*
		*	@return	The next leaf of the B-tree.
		*/
		public BTreeNode next()
		{
			if (childIterator != null && childIterator.hasNext())
				return childIterator.next();

			if (nextChild < children.length) {
				if (children[nextChild] instanceof BTree) {
					do {
						childIterator = ((BTree)children[nextChild++]).iterator();
					} while (!childIterator.hasNext() && nextChild <children.length);
					return childIterator.next();
				} else {
					return children[nextChild++];
				}
			}

			throw new java.util.NoSuchElementException();
		}

		/**	The remove function is not supported.
		*/
		public void remove()
		{
			throw new UnsupportedOperationException("remove not suported");
		}
	}

	/**	The children of this B-tree node. The children may be either intermediate nodes, which are themselves B-trees, or leaf
	*	nodes. */
	protected final BTreeNode[] children;

	/**	The key of this node is guaranteed to be the smallest key of any of its children. */
	protected final long key;

	/**	Create a B-tree using the given context.
	*
	*	@param	key	The key for this node of the B-tree. All child nodes are guaranteed to have keys greater than or equal
	*			to this.
	*	@param	context	Context data used to construct the B-tree
	*/
	protected BTree(final long key, Context<BTree, BTreeLeaf> context)
	throws
		java.io.IOException
	{
		final int numEntries = context.getNumEntries();
		final int entryWidth = context.getEntrySize();
		children = new BTreeNode[numEntries];
		if (numEntries > 0) {
			java.nio.ByteBuffer byteBuffer = context.entryDataStream();
			for (int i = 0; i < numEntries; ++i) {
				if (context.isLeafNode())
					children[i] = context.leafNodeFactory(byteBuffer);
				else
					children[i] = context.intermediateNodeFactory(byteBuffer);
	
				final int skip = entryWidth - children[i].actualSize(context);
				if (skip > 0)
					byteBuffer.position(byteBuffer.position() + skip);
			}
	
			this.key = key == 0L && children != null ? children[0].key() : key;
		} else {
			this.key = key;
		}
	}

	/**	Retrieve the data the given key in the B-tree, or null if the key is missing.
	*
	*	@param	key	The key to look for in the B-tree.
	*
	*	@return	The B-tree leaf node containing key, if any, otherwise, null.
	*/
	BTreeLeaf find(final long key)
	{
		if (key < this.key())
			return null;

		for (int i = 0; i < children.length; ++i) {
			if (children[i] instanceof BTree) {
				if (key >= children[i].key() && (i == children.length-1 || key < children[i+1].key()))
					return ((BTree)children[i]).find(key);
			} else {
				if (key == children[i].key())
					return (BTreeLeaf)children[i];
			}
		}
		return null;
	}

	/**	Obtain the given child of this node.
	*
	*	@param	parent	The parent node to return the child of.
	*	@param	index	The number of the child to return.
	*
	*	@return	The requested child node of the given parent node.
	*/
	public Object getChild(final Object parent, final int index)
	{
		return ((BTree)parent).children[index];
	}

	/**	Get the number of children of this node.
	*
	*	@param	parent	The parent node to return the number of child nodes for.
	*
	*	@return	The number of children of the given parent node.
	*/
	public int getChildCount(final Object parent)
	{
		if (parent instanceof BTree) {
			final BTree bTree = (BTree)parent;
			if (bTree.children != null)
				return bTree.children.length;
		}
		return 0;

	}

	/**	Get the index of this child node in the given node.
	*
	*	@param	parent	The parent to search for child.
	*	@param	child	The child node to look for in parent.
	*
	*	@return	The index of the given child in the given parent node, or -1 if it is not a child of parent.
	*/
	public int getIndexOfChild(final Object parent, final Object child)
	{
		if (parent == null || child == null)
			return -1;
		final BTree bTree = (BTree)parent;
		for (int i = 0; i < bTree.children.length; ++i) {
			if (bTree.children[i].equals(child))
				return i;
		}
		return -1;
	}

	/**	{@inheritDoc} */
	public javax.swing.table.TableModel getNodeTableModel()
	{
		final Object[] columnHeadings = {"", ""};
		final Object[][] cells = {
			new Object[]{"Key", key()},
		};

		return new ReadOnlyTableModel(cells, columnHeadings);
	}

	/**	{@inheritDoc} */
	public String getNodeText(final Object value)
	{
		return ((BTreeNode)value).getNodeText();
	}

	/**	Get the root of the tree.
	*
	*	@return	The root of this B-tree.
	*/
	public Object getRoot()
	{
		return this;
	}

	/**	Is the given node a leaf node?
	*
	*	@param	node	The node to check for leafiness.
	*
	*	@return	true if node is a leaf node, false if it is an intermediate node.
	*/
	public boolean isLeaf(final Object node)
	{
		if (node instanceof BTree)
			return ((BTree)node).children == null;
		return true;
	}

	/**	Provide an iterator over the leaves of the B-tree.
	*
	*	@return	An iterator over the leaves of the B-tree.
	*/
	public Iterator iterator()
	{
		return new Iterator();
	}

	/**	{@inheritDoc} */
	public long key()
	{
		return key;
	}

	/**	Obtain the number of leaf nodes in the B-tree.
	*
	*	@return	The number of leaf nodes in this B-tree.
	*/
	int numLeafNodes()
	{
		int i = 0;
		java.util.Iterator<BTreeNode> iterator = iterator();
		while (iterator.hasNext()){
			++i;
			iterator.next();
		}

		return i;
	}

	/**	Output this B-tree to the given stream.
	*	Each printed line begins with the String prefix, followed by a number of tabs which increases by one for each level
	*	This function is necessary because there can occasionally be too much information in a B-tree to use toString.
	*
	*	@param	out	The PrintStream object to write the tree to.
	*	@param	prefix	The prefix to use when printing this node.
	*/
	@Deprecated
	public void outputString(java.io.PrintStream out, final StringBuilder prefix)
	{
		out.println(prefix.toString() + this);
		prefix.append("\t");

		if (children == null)
			return;

		for (BTreeNode c : children)
			if (c instanceof BTree)
				((BTree)c).outputString(out, prefix);
			else
				out.println(prefix.toString() + c);
	}

	/**	{@inheritDoc} */
	public java.nio.ByteBuffer rawData(final BlockMap bbt, final PSTFile pstFile)
	throws
		java.io.IOException
	{
		return null;
	}

	/**	Provide a String which contains some information about this node. This is typically used for debugging.
	*
	*	@return	A string describing this B-tree.
	*/
	@Override
	public String toString()
	{
		int numChildren = children != null ? children.length : 0;
		return String.format("key 0x%08x, %d child%s", key, numChildren, numChildren == 1 ? "" : "ren");
	}
}
