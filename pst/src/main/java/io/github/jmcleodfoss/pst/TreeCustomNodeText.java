package io.github.jmcleodfoss.pst;

/**	The TreeCustomNodeText interface provides a library-specific mechanism for obtaining tree node text.
*/
public interface TreeCustomNodeText {

	/**	Obtain text suitable for display in a JTree node.
	*	@param	node	The node object to obtain display text for.
	*	@return	A short text description of the node suitable for display in a JTree.
	*	@see	TreeCustomNodeText
	*/
	String getNodeText(final Object node);
}
