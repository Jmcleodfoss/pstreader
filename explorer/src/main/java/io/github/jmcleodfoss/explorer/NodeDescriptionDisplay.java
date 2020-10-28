package io.github.jmcleodfoss.explorer;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import io.github.jmcleodfoss.pst.Attachment;
import io.github.jmcleodfoss.pst.LPTLeaf;
import io.github.jmcleodfoss.pst.PropertyContext;
import io.github.jmcleodfoss.swingutil.TreeNodePopupListener;

/**	The NodeDescriptionDisplay displays the the currently-selected node. */
@SuppressWarnings("serial")
class NodeDescriptionDisplay extends TreeDescriptionDisplay
{
	/**	The AttachmentSavePopupMenu is the popup menu for saving attachments. */
	static private class AttachmentSavePopupMenu extends TreeNodePopupListener
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
				pc = pstExplorer.pst().propertyContext(attachmentNode);
				try {
					attachment = new Attachment(attachmentNode, pstExplorer.pst().blockBTree, pstExplorer.pst());
				} catch (Exception e) {
					pc = null;
					return "";
				}
				return attachment.name;
			}
		
			/**	{@inheritDoc} */
			byte[] data()
			{
				final byte[] data = attachment.data(pc);
				attachment = null;
				pc = null;
				return data;
			}
		}

		/**	Create an FileSaverTreePopupMenu, including the save action listener. */
		AttachmentSavePopupMenu(JFrame parentFrame)
		{
			JMenuItem item = new JMenuItem("Save...");
			item.addActionListener(new AttachmentSaveActionListener(parentFrame));
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
	*/
	NodeDescriptionDisplay(BTreeJTree tree, JFrame parentFrame)
	{
		super(tree, new NodeContentsDisplay());
		tree.addMouseListener(new AttachmentSavePopupMenu(parentFrame));
	}
}
