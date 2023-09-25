package io.github.jmcleodfoss.swingutil;

/**	The EmptyTreeModel is a utility class to replace Swing's default tree model so that an empty tree doesn't show anything. */
public class EmptyTreeModel extends io.github.jmcleodfoss.pst.ReadOnlyTreeModel
{
	/**	The one publicly available empty tree model. */
	public static final EmptyTreeModel model = new EmptyTreeModel();

	/**	Only one instance of this class needs to exist. */
	private EmptyTreeModel()
	{
	}

	/**	Get the requested child of the given parent node in the empty tree model. Note that the empty tree model has no children
	*	at any level.
	*	@param	parent	The parent to return the child of.
	*	@param	index	The index of the child to return.
	*	@return	null, since there are no child nodes in the empty tree.
	*/
	@Override
	public Object getChild(Object parent, int index)
	{
		return null;
	}

	/**	Get the number of children of the given parent node. Node that the empty tree model has no children at any level.
	*	@param	parent	The parent to return the child of.
	*	@return	0, since there are no child nodes in the empty tree.
	*/
	@Override
	public int getChildCount(Object parent)
	{
		return 0;
	}

	/**	Get the index of the given child node under the given parent node. Note that the empty tree has no children at any
	*	level.
	*	@param	parent	The parent node to find the child node's index in.
	*	@param	child	The child node to find the index of under the parent node.
	*	@return	-1, as the empty tree model has no children.
	*/
	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		return -1;
	}

	/**	Get the root of this tree. Note that there is nothing to display even at the root level.
	*	@return	null, since there is nothing to display for the root.
	*/
	@Override
	public Object getRoot()
	{
		return null;
	}

	/**	Determine whether the given node is a leaf node or an intermediate node. Note that the root is a leaf, so this cannot
	*	be called for any other nodes.
	*	@param	node	The node to check to see whether it is a leaf or an intermediate node.
	*	@return	true, always, since this can only be called for the root node.
	*/
	@Override
	public boolean isLeaf(Object node)
	{
		return true;
	}
}
