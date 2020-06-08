package io.github.jmcleodfoss.pst;

/**	The NodeKey class represents a B-tree node which contains a key. */
interface NodeKey {

	/**	Obtain the key for this node.
	*	@return	The key for this node. All child node keys are less than or equal to this value.
	*/
	abstract long key();
}
