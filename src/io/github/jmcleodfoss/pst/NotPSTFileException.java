package io.github.jmcleodfoss.pst;

/**	The NotPSTFileException is thrown when the first four bytes of the file are not the PST flag bytes..
*
*	@see	Header#validate_magic
*/
public class NotPSTFileException extends Exception {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NotPSTFileException. */
	NotPSTFileException()
	{
		super();
	}
}

