package io.github.jmcleodfoss.pst;

/**	The ReadOnlyTreeModel abstract class implements trivial versions of functions irrelevant for read-only trees. */
public abstract class ReadOnlyTreeModel implements javax.swing.tree.TreeModel {

	/**	Add a TreeModelListener to a read-only tree. Note that there is no book-keeping required for adding a
	*	TreeModelListener, as a read-only tree cannot change.
	*
	*	@param	listener	The listener to "add".
	*/
	public void addTreeModelListener(final javax.swing.event.TreeModelListener listener)
	{
	}

	/**	Remove a TreeModelListener from a read-only tree. Note that there is no book-keeping required for adding a
	*	TreeModelListener, as a read-only tree cannot change.
	*
	*	@param	listener	The listener to "remove".
	*/
	public void removeTreeModelListener(final javax.swing.event.TreeModelListener listener)
	{
	}

	/**	Indicate that the given path in the tree has changed. Note that since a read-only is immutable, this does nothing
	*
	*	@param	path		The path which changed.
	*	@param	newValue	The value of the new path.
	*/
	public void valueForPathChanged(final javax.swing.tree.TreePath path, Object newValue)
	{
	}
}
