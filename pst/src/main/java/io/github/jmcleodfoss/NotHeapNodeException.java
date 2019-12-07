package io.github.jmcleodfoss.pst;

/**	The NotHeapNodeException is thrown when a node does not contain a valid heap-on-node.
*
*	@see	HeapOnNode
*/
public class NotHeapNodeException extends Exception {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NotHeapNodeException from the given description.
	*
	*	@param	s	More info about the unexpected node. 
	*/
	NotHeapNodeException(final String s)
	{
		super(s);
	}
}
