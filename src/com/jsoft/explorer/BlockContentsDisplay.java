package com.jsoft.explorer;

import java.nio.ByteBuffer;

import com.jsoft.pst.BTreeNode;
import com.jsoft.pst.PST;
import com.jsoft.swingutil.HexAndTextDisplay;

/**	The BlockContentsDisplay displays the raw bytes which make up the block. */
@SuppressWarnings("serial")
class BlockContentsDisplay extends HexAndTextDisplay implements BTreeContentsDisplay {

	/**	Construct the constituent elements of the display. */
	BlockContentsDisplay()
	{
		super();
	}

	/**	{@inheritDoc}. */
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
			read(byteBuffer);
		} catch (java.io.IOException e) {
			reset();
		}
	}

}