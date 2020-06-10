package io.github.jmcleodfoss.pst;

/**	The FileFormat class contains the file format (ANSI or Unicode) of the PST file.
*	@see	Header
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/c9876f5a-664b-46a3-9887-ba63f113abf5">MS-PST Section 2.2.2.6: Header</a>
*/
class FileFormat
{
	/**	The UnknnownFileFormatVersionException is thrown if one tries to create an FileFormat object with an invalid value for
	*	the file format.
	*/
	class UnknownFileFormatVersionException extends RuntimeException
	{
		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	Create an UnknownFileFormatVersionException object indicating the invalid file format encountered.
		*	@param	wVer	The file format version number encountered.
		*/
		UnknownFileFormatVersionException(short wVer)
		{
			super("Unknown file format version " + wVer); 
		}
	}

	/**	A value indicating that this is an ANSI PST file (one of two values indicating this).
	*	@see	#VER_ANSI_2
	*	@see	#VER_UNICODE_MIN
	*/
	private static final short VER_ANSI_1 = 14;

	/**	A value indicating that this is an ANSI PST file (one of two values indicating this).
	*	@see	#VER_ANSI_1
	*	@see	#VER_UNICODE_MIN
	*/
	private static final short VER_ANSI_2 = 15;

	/**	The value indicating that this is an Unicode PST file.
	*	@see	#VER_ANSI_1
	*	@see	#VER_ANSI_2
	*/
	private static final short VER_UNICODE_MIN = 23;

	/**	A flag indicating whether the file is Unicode. If it isn't Unicode, it must be ANSI. */
	final boolean fUnicode;

	/**	Create a FileFormat object from the given version.
	*	@param	wVer	The version number as found in the PST's header file.
	*/
	FileFormat(short wVer)
	{
		if (wVer == VER_ANSI_1 || wVer == VER_ANSI_2) {
			fUnicode = false;
		} else if (wVer >= VER_UNICODE_MIN) {
			fUnicode = true;
		} else {
			throw new UnknownFileFormatVersionException(wVer);
		}
	}

	/**	Obtain a string describing this file format.
	*	@return	A string describing this file format.
	*/
	@Override
	public String toString()
	{
		return fUnicode ? "Unicode" : "ANSI";
	}

	/**	Test ths class by indicating whether a file is unicode or not.
	*	@param	args	The file(s) to show the file format for.
	*/
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.FileFormat pst-filename [pst-filename...]");
			System.exit(1);
		}

		try {
			for (String a: args) {
				PST pst = new PST(a);
				System.out.printf("%s: %s\n", a, pst.unicode() ? "Unicode" : "ANSI");
			}
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		}
	}
}

