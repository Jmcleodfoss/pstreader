package io.github.jmcleodfoss.pst;

/**	The SimpleBlock class represents a leaf data block (i.e. not an XBLOCK or XXBLOCK).
*	@see	io.github.jmcleodfoss.pst.XBlock
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a9c1981d-d1ea-457c-b39e-dc7fb0eb95d4">MS-PST Section 2.2.2.8: Blocks</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/ddeb714d-8fd5-4a48-8019-8338cb511c80">MS-PST Section 2.2.2.8.2: Anatomy of a Block</a>
*/
class SimpleBlock extends BlockBase
{
	/**	The BlockBase Iterator class is a trivial iterator the data stream for this one block. */
	class Iterator implements java.util.Iterator<java.nio.ByteBuffer>
	{
		/**	The fNext flag indicates whether there is another block. It is true until the block is returned, when it
		*	becomes false.
		*/
		private boolean fNext;

		/**	Create a trivial iterator for the given block. */
		Iterator()
		{
			fNext = true;
		}

		/**	Determine if there is another block to return.
		*	@return	true if the data hasn't been returned, false if it has.
		*/
		public boolean hasNext()
		{
			return fNext;
		}

		/**	Retrieve the block data as a ByteBuffer.
		*	@return	A ByteBuffer from which the block data may be read.
		*	@throws	java.util.NoSuchElementException	There are no more elements to return
		*/
		public java.nio.ByteBuffer next()
		throws
			java.util.NoSuchElementException
		{
			if (!fNext)
				throw new java.util.NoSuchElementException();

			fNext = false;
			return PSTFile.makeByteBuffer(data);
		}

		/**	The remove function is not supported by the SimpleBlock iterator. */
		public void remove()
		{
			throw new UnsupportedOperationException("remove not suported");
		}
	}

	/**	The offset from which to start calculating the CRC for a block.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/ddeb714d-8fd5-4a48-8019-8338cb511c80">MS-PST Section 2.2.2.8.2: Anatomy of a Block</a>
	*/
	private static final int CRC_START_OFFSET = 0;

	/**	The number of bytes used to calculate the block's CRC.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/ddeb714d-8fd5-4a48-8019-8338cb511c80">MS-PST Section 2.2.2.8.2: Anatomy of a Block</a>
	*/
	private static final int CRC_BYTES = 236;

	/**	The block data. */
	final byte[] data;

	/**	Create a SimpleBlock object from the given block B-tree leaf entry and basic PST file object.
	*	@param	entry	The block B-tree leaf entry describing the block.
	*	@param	pstFile	The PST file's header, input data stream, etc.
	*	@throws	java.io.IOException	An I/O error was encountered when reading the data for this block.
	*/
	SimpleBlock(final BBTEntry entry, PSTFile pstFile)
	throws
		java.io.IOException
	{
		this(entry, pstFile.encryption(), pstFile);
	}

	/**	Create a SimpleBlock object from the given block B-tree leaf entry and PST file, using the given encryption method.
	*	Note that there is occasionally need to create a simple block which does not use the file encryption method.
	*	@param	entry		The block B-tree leaf entry describing the block.
	*	@param	encryption	The encryption method to use to decrypt the data.
	*	@param	pstFile		The PST file's header, input data stream, etc.
	*	@throws	java.io.IOException	An I/O error was encountered when reading the data for this block.
	*	@see	SubnodeBTree.BlockContext
	*/
	SimpleBlock(final BBTEntry entry, final Encryption encryption, PSTFile pstFile)
	throws
		java.io.IOException
	{
		this(entry, blockSize(entry.numBytes, pstFile), encryption, pstFile);
	}

	/**	Create a SimpleBlock object from the given block B-tree leaf entry with the given block size from the given PST file,
	*	using the given encryption method.
	*	@param	entry		The block B-tree leaf entry describing the block.
	*	@param	blockSize	The size of the block (including the {@link BlockTrailer BLOCKTRAILER})
	*	@param	encryption	The encryption method to use to decrypt the data.
	*	@param	pstFile		The PST file's header, input data stream, etc.
	*	@throws	java.io.IOException	An I/O error was encountered when reading the data for this block.
	*/
	SimpleBlock(final BBTEntry entry, final int blockSize, final Encryption encryption, PSTFile pstFile)
	throws
		java.io.IOException
	{
		pstFile.position(entry.bref.ib.ib);

		DataDefinition dataField = new DataDefinition(nm_data, new DataType.SizedByteArray(entry.numBytes), true);
		DataContainer dc = new DataContainer();
		dc.read(pstFile.mbb, dataField);
		data = (byte[])dc.get(nm_data);
		int crcCalculated = 0;
		if (Options.checkCRC)
			crcCalculated = CRC.crc(PST.makeByteBuffer(data), 0, data.length);
		encryption.translate(data, (int)(entry.bref.bid.key() & 0xffffffff));

		final int bytesToSkip = blockSize-entry.numBytes-BlockTrailer.size(pstFile);
		pstFile.mbb.position(pstFile.mbb.position() + bytesToSkip);
		final BlockTrailer trailer = new BlockTrailer(pstFile);
		if (Options.checkCRC && crcCalculated != trailer.crc)
			throw new RuntimeException("Block CRC "  + Integer.toHexString(trailer.crc) + " does not match calculated value " + Integer.toHexString(crcCalculated));
	}

	/**	Retrieve the data from this SimpleBlock.
	*	@return	The array of data bytes from this SimpleBlock object.
	*/
	byte[] data()
	{
		return data;
	}

	/**	Obtain an interator over the (one) block.
	*	@return	A trivial iterator over this single SimpleBlock.
	*/
	@SuppressWarnings("unchecked")
	java.util.Iterator<java.nio.ByteBuffer> iterator()
	{
		return new Iterator();
	}

	/**	Retrieve the required block specified by BlockBTree leaf entry.
	*	@param	entry		The block B-tree leaf entry describing the block.
	*	@param	pstFile		The PST file's header, input data stream, etc.
	*	@return	The data block for the requested block B-tree entry.
	*	@throws	java.io.IOException	An I/O error was encountered when reading the block data.
	*/
	static SimpleBlock read(final BBTEntry entry, PSTFile pstFile)
	throws
		java.io.IOException
	{
		return new SimpleBlock(entry, entry.bref.bid.fInternal ? Encryption.NONE : pstFile.encryption(), pstFile);
	}

	/**	Test this class by printing out blocks in the block B-tree.
	*	@param	args	The file(s) to print the blocks out for.
	*/
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.SimpleBlock pst-file [pst-file ...]");
			System.exit(1);
		}

		for (String a: args) {
			System.out.println(a);
			try {
				PSTFile pstFile = new PSTFile(new java.io.FileInputStream(a));
				final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
				java.util.Iterator<BTreeNode> iterator = bbt.iterator();
				while (iterator.hasNext()) {
					final BBTEntry entry = (BBTEntry)iterator.next();
					final SimpleBlock block = new SimpleBlock(entry, pstFile);
					System.out.println(entry + ": " + block);
				}
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				System.out.printf("Could not read %s%n", a);
				e.printStackTrace(System.out);
			}
		}
	}
}
