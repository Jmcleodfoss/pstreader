package com.jsoft.pst;

/**	The DataType class represents data types within a PST file as well as PST file properties.
*
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608 Section 2.1.1"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385331(v=office.12).aspx">Data Types (MSDN)</a>
*	@see	"[MS-OXDATA] Data Structures v20101026, Section 2.11.1"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157583.aspx">Property Data Types (MSDN)</a>
*/
abstract class DataType {
	static final short UNSPECIFIED = 0x0000;
	static final short NULL = 0x0001;
	static final short INTEGER_16 = 0x0002;
	static final short INTEGER_32 = 0x0003;
	static final short FLOATING_32 = 0x0004;
	static final short FLOATING_64 = 0x0005;
	static final short CURRENCY = 0x0006;
	static final short FLOATING_TIME = 0x0007;
	static final short ERROR_CODE = 0x000a;
	static final short BOOLEAN = 0x000b;
	static final short OBJECT = 0x000d;
	static final short INTEGER_64 = 0x0014;
	static final short STRING_8 = 0x001e;
	static final short STRING = 0x001f;
	static final short TIME = 0x0040;
	static final short GUID = 0x0048;
	static final short SERVER_ID = 0x00fb;
	static final short RESTRICTION = 0x00fd;
	static final short RULE_ACTION = 0x00fe;
	static final short BINARY = 0x0102;
	static final short MULTIPLE_INTEGER_16 = 0x1002;
	static final short MULTIPLE_INTEGER_32 = 0x1003;
	static final short MULTIPLE_FLOATING_32 = 0x1004;
	static final short MULTIPLE_FLOATING_64 = 0x1005;
	static final short MULTIPLE_CURRENCY = 0x1006;
	static final short MULTIPLE_FLOATING_TIME = 0x1007;
	static final short MULTIPLE_INTEGER_64 = 0x1014;
	static final short MULTIPLE_STRING_8 = 0x101e;
	static final short MULTIPLE_STRING = 0x101f;
	static final short MULTIPLE_TIME = 0x1040;
	static final short MULTIPLE_GUID = 0x1048;
	static final short MULTIPLE_BINARY = 0x1102;

	/**	The names of the known properties */
	static final java.util.HashMap<Short, String> propertyNames = new java.util.HashMap<Short, String>();
	static {
		propertyNames.put(UNSPECIFIED, "Unspecified");
		propertyNames.put(NULL, "Null");
		propertyNames.put(INTEGER_16, "16-bit Integer");
		propertyNames.put(INTEGER_32, "32-bit Integer");
		propertyNames.put(FLOATING_32, "32-bit Floating Point");
		propertyNames.put(FLOATING_64, "64-bit Floating Point");
		propertyNames.put(CURRENCY, "Currency");
		propertyNames.put(FLOATING_TIME, "Floating Time");
		propertyNames.put(ERROR_CODE, "Error Code");
		propertyNames.put(BOOLEAN, "Boolean");
		propertyNames.put(OBJECT, "Object");
		propertyNames.put(INTEGER_64, "64-bit Integer");
		propertyNames.put(STRING_8, "ANSI String");
		propertyNames.put(STRING, "Unicode String");
		propertyNames.put(TIME, "Time");
		propertyNames.put(GUID, "GUID");
		propertyNames.put(SERVER_ID, "Server ID");
		propertyNames.put(RESTRICTION, "Restriction");
		propertyNames.put(RULE_ACTION, "Rule Action");
		propertyNames.put(BINARY, "Binary");
		propertyNames.put(MULTIPLE_INTEGER_16, "Multiple 16-bit Integer");
		propertyNames.put(MULTIPLE_INTEGER_32, "Multiple 32-bit Integer");
		propertyNames.put(MULTIPLE_FLOATING_32, "Multiple 32-bit Floating Point");
		propertyNames.put(MULTIPLE_FLOATING_64, "Multiple 64-bit Floating Point");
		propertyNames.put(MULTIPLE_CURRENCY, "Multiple Currency");
		propertyNames.put(MULTIPLE_FLOATING_TIME, "Multiple Floating Time");
		propertyNames.put(MULTIPLE_INTEGER_64, "Multiple 64-bit Integer");
		propertyNames.put(MULTIPLE_STRING_8, "Multiple ANSI String");
		propertyNames.put(MULTIPLE_STRING, "Multiple Unicode String");
		propertyNames.put(MULTIPLE_TIME, "Multiple Time");
		propertyNames.put(MULTIPLE_GUID, "Multiple GUID");
		propertyNames.put(MULTIPLE_BINARY, "Multiple Binary");
	}

