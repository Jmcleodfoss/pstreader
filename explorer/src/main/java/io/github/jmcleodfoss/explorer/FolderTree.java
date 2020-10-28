package io.github.jmcleodfoss.explorer;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.tree.TreeModel;

/**	Specialization of BTreeWithData for folder information display. */
@SuppressWarnings("serial")
class FolderTree extends BTreeWithData
{
	/**	The component in which to display the folder data. */
	FolderContentsDisplay folderContentsDisplay;

	/**	Construct the FolderContents display object.
	*/
	FolderTree(JFrame parentFrame)
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		folderContentsDisplay = new FolderContentsDisplay(tree, parentFrame);
		setDataView(folderContentsDisplay);
	}

	/**	Clear the view.
	*/
	void reset()
	{
		super.reset();
		folderContentsDisplay.reset();
	}

	/**	Obtain the tree model for the folder tree.
	*	@return	The javax.swing.tree.TreeModel representing the folder hierarchy.
	*/
	TreeModel treeModel()
	{
		return pstExplorer.pst().getFolderTree();
	}

}
