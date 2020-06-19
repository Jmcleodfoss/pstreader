package io.github.jmcleodfoss.explorer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.event.TreeSelectionListener;

import io.github.jmcleodfoss.pst.Attachment;
import io.github.jmcleodfoss.pst.DistributionList;
import io.github.jmcleodfoss.pst.Folder;
import io.github.jmcleodfoss.pst.LPTLeaf;
import io.github.jmcleodfoss.pst.MessageObject;
import io.github.jmcleodfoss.pst.NotHeapNodeException;
import io.github.jmcleodfoss.pst.NotPropertyContextNodeException;
import io.github.jmcleodfoss.pst.NotTableContextNodeException;
import io.github.jmcleodfoss.pst.NullDataBlockException;
import io.github.jmcleodfoss.pst.PropertyContext;
import io.github.jmcleodfoss.pst.UnknownClientSignatureException;
import io.github.jmcleodfoss.pst.UnparseablePropertyContextException;
import io.github.jmcleodfoss.pst.UnparseableTableContextException;
import io.github.jmcleodfoss.swingutil.ProgressBar;
import io.github.jmcleodfoss.swingutil.TreeNodePopupListener;

/**	The FolderContentsDisplay is a specialization of BTreeWithData for folder display. */
@SuppressWarnings("serial")
class FolderContentsDisplay extends JTabbedPane implements TreeSelectionListener
{
	/**	The HtmlSavePopupMenu is the popup menu for saving messages in HTML. */
	class HtmlSavePopupMenu extends TreeNodePopupListener
	{
		/**	Handle attachment file save requests. */
		private class HTMLSaveActionListener extends FileSaverMenuItem
		{
			/**	{@inheritDoc} */
			String dialogTitle()
			{
				return "Save E-mail as HTML";
			}

			/**	{@inheritDoc} */
			String initialFilenameSuggestion()
			{
				return ((io.github.jmcleodfoss.pst.Message)clickedNode).subject + ".html";
			}

			/**	{@inheritDoc} */
			byte[] data()
			{
				assert messagePC != null;
				return ((io.github.jmcleodfoss.pst.Message)clickedNode).bodyHtmlBytes(messagePC);
			}
		}

		/**	Create a FileSaverTreePopupMenu, including the save action listener. */
		HtmlSavePopupMenu()
		{
			JMenuItem item = new JMenuItem("Save HTML...");
			item.addActionListener(new HTMLSaveActionListener());
			add(item);
		}

		/**	Does it make sense to display the dialog for this node?
		*	@param	o	The node to check to see whether the "Save as HTML" menu should be available.
		*	@return	true if the "Save as HTML" popup menu should be shown, false if it should not be shown.
		*/
		public boolean lookingFor(final Object o)
		{
			if (o instanceof io.github.jmcleodfoss.pst.Message) {
				assert messagePC != null;
				return ((io.github.jmcleodfoss.pst.Message)o).bodyHtml(messagePC) != null;
			}
			return false;
		}
	};

	/**	Thee AttachmentSavePopupMenu is the popup menu for saving message attachments. */
	static class AttachmentSavePopupMenu extends TreeNodePopupListener
	{
		/**	Handle attachment file save requests. */
		private class AttachmentSaveActionListener extends FileSaverMenuItem
		{
			/**	{@inheritDoc} */
			String dialogTitle()
			{
				return "Save Attachment" + ((Attachment)clickedNode).name;
			}

			/**	{@inheritDoc} */
			String initialFilenameSuggestion()
			{
				return ((Attachment)clickedNode).name;
			}

			/**	{@inheritDoc} */
			byte[] data()
			{
				Attachment attachmentObject = (Attachment)clickedNode;
				PropertyContext pc = pstExplorer.pst().propertyContext(attachmentObject.nodeInfo);
				return attachmentObject.data(pc);
			}
		}

		/**	Create a FileSaverTreePopupMenu, including the save action listener. */
		AttachmentSavePopupMenu()
		{
			javax.swing.JMenuItem item = new javax.swing.JMenuItem("Save...");
			item.addActionListener(new AttachmentSaveActionListener());
			add(item);
		}

		/**	Does it make sense to display the dialog for this node?
		*	@param	o	The node to check to see whether the "Save Attachment" menu should be available.
		*	@return	true if the "Save Attachment" popup menu should be shown, false if it should not be shown.
		*/
		public boolean lookingFor(final Object o)
		{
			return o instanceof Attachment;
		}
	};

	/**	The list of mime types which may be displayed as images (and for which there is built-in support in Java Swing). */
	private static final ArrayList<String> imageMimeTypes = new ArrayList<String>();
	static {
		imageMimeTypes.add("image/bmp");
		imageMimeTypes.add("image/gif");
		imageMimeTypes.add("image/jpeg");
		imageMimeTypes.add("image/pjpeg");
		imageMimeTypes.add("image/png");
		imageMimeTypes.add("image/tiff");
	}