	/**	The character encoding used for PST ANSI data.
	*
	*	@see	com.jsoft.pst.DataType.StringBase
	*	@see	com.jsoft.pst.DataType.MultipleString
	*	@see	com.jsoft.pst.DataType.PSTString
	*	@see	com.jsoft.pst.DataType#string8Reader
	*	@see	com.jsoft.pst.DataType#multipleString8Reader
	*/
	private static final String CHARSET_NARROW = new String("iso-8859-1");

	/**	The character encoding used for PST Unicode data.
	*
	*
	*	@see	com.jsoft.pst.DataType.StringBase
	*	@see	com.jsoft.pst.DataType.PSTString
	*	@see	com.jsoft.pst.DataType.MultipleString
	*	@see	com.jsoft.pst.DataType#stringReader
	*	@see	com.jsoft.pst.DataType#multipleStringReader
	*/
	private static final String CHARSET_WIDE = new String("UTF-16LE");

	/**	Create an object of type DataType. */
	protected DataType()
	{
	}

	/**	Create a String describing an object of the type read in by this class.
	*
	*	@param	o	The object to create a String representation of.
	*
	*	@return	A String describing the object.
	*/
	public abstract String makeString(final Object o);

	/**	Read in an object of the target type.
	*
	*	@param	byteBuffer	The incoming data stream from which to read the object.
	*
	*	@return	The object read from the data stream.
	*/
	public abstract Object read(java.nio.ByteBuffer byteBuffer)
	throws
		java.io.UnsupportedEncodingException;

	/**	Get the size of the object read in in this class.
	*
	*	@return	The size, in bytes, of the object read in by this class, if fixed (constant), otherwise 0.
	*/
	public abstract int size();

	/**	The SizedObject class contains functionality shared by manipulators for objects with known client-defined sizes. */
	private abstract static class SizedObject extends DataType {

		/**	The size of this object read in. */
		protected final int size;

		/**	Construct foundation for a manipulator of an object with known size.
		*
		*	@param	size	The number of bytes in this object.
		*/
		private SizedObject(final int size)
		{
			super();
			this.size = size;
		}

		/**	Obtain the size of this object in the PST file.
		*
		*	@return	The size of this object in the PST file, in bytes.
		*/
		public int size()
		{
			return size;
		}
	}

	/**	The BIDBase class contains functionality used by both ANSI and Unicode BID description objects.
	*
	*	@see	com.jsoft.pst.BID
	*/
	private abstract static class BIDBase extends SizedObject {

		/**	Construct a generic BID reader object.
		*
		*	@param	size	The number of bytes in this BID type.
		*
		*	@see com.jsoft.pst.BID#SIZE_ANSI
		*	@see com.jsoft.pst.BID#SIZE_UNICODE
		*/
		private BIDBase(final int size)
		{
			super(size);
		}

		/**	Create a String representation of the given BID object.
		*
		*	@param	o A BID to return a String representation of.
		*
		*	@return	A String describing this BID object.
		*/
		public String makeString(final Object o)
		{
			return ((BID)o).toString();
		}
	}

	/**	The BIDANSI class describes how to read and display a BID object from an ANSI PST file. */
	private static class BIDANSI extends BIDBase {

		/**	Construct an object to read in and display an ANSI BID. */
		private BIDANSI()
		{
			super(BID.SIZE_ANSI);
		}

		/**	Read in an ANSI BID object.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the ANSI BID object.
		*
		*	@return	The BID object read from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return new BID(byteBuffer.getInt());
		}
	}

	/**	The reader and displayer for ANSI BID objects. */
	static final BIDANSI bidAnsiReader = new BIDANSI();

	/**	The BIDUnicode class describes how to read and display a BID object in a Unicode PST file. */
	private static class BIDUnicode extends BIDBase {

		/**	Construct an object to read in and display a Unicode BID. */
		private BIDUnicode()
		{
			super(BID.SIZE_UNICODE);
		}

		/**	Read in a Unicode BID object.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the Unicode BID object.
		*
		*	@return	The BID object read from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return new BID(byteBuffer.getLong());
		}
	}

	/**	The reader and displayer for Unicode BID objects. */
	static final BIDUnicode bidUnicodeReader = new BIDUnicode();

	/**	Return an object for reading a BID for the given file type.
	*
	*	@param	fUnicode	A flag indicating whether the current PST file is Unicode or ANSI.
	*
	*	@return	A DataType suitable for reading in BIDs in the appropriate (ANSI or Unicode) file format.
	*
	*	@see	com.jsoft.pst.DataType.BIDANSI
	*	@see	com.jsoft.pst.DataType.BIDUnicode
	*	@see	com.jsoft.pst.XBlock#XBlock
	*/
	static DataType BIDFactory(final boolean fUnicode)
	{
		return fUnicode ? bidUnicodeReader : bidAnsiReader;
	}

