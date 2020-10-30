package io.github.jmcleodfoss.pst;

/**	The IncorrectNameIDStreamContentException is thrown when either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
*	@see	Header#validate_CRC
*/
public class IncorrectNameIDStreamContentException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a IncorrectNameIDStreamContentException. */
	IncorrectNameIDStreamContentException(String typeExpected, String typeFound)
	{
		super(String.format("Reading Name ID Mapping %s stream, found %s", typeExpected, typeFound));
	}
}

