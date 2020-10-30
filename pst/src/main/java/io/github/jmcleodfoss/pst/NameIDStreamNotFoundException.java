package io.github.jmcleodfoss.pst;

/**	The NameODStreamNotFoundException is thrown when one of the named property lookup map streams could not be found.
*)	@see	Header#validate_CRC
*/
public class NameIDStreamNotFoundException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NameIDStreamNotFoundException. */
	NameIDStreamNotFoundException(String streamName)
	{
		super(String.format("Stream %s was not found", streamName));
	}
}

