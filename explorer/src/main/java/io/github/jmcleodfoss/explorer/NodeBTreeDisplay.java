package io.github.jmcleodfoss.explorer;

import javax.swing.JSplitPane;
import javax.swing.tree.TreeModel;

/**	Specialization of BTreeWithData for node and sub-node B-tree display. */
@SuppressWarnings("serial")
class NodeBTreeDisplay extends BTreeWithData
{
	/**	Construct a BTreeWithData object with the given orientation and content pane. */
	protected NodeBTreeDisplay()
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		setDataView(new NodeDescriptionDisplay(tree));
	}

	/**	Get the tree model for the node B-tree.
	*	@return	The tree model for the node and sub-node B-tree.
	*/
	TreeModel treeModel()
	{
		return pstExplorer.explorer.nodeSubnodeBTree;
	}
}
