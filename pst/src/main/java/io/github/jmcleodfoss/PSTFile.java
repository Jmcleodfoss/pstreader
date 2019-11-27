package io.github.jmcleodfoss.pst;

/**	The PSTFile class is a convenience container class used to read later data from the file. */
public class PSTFile {

	/**	The data stream for the PST file. */
	private java.io.FileInputStream stream;

	/**	The FileChannel of the data stream, used to jump around the file. */
	private java.nio.channels.FileChannel fc;

	/**	The file, as a memory-mapped byte file. */
	java.nio.MappedByteBuffer mbb;

	/**	The PST header, which contains encryption and file format information as well as other useful data. */
	public final Header header;

	/**	Create a FileChannel for the given filename and read in the PST header.
	*
	*	@param	stream	The PST file to read.
	*
	*	@throws NotPSTFileException	The input stream does not contain a PST file.
	* 	@throws java.io.IOException	There was an I/O error reading the input stream.
	*/
	public PSTFile(java.io.FileInputStream stream)
	throws
		NotPSTFileException,
		java.io.IOException
	{
		this.stream = stream;

		fc = this.stream.getChannel();

		mbb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fc.size());
		mbb.order(java.nio.ByteOrder.LITTLE_ENDIAN);

		header = new io.github.jmcleodfoss.pst.Header(mbb);
	}

	/**	A convenience method to return the encryption method in the header.
	*
	*	@return	The encryption object from the PST {@link Header} member.
	*/
	Encryption encryption()
	{
		return header.encryption;
	}

	/**	A convenience method to move to the given point in the PST file.
	*
	*	@param	position	The location in the PST file to move the mapped byte buffer pointer to.
	*/
	void position(final long position)
	throws
		java.io.IOException
	{
		mbb.position((int)position);
	}

	/**	Read data from the given position as a ByteBuffer
	*
	*	@param	position	The location to read from.
	*	@param	length		The number of bytes to read.
	*
	*	@return	A ByteBuffer providing access to the requested bytes
	*/
	public java.nio.ByteBuffer read(final int position, final int length)
	{
		byte[] data = new byte[length];
		mbb.position(position);
		mbb.get(data);
		return java.nio.ByteBuffer.wrap(data).asReadOnlyBuffer();
	}

	/**	Close the PSTFile file.
	*
	* 	@throws java.io.IOException	There was a problem closing the file.
	*/
	public void close()
	throws
		java.io.IOException
	{
		fc.close();
	}

	/**	A convenience method to indicate whether the PST file uses Unicode or ANSI encoding.
	*
	*	@return	true if the PST file read in is a Unicode PST file, false if it is ANSI.
	*
	*	@see	FileFormat#fUnicode
	*/
	public boolean unicode()
	{
		return header.fileFormat.fUnicode;
	}

	/**	Create a ByteBuffer with the right byte ordering from the given array.
	*
	*	@param	bytes	The data to change into a ByteBuffer
	*
	*	@return	A ByteBuffer containing the input data with little-endian order from which PST fields may be read.
	*/
	public static java.nio.ByteBuffer makeByteBuffer(final byte[] bytes)
	{
		return makeByteBuffer(bytes, 0, bytes.length);
	}

	/**	Create a ByteBuffer with the right byte ordering from the given array.
	*
	*	@param	bytes		The data from which to create a ByteBuffer.
	*	@param	offset		The offset into the array of bytes to start the ByteBuffer at.
	*	@param	numBytes	The number of bytes to put into the ByteBuffer
	*
	*	@return	A ByteBuffer containing the input data with little-endian order from which PST fields may be read.
	*/
	static java.nio.ByteBuffer makeByteBuffer(final byte[] bytes, final int offset, final int numBytes)
	{
		java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(bytes, offset, numBytes).asReadOnlyBuffer();
		byteBuffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		return byteBuffer;
	}
}
