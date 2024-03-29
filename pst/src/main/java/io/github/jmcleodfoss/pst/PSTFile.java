package io.github.jmcleodfoss.pst;

/**	The PSTFile class is a convenience container class used to read later data from the file.
*	This object manages (and handles closing) the passed FileInputStream, although it is benign to close FileInputStreams twice.
*/
public class PSTFile
{
	/**	The FileInputStream for the file */
	private java.io.FileInputStream stream;

	/**	The FileChannel of the data stream, used to jump around the file. */
	private java.nio.channels.FileChannel fc;

	/**	The PST header, which contains encryption and file format information as well as other useful data. */
	public final Header header;

	/**	Create a FileChannel for the given filename and read in the PST header.
	*	@param	stream	The PST file to read.
	*	@throws	CRCMismatchException	The header's calculated CRC does not match the expected value.
	*	@throws NotPSTFileException	The input stream does not contain a PST file.
	* 	@throws java.io.IOException	There was an I/O error reading the input stream.
	*/
	PSTFile(java.io.FileInputStream stream)
	throws
		CRCMismatchException,
		NotPSTFileException,
		java.io.IOException
	{
		this.stream = stream;
		fc = stream.getChannel();

		try {
			fc = stream.getChannel();

			try {
				header = new Header(fc);
			} catch (final Exception e) {
				fc.close();
				throw e;
			}
		} catch (final Exception e) {
			stream.close();
			throw e;
		}
	}

	/**	Provide the name of String encoding.
	*	@return	DataType.CHARSET_NARROW for non-Unicode files, DataType.CHARSET_WIDE for Unicode files.
	*/
	public String charsetName()
	{
		return unicode() ? DataType.CHARSET_WIDE: DataType.CHARSET_NARROW;
	}

	/**	Close the PSTFile file.
	* 	@throws java.io.IOException	There was a problem closing the file.
	*/
	public void close()
	throws
		java.io.IOException
	{
		try {
			fc.close();
		} finally {
			stream.close();
		}
	}

	/**	A convenience method to return the encryption method in the header.
	*	@return	The encryption object from the PST {@link Header} member.
	*/
	Encryption encryption()
	{
		return header.encryption;
	}

	/**	Create a ByteBuffer with the right byte ordering from the given array.
	*	@param	bytes	The data to change into a ByteBuffer
	*	@return	A ByteBuffer containing the input data with little-endian order from which PST fields may be read.
	*/
	static java.nio.ByteBuffer makeByteBuffer(final byte[] bytes)
	{
		return makeByteBuffer(bytes, 0, bytes.length);
	}

	/**	Create a ByteBuffer with the right byte ordering from the given array.
	*	@param	bytes		The data from which to create a ByteBuffer.
	*	@param	offset		The offset into the array of bytes to start the ByteBuffer at.
	*	@param	numBytes	The number of bytes to put into the ByteBuffer
	*	@return	A ByteBuffer containing the input data with little-endian order from which PST fields may be read.
	*/
	static java.nio.ByteBuffer makeByteBuffer(final byte[] bytes, final int offset, final int numBytes)
	{
		java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(bytes, offset, numBytes).asReadOnlyBuffer();
		byteBuffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		return byteBuffer;
	}

	/**	Create a ByteBuffer suitable for reading the given number of bytes from the position in the BREF
	*	@param	bref		The block reference from which to read.
	*	@param	numBytes	The number of bytes to put into the ByteBuffer
	*	@return	A ByteBuffer containing the input data with little-endian order from which PST fields may be read.
	* 	@throws java.io.IOException	There was an I/O error reading the input stream.
	*/
	java.nio.ByteBuffer getByteBuffer(BREF bref, int numBytes)
	throws
		java.io.IOException
	{
		return read(bref.ib.ib, numBytes);
	}

	/**	Read data from the given position as a ByteBuffer
	*	@param	position	The location to read from.
	*	@param	length		The number of bytes to read.
	*	@return	A ByteBuffer providing access to the requested bytes
	* 	@throws java.io.IOException	There was an I/O error reading the input stream.
	*/
	public java.nio.ByteBuffer read(final long position, final int length)
	throws
		java.io.IOException
	{
		java.nio.ByteBuffer byteBuffer = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, position, length);
		byteBuffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		return byteBuffer;
	}

	/**	A convenience method to indicate whether the PST file uses Unicode (wide text) or ANSI (8-bit) text.
	*	Note that this is not the same as the file format Unicode type, since OST-2013 files use Unicode text.
	*	@return	true if the PST file read in is a Unicode PST file, false if it is ANSI.
	*	@see	FileFormat#fUnicode
	*/
	public boolean unicode()
	{
		return header.fileFormat.fUnicode;
	}
}
