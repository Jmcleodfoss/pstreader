package io.github.jmcleodfoss.pst;

/**	The CRCMismatchException is thrown when a file's calculated CRC does not match the expected value.
*	@see	Header#validate_CRC
*/
public class CRCMismatchException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a CRCMismatchException. */
	CRCMismatchException(String fieldName, int crcCalculated, int crcExpected)
	{
		super(String.format("%s calculated 0x%08x does not match expected value 0x%08x", fieldName, crcCalculated, crcExpected));
	}
}

