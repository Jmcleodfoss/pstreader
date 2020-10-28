package io.github.jmcleodfoss.explorer;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.tree.TreeModel;

import io.github.jmcleodfoss.pst.PST;

/**	Specialization of BTreeWithData for node and sub-node B-tree display. */
@SuppressWarnings("serial")
class NodeBTreeDisplay extends BTreeWithData
{
	/** The current node's contents */
	private NodeDescriptionDisplay nodeDescriptionDisplay;

	/**	Construct a BTreeWithData object with the given orientation and content pane. */
	protected NodeBTreeDisplay(pstExplorer explorer)
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		nodeDescriptionDisplay = new NodeDescriptionDisplay(explorer, tree);
		setDataView(nodeDescriptionDisplay);
	}

	/**	Update the views when a new file is read in.
	*	@param	pst	The PST object loaded.
	*/
	public void fileLoaded(final PST pst)
	{
		try {
			tree.setModel(pst.nodeBTreeRoot());
		} catch (IOException e) {
			tree.setModel(treeModel());
		}
	}

	/**	Clear the tree model. */
	void reset()
	{
		super.reset();
		nodeDescriptionDisplay.reset();
	}
}
