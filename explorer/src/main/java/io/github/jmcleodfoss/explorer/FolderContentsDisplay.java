package io.github.jmcleodfoss.explorer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import io.github.jmcleodfoss.pst.Attachment;
import io.github.jmcleodfoss.pst.BadXBlockLevelException;
import io.github.jmcleodfoss.pst.BadXBlockTypeException;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.DataOverflowException;
import io.github.jmcleodfoss.pst.DistributionList;
import io.github.jmcleodfoss.pst.Folder;
import io.github.jmcleodfoss.pst.LPTLeaf;
import io.github.jmcleodfoss.pst.MessageObject;
import io.github.jmcleodfoss.pst.NotHeapNodeException;
import io.github.jmcleodfoss.pst.NotPropertyContextNodeException;
import io.github.jmcleodfoss.pst.NotTableContextNodeException;
import io.github.jmcleodfoss.pst.NullDataBlockException;
import io.github.jmcleodfoss.pst.NullNodeException;
import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.pst.PropertyContext;
import io.github.jmcleodfoss.pst.UnimplementedPropertyTypeException;
import io.github.jmcleodfoss.pst.UnknownClientSignatureException;
import io.github.jmcleodfoss.pst.UnknownPropertyTypeException;
import io.github.jmcleodfoss.pst.UnparseablePropertyContextException;
import io.github.jmcleodfoss.pst.UnparseableTableContextException;
import io.github.jmcleodfoss.swingutil.ProgressBar;
import io.github.jmcleodfoss.swingutil.TreeNodePopupListener;

/**	The FolderContentsDisplay is a specialization of BTreeWithData for folder display. */
@SuppressWarnings("serial")
class FolderContentsDisplay extends JTabbedPane implements NewFileListener, TreeSelectionListener
{
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

	/**	The current pst object */
	private PST pst;

	/**	The parent frame to display dialog boxes in */
	private final JFrame parentFrame;

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
	private JScrollPane spAttachmentDisplay;

	/**	The attachment display component, if any. */
	private JComponent attachmentDisplay;

	/**	The attachment image, if any. */
	private JLabel attachmentImage;

	/**	The text attachment, if any. */
	private JTextArea attachmentText;

	/**	The HTML attachment, if any. */
	private JTextPane attachmentHtml;

	/**	The members of the distribution list, if any. */
	private DistributionListDisplay distributionList;

	/**	The message property context, when appropriate. */
	private PropertyContext messagePC;

	/**	The HtmlSavePopupMenu is the popup menu for saving messages in HTML. */
	class HtmlSavePopupMenu extends TreeNodePopupListener
	{
		/**	Handle attachment file save requests. */
		private class HTMLSaveActionListener extends FileSaverMenuItem
		{
			/** {@inheritDoc} */
			HTMLSaveActionListener(JFrame parentFrame)
			{
				super(parentFrame);
			}

			/**	{@inheritDoc} */
			@Override
			String dialogTitle()
			{
				return "Save E-mail as HTML";
			}

			/**	{@inheritDoc} */
			@Override
			String initialFilenameSuggestion()
			{
				return ((io.github.jmcleodfoss.pst.Message)clickedNode).subject + ".html";
			}

			/**	{@inheritDoc} */
			@Override
			byte[] data()
			{
				assert messagePC != null;
				try {
					return ((io.github.jmcleodfoss.pst.Message)clickedNode).bodyHtmlBytes(messagePC);
				} catch (final	BadXBlockLevelException
					|	BadXBlockTypeException
					|	CRCMismatchException e) {
					return null;
				}
			}
		}

		/**	Create a FileSaverTreePopupMenu, including the save action listener. */
		HtmlSavePopupMenu(JFrame parentFrame)
		{
			JMenuItem item = new JMenuItem("Save HTML...");
			item.addActionListener(new HTMLSaveActionListener(parentFrame));
			add(item);
		}

