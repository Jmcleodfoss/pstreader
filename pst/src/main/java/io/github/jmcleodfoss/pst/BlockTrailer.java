package io.github.jmcleodfoss.pst;

/**	The BlockTrailer class describes the BLOCKTRAILER structure at the end of each block.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/a14943ef-70c2-403f-898c-5bc3747117e1">MS-PST Section 2.2.2.8: BLOCKTRAILER</a>
*/
class BlockTrailer
{
	private static final String nm_cb = "cb";
	private static final String nm_wSig = "wSig";
	private static final String nm_dwCRC = "dwCRC";
	private static final String nm_bid = "bid";

	/**	The fields common to both ANSI and Unicode files in the input stream which make up the BLOCKTRAILER. */
	private static final DataDefinition[] common_fields = {
		new DataDefinition(nm_cb, DataType.integer16Reader, true),
		new DataDefinition(nm_wSig, DataType.integer16Reader, false)
	};

	/**	The Unicode-specific fields in the input stream which make up the BLOCKTRAILER. */
	private static final DataDefinition[] unicode_fields = {
		new DataDefinition(nm_dwCRC, DataType.integer32Reader, true),
		new DataDefinition(nm_bid, DataType.bidUnicodeReader, true)
	};

	/**	The size of a BLOCKTRAILER in a Unicode file. */
	private static final int TRAILER_SIZE_UNICODE = DataDefinition.size(common_fields) + DataDefinition.size(unicode_fields);

	/**	The ANSI-specific fields in the input stream which make up the BLOCKTRAILER. */
	private static final DataDefinition[] ansi_fields = {
		new DataDefinition(nm_bid, DataType.bidAnsiReader, true),
		new DataDefinition(nm_dwCRC, DataType.integer32Reader, true)
	};

	/**	The size of a BLOCKTRAILER in an ANSI file. */
	private static final int TRAILER_SIZE_ANSI = DataDefinition.size(common_fields) + DataDefinition.size(ansi_fields);

	/**	The CRC of this block. */
	final int crc;

	/**	Create a BlockTrailer object from the current position in the input datastream.
	*	@param	pstFile	The PST file's header, input stream, etc.
	*	@throws	java.io.IOException	An I/O error was encoutered while reading in the block trailer.
	*/
	BlockTrailer(PSTFile pstFile)
	throws
		java.io.IOException
	{
		DataContainer dc = new DataContainer();
		dc.read(pstFile.mbb, common_fields, pstFile.unicode() ? unicode_fields : ansi_fields);
		crc = (Integer)dc.get(nm_dwCRC);
	}

	/**	Return the size of the BLOCKTRAILER, which is different under Unicode and ANSI file formats.
	*	@param	pstFile	The PST file's Header object, etc.
	*	@return	The size, in bytes, of the BLOCKTRAILER object in the PST file.
	*/
	static int size(PSTFile pstFile)
	{
		return pstFile.unicode() ? TRAILER_SIZE_UNICODE : TRAILER_SIZE_ANSI;
	}
}
