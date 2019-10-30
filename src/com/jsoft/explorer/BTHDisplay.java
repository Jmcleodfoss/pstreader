package com.jsoft.explorer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javax.swing.JSplitPane;

import com.jsoft.pst.BTreeNode;
import com.jsoft.pst.BTreeOnHeap;
import com.jsoft.pst.HeapOnNode;
import com.jsoft.pst.PST;
import com.jsoft.swingutil.EmptyTreeModel;

/**	The BTHDisplay class displays the B-tree-on-Heap for a node and the data for the selected node of the B-tree. */
@SuppressWarnings("serial")
class BTHDisplay extends BTreeWithHexAndTextDisplay {

	/**	The heap-on-node on which this B-tree-on-heap is built. */
	private HeapOnNode hon;
	
	/**	Create the B-tree-on-heap display. */
	BTHDisplay()
	{
		super(JSplitPane.VERTICAL_SPLIT);
	}

	/**	Read in a new sub-node B-tree.
	*
	*	@param	hon	The heap-on-node containing the sub-node B-tree.
	*	@param	pst	The PST file being processed.
	*
	*	@return	true if the B-tree-on-heap could be displayed, false if it could not, or does not exist.
	*/
	boolean read(final HeapOnNode hon, PST pst)
	{
		try {
			this.hon = hon;
			BTreeOnHeap bth = new BTreeOnHeap(hon, pst);
			tree.setModel(bth);
			return true;
		} catch (IOException e) {
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
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}