		/**	Does it make sense to display the dialog for this node?
		*	@param	o	The node to check to see whether the "Save as HTML" menu should be available.
		*	@return	true if the "Save as HTML" popup menu should be shown, false if it should not be shown.
		*/
		@Override
		public boolean lookingFor(final Object o)
		{
			try {
				if (o instanceof io.github.jmcleodfoss.pst.Message) {
					assert messagePC != null;
					return ((io.github.jmcleodfoss.pst.Message)o).bodyHtml(messagePC) != null;
				}
			} catch (final BadXBlockLevelException
				|	BadXBlockTypeException
				|	CRCMismatchException e) {
				// Pass through to return
			}

			return false;
		}
	};

	/**	Thee AttachmentSavePopupMenu is the popup menu for saving message attachments. */
	class AttachmentSavePopupMenu extends TreeNodePopupListener
	{
		/**	Handle attachment file save requests. */
		private class AttachmentSaveActionListener extends FileSaverMenuItem
		{
			/** {@inheritDoc} */
			AttachmentSaveActionListener(JFrame parentFrame)
			{
				super(parentFrame);
			}

			/**	{@inheritDoc} */
			@Override
			String dialogTitle()
			{
				return "Save Attachment" + ((Attachment)clickedNode).name;
			}

			/**	{@inheritDoc} */
			@Override
			String initialFilenameSuggestion()
			{
				return ((Attachment)clickedNode).name;
			}

			/**	{@inheritDoc} */
			@Override
			byte[] data()
			{
				try {
					Attachment attachmentObject = (Attachment)clickedNode;
					PropertyContext pc = pst.propertyContext(attachmentObject.nodeInfo);
					return attachmentObject.data(pc);
				} catch (final	BadXBlockLevelException
					|	BadXBlockTypeException
					|	CRCMismatchException e) {
					return null;
				}
			}
		}

		/**	Create a FileSaverTreePopupMenu, including the save action listener. */
		AttachmentSavePopupMenu(JFrame parentFrame)
		{
			JMenuItem item = new JMenuItem("Save...");
			item.addActionListener(new AttachmentSaveActionListener(parentFrame));
			add(item);
		}

		/**	Does it make sense to display the dialog for this node?
		*	@param	o	The node to check to see whether the "Save Attachment" menu should be available.
		*	@return	true if the "Save Attachment" popup menu should be shown, false if it should not be shown.
		*/
		@Override
		public boolean lookingFor(final Object o)
		{
			return o instanceof Attachment;
		}
	};

	/**	ssCreate the display of various types of data within the folder tree (folders, messages, and attachments).
	*	@param	folderTree	The folder tree.
	*	@param	explorer	The main Explorer application
	*/
	FolderContentsDisplay(pstExplorer explorer, final BTreeJTree folderTree)
	{
		super();

		parentFrame = explorer;

		folderObject = new NodeContentsDisplay(explorer);
		hierarchyTable = new NodeContentsDisplay(explorer);
		contentsTable = new NodeContentsDisplay(explorer);
		associatedContentsTable = new NodeContentsDisplay(explorer);

		message = new Message(explorer);
		recipientTable = new NodeContentsDisplay(explorer);
		attachmentTable = new NodeContentsDisplay(explorer);

		attachment = new NodeContentsDisplay(explorer);

		spAttachmentDisplay = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		attachmentImage = new JLabel();
		attachmentText = new JTextArea();
		attachmentHtml = new JTextPane();
		attachmentHtml.setContentType("text/html");
		attachmentHtml.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);

		distributionList = new DistributionListDisplay();

