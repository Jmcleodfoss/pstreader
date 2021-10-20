package io.github.jmcleodfoss.pst;

/**	The XBlock class represents the XBlock and XXBlock block containers within a PST file.
*	@see	io.github.jmcleodfoss.pst.BlockBase
*	@see	io.github.jmcleodfoss.pst.SimpleBlock
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/8f34ce81-7a04-4a31-ba48-e05543daa77f">MS-PST Section 2.2.2.8.3: Block Types</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/d0e6fbaf-00e3-4d4d-bea8-8ab3cdb4fde6">MS-PST Section 2.2.2.8.3.1: Data Blocks</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/45688317-46fb-4038-9ed3-b845d80bdabb">MS-PST Section 2.2.2.8.3.2: Data Tree</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/5b7a6935-e83d-4917-9f62-6ce3707f09e0">MS-PST Section 2.2.2.8.3.2.1: XBLOCK</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/061b6ac4-d1da-468c-b75d-0303a0a8f468">MS-PST Section 2.2.2.8.3.2.2: XXBLOCK</a>
*/
class XBlock extends BlockBase
{
	private static final String nm_btype = "btype";
	private static final String nm_cLevel = "cLevel";
	private static final String nm_cEnt = "cEnt";
	private static final String nm_lcbTotal = "lcbTotal";

	/**	Descriptions of the fields which make up the XBLOCK and XXBLOCK information. */
	private static final DataDefinition[] data_fields = {
		new DataDefinition(nm_btype, DataType.integer8Reader, true),
		new DataDefinition(nm_cLevel, DataType.integer8Reader, true),
		new DataDefinition(nm_cEnt, DataType.integer16Reader, true),
		new DataDefinition(nm_lcbTotal, DataType.integer32Reader, true)
	};

	/**	The BIDs of the sub-blocks. This is saved for {link #toString} only. */
	private final BID[] bid;

	/**	The total amount of data in all sub-blocks */
	private final int dataBytes;

	/**	The sub-blocks in this multi-block structure. */
	final java.util.Vector<SimpleBlock> blockList;

	/**	The intermediate blocks (preserved allow the block B-Tree's internal structure to be shown. @see GetInternalDataTableModel). */
	final java.util.Vector<XBlock> xblockList;

	/**	The block type (preserved allow the block B-Tree's internal structure to be shown. @see GetInternalDataTableModel). */
	final byte type;

	/**	The block type name (preserved allow the block B-Tree's internal structure to be shown. @see GetInternalDataTableModel). */
	final String blockType;

	/**	The level (preserved allow the block B-Tree's internal structure to be shown. @see GetInternalDataTableModel). */
	final byte level;

	/**	The number of child entries of this node (preserved allow the block B-Tree's internal structure to be shown. @see GetInternalDataTableModel). */
	final int numEntries;

	/** The total count of all the bytes of external data (preserved allow the block B-Tree's internal structure to be shown. @see GetInternalDataTableModel). */
	final int lcbTotal;

	/**	An iterator which returns a ByteBuffer view of the underlying data block. */
	private class Iterator implements java.util.Iterator<java.nio.ByteBuffer>
	{
		/**	The underlying iterator - this class is just a thin wrapper around it to return the SimpleBlock's data as a
		*	ByteBuffer rather than the SimpleBlock itself.
		*/
		private java.util.Iterator<SimpleBlock> blockIterator;

		/**	Construct an iterator from the XBlock blockList vector. */
		Iterator()
		{
			blockIterator = blockList.iterator();
		}

		/**	Is there another value to return?
		*	@return	true if there is another SimpleBlock in the list, false otherwise.
		*/
		public boolean hasNext()
		{
			return blockIterator.hasNext();
		}

		/**	Retrieve the next value.
		*	@return	The data for the next SimpleBlock in the list, as a ByteBuffer.
		*/
		public java.nio.ByteBuffer next()
		{
			return PSTFile.makeByteBuffer(blockIterator.next().data());
		}

		/**	The remove function is not supported by the XBlock iterator. */
		public void remove()
		{
			throw new UnsupportedOperationException("remove not suported");
		}
	}

