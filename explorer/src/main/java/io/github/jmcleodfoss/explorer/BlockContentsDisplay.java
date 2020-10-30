package io.github.jmcleodfoss.explorer;

import java.nio.ByteBuffer;

import io.github.jmcleodfoss.pst.BTreeNode;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.swingutil.HexAndTextDisplay;

/**	The BlockContentsDisplay displays the raw bytes which make up the block. */
@SuppressWarnings("serial")
class BlockContentsDisplay extends HexAndTextDisplay implements BTreeContentsDisplay
{
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
			read(byteBuffer);
		} catch (CRCMismatchException e) {
			reset();
		} catch (java.io.IOException e) {
			reset();
		}
	}

}
