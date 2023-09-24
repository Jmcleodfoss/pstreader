package io.github.jmcleodfoss.pst;

/**	The PagedBTreeFinder class contains code shared by the {@link io.github.jmcleodfoss.pst.BlockFinder} and {@link io.github.jmcleodfoss.pst.NodeFinder}
*	classes.
*/
abstract class PagedBTreeFinder extends ReadOnlyTreeModel implements TreeCustomNodeText
{
	/**	The underlying PST file data stream, header, etc. */
	protected PSTFile pstFile;

	/**	The BTreePage class holds information about paged (block &amp; node) B-trees. */
	protected abstract class BTreePage
	{
		/**	The children (BTEntry or BBTEntry/NBTEntry) of this BTreePage */
		final protected Object[] children;

		/**	Create a BTreePage object form the given pstFile and bref.
		*	@param	bref	The block reference for this page.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*	@throws	java.io.IOException	An I/O error was encountered when reading the data for the B-tree page.
		*/
		protected BTreePage(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			final PagedBTree.PageContext<BTree, BTreeLeaf> context = contextFactory(bref, pstFile);
			java.nio.ByteBuffer entryStream = context.entryDataStream();

			final int numEntries = context.getNumEntries();
			Object[] children;
			final boolean fLeaf = context.isLeafNode();

			if (fLeaf)
				children = new BTreeLeaf[numEntries];
			else
				children = new PagedBTree.BTEntry[numEntries];

			for (int i = 0; i < numEntries; ++i) {
				int entrySize;
				if (fLeaf) {
					BTreeLeaf entry = leafNodeFactory(context, entryStream);
					children[i] = entry;
					entrySize = entry.actualSize(context);
				}
				else {
					PagedBTree.BTEntry entry = new PagedBTree.BTEntry(context, entryStream);
					children[i] = entry;
					entrySize = PagedBTree.BTEntry.actualSize(context);
				}
				final int skip = context.getEntrySize() - entrySize;
				if (skip > 0)
					entryStream.position(entryStream.position() + skip);
			}
			this.children = children;
		}

