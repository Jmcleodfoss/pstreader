package io.github.jmcleodfoss.pst;

/**	The IB class is a byte index into the PST file. The primary purpose of this class is to provide type safety and a useful
*	version of {@link #toString}
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/7d53d413-b492-4483-b624-4e2fa2a08cf3">MS PST: Outlook Personal Folders (.pst) File Format, Section 2.2.2.3: IB (Byte Index)</a>
*/
class IB {

	/**	The size of an IB object in ANSI files. @see BREF#SIZE_ANSI */
	static final int SIZE_ANSI = 4;

	/**	The size of an IB object in Unicode files. @see BREF#SIZE_UNICODE */
	static final int SIZE_UNICODE = 8;

	/**	The byte index (offset) into the PST file. */
	final long ib;

	/**	Create an IB object from a long value read our of a PST file.
	*	@param	ib	The block index to store in the IB object.
	*/
	IB(final long ib)
	{
		this.ib = ib;
	}

	/**	Describe an IB object. This is typically used for debugging.
	*	@return	A string representation of the IB object.
	*/
	@Override
	public String toString()
	{
		return Long.toHexString(ib);
	}
}
