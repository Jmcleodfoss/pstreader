package io.github.jmcleodfoss.explorer;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import io.github.jmcleodfoss.pst.Attachment;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.LPTLeaf;
import io.github.jmcleodfoss.pst.PropertyContext;
import io.github.jmcleodfoss.swingutil.TreeNodePopupListener;

/**	The NodeDescriptionDisplay displays the the currently-selected node. */
@SuppressWarnings("serial")
class NodeDescriptionDisplay extends TreeDescriptionDisplay
{
	/**	The main application object, needed to display dialog boxes in and to load new files to */
	private final pstExplorer explorer;

	/**	The AttachmentSavePopupMenu is the popup menu for saving attachments. */
	private class AttachmentSavePopupMenu extends TreeNodePopupListener
	{
		/**	Handle attachment file save requests. */
		private class AttachmentSaveActionListener extends FileSaverMenuItem
		{
			/**	The property context for the currently selected attachment node, if any. */
			private PropertyContext pc;

			/**	The attachment object for the currently selected node, if any. */
			private Attachment attachment;
	
			/**	Create the AttachmentSaveActionListener object. */
			AttachmentSaveActionListener(JFrame parentFrame)
			{
				super(parentFrame);
				pc = null;
				attachment = null;
			}
	
			/**	{@inheritDoc} */
			String dialogTitle()
			{
				return "Save Attachment " + clickedNode;
			}
		
			/**	{@inheritDoc} */
			String initialFilenameSuggestion()
			{
				final LPTLeaf attachmentNode = (LPTLeaf)clickedNode;
				pc = explorer.pst().propertyContext(attachmentNode);
				try {
					attachment = new Attachment(attachmentNode, explorer.pst().blockBTree, explorer.pst());
				} catch (Exception e) {
					pc = null;
					return "";
				}
				return attachment.name;
			}
		
			/**	{@inheritDoc} */
			byte[] data()
			{
				// attachment and pc were set in initialFilenameSuggestion, called before data is retrieved
				try {
					return attachment.data(pc);
				} catch (CRCMismatchException e) {
					return null;
				}
			}
		}

		/**	Create an FileSaverTreePopupMenu, including the save action listener.
		*	@param	explorer	The main pst Explorer application object
		*/
		AttachmentSavePopupMenu(pstExplorer explorer)
		{
			JMenuItem item = new JMenuItem("Save...");
			item.addActionListener(new AttachmentSaveActionListener(explorer));
			add(item);
		}

		/**	Is the currently selected node an attachment node?
		*	@param	o	The node to check to see whether it is an attachment.
		*	@return	true if this node is an attachment, false if it is not an attachmetn
		*/
		public boolean lookingFor(Object o)
		{
			return ((LPTLeaf)o).nid.isAttachment();
		}
	};

	/**	Construct a NodeDescriptionDisplay object.
	*	@param	tree	The node tree associated with this description.
	*	@param	explorer	The main pst Explorer application object
	*/
	NodeDescriptionDisplay(pstExplorer explorer, BTreeJTree tree)
	{
		super(explorer, tree, new NodeContentsDisplay(explorer));
		this.explorer = explorer;
		tree.addMouseListener(new AttachmentSavePopupMenu(explorer));
	}
}
