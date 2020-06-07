package io.github.jmcleodfoss.pst;

/**	The Header object is the PST header.
*
*	@see	io.github.jmcleodfoss.pst.Encryption
*	@see	io.github.jmcleodfoss.pst.FileFormat
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/c9876f5a-664b-46a3-9887-ba63f113abf5">MS-PST Section 2.2.2.6: Header</a>
*/
public class Header {

	private static final String nm_dwMagic = "dwMagic";
	private static final String nm_dwCRCPartial = "dwCRCPartial";
	private static final String nm_wVer = "wVer";
	private static final String BREFRootNBT = "BREFRootNBT";
	private static final String BREFRootBBT = "BREFRootBBT";
	private static final String nm_bCryptMethod = "bCryptMethod";
	private static final String nm_dwCRCFull = "dwCRCFull";

	/**	The fields common to both ANSI and Unicode files in the input stream the header is being read from. */
	private static final DataDefinition[] common_fields = {
		new DataDefinition(nm_dwMagic, DataType.integer32Reader, true),
		new DataDefinition(nm_dwCRCPartial, DataType.integer32Reader, true),
		new DataDefinition("wMagicClient", DataType.integer16Reader, true),
		new DataDefinition(nm_wVer, DataType.integer16Reader, true),
		new DataDefinition("wVerClient", DataType.integer16Reader, true),
		new DataDefinition("bPlatformCreate", DataType.integer8Reader),
		new DataDefinition("bPlatformAccess", DataType.integer8Reader),
		new DataDefinition("dwReserved1", new DataType.SizedByteArray(4)),
		new DataDefinition("dwReserved2", new DataType.SizedByteArray(4))
	};

	/**	The Unicode-specific fields in the input stream the header is being read from. Note that this includes all the fields
	*	after the initial common fields.
	*/
	private static final DataDefinition[] unicode_fields = {
		new DataDefinition("bidUnused", new DataType.SizedByteArray(8)),
		new DataDefinition("bidNextP", new DataType.SizedByteArray(8)),

		// There is conflicting information about whether this is here
		// Section 2.2.2.6 says it is, but Section 3.2 suggests it isn't
//			new DataDefinition("bidNextB", 8),
		new DataDefinition("dwUnique", new DataType.SizedByteArray(4)),
		new DataDefinition("rgnid", new DataType.SizedByteArray(128)),
		new DataDefinition("qwUnused", new DataType.SizedByteArray(8)),

		// Treat the root values as part of the header
//		new DataDefinition("root", 72),
		new DataDefinition("dwRootReserved", new DataType.SizedByteArray(4)),
		new DataDefinition("ibRootFileEOF", new DataType.SizedByteArray(8), true),
		new DataDefinition("ibRootAMapLast", new DataType.SizedByteArray(8)),
		new DataDefinition("cbRootAMapFree", new DataType.SizedByteArray(8)),
		new DataDefinition("cbRootPMapFree", new DataType.SizedByteArray(8)),
		new DataDefinition("BREFRootNBT", DataType.brefUnicodeReader, true),
		new DataDefinition("BREFRootBBT", DataType.brefUnicodeReader, true),
		new DataDefinition("fRootAMapValid", new DataType.SizedByteArray(1)),
		new DataDefinition("bRootReserved", new DataType.SizedByteArray(1)),
		new DataDefinition("wRootReserved", new DataType.SizedByteArray(2)),

		new DataDefinition("dwAlign", new DataType.SizedByteArray(4)),
		new DataDefinition("rgbFM", new DataType.SizedByteArray(128)),
		new DataDefinition("rgpFP", new DataType.SizedByteArray(128)),
		new DataDefinition("bSentinel", new DataType.SizedByteArray(1)),
		new DataDefinition(nm_bCryptMethod, DataType.integer8Reader, true),
		new DataDefinition("rgbReserved", new DataType.SizedByteArray(2)),
		new DataDefinition("bidNextB", new DataType.SizedByteArray(8)),
		new DataDefinition(nm_dwCRCFull, DataType.integer32Reader, true),
		new DataDefinition("rgbReserved2", new DataType.SizedByteArray(3)),
		new DataDefinition("bReserved", new DataType.SizedByteArray(1)),
		new DataDefinition("rgbReserved3", new DataType.SizedByteArray(32))
	};