	/**	The BREFBase class contains functionality used by both ANSI and Unicode BREF description objects.
	*
	*	@see	com.jsoft.pst.BREF
	*/
	private abstract static class BREFBase extends SizedObject {

		/**	Construct a generic BREF reader object.
		*
		*	@param	size	The number of bytes in this BREF type.
		*
		*	@see com.jsoft.pst.BREF#SIZE_ANSI
		*	@see com.jsoft.pst.BREF#SIZE_UNICODE
		*/
		BREFBase(final int size)
		{
			super(size);
		}

		/**	Create a String representation of the given BREF object.
		*
		*	@param	o	A BREF to return a String representation of.
		*
		*	@return	A String describing this BREF object.
		*/
		public String makeString(final Object o)
		{
			return ((BREF)o).toString();
		}
	}

	/**	The BREFAnsi class describes a BREF object in an ANSI PST file. */
	private static class BREFANSI extends BREFBase {

		/**	Construct an object to read in and display an ANSI BREF. */
		private BREFANSI()
		{
			super(BREF.SIZE_ANSI);
		}

		/**	Read in a Unicode BID object.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the ANSI BREF object.
		*
		*	@return	The BREF object read from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			BID bid = new BID(byteBuffer.getInt());
			IB ib = new IB(byteBuffer.getInt());
			return new BREF(bid, ib);
		}
	}

	/**	The reader and displayer for ANSI BREF objects. */
	public static final BREFANSI brefAnsiReader = new BREFANSI();

	/**	The BREFUnicode class describes a BREF object in a Unicode PST file. */
	private static class BREFUnicode extends BREFBase {

		/**	Construct an object to read in and display a Unicodde BREF. */
		private BREFUnicode()
		{
			super(BREF.SIZE_UNICODE);
		}

		/**	Read in a Unicode BREF object.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the Unicode BREF object.
		*
		*	@return	The BREF object read from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			BID bid = new BID(byteBuffer.getLong());
			IB ib = new IB(byteBuffer.getLong());
			return new BREF(bid, ib);
		}
	}

	/**	The reader and displayer for Unicode BREF objects. */
	public static final BREFUnicode brefUnicodeReader = new BREFUnicode();

	/**	The SizedByteArray class is used to read in and display an array of bytes whose size is known. */
	static class SizedByteArray extends SizedObject {

		/**	Create a reader/display manipulator for an array of bytes of known size.
		*
		*	@param	size	The number of bytes in the array.
		*/
		public SizedByteArray(final int size)
		{
			super(size);
		}

		/**	Create a String describing a array of bytes.
		*
		*	@param	o	The array of bytes to display.
		*
		*	@return	A String showing the bytes in the array in hexadecimal.
		*/
		public String makeString(final Object o)
		{
			byte[] a = (byte[])o;
			return com.jsoft.util.ByteUtil.createHexByteString(a);
		}

		/**	Read in an array of bytes of the given size.
		*
		*	@param	byteBuffer	The incoming data stream to read from. Note that this is entirely consumed.
		*	@param	size		The number of bytes to read in
		*
		*	@return	The array of bytes read in from the incoming data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer, final int size)
		{
			byte arr[] = new byte[size];
			byteBuffer.get(arr);
			return arr;
		}

		/**	Read in an array of bytes.
		*
		*	@param	byteBuffer	The incoming data stream to read from. Note that this is entirely consumed.
		*
		*	@return	The array of bytes read in from the incoming data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return read(byteBuffer, size);
		}
	}

	/**	The ByteArray class described an array of bytes taking up the remainder of byteBuffer. */
	private static class ByteArray extends SizedByteArray {

		/**	Construct an manipulator for a byte array. */
		private ByteArray()
		{
			super(0);
		}

		/**	Read in an array of bytes.
		*
		*	@param	byteBuffer	The incoming data stream to read from. Note that this is entirely consumed.
		*
		*	@return	The array of bytes read in from the incoming data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return read(byteBuffer, byteBuffer.remaining());
		}
	}

	/**	The reader for generic arrays of bytes. */
	private static final ByteArray byteArrayReader = new ByteArray();
	
	/**	The Floating64 data type described an 64-bit floating point value. */
	private static class Floating64 extends DataType {

		/**	Construct a manipulator for a PST PtypFloating64 data type. */
		private Floating64()
		{
			super();
		}

		/**	Create a String describing a byte array.
		*
		*	@param	o	The value to display.
		*
		*	@return	A String representing the 64-bit floating-point number.
		*/
		public String makeString(final Object o)
		{
			return ((Double)o).toString();
		}

		/**	Read in a 64-bit floating-point value.
		*
		*	@param	byteBuffer	The incoming data stream to read from..
		*
		*	@return	A Double object corresponding to the value read in.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return (Double)byteBuffer.getDouble();
		}

		/**	The size of the object in the PST file.
		*
		*	@return	The size of the 64-bit floating object in the PST file, in bytes.
		*/
		public int size()
		{
			return 8;
		}
	}

