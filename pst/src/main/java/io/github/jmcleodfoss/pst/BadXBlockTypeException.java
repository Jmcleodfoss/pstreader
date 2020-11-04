
package io.github.jmcleodfoss.pst;

/**	The BadXBlockTypeException is thrown when a value other than 1 is found for when reading an XBlock or XXBlock
*	@see	XBlock#XBlock
*/
public class BadXBlockTypeException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a BadXBlockTypeException. */
	BadXBlockTypeException(byte type)
	{
		super(String.format("XBlock/XXBlock type %d found, only the value 1 is valid", type));
	}
}

