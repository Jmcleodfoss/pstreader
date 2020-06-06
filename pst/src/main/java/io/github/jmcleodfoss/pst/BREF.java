package io.github.jmcleodfoss.pst;

/**	The BREF class is a block reference. Objects of this class may be used to find blocks in a PST file.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/844a5ebf-488a-45fd-8fce-92a84d8e24a3"> MS-PST: Outlook Personal Folders (.pst) File Format, section 2.2.2.4: BREF</a>
*/
class BREF {

	/**	The size of an BREF object in ANSI files. */
	static final int SIZE_ANSI = BID.SIZE_ANSI + IB.SIZE_ANSI;

	/**	The size of an BREF object in Unicode files. */
	static final int SIZE_UNICODE = BID.SIZE_UNICODE + IB.SIZE_UNICODE;

	/**	The block ID of the block this BREF object refers to /*/
	final BID bid;

	/**	The byte index of this block within the file */
	final IB ib;

	/**	Create a BREF object the given BID and IB.
	*	@param	bid	The block ID of this BREF.
	*	@param	ib	The block index of this BREF.
	*/
	public BREF(final BID bid, final IB ib)
	{
		this.bid = bid;
		this.ib = ib;
	}

	/**	Describe a BREF object.
	*	@return	A string represnetation of this BREF object.
	*/
	@Override
	public String toString()
	{
		return "BID " + bid + ", IB " + ib;
	}
}

