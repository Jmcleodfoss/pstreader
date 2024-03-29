package io.github.jmcleodfoss.pst;

/**	The BlockFinder class retrieves blocks from the block B-tree by walking it on disk rather than reading it in all at once.
*	@see	io.github.jmcleodfoss.pst.BlockBTree
*/
class BlockFinder extends PagedBTreeFinder implements BlockMap
{
	/**	The BTreePage class holds information about paged (block &amp; node) B-trees. */
	private class BTreePage extends PagedBTreeFinder.BTreePage
	{
		/**	Create a BTreePage object form the given pstFile and bref.
		*	@param	bref	The block reference for this page.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*	@throws	java.io.IOException	An I/O error was encoutered while reading in the B-tree.
		*/
		private BTreePage(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(bref, pstFile);
		}

		/**	{@inheritDoc} */
		@Override
		protected PagedBTree.PageContext<BTree, BTreeLeaf> contextFactory(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			return new BlockBTree.BBTContext(bref, pstFile);
		}

		/**	{@inheritDoc} */
		@Override
		protected BTreeLeaf leafNodeFactory(final PagedBTree.PageContext<BTree, BTreeLeaf> context, java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new BBTEntry(entryStream, context);
		}
	}

	/**	Construct a BlockFinder object.
	*	@param	pstFile	The PST file data stream, header, etc.
	*/
	BlockFinder(PSTFile pstFile)
	{
		super(pstFile);
	}

	/**	{@inheritDoc} */
	@Override
	protected PagedBTreeFinder.BTreePage bTreePageFactory(BREF bref)
	throws
		java.io.IOException
	{
		return new BTreePage(bref, pstFile);
	}

	private boolean compareTree(PagedBTree that)
	{
		return compareTree(pstFile.header.bbtRoot, that, that);
	}

	/**	Return the requested block, or null if the block was not found.
	*	@param	bid	The block ID of the block to look for.
	*	@return	The Block B-tree leaf entry for the requested block ID, or null if the block ID was not found.
	*/
	@Override
	public BBTEntry find(final BID bid)
	throws
		java.io.IOException
	{
		return (BBTEntry)super.find(bid, pstFile.header.bbtRoot);
	}

	/**	{@inheritDoc} */
	@Override
	public String getNodeText(final Object value)
	{
		if (value instanceof PagedBTree.BTEntry)
			return String.format("BID 0x%08x", ((PagedBTree.BTEntry)value).key());

		if (value instanceof BTreeLeaf) {
			BBTEntry bbtEntry = (BBTEntry)value;
			return String.format("%s - %d bytes", bbtEntry.bref.toString(), bbtEntry.numBytes);
		}

		try {
			PagedBTreeFinder.BTreePage pbt = bTreePageFactory(pstFile.header.bbtRoot);
			if (pbt.children[0] instanceof PagedBTree.BTEntry)
				return String.format("BID 0x%08x", ((PagedBTree.BTEntry)pbt.children[0]).key());
			else
				return String.format("BID 0x%08x", ((BBTEntry)pbt.children[0]).key());
		} catch (java.io.IOException e) {
			return "Uknown NID (IOException)";
		}
	}

	/**	Get the root of the tree.
	*	@return	The root of the block B-Tree.
	*/
	@Override
	public Object getRoot()
	{
		try {
			return bTreePageFactory(pstFile.header.bbtRoot);
		} catch (java.io.IOException e) {
			return null;
		}
	}

	/**	Test this class by reading in the block B-Tree and looking for the blocks in it.
	*	@param	args	The pst files to run the test on.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.BlockBTree pst-file [pst-file ...]");
			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {
				java.io.FileInputStream stream = new java.io.FileInputStream(a);
				final PSTFile pstFile = new PSTFile(stream);
				try {
					final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
					final BlockFinder bf = new BlockFinder(pstFile);

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
						System.out.printf("Success: all %d BIDs found%n", bids);
					else
						System.out.printf("Failure: %d out of %d BIDs not found%n", discrepancies, bids);

					if (bf.compareTree(bbt))
						System.out.printf("Success: BlockFinder TreeModel matches BlockBTree TreeModel");
					else
						System.out.printf("Failure: BlockFinder TreeModel does not match BlockBTree TreeModel");
				} finally {
					try {
						pstFile.close();
					} catch (final java.io.IOException e) {
						System.out.printf("There was a problem closing file %s%n", a);
					}
				}
			} catch (final CRCMismatchException e) {
				System.out.printf("File %s is corrupt (Calculated CRC does not match expected value)%n", a);
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
