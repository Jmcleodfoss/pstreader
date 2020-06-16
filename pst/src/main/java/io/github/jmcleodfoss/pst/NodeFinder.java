package io.github.jmcleodfoss.pst;

/**	The NodeFinder class retrieves blocks from the block B-tree by walking it on disk rather than reading it in all at once.
*	@see	io.github.jmcleodfoss.pst.NodeBTree
*/
class NodeFinder extends PagedBTreeFinder implements NodeMap
{
	/**	The BTreePage class holds information about paged (block &amp; node) B-trees. */
	private class BTreePage extends PagedBTreeFinder.BTreePage
	{
		/**	Create a BTreePage object form the given pstFile and bref.
		*	@param	bref	The block reference for this page.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*	@throws	java.io.IOException	THe data for the B-tree page could not be read.
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
			return new NodeBTree.NBTContext(bref, pstFile);
		}

		/**	{@inheritDoc} */
		protected BTreeLeaf leafNodeFactory(final PagedBTree.PageContext<BTree, BTreeLeaf> context, java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new NBTEntry(entryStream, context);
		}
	}

	/**	Construct a NodeFinder object.
	*	@param	pstFile	The PST file data stream, header, etc.
	*/
	NodeFinder(PSTFile pstFile)
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
	*	@param	nid	The node ID of the node to look for.
	*	@return	The node B-tree leaf entry for the requested block ID, or null if the block ID was not found.
	*/
	public NBTEntry find(final NID nid)
	throws
		java.io.IOException
	{
		return (NBTEntry)super.find(nid, pstFile.header.nbtRoot);
	}

	/**	Test this class by reading in the block B-Tree and looking for the blocks in it it.
	*	@param	args	The command line arguments to the test application.
	*/
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.NodeBTree pst-file");
			System.exit(1);
		}

		try {
			final java.util.logging.Level logLevel = args.length >= 2 ? Debug.getLogLevel(args[1]) : java.util.logging.Level.OFF;
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.github.jmcleodfoss.pst.BTree");
			logger.setLevel(logLevel);

			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(args[0]));

			final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);
			NodeFinder nf = new NodeFinder(pstFile);

			int discrepancies = 0;
			int nids = 0;
			java.util.Iterator<BTreeNode> iterator = nbt.iterator();
			while (iterator.hasNext()) {
				++nids;
				final NBTEntry treeEntry = (NBTEntry)iterator.next();
				final NBTEntry findEntry = nf.find(treeEntry.nid);
				if (treeEntry.key() != findEntry.key())
					++discrepancies;
			}
			if (discrepancies == 0)
				System.out.printf("Success: all %d NIDs found%n", nids);
			else
				System.out.printf("Failure: %d out of %d NIDs not found%n", discrepancies, nids);
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
