package io.github.jmcleodfoss.pst;

/**	The NotHeapNodeException is thrown when a node does not contain a valid heap-on-node.
*	This appears to indicate a corrupt byte in the Heap-on-Node structure, but in all test cases found, it is only this byte so it is benign. This excaption is normally dusabled;
*	to enable it, set {@link Options#strictHeapNodes} to true.
*	@see	HeapOnNode
*/
public class NotHeapNodeException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a NotHeapNodeException from the given description.
	*	@param	blockSignature	The block signature found (HeapOnNode.HNBitmapHeader.HN_SIGNATURE was expected)
	*	@see	io.github.jmcleodfoss.pst.HeapOnNode.Header#HN_SIGNATURE
	*	@see	io.github.jmcleodfoss.pst.HeapOnNode.Header#Header
	*/
	NotHeapNodeException(final byte blockSignature)
	{
		super("Bad block signature " + Integer.toHexString((int)blockSignature & 0xff));
	}
}
