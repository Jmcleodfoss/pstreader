package io.github.jmcleodfoss.explorer;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import io.github.jmcleodfoss.pst.BTreeNode;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.swingutil.HexAndTextDisplay;

/**	A specialized version of the generic HexAndTextDisplay class which is linked to a JTree. */
@SuppressWarnings("serial")
class BTreeWithHexAndTextDisplay extends BTreeWithData implements TreeSelectionListener
{
	/** The main Explorer application */
	pstExplorer explorer;

	/**	The display of hexadecimal and text. */
	private HexAndTextDisplay hexAndTextDisplay;

	/**	Create a BTreeWithData with a HexAndText display as its data view.
	*	@param	explorer	The main Explorer application
	*	@param	orientation	The orientation of the underlying BTreeWithData's JSplitPane
	*/
	BTreeWithHexAndTextDisplay(pstExplorer explorer, int orientation)
	{
		super(orientation, new HexAndTextDisplay());
		this.explorer = explorer;
		hexAndTextDisplay = (HexAndTextDisplay)getRightComponent();
		tree.addTreeSelectionListener(this);
	}

	/**	Read the raw data for the node.
	*	@param	node	The leaf node to display.
	*	@return	A ByteBuffer containing the raw data bytes for this node.
	*/
	protected ByteBuffer readRawData(BTreeNode node)
	{
		try {
			return node.rawData(explorer.pst().blockBTree, explorer.pst());
		} catch (CRCMismatchException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**	Reset the constituent objects. */
	void reset()
	{
		super.reset();
		hexAndTextDisplay.reset();
	}

	/**	Update the display based on the changed selection.
	*	@param	e	The TreeSelectionEvent describing the selection change.
	*/
	public void valueChanged(TreeSelectionEvent e)
	{
		HexAndTextDisplay hexAndTextDisplay = (HexAndTextDisplay)dataView;

		Object lastPathComponent = e.getPath().getLastPathComponent();
		if (lastPathComponent instanceof BTreeNode) {
			BTreeNode node = (BTreeNode)lastPathComponent;
			ByteBuffer rawData = readRawData(node);
			if (rawData != null) {
				hexAndTextDisplay.read(rawData);
				return;
			}
		}

		hexAndTextDisplay.reset();
	}
}
