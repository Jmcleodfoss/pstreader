package io.github.jmcleodfoss.explorer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javax.swing.JSplitPane;

import io.github.jmcleodfoss.pst.BTreeNode;
import io.github.jmcleodfoss.pst.BTreeOnHeap;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.HeapOnNode;
import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.pst.UnimplementedPropertyTypeException;
import io.github.jmcleodfoss.pst.UnknownPropertyTypeException;
import io.github.jmcleodfoss.swingutil.EmptyTreeModel;

/**	The BTHDisplay class displays the B-tree-on-Heap for a node and the data for the selected node of the B-tree. */
@SuppressWarnings("serial")
class BTHDisplay extends BTreeWithHexAndTextDisplay
{
	/**	The heap-on-node on which this B-tree-on-heap is built. */
	private HeapOnNode hon;
	
	/**	Create the B-tree-on-heap display.
	*	@param	explorer	The main Explorer application
	*/
	BTHDisplay(pstExplorer explorer)
	{
		super(explorer, JSplitPane.VERTICAL_SPLIT);
	}

	/**	Read in a new sub-node B-tree.
	*	@param	hon	The heap-on-node containing the sub-node B-tree.
	*	@param	pst	The PST file being processed.
	*	@return	true if the B-tree-on-heap could be displayed, false if it could not, or does not exist.
	*/
	boolean read(final HeapOnNode hon, PST pst)
	{
		try {
			this.hon = hon;
			BTreeOnHeap bth = new BTreeOnHeap(hon, pst);
			tree.setModel(bth);
			return true;
		} catch (final	CRCMismatchException
			|	IOException e) {
		}

		tree.setModel(EmptyTreeModel.model);
		return false;
	}

	/**	{@inheritDoc} */
	@Override
	protected ByteBuffer readRawData(BTreeNode node)
	{
		try {
			return BTreeOnHeap.getData(node, hon);
		} catch (final	UnimplementedPropertyTypeException
			|	UnknownPropertyTypeException
			|	UnsupportedEncodingException e) {
			return null;
		}
	}
}
