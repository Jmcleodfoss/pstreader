package io.github.jmcleodfoss.pst;

/**	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
*	@see	PropertyContext
*/
public class NullNodeException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NullNodeException for the given node.
 	*/
	NullNodeException()
	{
		super("Null node found while building PropertyContext");
	}
}