	/**	Size of the header block in Unicode files */
	private static final int SIZE_UNICODE = DataDefinition.size(common_fields) + DataDefinition.size(unicode_fields); 

	// Not tested - no data available
	/**	The ANSI-specific fields in the input stream the header is being read from. Note that this includes all the fields after
	*	the initial common fields.
	*/
	private static final DataDefinition[] ansi_fields = {
		new DataDefinition("bidNextP", new DataType.SizedByteArray(4)),
		new DataDefinition("bidNextB", new DataType.SizedByteArray(4)),
		new DataDefinition("dwUnique", new DataType.SizedByteArray(4)),
		new DataDefinition("rgnid", new DataType.SizedByteArray(128)),

		// Treat the root values as part of the header
//		new DataDefinition("root", 40),
		new DataDefinition("dwRootReserved", new DataType.SizedByteArray(4)),
		new DataDefinition("ibRootFileEOF", DataType.integer32Reader, true),
		new DataDefinition("ibRootAMapLast", new DataType.SizedByteArray(4)),
		new DataDefinition("cbRootAMapFree", new DataType.SizedByteArray(4)),
		new DataDefinition("cbRootPMapFree", new DataType.SizedByteArray(4)),
		new DataDefinition("BREFRootNBT",DataType.brefAnsiReader, true),
		new DataDefinition("BREFRootBBT",DataType.brefAnsiReader, true),
		new DataDefinition("fRootAMapValid", new DataType.SizedByteArray(1)),
		new DataDefinition("bRootReserved", new DataType.SizedByteArray(1)),
		new DataDefinition("wRootReserved", new DataType.SizedByteArray(2)),

		new DataDefinition("rgbFM", new DataType.SizedByteArray(128)),
		new DataDefinition("rgpFP", new DataType.SizedByteArray(128)),
		new DataDefinition("bSentinel", new DataType.SizedByteArray(1)),
		new DataDefinition(nm_bCryptMethod, DataType.integer8Reader, true),
		new DataDefinition("rgbReserved", new DataType.SizedByteArray(1)),
		new DataDefinition("ullReserved", new DataType.SizedByteArray(8)),
		new DataDefinition("dwReserved", new DataType.SizedByteArray(4)),
		new DataDefinition("rgbReserved2", new DataType.SizedByteArray(3)),
		new DataDefinition("bReserved", new DataType.SizedByteArray(1)),
		new DataDefinition("rgbReserved3", new DataType.SizedByteArray(32))
	};

	/**	Size of the heeader block in ANSI files */
	private static final int SIZE_ANSI = DataDefinition.size(common_fields) + DataDefinition.size(ansi_fields); 

	/**	The offset from which to start calculating the full and partial CRC's. */
	private static final int CRC_START_OFFSET = common_fields[0].description.size() + common_fields[1].description.size();

	/**	The number of bytes used to calculate the header's partial CRC. */
	private static final int CRC_PARTIAL_BYTES = 471;

	/**	The number of bytes used to calculate the header's full CRC. */
	private static final int CRC_FULL_BYTES = 516;

	/**	First byte of the header block. */
	public static final long OFFSET = 0;

	/**	The file format (ANSI or Unicode) of the PST file. */
	public final FileFormat fileFormat;

	/**	The encryption mechanism used by the PST file. */
	public final Encryption encryption;

	/**	The location of the root of the node B-tree. */
	public final BREF nbtRoot;

	/**	The location of the root of the block B-tree. */
	public final BREF bbtRoot;