	/**	The list of mime types which may be displayed as text. */
	private static final ArrayList<String> textMimeTypes = new ArrayList<String>();
	static {
		textMimeTypes.add("message/delivery-status");
		textMimeTypes.add("text/plain");
		textMimeTypes.add("text/x-vcard");
	}

	/**	The list of mime types which may be displayed as HTML. */
	private static final ArrayList<String> htmlMimeTypes = new ArrayList<String>();
	static {
		htmlMimeTypes.add("text/html");
	}

	/**	The display of the folder raw data. */
	private NodeContentsDisplay folderObject;

	/**	The display of the folder hierarchy table. */
	private NodeContentsDisplay hierarchyTable;

	/**	The display of the folder contents table. */
	private NodeContentsDisplay contentsTable;

	/**	The display of the folder associated contents table. */
	private NodeContentsDisplay associatedContentsTable;

	/**	The display of the message data. */
	private Message message;

	/**	The display of the message recipient table. */
	private NodeContentsDisplay recipientTable;

	/**	The display of the message attachment info. */
	private NodeContentsDisplay attachmentTable;

	/**	The display of attachment data. */
	private NodeContentsDisplay attachment;

	/**	The ScrollPane for the attachment display, if any. */
	private javax.swing.JScrollPane spAttachmentDisplay;

	/**	The attachment display component, if any. */
	private javax.swing.JComponent attachmentDisplay;

	/**	The attachment image, if any. */
	private javax.swing.JLabel attachmentImage;

	/**	The text attachment, if any. */
	private javax.swing.JTextArea attachmentText;

	/**	The HTML attachment, if any. */
	private javax.swing.JTextPane attachmentHtml;

	/**	The members of the distribution list, if any. */
	private DistributionListDisplay distributionList;

	/**	The message property context, when appropriate. */
	private PropertyContext messagePC;

	/**	ssCreate the display of various types of data within the folder tree (folders, messages, and attachments).
	*	@param	folderTree	The folder tree.
	*/
	FolderContentsDisplay(final BTreeJTree folderTree)
	{
		super();

		folderObject = new NodeContentsDisplay();
		hierarchyTable = new NodeContentsDisplay();
		contentsTable = new NodeContentsDisplay();
		associatedContentsTable = new NodeContentsDisplay();

		message = new Message();
		recipientTable = new NodeContentsDisplay();
		attachmentTable = new NodeContentsDisplay();

		attachment = new NodeContentsDisplay();

		spAttachmentDisplay = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		attachmentImage = new javax.swing.JLabel();
		attachmentText = new javax.swing.JTextArea();
		attachmentHtml = new javax.swing.JTextPane();
		attachmentHtml.setContentType("text/html");
		attachmentHtml.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);

		distributionList = new DistributionListDisplay();

