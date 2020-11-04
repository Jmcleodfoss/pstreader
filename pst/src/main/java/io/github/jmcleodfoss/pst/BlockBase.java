package io.github.jmcleodfoss.pst;

/**	The BlockBase class is the base class for both data blocks ({@link SimpleBlock}) and metadata XBLOCK/XXBLOCK ({@link XBlock}) classes.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/8f34ce81-7a04-4a31-ba48-e05543daa77f">MS-PST Section 2.2.2.8.3: Block Types</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/d0e6fbaf-00e3-4d4d-bea8-8ab3cdb4fde6">MS-PST Section 2.2.2.8.3.1: Data Blocks</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/45688317-46fb-4038-9ed3-b845d80bdabb">MS-PST Section 2.2.8.3.2: Data Tree</a>
*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
*/
abstract class BlockBase
{
	/**	All blocks are a multiple of ({@value}) in size for ANSI and Unicode files.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a9c1981d-d1ea-457c-b39e-dc7fb0eb95d4">MS-PST Section 2.2.2.8: Blocks</a>
	*/
	private static final int BASE_BYTES = 64;

	/**	All blocks are a multiple of ({@value}) in size for OST 2013 files.
	*	@see <a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
	*/
	private static final int BASE_BYTES_OST_2013 = 512;

	/**	The maximum number of bytes in a block is {@value} for an ANSI or Unicode file
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a9c1981d-d1ea-457c-b39e-dc7fb0eb95d4">MS-PST Section 2.2.2.8: Blocks</a>
	*/
	private static final int MAX_BLOCK_BYTES = 8192;

	/**	The maximum number of bytes in a block is {@value} for an OST-2013 file
	*	@see <a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
	*/
	private static final int MAX_BLOCK_BYTES_OST_2013 = 65535;

	/**	Hash key for the data read within the block. */
	protected static final String nm_data = "data";

	/**	Obtain the data within this block, returned as an array of bytes.
	*	@return	The contents of the block, as an array of bytes.
	*/
	abstract byte[] data();

	/**	Return the size of the block, which depends on both the number of data bytes and the file format.
	*	@param	numBytes	The number of data bytes in the block.
	*	@param	pstFile		The PST file input data stream, {@link Header}, etc
	*	@return	The size of the block with padding given the target size in bytes.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a9c1981d-d1ea-457c-b39e-dc7fb0eb95d4">MS-PST Section 2.2.2.8: Blocks</a>
	*	@see <a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
	*/
	protected static int blockSize(final int numBytes, final PSTFile pstFile)
	{
		int requiredSize = numBytes + BlockTrailer.size(pstFile);

		final int baseBytes = pstFile.header.fileFormat.index == FileFormat.Index.OST_2013 ? BASE_BYTES_OST_2013 : BASE_BYTES;
		if (requiredSize % baseBytes == 0)
			return requiredSize;
		return ((requiredSize/baseBytes) + 1) * baseBytes;
	}

	/**	Return the data within this block, returned as a ByteBuffer.
	*	@return	A ByteBuffer object from which the data in this block may be read.
	*/
	java.nio.ByteBuffer dataStream()
	{
		return PSTFile.makeByteBuffer(data());
	}

	/**	Obtain an iterator which steps through the component blocks.
	*	@return	An iterator through the block or blocks associated with this {@link SimpleBlock} or {@link XBlock} object.
	*/
	abstract java.util.Iterator<java.nio.ByteBuffer> iterator();

	/**	The maximum size of a block.
	*	@param	pstFile	The pstFile object for the file being read
	*	@return	The maximum block size
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a9c1981d-d1ea-457c-b39e-dc7fb0eb95d4">MS-PST Section 2.2.2.8: Blocks</a>
	*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
	*/
	static int maxBlockSize(PSTFile pstFile)
	{
		if (pstFile.header.fileFormat.index == FileFormat.Index.OST_2013)
			return MAX_BLOCK_BYTES_OST_2013;

		return MAX_BLOCK_BYTES;
	}

	/**	Retrieve the required block specified by BlockBTree leaf entry.
	*	@param	entry	The block B-tree entry from which to read the block.
	*	@param	bbt	The PST file's block B-tree
	*	@param	pstFile	The PST file input stream, etc.
	*	@return	A BlockBase object from which the data may be retrieved.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the requested block.
	*	@see	#data
	*/
	static BlockBase read(final BBTEntry entry, final BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		java.io.IOException
	{
		return entry.bref.bid.fInternal ? new XBlock(entry, bbt, pstFile) : new SimpleBlock(entry, pstFile);
	}

	/**	Obtain the string representation of a block is its contents in hex.
	*	@return	A string representation of the data bytes making up this block.
	*/
	@Override
	public String toString()
	{
		return ByteUtil.createHexByteString(data());
	}
}
