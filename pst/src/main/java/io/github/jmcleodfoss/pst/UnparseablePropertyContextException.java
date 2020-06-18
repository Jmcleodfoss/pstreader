package io.github.jmcleodfoss.pst;

/**	The UnparseablePropertyContextException is thrown when the property context cannot be parsed (probably because of an I/O error).
*	@see	PropertyContext
*/
public class UnparseablePropertyContextException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a UnparseablePropertyContextException from the given description.
	*	@param	s	A description of the reason the property context cannot be parsed.
	*/
	UnparseablePropertyContextException(final String s)
	{
		super(s);
	}
}
