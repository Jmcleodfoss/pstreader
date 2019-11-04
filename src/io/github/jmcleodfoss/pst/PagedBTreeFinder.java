package io.github.jmcleodfoss.pst;

/**	The PagedBTreeFinder class contains code shared by the {@link io.github.jmcleodfoss.pst.BlockFinder} and {@link io.github.jmcleodfoss.pst.NodeFinder}
*	classes.
*/
abstract class PagedBTreeFinder {

	/**	The BTreePage class holds information about paged (block & node) B-trees. */
	protected abstract class BTreePage {

		/**	The children (BTEntry or BBTEntry/NBTEntry) of this BTreePage */
		private Object[] children;

		/**	Create a BTreePage object form the given pstFile and bref.
		*
		*	@param	bref	The block reference for this page.
		*	@param	pstFile	The PST file's data stream, header, etc.
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
		*
		*	@param	bref	The block reference of the current node of the B-tree.
		*	@param	pstFile	The PST file data stream, header, etc.
		*
		*	@return	A context suitable for constructing intermediate and leaf nodes.
		*/
		protected abstract PagedBTree.PageContext<BTree, BTreeLeaf> contextFactory(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException;

		/**	Create a leaf node record.
		*
		*	@param	context		The B-tree context under which this node should be created.
		*	@param	entryStream	The data stream from which to read the node record.
		*
		*	@param	A leaf node for this B-tree.
		*/
		protected abstract BTreeLeaf leafNodeFactory(final PagedBTree.PageContext<BTree, BTreeLeaf> context, java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException;
	}

	/**	The underlying PST file data stream, header, etc. */
	protected PSTFile pstFile;

	/**	Construct a BlockFinder object.
	*
	*	@param	pstFile	The PST file data stream, header, etc.
	*/
	protected PagedBTreeFinder(PSTFile pstFile)
	{
		this.pstFile = pstFile;
	}

	/**	Return the requested block, or null if the block was not found.
	*
	*	@param	bid	The block ID of the block to look for.
	*	@param	bref	The block reference of the B-Tree page block to start searching in.
	*
	*	@return	The Block B-tree leaf entry for the requested block ID, or null if the block ID was not found.
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

	/**	Return a BTreePage to read the next child level of the B-tree.
	*
	*	@param	bref	The block reference of the B-tree page block to start searching in.
	*
	*	@return	The BTreePage for this block reference.
	*/
	protected abstract BTreePage bTreePageFactory(BREF bref)
	throws
		java.io.IOException;
}
