package io.github.jmcleodfoss.pst;

/**	The BadXBlockLevelException is thrown when a value other than 1 or 2 is found when reading an XBlock (1) or XXBlock (2).
*	@see	XBlock#XBlock
*/
public class BadXBlockLevelException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a BadXBlockLevelException. */
	BadXBlockLevelException(byte level)
	{
		super(String.format("Found level %d, only levels 1 (XBLOCK) and 2 (XXBLOCK) are allowed", level));
	}
}

