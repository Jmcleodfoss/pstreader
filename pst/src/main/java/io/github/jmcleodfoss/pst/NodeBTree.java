package io.github.jmcleodfoss.pst;

/**	The NodeBTree class contains the B-tree of the PST file's nodes.
*	@see	io.github.jmcleodfoss.pst.NBTEntry
*	@see	io.github.jmcleodfoss.pst.BlockBTree
*	@see	io.github.jmcleodfoss.pst.PagedBTree.BTEntry
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/e4efaad0-1876-446e-9d34-bb921588f924">MS-PST Section 1.3.1.1: Node Database (NDB) Layer</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/7d759bcb-7864-480c-8746-f6af913ab085">MS-PST Section 2.2.2.7.7: BTrees</a>
*/
class NodeBTree extends PagedBTree implements NodeMap
{
	/**	The base class of the context used for node B-Tree construction. */
	protected abstract static class NBTContextBase<I, L> extends PagedBTree.PageContext<BTree, BTreeLeaf>
	{
		/**	Constructor trivially forwards parameters to the base class' constructor.
		*	@param	bref	The block reference for the node B-tree node being built.
		*	@param	pstFile	The PST data stream, header, etc.
		*	@throws	java.io.IOException	The data for the context information could not be read.
		*/
		NBTContextBase(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(bref, pstFile);
		}
	}

	/**	The context for construction of the node B-tree. This holds information required to build the B-tree but which doesn't
	*	need to be saved in it.
	*/
	static class NBTContext extends NBTContextBase<BTree, BTreeLeaf>
	{
		/**	The constructor trivially forwards parameters to the base class' constructor.
		*	@param	bref	The block reference for the node B-tree node being built.
		*	@param	pstFile	The PST data stream, header, etc.
		*	@throws	java.io.IOException	The data for the context information could not be read.
		*/
		NBTContext(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(bref, pstFile);
		}

		/**	Create an intermediate node B-tree entry using data read in from the input stream.
		*	@param	entryStream	The data stream from which to read the intermediate node data.
		*	@return	A node B-tree containing all childrend of this intermediate node.
		*/
		@Override
		protected NodeBTree intermediateNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			final BTEntry entry = new BTEntry(this, entryStream);
			return new NodeBTree(entry.key, entry.bref, pstFile);
		}
	
		/**	Create a leaf block B-tree entry using data read in from the input stream.
		*	@param	entryStream	The data stream from which to read the leaf node data.
		*	@return	A node leaf structure containing information about a PST node.
		*/
		@Override
		protected NBTEntry leafNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new NBTEntry(entryStream, this);
		}
	}

	/**	Create a node B-tree node. This constructor is called when constructing non-leaf nodes, and creates a context from
	*	scratch.
	*	@param	key	The key for this node. All child node keys are greater than or equal to this key.
	*	@param	bref	The block reference indicating where to read this node B-tree node from.
	*	@param	pstFile	The PST file's data stream, header, etc.
	*	@throws	java.io.IOException	The data for the node B-tree could not be read.
	*/
	NodeBTree(final long key, final BREF bref, PSTFile pstFile)
	throws
		java.io.IOException
	{
		super(key, bref, new NBTContext(bref, pstFile));
	}

	/**	Create a node B-Tree using the given context rather than creating one.
	*	@param	key	The key for this node. All child node keys are greater than or equal to this key.
	*	@param	bref	The block reference indicating where to read this node B-tree node from.
	*	@param	context	The context from which to read the node B-tree.
	*	@throws	java.io.IOException	The data for the node B-tree could not be read.
	*/
	protected NodeBTree(final long key, final BREF bref, NBTContextBase<BTree, BTreeLeaf> context)
	throws
		java.io.IOException
	{
		super(key, bref, context);
	}

	/**	This is a convenience wrapper function to find a node ID in the node B-tree.
	*	@param	nid	The node ID to find.
	*	@return	The node B-tree entry which has the requested node id as its key, or null if no node contains this key.
	*/
	public NBTEntry find(final NID nid)
	{
		return (NBTEntry)super.find(nid.key());
	}

	/**	{@inheritDoc} */
	public String getNodeText()
	{
		return String.format("NID 0x%08x", key);
	}

	/**	Test this class by reading in the node B-Tree and printing it.
	*	@param	args	The file(s) to display the B-Trees for.
	*/
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.NodeBTree pst-file [pst-file ...]");
			System.exit(1);
		}

		for (String a: args) {
			try {
				PSTFile pstFile = new PSTFile(new java.io.FileInputStream(a));

				final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

				System.out.println("Node B-tree\n___________");
				java.util.Iterator<BTreeNode> iterator = nbt.iterator();
				while (iterator.hasNext()) {
					final NBTEntry entry = (NBTEntry)iterator.next();
					System.out.println(entry);
				}
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				e.printStackTrace(System.out);
			}
		}
	}
}
