package io.github.jmcleodfoss.pst;

/**	The BlockMap interface describes classes which allow a block to be found in the block B-tree. */
interface BlockMap {

	/**	Find the block with the given block ID.
	*
	*	@param	bid	The block ID of the block to be found.
	*
	*	@return The block for this block ID, if any, or null if the block ID was not found.
	*
	*	@throws	java.io.IOException	An I/O error was encountered while reading in ths requested block.
	*/
	BBTEntry find(final BID bid)
	throws
		java.io.IOException;
}
