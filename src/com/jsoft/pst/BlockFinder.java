package com.jsoft.pst;

/**	The BlockFinder class retrieves blocks from the block B-tree by walking it on disk rather than reading it in all at once.
*
*	@see	com.jsoft.pst.BlockBTree
*/
class BlockFinder extends PagedBTreeFinder implements BlockMap {

	/**	The BTreePage class holds information about paged (block & node) B-trees. */
	private class BTreePage extends PagedBTreeFinder.BTreePage {

		/**	Create a BTreePage object form the given pstFile and bref.
		*
		*	@param	bref	The block reference for this page.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*/
		private BTreePage(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(bref, pstFile);
		}

		/**	{@inheritDoc} */
		protected PagedBTree.PageContext<BTree, BTreeLeaf> contextFactory(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			return new BlockBTree.BBTContext(bref, pstFile);
		}

		/**	{@inheritDoc} */
		protected BTreeLeaf leafNodeFactory(final PagedBTree.PageContext<BTree, BTreeLeaf> context, java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new BBTEntry(entryStream, context);
		}
	}

	/**	Construct a BlockFinder object.
	*
	*	@param	pstFile	The PST file data stream, header, etc.
	*/
	BlockFinder(PSTFile pstFile)
	{
		super(pstFile);
	}

	/**	{@inheritDoc} */
	protected PagedBTreeFinder.BTreePage bTreePageFactory(BREF bref)
	throws
		java.io.IOException
	{
		return new BTreePage(bref, pstFile);
	}

	/**	Return the requested block, or null if the block was not found.
	*
	*	@param	bid	The block ID of the block to look for.
	*
	*	@return	The Block B-tree leaf entry for the requested block ID, or null if the block ID was not found.
	*/
	public BBTEntry find(final BID bid)
	throws
		java.io.IOException
	{
		return (BBTEntry)super.find(bid, pstFile.header.bbtRoot);
	}

	/**	Test this class by reading in the block B-Tree and looking for the blocks in it.
	*
	*	@param	args	The command line arguments to the test application.
	*/
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava com.jsoft.pst.BlockBTree pst-file");
			System.exit(1);
		}

		try {
			final java.util.logging.Level logLevel = args.length >= 2 ? Debug.getLogLevel(args[1]) : java.util.logging.Level.OFF;
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("com.jsoft.pst.BTree");
			logger.setLevel(logLevel);

			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(args[0]));

			final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
			BlockFinder bf = new BlockFinder(pstFile);

			int discrepancies = 0;
			int bids = 0;
			java.util.Iterator<BTreeNode> iterator = bbt.iterator();
			while (iterator.hasNext()) {
				++bids;
				final BBTEntry treeEntry = (BBTEntry)iterator.next();
				final BBTEntry findEntry = bf.find(treeEntry.bref.bid);
				if (treeEntry.key() != findEntry.key())
					++discrepancies;
			}
			if (discrepancies == 0)
				System.out.printf("Success: all %d BIDs found\n", bids);
			else
				System.out.printf("Failure: %d out of %d BIDs not found\n", discrepancies, bids);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
}