	/**	The reader for 64-bit floating values. */
	private static final Floating64 floating64Reader = new Floating64();

	/**	The GUID class describes how to read a 16-byte PST GUID in.
	*
	*	@see	com.jsoft.pst.GUID
	*/
	private static class GUID extends DataType {

		/**	Construct a GUID manipulator object. */
		private GUID()
		{
			super();
		}

		/**	Create a String from the passed GUID object.
		*
		*	@param	o	The GUID object to print out.
		*
		*	@return	A String representation of the GUID object.
		*/
		public String makeString(final Object o)
		{
			return ((com.jsoft.pst.GUID)o).toString();
		}

		/**	Read in a GUID from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the GUID.
		*
		*	@return	The GUID object read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			byte arr[] = new byte[com.jsoft.pst.GUID.SIZE];
			byteBuffer.get(arr);
			return new com.jsoft.pst.GUID(arr);
		}

		/**	Obtain the size of the GUID.
		*
		*	@return	The size of the GUID object in the PST file, in bytes.
		*/
		public int size()
		{
			return com.jsoft.pst.GUID.SIZE;
		}
	}

	/**	The manipulation object for PST GUIDs.
	*/
	private static final GUID guidReader = new GUID();

	/**	The HID class describes an HID object in a PST file.
	*
	*	@see	com.jsoft.pst.HeapOnNode
	*/
	private static class HID extends DataType {

		/**	Construct an HID manipulator object. */
		private HID()
		{
			super();
		}

		/**	Create a String from the passed HID object.
		*
		*	@param	o	The HID object to display.
		*
		*	@return	A String representation of the HID object.
		*/
		public String makeString(final Object o)
		{
			return ((com.jsoft.pst.HeapOnNode.HID)o).toString();
		}

		/**	Read in an HID from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the HID.
		*
		*	@return	The HID object read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return new com.jsoft.pst.HeapOnNode.HID(byteBuffer.getInt());
		}

		/**	Obtain the size of the HID in a PST file.
		*
		*	@return	The size of the HID object in the PST file, in bytes.
		*/
		public int size()
		{
			return 4;
		}
	}

	/**	The manipulator for HID objects in a PST file. */
	static final HID hidReader = new HID();

	/**	The Integer8 data type describes how to manipulate an 8-bit integer. */
	private static class Integer8 extends DataType {

		/**	Construct a manipulator for an 8-bit integer. */
		private Integer8()
		{
			super();
		}

		/**	Create a String from the passed Byte object.
		*
		*	@param	o	The Byte object to display.
		*
		*	@return	A String representation of the Byte object (in hexadecimal).
		*/
		public String makeString(final Object o)
		{
			return Integer.toHexString((Byte)o & 0xff);
		}

		/**	Read in an 8-bit integer from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the 8-bit integer.
		*
		*	@return	A Byte object corresponding to the 8-bit integer read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return (Byte)byteBuffer.get();
		}

		/**	Obtain the size of an 8-bit integer in a PST file.
		*
		*	@return	The size of an 8-bit integer in the PST file, in bytes.
		*/
		public int size()
		{
			return 1;
		}
	}

	/**	The manipulator for reading and displaying 8-bit integers. */
	static final Integer8 integer8Reader = new Integer8();

	/**	The Integer16 data type describes how to read and display a 16-bit integer. */
	private static class Integer16 extends DataType {

		/**	Construct a manipulator for an PST PtypInteger32 data type. */
		private Integer16()
		{
			super();
		}

		/**	Create a String from the passed Short object.
		*
		*	@param	o	The Short object to display.
		*
		*	@return	A String representation of the Short object (in hexadecimal).
		*/
		public String makeString(final Object o)
		{
			return Integer.toHexString((Short)o & 0xffff);
		}

		/**	Read in a 16-bit integer from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the 16-bit integer.
		*
		*	@return	A Short object corresponding to the 16-bit integer read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return (Short)byteBuffer.getShort();
		}

		/**	Obtain the size of a 16-bit integer.
		*
		*	@return	The size of a 16-bit integer in the PST file, in bytes.
		*/
		public int size()
		{
			return 2;
		}
	}

	/**	The reader/display manipulator for 16-bit integers in the PST file. */
	static final Integer16 integer16Reader = new Integer16();

	/**	The Integer32 data type describes a 32-bit integer. */
	private static class Integer32 extends DataType {

		/**	Construct a manipulator for an PST PtypInteger32 data type. */
		public Integer32()
		{
			super();
		}

