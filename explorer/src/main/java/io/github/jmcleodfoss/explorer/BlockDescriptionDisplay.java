package io.github.jmcleodfoss.explorer;

/**	The BlockDescriptionDisplay displays the currently-selected block. */
@SuppressWarnings("serial")
class BlockDescriptionDisplay extends TreeDescriptionDisplay
{
	/**	Construct a BlockDescriptionDisplay object.
	*	@param	explorer	The main pst Explorer application object
	*	@param	tree	The tree associated with this description.
	*/
	BlockDescriptionDisplay(pstExplorer explorer, BTreeJTree tree)
	{
		super(explorer, tree, new BlockContentsDisplay());
	}
}