		/**	Create a B-tree context to use when creating child nodes.
		*	@param	bref	The block reference of the current node of the B-tree.
		*	@param	pstFile	The PST file data stream, header, etc.
		*	@return	A context suitable for constructing intermediate and leaf nodes.
		*	@throws	java.io.IOException	An I/O error was encountered while reading in the B-tree page context.
		*/
		protected abstract PagedBTree.PageContext<BTree, BTreeLeaf> contextFactory(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException;

		/**	Create a leaf node record.
		*	@param	context		The B-tree context under which this node should be created.
		*	@param	entryStream	The data stream from which to read the node record.
		*	@return	A leaf node for this B-tree.
		*	@throws	java.io.IOException	An I/O error was encountered while reading in the B-tree node.
		*/
		protected abstract BTreeLeaf leafNodeFactory(final PagedBTree.PageContext<BTree, BTreeLeaf> context, java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException;
	}

	/**	Construct a BlockFinder object.
	*	@param	pstFile	The PST file data stream, header, etc.
	*/
	protected PagedBTreeFinder(PSTFile pstFile)
	{
		this.pstFile = pstFile;
	}

	/**	Return a BTreePage to read the next child level of the B-tree.
	*	@param	bref	The block reference of the B-tree page block to start searching in.
	*	@return	The BTreePage for this block reference.
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the B-tree page.
	*/
	protected abstract BTreePage bTreePageFactory(BREF bref)
	throws
		java.io.IOException;

	/**	Compare the TreeModel from this PagedBTreeFinder object to that from the PagedBTree object.
	*	@param	bref	The BREF of the node on this PagedBTreeFinder object's tree to check
	*	@param	thatTree	The TreeModel for the PagedBTree object to compare this PagedBTreeFinder's TreeModel with
	*	@param	thatParent	The node on thePagedBTree that corresponds to bref on the PagedBTreeFinder object.
	*	@return	true if the trees are equivalent, false if they differ.
	*/
	protected boolean compareTree(BREF bref, PagedBTree thatTree, Object thatParent)
	{
		try {
			BTreePage btp = bTreePageFactory(bref);

			int thisChildCount = this.getChildCount(btp);
			int thatChildCount = thatTree.getChildCount(thatParent);
			if (thisChildCount != thatChildCount) {
				System.out.printf("%s: getChildCount this %d that %d%n", toString(), thisChildCount, thatChildCount);
				return false;
			}

			for (int i = 0; i < thisChildCount; ++i) {
				Object thisChild = getChild(btp, i);
				Object thatChild = thatTree.getChild(thatParent, i);

				boolean thisChildIsLeaf = isLeaf(thisChild);
				boolean thatChildIsLeaf = thatTree.isLeaf(thatChild);

				if (thisChildIsLeaf != thatChildIsLeaf) {
					System.out.printf("%s: isLeaf child %d this %b that %b%n", toString(), i, thisChildIsLeaf, thatChildIsLeaf);
					return false;
				}

				long thisKey = thisChildIsLeaf ? ((BTreeLeaf)thisChild).key() : ((PagedBTree.BTEntry)thisChild).key();
				long thatKey = thisChildIsLeaf ? ((BTreeLeaf)thatChild).key() : ((PagedBTree)thatChild).key();

				if (thisKey != thatKey) {
					System.out.printf("%s: child %d this 0x%08x that 0x%08x%n", toString(), i, thisKey, thatKey);
					return false;
				}

				if (!thisChildIsLeaf && !compareTree(((PagedBTree.BTEntry)thisChild).bref, thatTree, (PagedBTree)thatChild)) {
					System.out.printf("%s: child %d this %s that %s%n", this.toString(), i, thisChild.toString(), thatChild.toString());
					return false;
				}
			}
			return true;
		} catch (java.io.IOException e) {
			System.out.println(e);
			e.printStackTrace(System.out);
			return false;
		}
	}

	/**	Return the requested block, or null if the block was not found.
	*	@param	keyedItem	The block ID of the block to look for.
	*	@param	bref		The block reference of the B-Tree page block to start searching in.
	*	@return	The Block B-tree leaf entry for the requested block ID, or null if the block ID was not found.
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the node.
	*/
	protected BTreeLeaf find(final NodeKey keyedItem, final BREF bref)
	throws
		java.io.IOException
	{
		final BTreePage btp = bTreePageFactory(bref);
		if (btp.children.length == 0)
			return null;
		if (btp.children[0] instanceof BTreeLeaf) {
			for (Object o : btp.children) {
				final BTreeLeaf entry = (BTreeLeaf)o;
				if (keyedItem.key() == entry.key())
					return entry;
			}
			return null;
		}

		for (int i = 0; i < btp.children.length; ++i) {
			final PagedBTree.BTEntry entry = (PagedBTree.BTEntry)btp.children[i];
			if (keyedItem.key() < entry.key())
				continue;
			if (keyedItem.key() >= entry.key() && (i == btp.children.length-1 || keyedItem.key() < ((PagedBTree.BTEntry)btp.children[i+1]).key()))
				return find(keyedItem, entry.bref);
		}

		return null;
	}

	/**	Get the BTreePage (i.e. context) for the given node.
	*	@param	node	The node to retrieve the BTreePage for
	*	@return	The BTreePage for this object.
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the B-tree page.
	*/
	BTreePage getBTreePageForNode(final Object node)
	throws
		java.io.IOException
	{
		if (node instanceof PagedBTree.BTEntry)
			return bTreePageFactory(((PagedBTree.BTEntry)node).bref);

		if (node instanceof BTreePage)
			return (BTreePage)node;

		return null;
	}

	/**	Obtain the given child of this node.
	*	@param	parent	The parent node to return the child of.
	*	@param	index	The number of the child to return.
	*	@return	The requested child node of the given parent node.
	*/
	@Override
	public Object getChild(final Object parent, final int index)
	{
		try {
			BTreePage btp = getBTreePageForNode(parent);
			return btp.children[index];
		} catch(java.io.IOException e) {
			return null;
		}
	}

	/**	Get the number of children of this node.
	*	@param	parent	The parent node to return the number of child nodes for.
	*	@return	The number of children of the given parent node.
	*/
	@Override
	public int getChildCount(final Object parent)
	{
		if (parent instanceof BTreeLeaf)
			return 0;

		try {
			BTreePage btp = getBTreePageForNode(parent);
			return btp.children.length;
		} catch(java.io.IOException e) {
			return 0;
		}
	}

	/**	Get the index of this child node in the given node.
	*	@param	parent	The parent to search for child.
	*	@param	child	The child node to look for in parent.
	*	@return	The index of the given child in the given parent node, or -1 if it is not a child of parent.
	*/
	@Override
	public int getIndexOfChild(final Object parent, final Object child)
	{
		if (parent == null || child == null)
			return -1;
		try {
			BTreePage btp = getBTreePageForNode(parent);
			for (int i = 0; i < btp.children.length; ++i) {
				if (btp.children[i] instanceof BTreeLeaf) {
					if ((BTreeLeaf)btp.children[i].key() == ((BTreeLeaf)child).key())
						return i;
				} else {
					if ((PagedBTree.BTEntry)btp.children[i].bref == ((PagedBTree.BTEntry)child).bref)
						return i;
				}
			}
			return -1;
		} catch (java.io.IOException e) {
			return -1;
		}
	}

	/**	Is the given node a leaf node?
	*	@param	node	The node to check for leafiness.
	*	@return	true if node is a leaf node, false if it is an intermediate node.
	*/
	@Override
	public boolean isLeaf(final Object node)
	{
		return node instanceof BTreeLeaf;
	}
}
