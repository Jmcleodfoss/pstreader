package io.github.jmcleodfoss.pst;

/**	The UnparseableTableContextException is thrown when the table context cannot be parsed (probably because of an I/O error).
*	@see	io.github.jmcleodfoss.pst.TableContext
*/
public class UnparseableTableContextException extends Exception {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a UnparseableTableContextException from the given description.
	*	@param	s	A description of the reason the table context cannot be parsed.
	*/
	UnparseableTableContextException(final String s)
	{
		super(s);
	}
}
