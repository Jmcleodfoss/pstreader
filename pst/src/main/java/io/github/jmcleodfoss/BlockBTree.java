package io.github.jmcleodfoss.pst;

/**	The BlockBTree class holds the PST file's block B-tree structure, which provides access to all the data blocks in the PST file.
*
*	@see	io.github.jmcleodfoss.pst.BBTEntry
*	@see	io.github.jmcleodfoss.pst.NodeBTree
*	@see	io.github.jmcleodfoss.pst.PagedBTree.BTEntry
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 1.3.1.1"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff387767(v=office.12).aspx">Node Database (NDB) Layer (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.2.2.7.7"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386554(v=office.12).aspx">BTrees (MSDN)</a>
*/
class BlockBTree extends PagedBTree implements BlockMap {

	/**	The BBTContext class provides context for block B-Tree construction. This information is not kept in the tree, but
	*	passed through in turn to each BlockBTree child constructor call.
	*/
	static class BBTContext extends PagedBTree.PageContext<BTree, BTreeLeaf> {

		/**	Constructor the context for building the block B-tree.
		*
		*	@param	bref	The block reference for the current node of the block B-tree under construction.
		*	@param	pstFile	The PST file's input stream, header, etc.
		*/
		BBTContext(final BREF bref, final PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(bref, pstFile);
		}

		/**	Create an intermediate block B-tree entry using data read in from the input stream.
		*
		*	@param	entryStream	The data stream from which to read the intermediate block data information.
		*
		*	@return	A BlockBTree object representing the intermediate block B-tree node.
		*/
		@Override
		protected BlockBTree intermediateNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			BTEntry entry = new BTEntry(this, entryStream);
			return new BlockBTree(entry.key, entry.bref, pstFile);
		}
	
		/**	Create a leaf block B-tree entry using data read in from the input stream.
		*
		*	@param	entryStream	The data stream from which to read the intermediate block data information.
		*/
		@Override
		protected BBTEntry leafNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new BBTEntry(entryStream, this);
		}
	}

	/**	Create a block B-tree node. This constructor is called when constructing both root and leaf nodes.
	*
	*	@param	key	The key of this node. The keys of all children of this node will be greater than or equal to this value.
	*	@param	bref	The block reference from which to read the data for this node.
	*	@param	pstFile	The PST file header, input stream, etc.
	*
	*/
	BlockBTree(final long key, final BREF bref, PSTFile pstFile)
	throws
		java.io.IOException
	{
		super(key, bref, new BBTContext(bref, pstFile));
	}

	/**	Convenience wrapper function to find a block ID in the block B-tree.
	*
	*	@param	bid	The block ID to find in the block B-tree.
	*
	*	@return	The leaf block B-tree entry indicating where the given block lies.
	*/
	public BBTEntry find(final BID bid)
	{
		return (BBTEntry)super.find(bid.key());
	}

	/**	{@inheritDoc} */
	public String getNodeText()
	{
		return String.format("BID 0x%08x", key);
	}

	/**	Test this class by reading in the block B-Tree and printing it.
	*
	*	@param	args	The command line arguments to the test application.
	*/
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.BlockBTree pst-file");
			System.exit(1);
		}

		try {
			final java.util.logging.Level logLevel = args.length >= 2 ? Debug.getLogLevel(args[1]) : java.util.logging.Level.OFF;
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.github.jmcleodfoss.pst.BTree");
			logger.setLevel(logLevel);

			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(args[0]));

			final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);

			System.out.println("Block B-tree\n____________");

			java.util.Iterator<BTreeNode> iterator = bbt.iterator();
			while (iterator.hasNext())
				System.out.println((BBTEntry)iterator.next());
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
