package io.github.jmcleodfoss.pst;

/**	The NotPropertyContextNodeException is thrown when a node expected to have a Property Context client signature does not
*
*	@see	PropertyContext
*/
public class NotPropertyContextNodeException extends Exception {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NotPropertyContextException from the given description.
 	*
 	* 	@param node	The node with the unexpected client signature.
 	* 	@param cs	The node's client signature.
 	*/
	NotPropertyContextNodeException(final LPTLeaf node, ClientSignature cs)
	{
		super("Node " + node + " with client signature " + cs + " found when creating PropertyContext");
	}
}