	/**	Create an XBlock/XXBlock from the given block B-tree entry.
	*	@param	entry	The block B-tree entry describing the root of this XBLOCK/XXBLOCK tree structure.
	*	@param	bbt	The PST file's block B-tree (required to find the child blocks).
	*	@param	pstFile	The PST file's input stream, etc.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	java.io.IOException	An I/O exception was encountered when reading the XBLOCK / XXBLOCK data.
	*/
	XBlock(final BBTEntry entry, final BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		java.io.IOException
	{
		java.nio.ByteBuffer byteBuffer = pstFile.getByteBuffer(entry.bref, blockSize(entry.numBytes, pstFile.header.fileFormat));
		byteBuffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);

		DataContainer dc = new DataContainer();
		dc.read(byteBuffer, data_fields);

		type = (Byte)dc.get(nm_btype);
		if (type != 0x01)
			throw new BadXBlockTypeException(type);

		level = (Byte)dc.get(nm_cLevel);
		if (level != 1 & level != 2)
			throw new BadXBlockLevelException(level);

		numEntries = (Short)dc.get(nm_cEnt);
		lcbTotal = (Integer)dc.get(nm_lcbTotal);

		DataDefinition bidField = new DataDefinition(nm_data, DataType.BIDFactory(pstFile.unicode()), true);
		BID[] bid = new BID[numEntries];
		for (int i = 0; i < numEntries; ++i) {
			dc.read(byteBuffer, bidField);
			bid[i] = (BID)dc.get(nm_data);
		}
		this.bid = bid;

		final int blockSize = blockSize(entry.numBytes, pstFile.header.fileFormat);
		final int bytesToSkip = blockSize-entry.numBytes - BlockTrailer.size(pstFile.header.fileFormat);
		byteBuffer.position(byteBuffer.position() + bytesToSkip);

		new BlockTrailer(byteBuffer, pstFile.header.fileFormat);

		if (level == 1) {
			blockType = "XBLOCK";
			this.blockList = readXBlock(numEntries, bid, bbt, pstFile);
			xblockList = null;
		} else {
			blockType = "XBLOCK";
			xblockList = readXXBlock(numEntries, bid, bbt, pstFile);

			int nBlocks = 0;
			for (java.util.Iterator<XBlock> xIter = xblockList.iterator(); xIter.hasNext(); )
				nBlocks += xIter.next().blockList.size();

			java.util.Vector<SimpleBlock> blockList = new java.util.Vector<SimpleBlock>(nBlocks);
			for (java.util.Iterator<XBlock> xIter = xblockList.iterator(); xIter.hasNext(); ) {
				final XBlock xblock = xIter.next();
				for (java.util.Iterator<SimpleBlock> blockIter = xblock.blockList.iterator(); blockIter.hasNext(); )
					blockList.add(blockIter.next());
			}
			this.blockList = blockList;
		}

		int size = 0;
		for (java.util.Iterator<SimpleBlock> bIter = blockList.iterator(); bIter.hasNext(); )
			size += bIter.next().data.length;
		dataBytes = size;
	}

	/**	Retrieve the consolidated data array for this data tree.
	*	@return	An array consisting of the data in all the leaf blocks of the data tree.
	*/
	byte[] data()
	{
		byte[] data = new byte[dataBytes];
		int destOffset = 0;
		java.util.Iterator<SimpleBlock> blockIterator = blockList.iterator();
		while (blockIterator.hasNext()) {
			final SimpleBlock block = blockIterator.next();
			System.arraycopy(block.data, 0, data, destOffset, block.data.length);
			destOffset += block.data.length;
		}

		return data;
	}

	/**	Get a table model which can be used to describe this node.
	*	@return	A DefaultTableModel describing this node.
	*/
	public javax.swing.table.TableModel getInternalDataTableModel()
	{
		Object[][] cells = new Object[5 + numEntries][];
		int i = 0;
		cells[i++] = new Object[]{"Block type", blockType};
		cells[i++] = new Object[]{nm_btype, type};
		cells[i++] = new Object[]{nm_cLevel, level};
		cells[i++] = new Object[]{nm_cEnt, numEntries};
		cells[i++] = new Object[]{nm_lcbTotal, lcbTotal};

		java.util.Vector<Object> contents;
		String prefix;
		if (level == 1) {
			prefix = "Data Block";
			java.util.Iterator<SimpleBlock> iterator = blockList.iterator();
			for (int j = 0; j < bid.length; ++j)
				cells[i++] = new Object[]{String.format("%s %d", prefix, j), bid[j].toString()};
		} else {
			prefix = "XBLOCK";
			java.util.Iterator<XBlock> iterator = xblockList.iterator();
			while (iterator.hasNext())
				cells[i++] = new Object[]{String.format("%s %d", prefix, i-4), ((BlockBase)iterator.next()).toString()};
		}

		return new javax.swing.table.DefaultTableModel(cells, new Object[]{"", ""});
	}