		folderTree.addMouseListener(new HtmlSavePopupMenu());
		folderTree.addMouseListener(new AttachmentSavePopupMenu());
	}

	/**	Update the display to show information about the current node of the folder tree.
	*	@param	treeNode	The new folder tree node to display.
	*/
	private void doUpdate(final Object treeNode)
	{
		if (treeNode instanceof Folder) {

			final Folder folder = (Folder)treeNode;
			assert folder.nodeFolderObject != null;

			updateComponent(folderObject, folder.nodeFolderObject, "Info");
			updateComponent(hierarchyTable, folder.nodeHierarchyTable, "Hierarchy");
			updateComponent(contentsTable, folder.nodeContentsTable, "Contents");
			updateComponent(associatedContentsTable, folder.nodeAssociatedContentsTable, "Associated Contents");

			remove(message);
			remove(recipientTable);
			remove(attachmentTable);
			messagePC = null;

			remove(attachment);
			remove(spAttachmentDisplay);

			return;
		}

		if (treeNode instanceof MessageObject) {

			final MessageObject messageObject = (MessageObject)treeNode;
			assert messageObject.nodeMessageObject != null;

			try {
				messagePC = messageObject.getMessage(pstExplorer.pst());
			} catch (NotHeapNodeException e) {
			} catch (NotPropertyContextNodeException e) {
			} catch (NotTableContextNodeException e) {
			} catch (NullDataBlockException e) {
			} catch (UnknownClientSignatureException e) {
			} catch (UnparseablePropertyContextException e) {
			} catch (UnparseableTableContextException e) {
			} catch (java.io.IOException e) {
			}

			if (messagePC != null) {
				updateComponent(message, messageObject.nodeMessageObject, "Message");
				message.update(messageObject, messagePC);
				if (messageObject instanceof io.github.jmcleodfoss.pst.Message) {
					io.github.jmcleodfoss.pst.Message messageMessage = (io.github.jmcleodfoss.pst.Message)messageObject;
					updateComponent(recipientTable, messageMessage.nodeRecipientTable, "Recipients");
					updateComponent(attachmentTable, messageMessage.nodeAttachmentTable, "Attachments");
				} else {
					remove(recipientTable);
					remove(attachmentTable);
				}

				if (messageObject instanceof DistributionList) {
					DistributionList distributionListObject = (DistributionList)messageObject;
					if (distributionList.update(distributionListObject, messagePC, pstExplorer.pst())) {
						if (indexOfComponent(distributionList) == -1)
							add("Distribution List Members", distributionList);
					} else {
						remove(distributionList);
					}

				} else {
					remove(distributionList);
				}
			} else {
				remove(message);
				remove(recipientTable);
				remove(attachmentTable);
				remove(distributionList);
			}

			remove(folderObject);
			remove(hierarchyTable);
			remove(contentsTable);
			remove(associatedContentsTable);

			remove(attachment);
			remove(spAttachmentDisplay);

			return;
		}

		if (treeNode instanceof Attachment) {
			
			final Attachment attachmentObject = (Attachment)treeNode;
			assert attachmentObject.nodeInfo != null;

			updateComponent(attachment, attachmentObject.nodeInfo, "Attachment");

			final PropertyContext pc = pstExplorer.pst().propertyContext(attachmentObject.nodeInfo);

			if (attachmentDisplay != null) {
				if (attachmentDisplay == attachmentImage)
					attachmentImage.setIcon(null);
				else if (attachmentDisplay == attachmentText)
					attachmentText.setText("");
				else if (attachmentDisplay == attachmentHtml)
					attachmentHtml.setText("");

				spAttachmentDisplay.getViewport().remove(attachmentDisplay);
				attachmentDisplay = null;
			}

			if (imageMimeTypes.contains(attachmentObject.mimeType)) {
				try {
					ByteArrayInputStream imageData = new ByteArrayInputStream(attachmentObject.data(pc));
					BufferedImage bufferedImage = ImageIO.read(imageData);
					attachmentImage.setIcon(new ImageIcon(bufferedImage));
					attachmentDisplay = attachmentImage;
				} catch (java.io.IOException e) {
					remove(spAttachmentDisplay);
				}

			} else if (textMimeTypes.contains(attachmentObject.mimeType)) {
				try {
					attachmentText.setText(new String(attachmentObject.data(pc), pstExplorer.explorer.charsetName()));
				} catch (final java.io.UnsupportedEncodingException e) {
					e.printStackTrace(System.out);
					attachmentText.setText("");
				} finally {
					attachmentDisplay = attachmentText;
				}
			} else if (htmlMimeTypes.contains(attachmentObject.mimeType)) {
				try {
					attachmentHtml.setText(new String(attachmentObject.data(pc), pstExplorer.explorer.charsetName()));
				} catch (final java.io.UnsupportedEncodingException e) {
					e.printStackTrace(System.out);
					attachmentHtml.setText("");
				} finally {
					attachmentDisplay = attachmentHtml;
				}
			} else {
				// we don't know how to display this attachment.
			}

			if (attachmentDisplay != null) {
				spAttachmentDisplay.getViewport().add(attachmentDisplay);
				if (indexOfComponent(spAttachmentDisplay) == -1)
					add("Display", spAttachmentDisplay);
			} else {
				remove(spAttachmentDisplay);
			}

			remove(folderObject);
			remove(hierarchyTable);
			remove(contentsTable);
			remove(associatedContentsTable);

			remove(message);
			remove(recipientTable);
			remove(attachmentTable);
			messagePC = null;

			return;
		}

		assert false : "Unhandled IPM message class.";
	}

	/**	Reset all displays and data. */
	void reset()
	{
		folderObject.reset();
		hierarchyTable.reset();
		contentsTable.reset();
		associatedContentsTable.reset();

		message.reset();
		recipientTable.reset();
		attachmentTable.reset();

		attachment.reset();
		attachmentImage.setIcon(null);
		attachmentText.setText("");
		attachmentHtml.setText("");

		remove(folderObject);
		remove(hierarchyTable);
		remove(contentsTable);
		remove(associatedContentsTable);

		remove(message);
		remove(recipientTable);
		remove(attachmentTable);

		remove(attachment);
		remove(spAttachmentDisplay);
	}

	/**	Update the given component (of NodeContentsDisplay type or derived there-from).
	*	@param	component	The component to display.
	*	@param	node		The new node.
	*	@param	title		The tab title for the TabbedPane.
	*/
	private void updateComponent(final NodeContentsDisplay component, final LPTLeaf node, final String title)
	{
		if (node != null) {
			component.update(node, pstExplorer.pst());
			if (indexOfComponent(component) == -1)
				add(title, component);
		} else {
			remove(component);
		}
	}

	/**	Update the component displays as nodes are selected.
	*	@param	e	The TreeSelectionEvent which triggered the update.
	*/
	public void valueChanged(final javax.swing.event.TreeSelectionEvent e)
	{
		new ProgressBar(pstExplorer.explorer, "Reading folder data") {
			public void run()
			{
				doUpdate(e.getPath().getLastPathComponent());
				setVisible(false);
			}
		};
	}
}
