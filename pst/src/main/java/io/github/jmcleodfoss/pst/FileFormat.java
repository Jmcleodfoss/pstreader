package io.github.jmcleodfoss.pst;

/**	The FileFormat class contains the file format (ANSI or Unicode) of the PST file.
*	@see	Header
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/c9876f5a-664b-46a3-9887-ba63f113abf5">MS-PST Section 2.2.2.6: Header</a>
*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
*/
class FileFormat
{
	/**	A value indicating that this is an ANSI PST file (one of two values indicating this).
	*	@see	#VER_ANSI_2
	*	@see	#VER_UNICODE_MIN
	*	@see	#VER_OST_2013
	*/
	private static final short VER_ANSI_1 = 14;

	/**	A value indicating that this is an ANSI PST file (one of two values indicating this).
	*	@see	#VER_ANSI_1
	*	@see	#VER_UNICODE_MIN
	*	@see	#VER_OST_2013
	*/
	private static final short VER_ANSI_2 = 15;

	/**	The value indicating that this is an Unicode PST file.
	*	@see	#VER_ANSI_1
	*	@see	#VER_ANSI_2
	*	@see	#VER_OST_2013
	*/
	private static final short VER_UNICODE_MIN = 23;

	/**	The value indicating that this is an OST-2013 file
	*	@see <a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
	*	@see	#VER_ANSI_1
	*	@see	#VER_ANSI_2
	*	@see	#VER_UNICODE_MIN
	*/
	private static final short VER_OST_2013 = 36;

	/**	The file type index for the current file. */
	final Index index;

	/**	A flag indicating whether the file uses wide tesxt. If it isn't Unicode, it must be ANSI. */
	final boolean fUnicode;

	/**	Internal indexes used to describe versions of files (to govern certain object sizes) */
	enum Index {
		/**	The original ANSI file type - not tested for this library due to lack of availability. */
		ANSI(0),

		/**	The modern UNICODE file type. */
		UNICODE(1),

		/**	An OST file from 2013 on.
		*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
		*/
		OST_2013(2);

		/** An index into arrays where each entry corresponds to a file type (some data definitions, for example) */
		private final int index;

		/** Initialize the index.
		*	@param	index The index this value maps to.
		*/
		Index(int index)
		{
			this.index = index;
		}

		/** Retrive the index for this enum value.
		*	@return	THe index for this value.
		*/
		int getIndex()
		{
			return index;
		}

		/** Provide a String for display
		*	@return	A String describing the enum object.
		*/
		@Override
		public String toString()
		{
			switch(this){
			case ANSI:
				return "ANSI";

			case UNICODE:
				return "Unicode";

			case OST_2013:
				return "OST-2013";

			default:
				return "Unknown";
			}
		}
	}

	/**	The UnknnownFileFormatVersionException is thrown if one tries to create an FileFormat object with an invalid value for
	*	the file format.
	*/
	static class UnknownFileFormatVersionException extends RuntimeException
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

	/**	Create a FileFormat object from the given version.
	*	@param	wVer	The version number as found in the PST's header file.
	*/
	FileFormat(short wVer)
	{
		if (wVer == VER_ANSI_1 || wVer == VER_ANSI_2) {
			fUnicode = false;
			index = Index.ANSI;
		} else if (wVer >= VER_UNICODE_MIN) {
			fUnicode = true;
			if (wVer == VER_OST_2013)
				index = Index.OST_2013;
			else
				index = Index.UNICODE;
		} else {
			throw new UnknownFileFormatVersionException(wVer);
		}
		HeapOnNode.HID.setOst2013(index);
	}

	/**	Obtain a string describing this file format.
	*	@return	A string describing this file format.
	*/
	@Override
	public String toString()
	{
		return index.toString();
	}

	/**	Test ths class by indicating whether a file is unicode or not.
	*	@param	args	The file(s) to show the file format for.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.FileFormat pst-file [pst-file ...]");
			System.exit(1);
		}

		for (final String a: args) {
			try {
				final PST pst = new PST(a);
				System.out.printf("%s: %s%n", a, pst.header.fileFormat.toString());
				pst.close();
			} catch (final	BadXBlockLevelException
				|	BadXBlockTypeException
				|	CRCMismatchException
				|	DataOverflowException
				|	IncorrectNameIDStreamContentException
				|	NameIDStreamNotFoundException
				|	NotHeapNodeException
				|	NotPropertyContextNodeException
				|	NotTableContextNodeException
				|	NullDataBlockException
				|	NullNodeException
				|	UnimplementedPropertyTypeException
				|	UnknownClientSignatureException
				|	UnknownPropertyTypeException
				|	UnparseablePropertyContextException
				|	UnparseableTableContextException e) {
				System.out.println(e);
				e.printStackTrace(System.out);
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				System.out.println(e);
				e.printStackTrace(System.out);
			}
		}
	}
}

