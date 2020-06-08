package io.github.jmcleodfoss.pst;

/**	The UnknownClientSignatureException exception indicates an unknown client signature was encountered,
*	@see	HeapOnNode
*/
public class UnknownClientSignatureException extends Exception {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create an UnknownClientSignatureException object
	*	@param	signature	The unrecognized client signature.
	*/
	UnknownClientSignatureException(final byte signature)
	{
		super("Unknown client signature 0x" + Integer.toHexString(signature & 0xff));
	}
}
