package io.github.jmcleodfoss.explorer;

import java.nio.ByteBuffer;
import javax.swing.JTabbedPane;

import io.github.jmcleodfoss.pst.BTreeNode;
import io.github.jmcleodfoss.pst.BadXBlockLevelException;
import io.github.jmcleodfoss.pst.BadXBlockTypeException;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.pst.PagedBTree;
import io.github.jmcleodfoss.swingutil.HexAndTextDisplay;

/**	The BlockContentsDisplay displays the raw bytes which make up the block. */
@SuppressWarnings("serial")
class BlockContentsDisplay extends JTabbedPane implements BTreeContentsDisplay
{
	/**	The raw data, in bytes and ASCII. */
	private HexAndTextDisplay rawData;

	/**	Container for intermediate node data */
	private AppTable btPage;

	/**	Construct the constituent elements of the display.
	*	@param	explorer	The main Explorer application
	*/
	BlockContentsDisplay()
	{
		rawData = new HexAndTextDisplay();
		btPage = new AppTable();
	}

	/**	{@inheritDoc}. */
	@Override
	public void reset()
	{
		remove(rawData);
		remove(btPage);
	}

	/**	{@inheritDoc}. */
	@Override
	public void update(BTreeNode node, PST pst)
	{
		if (pst == null) {
			reset();
			return;
		}

		try {
			ByteBuffer byteBuffer = node.rawData(pst.blockBTree, pst);
			if (byteBuffer == null) {
				reset();
				return;
			}
			rawData.read(byteBuffer);
			if (indexOfComponent(rawData) == -1)
				add("Raw", rawData);
			if (node instanceof PagedBTree) {
				add("BTPage", btPage);
				btPage.setModel(((PagedBTree)node).getBTPageTableModel());
			} else {
				remove(btPage);
			}
		} catch (final	BadXBlockLevelException
			|	BadXBlockTypeException
			|	CRCMismatchException
			|	java.io.IOException e) {
			reset();
		}
	}

}
