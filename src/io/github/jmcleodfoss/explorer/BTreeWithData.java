package io.github.jmcleodfoss.explorer;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;

import io.github.jmcleodfoss.swingutil.EmptyTreeModel;

/**	The BTreeWithData view permits simple display of a B-tree which displays the corresponding data below or to the left in a
*	JSplitPane.
*/
@SuppressWarnings("serial")
abstract class BTreeWithData extends JSplitPane implements NewFileListener {

	/**	The B-tree to display. */
	protected BTreeJTree tree;

	/**	The data for the currently selected node of the B-tree. */
	protected JComponent dataView;

	/**	Construct a BTreeWithData object with the given orientation and data view component.
	*
	*	@param	orientation	The orientation of the JSplitPane
	*	@param	dataView	The data to show with the B-tree
	*/
	protected BTreeWithData(final int orientation, JComponent dataView)
	{
		super(orientation);

		tree = new BTreeJTree();
		setTopComponent(tree);

		setDataView(dataView);
	}

	/**	Construct a BTreeWithData object with the given orientation.
	*
	*	@param	orientation	The orientation of the JSplitPane
	*/
	protected BTreeWithData(final int orientation)
	{
		this(orientation, null);
	}

	/**	Update the views when a new file is read in.
	*
	*	@param	e	The description of new file.
	*/
	public void fileLoaded(final NewFileEvent e)
	{
		TreeModel tm = treeModel();
		assert tm != null : "TreeModel cannot be null.";
		tree.setModel(tm);
	}

	/**	Override paint method to set divider location.
	*
	*	@param	g	The graphics context in which to paint the object.
	*/
	public void paint(java.awt.Graphics g)
	{
		if (getDividerLocation() == -1)
			setDividerLocation(0.25);
		super.paint(g);
	}

	/**	Clear the tree model. */
	void reset()
	{
		tree.setModel(null);
	}

	/**	Set the data component to the given object, setting it as a TreeSelectionListener if supported.
	*
	*	@param	dataView	The new dataView component.
	*/
	void setDataView(JComponent dataView)
	{
		setBottomComponent(this.dataView = dataView);
		if (dataView != null && dataView instanceof TreeSelectionListener)
			tree.addTreeSelectionListener((TreeSelectionListener)dataView);
	}

	/**	Get the tree model from the PST file for this client. */
	TreeModel treeModel()
	{
		return EmptyTreeModel.model;
	}
}
