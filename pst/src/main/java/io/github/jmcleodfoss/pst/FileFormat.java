package io.github.jmcleodfoss.pst;

/**	The FileFormat class contains the file format (ANSI or Unicode) of the PST file.
*	@see	Header
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/c9876f5a-664b-46a3-9887-ba63f113abf5">MS-PST Section 2.2.2.6: Header</a>
*/
class FileFormat {

	/**	The UnknnownFileFormatVersionException is thrown if one tries to create an FileFormat object with an invalid value for
	*	the file format.
	*/
	class UnknownFileFormatVersionException extends RuntimeException {

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
	*	@see	#VER_UNICODE
	*/
	private static final short VER_ANSI_1 = 14;

	/**	A value indicating that this is an ANSI PST file (one of two values indicating this).
	*	@see	#VER_ANSI_1
	*	@see	#VER_UNICODE
	*/
	private static final short VER_ANSI_2 = 15;

	/**	The value indicating that this is an Unicode PST file.
	*	@see	#VER_ANSI_1
	*	@see	#VER_ANSI_2
	*/
	private static final short VER_UNICODE = 23;

	/**	A flag indicating whether the file is Unicode. If it isn't Unicode, it must be ANSI. */
	final boolean fUnicode;

	/**	Create a FileFormat object from the given version.
	*	@param	wVer	The version number as found in the PST's header file.
	*/
	FileFormat(short wVer)
	{
		switch (wVer){
		case VER_ANSI_1:
		case VER_ANSI_2:
			fUnicode = false;
			break;

		case VER_UNICODE:
			fUnicode = true;
			break;

		default:
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
}

