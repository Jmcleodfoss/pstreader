package io.github.jmcleodfoss.explorer;

import java.nio.ByteBuffer;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import io.github.jmcleodfoss.pst.BTreeNode;
import io.github.jmcleodfoss.pst.BadXBlockLevelException;
import io.github.jmcleodfoss.pst.BadXBlockTypeException;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.HeapOnNode;
import io.github.jmcleodfoss.pst.LPTLeaf;
import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.swingutil.HexAndTextDisplay;

/**	The NodeContentsDisplay provides a view of the contents of a node. */
@SuppressWarnings("serial")
class NodeContentsDisplay extends JTabbedPane implements BTreeContentsDisplay
{
	/**	The raw data, in bytes and ASCII. */
	private HexAndTextDisplay rawData;

	/**	The Heap-On-Node, if present. */
	private JList<Object> heapOnNode;

	/**	Scrollable container for the heap-on-node */
	private JScrollPane sbHeapOnNode;

	/**	The BTree-on-Heap, if present. */
	private BTHDisplay bth;

	/**	The client-level object (Property Context or Table Context. */
	private AppTable lpt;

	/**	Construct the constituent elements of the display.
	*	@param	explorer	The main Explorer application
	*/
	NodeContentsDisplay(pstExplorer explorer)
	{
		rawData = new HexAndTextDisplay();
		heapOnNode = new JList<Object>();
		sbHeapOnNode = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sbHeapOnNode.getViewport().add(heapOnNode);
		bth = new BTHDisplay(explorer);
		lpt = new AppTable();
	}

	/**	{@inheritDoc}. */
	@Override
	public void reset()
	{
		remove(rawData);
		remove(sbHeapOnNode);
		remove(bth);
		remove(lpt);
	}

	/**	{@inheritDoc}. */
	@Override
	@SuppressWarnings("PMD.NPathComplexity")
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
		} catch (final	BadXBlockLevelException
			|	BadXBlockTypeException
			|	CRCMismatchException
			|	java.io.IOException e) {
			reset();
			return;
		}

		if (!(node instanceof LPTLeaf)) {
			remove(heapOnNode);
			remove(bth);
			remove(lpt);
			return;
		}

		LPTLeaf leaf = (LPTLeaf)node;

		HeapOnNode hon = pst.heapOnNode(leaf.bidData);
		if (hon == null) {
			remove(sbHeapOnNode);
			remove(bth);
			remove(lpt);
			return;
		}

		heapOnNode.setModel(hon);
		if (indexOfComponent(sbHeapOnNode) == -1) {
			add("Heap", sbHeapOnNode);
		}

		if(bth.read(hon, pst)) {
			if (indexOfComponent(bth) == -1)
				add("BTree", bth);
		} else {
			remove(bth);
			remove(lpt);
		}

		int tabIndex = indexOfComponent(lpt);
		String tabName = null;
		if (hon.isTableContext()) {
			lpt.setModel(pst.tcTableModel(leaf, hon));
			tabName = "TC";
		} else if (hon.isPropertyContext()) {
			lpt.setModel(pst.pcTableModel(leaf));
			tabName = "PC";
		} else {
			assert false : "HeapOnNode not TC or PC";
		}

		if (tabName != null) {
			if (tabIndex == -1)
				add(tabName, lpt);
			else
				setTitleAt(tabIndex, tabName);
		} else {
			remove(lpt);
		}
	}
}
