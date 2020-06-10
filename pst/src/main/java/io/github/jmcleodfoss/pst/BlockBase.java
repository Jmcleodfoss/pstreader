package io.github.jmcleodfoss.pst;

/**	The BlockBase class is the base class for both data blocks ({@link SimpleBlock}) and metadata XBLOCK/XXBLOCK ({@link XBlock}) classes.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/8f34ce81-7a04-4a31-ba48-e05543daa77f">MS-PST Section 2.2.2.8.3: Block Types</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/d0e6fbaf-00e3-4d4d-bea8-8ab3cdb4fde6">MS-PST Section 2.2.2.8.3.1: Data Blocks</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/45688317-46fb-4038-9ed3-b845d80bdabb">MS-PST Section 2.2.8.3.2: Data Tree</a>
*/
abstract class BlockBase
{
	/**	Logger for debugging block classes. */
	protected static java.util.logging.Logger logger = Debug.getLogger("io.github.jmcleodfoss.pst.BlockBase");

	/**	All blocks are a multiple of BASE_BYTES ({@value}) in size. */
	private static final int BASE_BYTES = 64;

	/**	The maximum number of bytes in a block is {@value}. */
	static final int MAX_BLOCK_BYTES = 8192;

	/**	Hash key for the data read within the block. */
	protected static final String nm_data = "data";

	/**	Hash key for the padding after the data within the block. */
	protected static final String nm_padding = "padding";

	/**	Obtain the data within this block, returned as an array of bytes.
	*	@return	The contents of the block, as an array of bytes.
	*/
	abstract byte[] data();

	/**	Return the size of the block, which depends on both the number of data bytes and the file format.
	*	@param	numBytes	The number of data bytes in the block.
	*	@param	pstFile		The PST file input data stream, {@link Header}, etc
	*	@return	The size of the block with padding given the target size in bytes.
	*/
	protected static int blockSize(final int numBytes, final PSTFile pstFile)
	{
		int requiredSize = numBytes + BlockTrailer.size(pstFile);
		if (requiredSize % BASE_BYTES == 0)
			return requiredSize;
		return ((requiredSize/BASE_BYTES) + 1) * BASE_BYTES;
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

	/**	Retrieve the required block specified by BlockBTree leaf entry.
	*	@param	entry	The block B-tree entry from which to read the block.
	*	@param	bbt	The PST file's block B-tree
	*	@param	pstFile	The PST file input stream, etc.
	*	@return	A BlockBase object from which the data may be retrieved.
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the requested block.
	*	@see	#data
	*/
	static BlockBase read(final BBTEntry entry, final BlockMap bbt, PSTFile pstFile)
	throws
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
