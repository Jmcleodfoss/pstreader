package io.github.jmcleodfoss.pst;

/**	The BlockTrailer class describes the BLOCKTRAILER structure at the end of each block.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a14943ef-70c2-403f-898c-5bc3747117e1">MS-PST Section 2.2.2.8: BLOCKTRAILER</a>
*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
*/
class BlockTrailer
{
	private static final String nm_cb = "cb";
	private static final String nm_wSig = "wSig";
	private static final String nm_dwCRC = "dwCRC";
	private static final String nm_bid = "bid";

	/** The fields for each file type.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a14943ef-70c2-403f-898c-5bc3747117e1">MS-PST Section 2.2.2.8: BLOCKTRAILER</a>
	*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
	*/
	private static final DataDefinition[][] fields = {
		/* ANSI fields */
		{
			new DataDefinition(nm_cb, DataType.integer16Reader, true),
			new DataDefinition(nm_wSig, DataType.integer16Reader, false),
			new DataDefinition(nm_bid, DataType.bidAnsiReader, true),
			new DataDefinition(nm_dwCRC, DataType.integer32Reader, true)
		},

		/* Unicode fields */
		{
			new DataDefinition(nm_cb, DataType.integer16Reader, true),
			new DataDefinition(nm_wSig, DataType.integer16Reader, false),
			new DataDefinition(nm_dwCRC, DataType.integer32Reader, true),
			new DataDefinition(nm_bid, DataType.bidUnicodeReader, true)
		},

		/* OST 2013 fields */
		{
			new DataDefinition(nm_cb, DataType.integer16Reader, true),
			new DataDefinition(nm_wSig, DataType.integer16Reader, true),
			new DataDefinition(nm_dwCRC, DataType.integer32Reader, true),
			new DataDefinition(nm_bid, DataType.bidUnicodeReader, true),
			new DataDefinition("unused", new DataType.SizedByteArray(8), true)
		}
	};

	/**	The CRC of this block. */
	final int crc;

	/**	Create a BlockTrailer object from the current position in the input datastream.
	*	@param	byteBuffer	The mapped byte buffer for the current block.
	*	@param	fileFormat	The PST file format.
	*	@throws	java.io.IOException	An I/O error was encoutered while reading in the block trailer.
	*/
	BlockTrailer(java.nio.ByteBuffer byteBuffer, FileFormat fileFormat)
	throws
		java.io.IOException
	{
		DataContainer dc = new DataContainer();
		dc.read(byteBuffer, fields[fileFormat.index.getIndex()]);
		crc = (Integer)dc.get(nm_dwCRC);
	}

	/**	Return the size of the BLOCKTRAILER, which is different under Unicode and ANSI file formats.
	*	@param	fileFormat	The PST file format.
	*	@return	The size, in bytes, of the BLOCKTRAILER object in the PST file.
	*/
	static int size(final FileFormat fileFormat)
	{
		switch(fileFormat.index){
		case ANSI:
			return DataDefinition.size(fields[FileFormat.Index.ANSI.getIndex()]);

		case UNICODE:
		default:
			return DataDefinition.size(fields[FileFormat.Index.UNICODE.getIndex()]);

		case OST_2013:
			return DataDefinition.size(fields[FileFormat.Index.OST_2013.getIndex()]);
		}
	}
}
