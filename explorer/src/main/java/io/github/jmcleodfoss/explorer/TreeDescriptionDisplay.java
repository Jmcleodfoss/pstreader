package io.github.jmcleodfoss.explorer;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import io.github.jmcleodfoss.pst.BTreeNode;

/**	The TreeDescriptionDisplay allows a complex description object to be associated with a JTree. */
@SuppressWarnings("serial")
class TreeDescriptionDisplay extends JSplitPane implements TreeSelectionListener
{
	/** The main Explorer application */
	pstExplorer explorer;

	/**	The node summary/overview. */
	private AppTable summary;

	/**	The contents of the node. */
	private JComponent contents;

	/**	Construct a NodeDescriptionDisplay object.
	*	@param	explorer	The main Explorer application
	*	@param	tree	The tree associated with this description.
	*	@param	contentsDisplay	The content display for the currently selected node.
	*/
	TreeDescriptionDisplay(pstExplorer explorer, BTreeJTree tree, JComponent contentsDisplay)
	{
		super(JSplitPane.VERTICAL_SPLIT);

		this.explorer = explorer;
		summary = new AppTable();
		setTopComponent(summary);
		tree.addTreeSelectionListener(this);

		setBottomComponent(contents = contentsDisplay);
	}

	/**	The paint method is over-ridden to place the divider correctly based on the top component's size.
	*	@param	g	The graphics context in which to paint the component.
	*/
	@Override
	public void paint(Graphics g)
	{
		if (getDividerLocation() == -1) {
			final int newDividerLocation = getTopComponent().getSize().height;
			setDividerLocation(newDividerLocation);
		}
		super.paint(g);
	}

	/**	Clear the constituent views. */
	void reset()
	{
		summary.reset();
		if (contents instanceof BTreeContentsDisplay)
			((BTreeContentsDisplay)contents).reset();
	}

	/**	Update the display based on the changed selection.
	*	@param	e	The TreeSelectionEvent indicating the new node information.
	*/
	@Override
	public void valueChanged(final TreeSelectionEvent e)
	{
		final Object lastPathComponent = e.getPath().getLastPathComponent();
		assert lastPathComponent instanceof BTreeNode;

		final BTreeNode node = (BTreeNode)lastPathComponent; 
		if (contents instanceof BTreeContentsDisplay)
			((BTreeContentsDisplay)contents).update(node, explorer.pst());
		summary.setModel(((BTreeNode)lastPathComponent).getNodeTableModel());
		setDividerLocation(-1);
	}
}
