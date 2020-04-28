package io.github.jmcleodfoss.pst;

/**	The GUID class holds a PST GUID.
*
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.5"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386696(v=office.12).aspx">Calculated Properties (MSDN)</a>
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 1.3.2"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/gg318108.aspx">Commonly Used Property Sets (MSDN)</a>
*/
class GUID {

	/**	The size of a GUID, in bytes */
	static final int SIZE = 16;

	/**	The null GUID (used for unassigned or unknown GUIDs). */
	static final GUID PS_NULL = createGUID(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

	/**	The public strings property set GUID */
	static final GUID PS_PUBLIC_STRINGS = createGUID(new byte[]{0x00, 0x02, 0x03, 0x29, 0x00, 0x00, 0x00, 0x00, (byte)0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46});

	/**	The common property set GUID */
	static final GUID PS_COMMON = createGUID(new byte[]{0x00, 0x06, 0x20, 0x08, 0x00, 0x00, 0x00, 0x00, (byte)0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46});

	/**	The address property set GUID */
	static final GUID PS_ADDRESS = createGUID(new byte[]{0x00, 0x06, 0x20, 0x04, 0x00, 0x00, 0x00, 0x00, (byte)0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46});

	/**	The appointment property set GUID */
	static final GUID PS_APPOINTMENT = createGUID(new byte[]{0x00, 0x06, 0x20, 0x02, 0x00, 0x00, 0x00, 0x00, (byte)0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46});

	/**	The meetings property set GUID. Note that the documented value does not match what has been observed in PST files. */
//	static final GUID PS_MEETING = createGUID(new byte[]{0x6E, (byte)0xD8, (byte)0xDA, (byte)0x90, 0x0B, 0x45, 0x1B, 0x10, (byte)0x98, (byte)0xDA, 0x00, (byte)0xAA, 0x00, 0x3F, 0x13, 0x05});
	static final GUID PS_MEETING = createGUID(new byte[]{0x6E, (byte)0xD8, (byte)0xDA, (byte)0x90, 0x45, 0x0B, 0x1B, 0x10, (byte)0x98, (byte)0xDA, 0x00, (byte)0xAA, 0x00, 0x3F, 0x13, 0x05});

	/**	The sticky notes property set GUID */
	static final GUID PS_NOTE = createGUID(new byte[]{0x00, 0x06, 0x20, 0x0E, 0x00, 0x00, 0x00, 0x00, (byte)0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46});

	/**	The task property set GUID */
	static final GUID PS_TASK = createGUID(new byte[]{0x00, 0x06, 0x20, 0x03, 0x00, 0x00, 0x00, 0x00, (byte)0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46});

	/**	The MAPI property set GUID */
	static final GUID PS_MAPI = createGUID(new byte[]{0x00, 0x02, 0x03, 0x28, 0x00, 0x00, 0x00, 0x00, (byte)0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x46});

	/**	The (unknown use) property set GUID */
	static final GUID PS_INTERNAL = createGUID(new byte[]{(byte)0xc1, (byte)0x84, 0x32, (byte)0x81, (byte)0x85, 0x05, (byte)0xd0, 0x11, (byte)0xb2, (byte)0x90, 0x00, (byte)0xaa, 0x00, 0x3c, (byte)0xf6, (byte)0x76});

	/**	The ProviderUID for a one-off recipient in a distribution list. */
	static final GUID PROVIDER_UID_ONE_OFF = createGUID(new byte[]{(byte)0x81, (byte)0x2b, (byte)0x1f, (byte)0xa4, (byte)0xbe, (byte)0xa3, (byte)0x10, (byte)0x19, (byte)0x9d, (byte)0x6e, (byte)0x00, (byte)0xdd, (byte)0x01, (byte)0x0f, (byte)0x54, (byte)0x02});

	/**	The actual GUID */
	final byte[] guid;

	/**	Create a GUID from the bytes in the given array.
	*
	*	@param	arr	The array of bytes from which to construct the GUID.
	*/
	GUID(byte[] arr)
	{
		this(arr, 0);
	}

	/**	Create a GUID from the bytes at the given offset in the given array.read in from the PST file. Note that the first four
	*	bytes form a little-endian 16 bit value, and the remaining bytes form big-endian values.
	*
	*	@param	arr	The array of bytes containing the GUID
	*	@param	offset	The offset to the start of the GUID in arr.
	*/
	GUID(byte[] arr, int offset)
	{
		byte[] guid = new byte[SIZE];
		int iSrc, iDest;
		for (iSrc = offset, iDest = 0; iDest < 4; ++iSrc, ++iDest)
			guid[3-iDest] = arr[iSrc];
		for (; iDest < SIZE; ++iSrc, ++iDest)
			guid[iDest] = arr[iSrc];
		this.guid = guid;
	}

	/**	Create a GUID from the given bytes assuming they are in the correct
	*	order (normally, bytes 0-3 are little-endian and the constructor
	*	reverses these, but it is convenient here to show them in the order
	*	they appear to make it easier to check them visually)
	*
	*	@param	arr	The array of bytes containing the GUID
	*	@return	A GUID object consisting of the bytes passed in.
	*/
	private static GUID createGUID(byte[] arr)
	{
		byte[] arr2 = {	arr[ 3], arr[ 2], arr[ 1], arr[ 0],
				arr[ 4], arr[ 5], arr[ 6], arr[ 7],
				arr[ 8], arr[ 9], arr[10], arr[11],
				arr[12], arr[13], arr[14], arr[15] };
		return new GUID(arr2);
	}
	/**	Compare two GUIDs.
	*
	*	@param	o	The other GUID to check.
	*
	*	@return	false if the other guid differs from this one, true if it is the same as this one.
	*/
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;

		if (!(o instanceof GUID))
			return false;

		if (o == null)
			return this == null;

		final GUID guid = (GUID)o;
		for (int i = 0; i < this.guid.length; ++i) {
			if (this.guid[i] != guid.guid[i])
				return false;
		}

		return true;
	}