		folderTree.addMouseListener(new HtmlSavePopupMenu(explorer));
		folderTree.addMouseListener(new AttachmentSavePopupMenu(explorer));
	}

	/**	Update the display for a new node of type Attachment
	*	@param	attachmentObject	The Attachment object for the node
	*/
	@SuppressWarnings("PMD.EmptyIfStmt")
	private void doAttachmentUpdate(Attachment attachmentObject)
	{
		assert attachmentObject.nodeInfo != null;

		updateComponent(attachment, attachmentObject.nodeInfo, "Attachment");

		final PropertyContext pc = pst.propertyContext(attachmentObject.nodeInfo);

		if (attachmentDisplay != null) {
			if (attachmentDisplay.equals(attachmentImage))
				attachmentImage.setIcon(null);
			else if (attachmentDisplay.equals(attachmentText))
				attachmentText.setText("");
			else if (attachmentDisplay.equals(attachmentHtml))
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
			} catch (final	java.io.IOException 
				|	BadXBlockLevelException
				|	BadXBlockTypeException
				|	CRCMismatchException e) {
				remove(spAttachmentDisplay);
			}

		} else if (textMimeTypes.contains(attachmentObject.mimeType)) {
			try {
				attachmentText.setText(new String(attachmentObject.data(pc), pst.charsetName()));
			} catch (final	java.io.UnsupportedEncodingException 
				|	BadXBlockLevelException
				|	BadXBlockTypeException
				|	CRCMismatchException e) {
				e.printStackTrace(System.out);
				attachmentText.setText("");
			} finally {
				attachmentDisplay = attachmentText;
			}
		} else if (htmlMimeTypes.contains(attachmentObject.mimeType)) {
			try {
				attachmentHtml.setText(new String(attachmentObject.data(pc), pst.charsetName()));
			} catch (final	java.io.UnsupportedEncodingException 
				|	BadXBlockLevelException
				|	BadXBlockTypeException
				|	CRCMismatchException e) {
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
	}

	/**	Update the display for a new node of type Folder.
	*	@param	folder	The Folder object for the node
	*/
	private void doFolderUpdate(Folder folder)
	{
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
	}

	/**	Update the display for a new node of type MessageObject
	*	@param	messageObject	The MessageObject object for the node
	*/
	private void doMessageObjectUpdate(MessageObject messageObject)
	{
		assert messageObject.nodeMessageObject != null;

		try {
			messagePC = messageObject.getMessage(pst);
		} catch (final	BadXBlockLevelException
			|	BadXBlockTypeException
			|	CRCMismatchException
			|	DataOverflowException
			|	NotHeapNodeException
			|	NotPropertyContextNodeException
			|	NotTableContextNodeException
			|	NullDataBlockException
			|	NullNodeException
			|	UnimplementedPropertyTypeException
			|	UnknownClientSignatureException
			|	UnknownPropertyTypeException
			|	UnparseablePropertyContextException
			|	UnparseableTableContextException
			|	java.io.IOException e) {
			// Pass through and return
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
				if (distributionList.update(distributionListObject, messagePC, pst)) {
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
	}

	/**	Update the display to show information about the current node of the folder tree.
	*	@param	treeNode	The new folder tree node to display.
	*/
	private void doUpdate(final Object treeNode)
	{
		if (treeNode instanceof Folder) {
			doFolderUpdate((Folder)treeNode);
			return;
		}

		if (treeNode instanceof MessageObject) {
			doMessageObjectUpdate((MessageObject)treeNode);
			return;
		}

		if (treeNode instanceof Attachment) {
			doAttachmentUpdate((Attachment)treeNode);
			return;
		}

		assert false : "Unhandled IPM message class.";
	}

	/**	Update with information from the new file.
	*	@param	pst	The PST object loaded.
	*/
	@Override
	public void fileLoaded(final PST pst)
	{
		this.pst = pst;
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

		pst = null;

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
			component.update(node, pst);
			if (indexOfComponent(component) == -1)
				add(title, component);
		} else {
			remove(component);
		}
	}

	/**	Update the component displays as nodes are selected.
	*	@param	e	The TreeSelectionEvent which triggered the update.
	*/
	@Override
	public void valueChanged(final TreeSelectionEvent e)
	{
		new ProgressBar(parentFrame, "Reading folder data") {
			@Override
			public void run()
			{
				doUpdate(e.getPath().getLastPathComponent());
				setVisible(false);
			}
		};
	}
}
