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
	*	@param	blockSignature	The block signature found (HeapOnNode.HNBitmapHeader.HN_SIGNATURE was expected)
	*
	*	@see	io.github.jmcleodfoss.pst.HeapOnNode.Header#HN_SIGNATURE
	*	@see	io.github.jmcleodfoss.pst.HeapOnNode.Header#Header
	*/
	NotHeapNodeException(final byte blockSignature)
	{
		super("Bad block signature " + Integer.toHexString((int)blockSignature & 0xff));
	}
}