	/**	Calculate hashcode.
	*
	*	@return	Hashcode for the guid.
	*/
	@Override
	public int hashCode()
	{
		int hashcode = 0;
		for (int i = 0; i < guid.length; ++i) {
			hashcode += guid[i];
		}

		return hashcode;
	}

	/**	Obtain a string representation of this GUID.
	*
	*	@return	A string describing the GUID in PST "Canonical" format (00000000-0000-0000-0000-0000000000).
	*/
	@Override
	public String toString()
	{
		final int[] blockOffsets = {4, 6, 8, 10, SIZE}; 
		String s = new String("");

		int i = 0;
		for (int b = 0; b < blockOffsets.length; ++b) {
			if (b > 0)
				s += "-";

			for (; i < blockOffsets[b]; ++i) 
				s += String.format("%02x", guid[i] & 0xff);
		}

		return s;
	}

	/**	Test this class by printing out the known GUIDs
	*
	*	@param	args	The command line arguments to the test application (unused).
	*/
	public static void main(String[] args)
	{
		String format = "%15s\t%s\n";

		System.out.printf(format, "Name", "GUID");
		System.out.printf(format, "____", "____");
		System.out.printf(format, "Null", PS_NULL);
		System.out.printf(format, "Public Strings", PS_PUBLIC_STRINGS);
		System.out.printf(format, "Common", PS_COMMON);
		System.out.printf(format, "Address", PS_ADDRESS);
		System.out.printf(format, "Appointment", PS_APPOINTMENT);
		System.out.printf(format, "MEETING", PS_MEETING);
		System.out.printf(format, "Task", PS_TASK);
		System.out.printf(format, "Note", PS_NOTE);
		System.out.printf(format, "MAPI", PS_MAPI);
		System.out.printf(format, "Internal", PS_INTERNAL);
	}
}