		/**	Create a String from the passed Integer object.
		*
		*	@param	o	The Integer object to display.
		*
		*	@return	A String representation of the Integer object (in hexadecimal).
		*/
		public String makeString(final Object o)
		{
			return Integer.toHexString((Integer)o);
		}

		/**	Read in a 32-bit integer from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the 32-bit integer.
		*
		*	@return	An Integer object corresponding to the 32-bit integer read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return (Integer)byteBuffer.getInt();
		}

		/**	Obtain the size of a 32-bit integer in a PST file.
		*
		*	@return	The size of a 32-bit integer in the PST file, in bytes.
		*/
		public int size()
		{
			return 4;
		}
	}

	/**	The reader/display manipulator for 32-bit integers in the PST file. */
	static final Integer32 integer32Reader = new Integer32();

	/**	The Integer64 data type described a 64-bit integer. */
	private static class Integer64 extends DataType {

		/**	Construct a manipulator for an PST PtypInteger32 data type. */
		private Integer64()
		{
			super();
		}

		/**	Create a String from the passed Long object.
		*
		*	@param	o	The Long object to display.
		*
		*	@return	A String representation of the Long object (in hexadecimal).
		*/
		public String makeString(final Object o)
		{
			return Long.toHexString((Long)o);
		}

		/**	Read in a 64-bit integer from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the 64-bit integer.
		*
		*	@return	A Long object corresponding to the 64-bit integer read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return (Long)byteBuffer.getLong();
		}

		/**	Obtain the size of a 64-bit integer in a PST file.
		*
		*	@return	The size of a 64-bit integer in the PST file, in bytes.
		*/
		public int size()
		{
			return 8;
		}
	}

	/**	The reader/display manipulator for 64-bit integers. */
	static final Integer64 integer64Reader = new Integer64();

	/**	The MultipleBinary class describes how to read in multiple binary objects from a PST file. */
	private static class MultipleBinary extends DataType {

		/**	Create a manipulator for reading and displaying multiple binary objects. */
		private MultipleBinary()
		{
			super();
		}

		/**	Create a String representation of a list of binary object.
		*
		*	@param	o	The list of binary object to display.
		*
		*	@return	A String representation of the given list of binary objects.
		*/
		public String makeString(final Object o)
		{
			byte[][] arr = (byte[][])o;
			int iFirstList;
			for (iFirstList = 0; iFirstList < arr.length; ++iFirstList)
				if (arr[iFirstList] != null)
					break;
			if (iFirstList >= arr.length)
				return "multiple-binary - empty";
			return "multiple-binary " + com.jsoft.util.ByteUtil.createHexByteString(arr[iFirstList]) + "...";
		}

		/**	Read in a list of binary objects.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the list of binary objects.
		*
		*	@return	An array containing the binary objects read in, as an array of bytes.
		*
		*	@see	"[MS-OXDATA] Data Structures v20101026, Section 2.11.1"
		*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157583.aspx">Property Data Types (MSDN)</a>
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			if (byteBuffer.remaining() < 6) {
				// No room for count, offset, and data - the entire buffer must be the data!
				byte[][] arr = new byte[1][];
				arr[0] = new byte[byteBuffer.remaining()];
				byteBuffer.get(arr[0]);
				return arr;
			}
			final int count = byteBuffer.getInt();
			int[] offsets = new int[count+1];

			for (int i = 0; i < count; ++i)
				offsets[i] = byteBuffer.getInt();
			offsets[count] = offsets[0] + byteBuffer.remaining();

			int uniqueCount = 0;
			for (int i = 1; i < count+1; ++i) {
				if (offsets[i] != offsets[i-1])
					uniqueCount++;
			}

			byte[][] arr = new byte[uniqueCount][];
			for (int iSrc = 0, iDest = 0; iSrc < count; ++iSrc) {
				final int size = offsets[iSrc+1] - offsets[iSrc];
				if (size == 0)
					continue;
				arr[iDest] = new byte[size];

				byteBuffer.get(arr[iDest]);
				++iDest;
			}
			return arr;
		}

		/**	Obtain the size of the multiple binary object list in the PST file. Note that since this is of unknown size,
		*	0 is returned.
		*
		*	@return	0, since the size of the multiple binary object is unknown in the DataType context.
		*/
		public int size()
		{
			return 0;
		}
	}

	/**	The reader/display manipulator for lists containing multiple binary objects. */
	private static final MultipleBinary multipleBinaryReader = new MultipleBinary();

	/**	The MultipleInteger32 class describes how to read in multiple 32-bit integers from a PST file. */
	private static class MultipleInteger32 extends DataType {

		/**	Create a reader/display manipulator for a list of 32-bit integers. */
		private MultipleInteger32()
		{
			super();
		}

