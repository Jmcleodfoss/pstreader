package com.jsoft.pst;

/**	The IB class is a byte index into the PST file. The primary purpose of this class is to provide type safety and a useful
*	version of {@link #toString}
*
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.2.2.3"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386553(v=office.12).aspx">IB (Byte Index) (MSDN)</a>
*/
class IB {

	/**	The size of an IB object in ANSI files. */
	static final int SIZE_ANSI = 4;

	/**	The size of an IB object in Unicode files. */
	static final int SIZE_UNICODE = 8;

	/**	The byte index (offset) into the PST file. */
	final long ib;

	/**	Create an IB object from a long value read our of a PST file.
	*
	*	@param	ib	The block index to store in the IB object.
	*/
	IB(final long ib)
	{
		this.ib = ib;
	}

	/**	Describe an IB object. This is typically used for debugging.
	*
	*	@return	A string representation of the IB object.
	*/
	@Override
	public String toString()
	{
		return Long.toHexString(ib);
	}
}