	/**	Read in the header data and save the fields we need for later.
	*
	*	@param	byteBuffer	The data stream from which to read the PST header.
	*
	*	@throws	NotPSTFileException	This is not a pst file.
	*	@throws	java.io.IOException	An I/O error was encountered when reading the pst header.
	*/
	Header(java.nio.ByteBuffer byteBuffer)
	throws
		NotPSTFileException,
		java.io.IOException
	{
		int crcPartialCalculated = 0;
		int crcFullCalculated = 0;
		if (Options.checkCRC) {
			crcPartialCalculated = CRC.crc(byteBuffer, CRC_START_OFFSET, CRC_PARTIAL_BYTES);
			crcFullCalculated = CRC.crc(byteBuffer, CRC_START_OFFSET, CRC_FULL_BYTES);
		}

		DataContainer dc = new DataContainer();
		dc.read(byteBuffer, common_fields);
		validate_dwMagic(dc);
		validate_CRC(crcPartialCalculated, nm_dwCRCPartial, dc);

		fileFormat = new FileFormat((Short)dc.get(nm_wVer));

		dc.read(byteBuffer, fileFormat.fUnicode ? unicode_fields : ansi_fields);
		if (fileFormat.fUnicode)
			validate_CRC(crcFullCalculated, nm_dwCRCFull, dc);

		encryption = new Encryption((Byte)dc.get(nm_bCryptMethod));
		nbtRoot = (BREF)dc.get(BREFRootNBT);
		bbtRoot = (BREF)dc.get(BREFRootBBT);
	}

	/**	Calculate the size of the header block.
	*
	*	@return	The size of the header for this file.
	*/
	public int size()
	{
		return fileFormat.fUnicode ? SIZE_UNICODE : SIZE_ANSI;
	}

	/**	Provide a summary of the header in String form. This is typically used for debugging.
	*
	*	@return	A description of the header.
	*/
	@Override
	public String toString()
	{
		return String.format("Format %s, Encoding %s, BBT BID 0x%08x IB 0x%08x, NBT BID 0x%08x IB 0x%08x", fileFormat, encryption, bbtRoot.bid.bid, bbtRoot.ib.ib, nbtRoot.bid.bid, nbtRoot.ib.ib);
	}

	/**	Ensure that the file's magic number is correct for a PST file.
	*
	*	@param	dc	The DataContainer object holding the values read in from the header.
	*
	*	@throws	NotPSTFileException	The magic number for this file is not that for a pst file.
	*/
	private void validate_dwMagic(final DataContainer dc)
	throws
		NotPSTFileException
	{
		final int verify_dwMagic = 0x4e444221;
		if (verify_dwMagic != (Integer)dc.get(nm_dwMagic))
			throw new NotPSTFileException();
	}

	/**	Ensure that the header's initial (partial) CRC is correct. Note that this is used for both the partial and full CRCs.
	*
	*	@param	crcCalculated	The CRC of the header caculated from its contents.
	*	@param	nm_field	The field name of the CRC to check.
	*	@param	dc		The DataContainer object holding the values read in from the header.
	*/
	private void validate_CRC(final int crcCalculated, final String nm_field, final DataContainer dc)
	{
		if (Options.checkCRC && crcCalculated != (Integer)dc.get(nm_field))
			throw new RuntimeException(nm_field + " "  + Integer.toHexString((Integer)dc.get(nm_field)) + " does not match calculated value " + Integer.toHexString(crcCalculated));
	}

	/**	Test this class by reading in the PST file header and printing it out.
	*
	*	@param	args	The command line arguments to the test application.
	*/
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.Header pst-file [log-level]");
			System.exit(1);
		}
		try {
			java.util.logging.Level logLevel = args.length >= 2 ? Debug.getLogLevel(args[1]) : java.util.logging.Level.OFF;
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.github.jmcleodfoss.pst");
			logger.setLevel(logLevel);

			java.io.File file = new java.io.File(args[0]);
			java.io.FileInputStream stream = new java.io.FileInputStream(file);
			java.nio.channels.FileChannel fc = stream.getChannel();
			java.nio.MappedByteBuffer mbb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fc.size());
			mbb.order(java.nio.ByteOrder.LITTLE_ENDIAN);

			Header header = new Header(mbb);
			System.out.println(header);
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