		/**	Create a String representation of a list of Integer values.
		*
		*	@param	o	The list of Integer values to display.
		*
		*	@return	A String representation of the given list of Integer values.
		*/
		public String makeString(final Object o)
		{
			StringBuilder s = new StringBuilder();
			for (int i : (int[])o) {
				if (s.length() > 0)
					s.append(',');
				s.append(Integer.toHexString(i));
			}
			return s.toString();
//			return "multiple-integer32 " + o.toString();
		}

		/**	Read in a list of 32-bit integers.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the list of 32-bit integers.
		*
		*	@return	An array containing the integers read in, as an array of Integer values.
		*
		*	@see	"[MS-OXDATA] Data Structures v20101026, Section 2.11.1"
		*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157583.aspx">Property Data Types (MSDN)</a>
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			final int count = byteBuffer.remaining()/(Integer.SIZE/Byte.SIZE);
			int[] arr = new int[count];
			java.nio.IntBuffer al = byteBuffer.asIntBuffer();
			al.get(arr);
			return arr;
		}

		/**	Obtain the size of the multiple 32-bit integer object list in the PST file.
		*
		*	@return	The size of a single 32-bit integer value.
		*/
		public int size()
		{
			return 4;
		}
	}

	/**	The reader/display manipulator for lists of 32-bit integers. */
	private static final MultipleInteger32 multipleInteger32Reader = new MultipleInteger32();

	/**	The MultipleString class describes how to read in a list of character strings from a PST file. */
	private static class MultipleString extends StringBase {

		/**	Create a manipulator for reading and displaying lists of PST-file strings. */
		private MultipleString(final short dataType)
		{
			super(dataType);
		}

		/**	Create a String representation of a list of Strings.
		*
		*	@param	o	The list of Strings to display.
		*
		*	@return	A String representation of the given list of Strings.
		*/
		public String makeString(final Object o)
		{
			return "multiple-string " + o.toString();
		}

		/**	Read in a list of strings.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the list of character strings.
		*
		*	@return	An array containing the character strings read in, as an array of Strings.
		*
		*	@see	"[MS-OXDATA] Data Structures v20101026, Section 2.11.1"
		*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157583.aspx">Property Data Types (MSDN)</a>
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		throws
			java.io.UnsupportedEncodingException
		{
			if (byteBuffer.remaining() <= 8) {
				byte[] data = new byte[byteBuffer.remaining()];
				byteBuffer.get(data);
				String[] arr = new String[1];
				arr[0] = new String(data, charset); 
				return arr;
			}

			final int count = byteBuffer.getInt();
			int[] offsets = new int[count+1];
			for (int i = 0; i < count; ++i)
				offsets[i] = byteBuffer.getInt();

			offsets[count] = offsets[0] + byteBuffer.remaining();

			int uniqueCount = 0;
			for (int i = 1; i < count+1; ++i) {
				if (offsets[i] != offsets[i-1])
					uniqueCount++;
			}

			String[] arr = new String[uniqueCount];
			for (int iSrc = 0, iDest = 0; iSrc < count; ++iSrc) {
				final int size = offsets[iSrc+1] - offsets[iSrc];
				if (size == 0)
					continue;
				byte[] data = new byte[size];
				byteBuffer.get(data);
				arr[iDest] = new String(data, charset);
			}
			return arr;
		}
	}

	/**	The reader/display manipulator for lists of multiple Unicode strings. */
	private static final MultipleString multipleString8Reader = new MultipleString(STRING_8);

	/**	The reader/display manipulator for lists of multiple Unicode strings. */
	private static final MultipleString multipleStringReader = new MultipleString(STRING);

	/**	The NID class describes how to read and display an NID object in a PST file. */
	private static class NID extends DataType {

		/**	Create a reader/display manipulator for PST NID objects. */
		public NID()
		{
			super();
		}

		/**	Create a String representation of an NID.
		*
		*	@param	o	The NID object to display.
		*
		*	@return	A String representation of the given NID.
		*/
		public String makeString(final Object o)
		{
			return ((com.jsoft.pst.NID)o).toString();
		}

		/**	Read in a NID from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the NID.
		*
		*	@return	An NID object corresponding to the NID value read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return new com.jsoft.pst.NID(byteBuffer.getInt());
		}

		/**	Obtain the size of an NID object in a PST file.
		*
		*	@return	The size of an NID in the PST file, in bytes.
		*/
		public int size()
		{
			return 4;
		}
	}

	/**	The NID reader/manipulator object. */
	static final NID nidReader = new NID();

	/**	The PSTBoolean data type describes how to read in and display a boolean value in a PST file. */
	private static class PSTBoolean extends DataType {

		/**	Create a reader/display manipulator object for PST file Boolean values. */
		private PSTBoolean()
		{
			super();
		}

