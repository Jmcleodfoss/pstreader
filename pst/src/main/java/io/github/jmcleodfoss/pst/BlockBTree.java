package io.github.jmcleodfoss.pst;

/**	The BlockBTree class holds the PST file's block B-tree structure, which provides access to all the data blocks in the PST file.
*	@see	io.github.jmcleodfoss.pst.BBTEntry
*	@see	io.github.jmcleodfoss.pst.NodeBTree
*	@see	io.github.jmcleodfoss.pst.PagedBTree.BTEntry
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/e4efaad0-1876-446e-9d34-bb921588f924">MS-PST Section 1.3.1.1: Node Database (NDB) Layer</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/7d759bcb-7864-480c-8746-f6af913ab085">MS-PST Section 2.2.2.7.7: BTrees</a>
*/
class BlockBTree extends PagedBTree implements BlockMap
{
	/**	The BBTContext class provides context for block B-Tree construction. This information is not kept in the tree, but
	*	passed through in turn to each BlockBTree child constructor call.
	*/
	static class BBTContext extends PagedBTree.PageContext<BTree, BTreeLeaf>
	{
		/**	Constructor the context for building the block B-tree.
		*	@param	bref	The block reference for the current node of the block B-tree under construction.
		*	@param	pstFile	The PST file's input stream, header, etc.
		*	@throws	java.io.IOException	An I/O error was encoutered while reading in the block B-tree context.
		*/
		BBTContext(final BREF bref, final PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(bref, pstFile);
		}

		/**	Create an intermediate block B-tree entry using data read in from the input stream.
		*	@param	entryStream	The data stream from which to read the intermediate block data information.
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
		*	@param	entryStream	The data stream from which to read the intermediate block data information.
		*	@throws	java.io.IOException	An I/O error was encountered while reading in the node.
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
	*	@param	key	The key of this node. The keys of all children of this node will be greater than or equal to this value.
	*	@param	bref	The block reference from which to read the data for this node.
	*	@param	pstFile	The PST file header, input stream, etc.
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the block B-tree.
	*
	*/
	BlockBTree(final long key, final BREF bref, PSTFile pstFile)
	throws
		java.io.IOException
	{
		super(key, bref, new BBTContext(bref, pstFile));
	}

	/**	Convenience wrapper function to find a block ID in the block B-tree.
	*	@param	bid	The block ID to find in the block B-tree.
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
	*	@param	args	The pst files to display the block B-Tree of.
	*/
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.BlockBTree pst-file [pst-file ...]");
			System.exit(1);
		}

		for (String a: args) {
			try {
				final PSTFile pstFile = new PSTFile(new java.io.FileInputStream(a));
				final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);

				System.out.println(a);
				System.out.println("Block B-tree\n____________");

				java.util.Iterator<BTreeNode> iterator = bbt.iterator();
				while (iterator.hasNext())
					System.out.println((BBTEntry)iterator.next());
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
