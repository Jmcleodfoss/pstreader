package io.github.jmcleodfoss.pst;

/**	The UnknownPropertyTypeException exception indicates an unknown property type was encountered,
*	@see	HeapOnNode
*/
public class UnknownPropertyTypeException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create an UnknownPropertyTypeException object
	*	@param	type	The unrecognized client signature.
	*/
	UnknownPropertyTypeException(final short type)
	{
		super(String.format("Unknown property type 0x%04x", type));
	}
}
