package io.github.jmcleodfoss.explorer;

/**	The BlockDescriptionDisplay displays the currently-selected block. */
@SuppressWarnings("serial")
class BlockDescriptionDisplay extends TreeDescriptionDisplay
{
	/**	Construct a BlockDescriptionDisplay object.
	*	@param	tree	The tree associated with this description.
	*/
	BlockDescriptionDisplay(BTreeJTree tree)
	{
		super(tree, new BlockContentsDisplay());
	}
}
