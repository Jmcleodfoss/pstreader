package io.github.jmcleodfoss.pst;

/**	The BID class represents a PST file block ID.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/d3155aa1-ccdd-4dee-a0a9-5363ccca5352">MS-PST Section 2.2.2.2: BID (Block ID)</a>
*/
class BID implements NodeKey {

	/**	The size of a BID in an ANSI PST file.
	*	@see	#SIZE_UNICODE
	*	@see	#size
	*/
	static final int SIZE_ANSI = 4;

	/**	The size of a BID in a Unicode PST file.
	*	@see	#SIZE_ANSI
	*	@see	#size
	*/
	static final int SIZE_UNICODE = 8;

	/**	When searching for blocks, the Internal flag must be cleared.
	*	@see	BID#key
	*/
	private static final long SEARCH_MASK = 0xfffffffffffffffeL;

	/**	The look-up key for this block ID, which is used when searching for it in the block B-tree.
	*	@see	BID#key()
	*/
	private final long key;

	/**	The block ID. */
	final long bid;

	/**	Is the block ID is internal (metadata) or external (user data)? */
	final boolean fInternal;

	/**	Construct a block ID from the byte sequence read directly out of a PST file.
	*	@param	rawBid	The raw data as read in from the file (a 64-bit little-endian value);
	*/
	BID(final long rawBid)
	{
		key = rawBid & SEARCH_MASK;
		fInternal = (rawBid & 0x02) != 0x00;
		bid = rawBid >> 2;
	}

	/**	Is this BID empty?
	*	@return	true if the BID key is 0, false otherwise (in which case it is assumed to be valid).
	*/
	boolean isNull()
	{
		return key == 0;
	}

	/**	Retrieve the lookup key for this block ID {@link BID}. Note that the reserved bit (LSB) must be masked off from the BID
	*	before searching for it in the block B-tree.
	*	@return	The search key for this block ID
	*	@see	#key
	*/
	public long key()
	{
		return key;
	}

	/**	Retrieve the size of a block ID object (which is different under Unicode and ANSI).
	*	@param	format	The FileFormat object as read in from the PST header and stored in the {@link io.github.jmcleodfoss.pst.Header}
	*			object in the {@link io.github.jmcleodfoss.pst.PSTFile} object.
	*	@return	The size of the BID object for the given file format.
	*	@see	#SIZE_ANSI
	*	@see	#SIZE_UNICODE
	*/
	static int size(final FileFormat format)
	{
		return format.fUnicode ? SIZE_UNICODE : SIZE_ANSI;
	}

	/**	Return a brief description of this block ID. This is typically used only for debugging.
	*/
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder(String.format("key 0x%08x 0x%08x", key, bid));
		if (fInternal)
			s.append(" (internal)");
		return s.toString();
	}
}
