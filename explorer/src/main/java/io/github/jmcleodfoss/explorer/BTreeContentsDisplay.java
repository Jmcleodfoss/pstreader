package io.github.jmcleodfoss.explorer;

import io.github.jmcleodfoss.pst.BTreeNode;
import io.github.jmcleodfoss.pst.PST;

/**	The BTreeContentsDisplay interface defines the behavior of complex B-tree display panes.
*	Note that the BTreeContentsDisplay object must also extend JComponent.
*/
interface BTreeContentsDisplay {

	/**	Reset all child views. */
	void reset();

	/**	Update all child views.
	*	@param	node	The new B-tree node selected.
	*	@param	pst	The PST object for the PST file being displayed.
	*/
	void update(BTreeNode node, PST pst);
}
