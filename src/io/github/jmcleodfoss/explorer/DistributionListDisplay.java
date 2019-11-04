package io.github.jmcleodfoss.explorer;

import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import io.github.jmcleodfoss.pst.DistributionList;
import io.github.jmcleodfoss.pst.PropertyContext;
import io.github.jmcleodfoss.pst.PST;

/**	The DistributionListDisplay class displays a distribution list. */
@SuppressWarnings("serial")
class DistributionListDisplay extends JScrollPane {

	/**	The raw data, in bytes and ASCII. */

	/**	The members of the distribution list. */
	private JList<DistributionList.Entry> members;

	/**	Construct the constituent elements of the display. */
	DistributionListDisplay()
	{
		super(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		members = new javax.swing.JList<DistributionList.Entry>();
		getViewport().add(members);
	}

	/**	Read the blocks for the given node, and update all relevant views.
	*
	*	@param	distributionList	The distribution list to display.
	*	@param	pc			The property context of the distribution list object.
	*	@param	pst			The PST object for the PST file being displayed.
	*/
	boolean update(DistributionList distributionList, PropertyContext pc, PST pst)
	{
		try {
			DefaultListModel<DistributionList.Entry> listModel = new DefaultListModel<DistributionList.Entry>();
			Iterator<DistributionList.Entry> memberIterator = distributionList.members(pc, pst.blockBTree, pst.nodeBTree, pst);
			while (memberIterator.hasNext())
				listModel.addElement(memberIterator.next());
			members.setModel(listModel);
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
