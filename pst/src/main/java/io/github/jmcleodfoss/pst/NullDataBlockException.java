package io.github.jmcleodfoss.pst;

/**	The NullDataBlockException is thrown when a data block is found to be null unexpectedly.
*	@see	PropertyContext
*/
public class NullDataBlockException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NullDataBlockException for the given node.
 	* 	@param node	The node with the null data block.
 	*/
	NullDataBlockException(final LPTLeaf node)
	{
		super("Node " + node + " has null data block");
	}
}
