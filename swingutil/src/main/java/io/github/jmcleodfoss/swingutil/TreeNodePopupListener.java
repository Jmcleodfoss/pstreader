package io.github.jmcleodfoss.swingutil;

/**	The TreeNodePopupListener provides a mechanism allowing a popup listener to be associated with the nodes of a JTree. */
public class TreeNodePopupListener extends java.awt.event.MouseAdapter
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	The pop-up menu to invoke - items are added via the add(JMenuItem) function. */
	private final javax.swing.JPopupMenu menu;

	/**	The node which was clicked. */
	public Object clickedNode;

	/**	Create the menu to be populated by the client application. */
	public TreeNodePopupListener()
	{
		menu = new javax.swing.JPopupMenu();
	}

	/**	Add a menu item to the menu.
	*	@param	item	The item to add to the menu.
	*/
	public void add(javax.swing.JMenuItem item)
	{
		menu.add(item);
	}

	/**	Determine whether the clicked-on node is of interest.
	*	@param	o	The node to check to see whether it is of interest.
	*	@return	true: the default implementation assumes that all nodes are of interest.
	*/
	public boolean lookingFor(final Object o)
	{
		return true;
	}

	/**	Take action when the mouse button is pressed.
	*	@param	e	The mouse event describing the button press.
	*/
	public void mousePressed(final java.awt.event.MouseEvent e)
	{
		if (!e.isPopupTrigger() || menu.isVisible()) {
			doPopupCancel();
			return;
		}

		doPopupTrigger(e);
	}

	/**	Take action when the mouse button is released.
	*	@param	e	The mouse event describing the button press.
	*/
	public void mouseReleased(java.awt.event.MouseEvent e)
	{
		if (!e.isPopupTrigger() || menu.isVisible()) {
			doPopupCancel();
			return;
		}

		doPopupTrigger(e);
	}

	/**	Remove the popup menu. */
	private void doPopupCancel()
	{
		if (menu.isVisible())
			menu.setVisible(false);
	}

	/**	Display the popup menu.
	*	@param	e	The mouse event describing the button action which might trigger the display.
	*/
	private void doPopupTrigger(java.awt.event.MouseEvent e)
	{
		final javax.swing.JTree tree = (javax.swing.JTree)e.getSource();
		final int selectedRow = tree.getRowForLocation(e.getX(), e.getY());
		if (selectedRow == -1)
			return;

		final Object o = tree.getPathForRow(selectedRow).getLastPathComponent();
		if (!lookingFor(o))
			return;

		clickedNode = o;
		menu.show(tree, e.getX(), e.getY());
		menu.setVisible(true);
	}
}

