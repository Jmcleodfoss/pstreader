package io.github.jmcleodfoss.explorer;

import javax.swing.JSplitPane;

import javax.swing.tree.TreeModel;

/**	Specialization of BTreeWithData for node and sub-node B-tree display. */
@SuppressWarnings("serial")
class NodeBTreeDisplay extends BTreeWithData
{
	/** The main Explorer application */
	pstExplorer explorer;

	/** The current node's contents */
	private NodeDescriptionDisplay nodeDescriptionDisplay;

	/**	Construct a BTreeWithData object with the given orientation and content pane. */
	protected NodeBTreeDisplay(pstExplorer explorer)
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		this.explorer = explorer;
		nodeDescriptionDisplay = new NodeDescriptionDisplay(explorer, tree);
		setDataView(nodeDescriptionDisplay);
	}

	/**	Get the tree model for the block B-tree. */
	@Override
	TreeModel treeModel()
	{
		return (TreeModel)explorer.pst().nodeBTree;
	}

	/**	Clear the tree model. */
	@Override
	void reset()
	{
		super.reset();
		nodeDescriptionDisplay.reset();
	}
}
