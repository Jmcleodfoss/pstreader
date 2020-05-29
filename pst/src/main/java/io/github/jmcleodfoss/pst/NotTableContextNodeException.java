package io.github.jmcleodfoss.pst;

/**	The NotTableContextNodeException is thrown when a node expected to have a Table Context client signature does not
*
*	@see	TableContext
*/
public class NotTableContextNodeException extends Exception {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NotTableContextException from the given description.
 	*
 	* 	@param cs	The node's client signature.
 	*/
	NotTableContextNodeException(ClientSignature cs)
	{
		super("Node with unexpected client signature " + cs + " found when creating TableContext");
	}
}
