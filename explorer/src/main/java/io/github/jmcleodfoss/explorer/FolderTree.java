package io.github.jmcleodfoss.explorer;

import javax.swing.JSplitPane;
import javax.swing.tree.TreeModel;

/**	Specialization of BTreeWithData for folder information display. */
@SuppressWarnings("serial")
class FolderTree extends BTreeWithData
{
	/**	The main application object, needed to display dialog boxes in and to load new files to */
	private final pstExplorer explorer;

	/**	The component in which to display the folder data. */
	FolderContentsDisplay folderContentsDisplay;

	/**	Construct the FolderContents display object.
	*	@param	explorer	The main pst Explorer application object
	*/
	FolderTree(pstExplorer explorer)
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		this.explorer = explorer;
		folderContentsDisplay = new FolderContentsDisplay(explorer, tree);
		setDataView(folderContentsDisplay);
	}

	/**	Clear the view.
	*/
	@Override
	void reset()
	{
		super.reset();
		folderContentsDisplay.reset();
	}

	/**	Obtain the tree model for the folder tree.
	*	@return	The javax.swing.tree.TreeModel representing the folder hierarchy.
	*/
	@Override
	TreeModel treeModel()
	{
		return explorer.pst().getFolderTree();
	}

}
