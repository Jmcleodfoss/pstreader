package io.github.jmcleodfoss.pst;

/**	The DataOverflowException is thrown when more data is found than was expected.
*	@see	TableContext#readRows
*/
public class DataOverflowException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a DataOverflowException. */
	DataOverflowException(int rows)
	{
		super(String.format("Too many rows found - expected %d", rows));
	}
}