		/**	Create a String representation of a Boolean
		*
		*	@param	o	The Boolean to display.
		*
		*	@return	A String representation of the given Boolean value.
		*/
		public String makeString(final Object o)
		{
			return o.toString();
		}

		/**	Read in a Boolean from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the Boolean value.
		*
		*	@return	A Boolean object corresponding to the Boolean read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			return (Boolean)((Byte)byteBuffer.get() != 0);
		}

		/**	Obtain the size of a Boolean object in a PST file.
		*
		*	@return	The size of an Boolean value in the PST file, in bytes.
		*/
		public int size()
		{
			return 1;
		}
	}

	/**	A reader/display manipulator for Boolean values in the PST file. */
	static final PSTBoolean booleanReader = new PSTBoolean();

	/**	The StringBase class contains logic shared by the manipulators for single strings and lists of strings. */
	private abstract static class StringBase extends DataType {

		/**	The character set encoding of the String values read and displayed by this manipulator. */
		protected final String charset;

		/**	Create a manipulator for string objects.
		*
		*	@param	dataType	The actual data type.
		*
		*	@see	#STRING
		*	@see	#STRING_8
		*/
		private StringBase(final short dataType)
		{
			super();
			this.charset = dataType == STRING ? CHARSET_WIDE : CHARSET_NARROW;
			assert dataType == STRING || dataType == STRING_8;
		}

		/**	Obtain the size of the string object or list in the PST file. Note that since this is of unknown size,
		*	0 is returned.
		*
		*	@return	0, since the size of a string is unknown in the DataType context.
		*/
		public int size()
		{
			return 0;
		}
	}

	/**	The PSTString class describes a string (Unicode or ASCII) which occupies the remainder of the given byteBuffer. */
	private static class PSTString extends StringBase {

		/**	Create a manipulator for string objects.
		*
		*	@param	dataType	The actual data type.
		*
		*	@see	#STRING
		*	@see	#STRING_8
		*/
		private PSTString(final short dataType)
		{
			super(dataType);
		}

		/**	Create a String representation of a String (to be consistent with other data types).
		*
		*	@param	o	The String to display.
		*
		*	@return	The given String.
		*/
		public String makeString(final Object o)
		{
			return (String)o;
		}

		/**	Read in a String from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the data.
		*
		*	@return	A String corresponding to the Boolean read in from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		throws
			java.io.UnsupportedEncodingException
		{
			byte arr[] = new byte[byteBuffer.remaining()];
			byteBuffer.get(arr);
			return new String(arr, charset);
		}
	}

	/**	A reader/display manipulator for wide-character (Unicode) character strings values in the PST file. */
	private static final PSTString stringReader = new PSTString(STRING); 

	/**	A reader/display manipulator for narrow-character (ANSI) character strings values in the PST file. */
	private static final PSTString string8Reader = new PSTString(STRING_8); 

	/**	The Time class represents an MS Time object. It is converted on input to a standard Java Date object.
	*
	*	@see	"[MS-OXDATA] Data Structures v20101026, Section 2.11.1"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157583.aspx">Property Data Types (MSDN)</a>
	*/
	private static class Time extends DataType {

		/**	The base for MS time, which is measured in hundreds of nanosecondss since January 1, 1601. */
		private static final java.util.Date PST_BASE_TIME = initBaseTime();

		/**	The format to use when converting time objects to strings. */
		private static final java.text.SimpleDateFormat OUTPUT_FORMAT = new java.text.SimpleDateFormat("MMMM dd, yyyy hh:mm:ss");

		/**	Initialize the base time; exit on exception. */
		private static java.util.Date initBaseTime()
		{
			try {
				final java.text.SimpleDateFormat PST_BASE_FORMAT = new java.text.SimpleDateFormat("MMMM dd, yyyy");
				return PST_BASE_FORMAT.parse("January 1, 1601");
			} catch (java.text.ParseException e) {
				e.printStackTrace(System.out);
				System.exit(1);
			}
			return new java.util.Date();
		}

		/**	Create a time reader/display manipulation object. */
		private Time()
		{
			super();
		}

		/**	Create a String representation of a Date.
		*
		*	@param	o	The Date to display.
		*
		*	@return	A String representation of the given object, formatted according to {@link #OUTPUT_FORMAT}.
		*
		*	@see	#OUTPUT_FORMAT
		*/
		public String makeString(final Object o)
		{
			return OUTPUT_FORMAT.format((java.util.Date)o);
		}

		/**	Read in an MS time from the data stream.
		*
		*	@param	byteBuffer	The incoming data stream from which to read the time.
		*
		*	@return	A Java Date object corresponding to the MS time read from the data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			long hundred_ns = byteBuffer.getLong();
			long ms = hundred_ns/10000;
			ms += PST_BASE_TIME.getTime();
			return new java.util.Date(ms);
		}

		/**	Obtain the size in bytes of an MS time object in a PST file.
		*
		*	@return	The size of an MS time object in a PST file.
		*/
		public int size()
		{
			return 8;
		}

