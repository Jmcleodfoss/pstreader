package io.github.jmcleodfoss.pst;

/**	The NodeMap interface describes classes which allow a node to be found in the node B-tree. */
public interface NodeMap {

	/**	Find the node with the given node ID.
	*	@param	nid	The node ID of the node to be found.
	*	@return The node for this node ID, if any, or null if the node ID was not found.
	*	@throws java.io.IOException	There was a problem reading the node B-tree entry.
	*/
	NBTEntry find(final NID nid)
	throws
		java.io.IOException;
}
