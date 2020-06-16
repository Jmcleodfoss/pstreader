package io.github.jmcleodfoss.explorer;

import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import io.github.jmcleodfoss.pst.DistributionList;
import io.github.jmcleodfoss.pst.NotHeapNodeException;
import io.github.jmcleodfoss.pst.NotPropertyContextNodeException;
import io.github.jmcleodfoss.pst.NullDataBlockException;
import io.github.jmcleodfoss.pst.PropertyContext;
import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.pst.UnknownClientSignatureException;
import io.github.jmcleodfoss.pst.UnparseablePropertyContextException;
import io.github.jmcleodfoss.pst.UnparseableTableContextException;

/**	The DistributionListDisplay class displays a distribution list. */
@SuppressWarnings("serial")
class DistributionListDisplay extends JScrollPane
{
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
	*	@param	distributionList	The distribution list to display.
	*	@param	pc			The property context of the distribution list object.
	*	@param	pst			The PST object for the PST file being displayed.
	*	@return	true if the update was successful, false if the update failed (because an exception was thrown and eaten).
	*/
	boolean update(DistributionList distributionList, PropertyContext pc, PST pst)
	{
		try {
			DefaultListModel<DistributionList.Entry> listModel = new DefaultListModel<DistributionList.Entry>();
			Iterator<DistributionList.Entry> memberIterator = distributionList.members(pc, pst.blockBTree, pst.nodeBTree, pst);
			while (memberIterator.hasNext())
				listModel.addElement(memberIterator.next());
			members.setModel(listModel);
		} catch (final NotHeapNodeException e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		} catch (final NotPropertyContextNodeException e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		} catch (final NullDataBlockException e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		} catch (final UnknownClientSignatureException e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		} catch (final UnparseablePropertyContextException e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		} catch (final UnparseableTableContextException e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		} catch (final java.io.IOException e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		}

		return true;
	}
}