		/**	A reader/display manipulation object for times in PST files. */
		private static final Time reader = new Time();
	}


	/**	The SizedInteger16Array class described how to read and display an array of 16-bit integers whose size is known. */
	static class SizedInt16Array extends SizedObject {

		/**	Create a reader/display manipulator for an array of 16-bit integers of known size.
		*
		*	@param	size	The number of 16-bit integers in the array.
		*/
		public SizedInt16Array(final int size)
		{
			super(size);
		}

		/**	Create a String describing an array of shorts array.
		*
		*	@param	o	The array of shorts to display.
		*
		*	@return	A String showing the shorts in the array.
		*/
		public String makeString(final Object o)
		{
			short[] a = (short[])o;
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < size; ++i) {
				if (i > 0)
					s.append(' ');
				s.append(a[i]);
			}
			return s.toString();
		}

		/**	Read in an array of 16-bit integers of the predetermined size.
		*
		*	@param	byteBuffer	The incoming data stream to read from.
		*
		*	@return	The array of shorts read in from the incoming data stream.
		*/
		public Object read(java.nio.ByteBuffer byteBuffer)
		{
			short arr[] = new short[size];
			for (int i = 0; i < size; ++i)
				arr[i] = byteBuffer.getShort();
			return arr;
		}

		/**	Return the size of the array of 16-bit integers.
		*
		*	@return	The size of the array of 16-bit integers in the PST file.
		*/
		public int size()
		{
			return size*2;
		}
	}

	/**	Create a object for reading data of given property type, with the given size if required.
	*
	*	@param	propertyType	The property type to obtain a reader/display manipulator for.
	*
	*	@return	A reader/display manipulator for the given type.
	*
	*	@see	#booleanReader
	*	@see	#byteArrayReader
	*	@see	#floating64Reader
	*	@see	#guidReader
	*	@see	#integer16Reader
	*	@see	#integer32Reader
	*	@see	#integer64Reader
	*	@see	#multipleBinaryReader
	*	@see	#multipleInteger32Reader
	*	@see	#multipleStringReader
	*	@see	#string8Reader
	*	@see	#stringReader
	*	@see	Time#reader
	*/
	static DataType definitionFactory(final short propertyType)
	{
		switch (propertyType) {
		case INTEGER_16:
			return integer16Reader;

		case INTEGER_32:
			return integer32Reader;

		case FLOATING_32:
			throw new RuntimeException("Property Type " + Integer.toHexString(propertyType) +" not implemented");

		case FLOATING_64:
			return floating64Reader;

		case CURRENCY:
		case FLOATING_TIME:
		case ERROR_CODE:
			throw new RuntimeException("Property Type " + Integer.toHexString(propertyType) +" not implemented");

		case BOOLEAN:
			return booleanReader;

		case OBJECT:
			return byteArrayReader;

		case INTEGER_64:
			return integer64Reader;

		case STRING_8:
			return string8Reader;

		case STRING:
			return stringReader;

		case TIME:
			return Time.reader;

		case GUID:
			return guidReader;

		case SERVER_ID:
		case RESTRICTION:
		case RULE_ACTION:
			throw new RuntimeException("Property Type " + Integer.toHexString(propertyType) +" not implemented");

		case BINARY:
			return byteArrayReader;

		case MULTIPLE_INTEGER_16:
			throw new RuntimeException("Property Type " + Integer.toHexString(propertyType) +" not implemented");

		case MULTIPLE_INTEGER_32:
			return multipleInteger32Reader;

		case MULTIPLE_FLOATING_32:
		case MULTIPLE_FLOATING_64:
		case MULTIPLE_FLOATING_TIME:
			throw new RuntimeException("Property Type " + Integer.toHexString(propertyType) +" not implemented");

		case MULTIPLE_STRING_8:
			return multipleString8Reader;

		case MULTIPLE_STRING:
			return multipleStringReader;

		case MULTIPLE_GUID:
			throw new RuntimeException("Property Type " + Integer.toHexString(propertyType) +" not implemented");

		case MULTIPLE_BINARY:
			return multipleBinaryReader;

		default:
			throw new RuntimeException("Invalid Property Type " + Integer.toHexString(propertyType));
			
		}
	}

	/**	Return a String representing Object o with the data type given by tag.
	*
	*	@param	tag	The property tag describing the object to display.
	*	@param	o	The object to display.
	*
	*	@return	A String describing the object, given that it is of the type defined by tag.
	*/
	static String makeString(final int tag, final Object o)
	{
		final DataType writer = definitionFactory((short)(tag & 0xffff));
		return writer.makeString(o);
	}
}