	/**	Obtain an iterator to iterate through the child blocks.
	*	@return	An iterator through the SimpleBlock objects making up the leaf nodes of this XBLOCK/XXBLOCK structure.
	*/
	java.util.Iterator<java.nio.ByteBuffer> iterator()
	{
		return new Iterator();
	}

	/**	Read in an XBLOCK.
	*	@param	numEntries	The number of child block entries in this XBlock.
	*	@param	bid		The array of BIDs of the blocks.
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The underlying PST file's data stream, header, etc.
	*	@return	A vector of SimpleBlock objects.
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	java.io.IOException	An I/O exception was encountered while reading in the requested XBlocks.
	*/
	static java.util.Vector<SimpleBlock> readXBlock(final int numEntries, final BID[] bid, final BlockMap bbt, PSTFile pstFile)
	throws
		CRCMismatchException,
		java.io.IOException
	{
		java.util.Vector<SimpleBlock> blockList = new java.util.Vector<SimpleBlock>(numEntries);
		for (BID b : bid) {
			final BBTEntry blockEntry = bbt.find(b);
			assert blockEntry != null;
			final SimpleBlock block = new SimpleBlock(blockEntry, pstFile);
			blockList.add(block);
		}
		return blockList;
	}

	/**	Read in an XXBLOCK.
	*	@param	numEntries	The number of child XBLOCK entries in this XXBlock.
	*	@param	bid		The array of BIDs of the XBLOCKs.
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The underlying PST file's data stream, header, etc.
	*	@return	A vector of XBlock objects.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	java.io.IOException	An I/O exception was encountered while reading in the requested XXBlocks.
	*/
	static java.util.Vector<XBlock> readXXBlock(final int numEntries, final BID[] bid, final BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		java.io.IOException
	{
		java.util.Vector<XBlock> xblockList = new java.util.Vector<XBlock>(numEntries);
		for (BID b : bid) {
			final BBTEntry blockEntry = bbt.find(b);
			assert blockEntry != null;
			final XBlock xBlock = new XBlock(blockEntry, bbt, pstFile);
			xblockList.add(xBlock);
		}

		return xblockList;
	}

	/**	Obtain a string representation of the XBlock object.
	*	@return	A string representation of the data tree.
	*	@see	#bid
	*/
	@Override
	public String toString()
	{
		String s = String.format("%d bytes in %d data blocks:%n", dataBytes, blockList.size());
		for (int i = 0; i < bid.length; ++i) {
			if (i > 0)
				s += "\n";
			s += bid[i];
		}
		return s;
	}

	/**	Test the XBlock class by traversing the node BTree and getting the XBlocks for each internal data block.
	*	This should also display the XBlocks in each node subtree block, but that's a little tougher.
	*	@param	args	The file(s) to show the XBlocks for.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.XBlock pst-file [pst-file ...]");
			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {
				java.io.FileInputStream stream = new java.io.FileInputStream(a);
				final PSTFile pstFile = new PSTFile(stream);
				try {
					final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
					final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

					java.util.Iterator<BTreeNode> iterator = nbt.iterator();
					while (iterator.hasNext()) {
						final NBTEntry node = (NBTEntry)iterator.next();
						if (node.bidData.fInternal) {
							final BBTEntry block = bbt.find(node.bidData);
							if (block == null) {
								System.out.printf("Block for node %s is null%n", node.toString());
								continue;
							}
							try {
								final XBlock xblock = new XBlock(block, bbt, pstFile);
								System.out.println(xblock);
							} catch (final	BadXBlockLevelException
								|	BadXBlockTypeException e) {
								System.out.println(e);
								e.printStackTrace(System.out);
							}
						}
					}
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
