package io.github.jmcleodfoss.pst;

/**	The Message class represents a PST email message.
*	The values set in the constructor are those which come from the folder Contents	Table; retrieval of other fields,
*	including the message body, require that the client provide the message property context from which the fields may be extracted.
*	An example of the expected use case is:
*	<pre>
*	{@code
*	// given the contents table object contentsTable and the PST object pst:
*
*	Message message = new Mesage(contentsTable, 0, pst.blockBTree, pst.nodeBTree, pst);
*	System.out.printf("sender %s: subject %s (received %s)\n", message.sentRepresentingName, messsage.subject, message.messageDeliveryTime);
*
*	PropertyContext messagePC = message.getMessage(pst.blockBTree, pst);
*	System.out.printf("body %s\n", message.body(messagePC);
*	}
*	</pre>
*	@see	io.github.jmcleodfoss.pst.Appointment
*	@see	io.github.jmcleodfoss.pst.Attachment
*	@see	io.github.jmcleodfoss.pst.Contact
*	@see	io.github.jmcleodfoss.pst.Folder
*	@see	io.github.jmcleodfoss.pst.Recipient
*	@see	io.github.jmcleodfoss.pst.StickyNote
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">MS-OXCMSG: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">MS-PST Section 2.4.5: Message Objects</a>
*/
public class Message extends MessageObjectWithBody
{
	/**	Flag indicating the message has attachments. */
	private static final int MSG_FLAG_ATTACHMENT = 0x10;

	/**	The attachment table node information. */
	public final SLEntry nodeAttachmentTable;

	/**	The recipient table node information. */
	public final SLEntry nodeRecipientTable;

	/**	The sender's name or email address as given in the folder Contents table. */
	public final String sentRepresentingName;

	/**	The message deliver time. */
	public final java.util.Date messageDeliveryTime;

	/**	The recipients; this is created when required.
	*	Note that not all message objects have recipient tables - only actual mail messages have recipients.
	*/
	private java.util.Vector<Recipient> recipients;

	/**	The attachments; this is created when the Message object is constructed. */
	private java.util.Vector<Attachment> attachments;

	/**	Create a message for the given row in the folder contents table.
	*	@param	contentsTable	The containing folder's contents table
	*	@param	messageRow	The row of the contents table from which to create the message
	*	@param	bbt		The PST file's block B-Tree
	*	@param	nbt		The PST file's node B-Tree
	*	@param	pstFile		The PST file's header, input stream, etc.
	* 	@throws	NotHeapNodeException	A node which is not a heap node was found while building the message.
	* 	@throws	NotPropertyContextNodeException	A node which was not a property context was found when a property context was expected.
	* 	@throws	NotTableContextNodeException	A node which was not a table context was found when a table context was expected.
	* 	@throws NullDataBlockException	A null data block wsa found while building the message.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	* 	@throws	UnknownClientSignatureException	An unknown client signature was found while building the message.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	* 	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while building the message.
	* 	@throws	UnparseableTableContextException	A bad / corrupt table context was found while building the message.
	* 	@throws java.io.IOException	An I/O exception was encountered while reading in the data for the message.
	*/
	Message(final TableContext contentsTable, final int messageRow, final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(contentsTable, messageRow, nbt, pstFile);

		messageDeliveryTime = (java.util.Date)contentsTable.get(messageRow, PropertyTags.MessageDeliveryTime);
		sentRepresentingName = (String)contentsTable.get(messageRow, PropertyTags.SentRepresentingNameW);

		SLEntry nodeAttachmentTable = null;
		SLEntry nodeRecipientTable = null;
		java.util.Vector<Attachment> attachments = null;
		if (!nodeMessageObject.bidSubnode.isNull()) {
			final SubnodeBTree snb = new SubnodeBTree(nodeMessageObject.bidSubnode, bbt, pstFile);
			for (java.util.Iterator<BTreeNode> snbIterator = snb.iterator(); snbIterator.hasNext(); ) {

				SLEntry subnode = (SLEntry)snbIterator.next();

				if (subnode.nid.isRecipientTable()) {
					nodeRecipientTable = subnode;
					continue;
				}

				if (subnode.nid.isAttachmentTable()) {
					nodeAttachmentTable = subnode;

					final TableContext tcAttachments = new TableContext(nodeAttachmentTable, bbt, pstFile);

					attachments = new java.util.Vector<Attachment>(tcAttachments.getRowCount());
					for (int i = 0; i < tcAttachments.getRowCount(); ++i) {
						final SLEntry nodeAttachmentPC = (SLEntry)snb.find((Integer)tcAttachments.get(i, PropertyTags.LtpRowId));
						assert nodeAttachmentPC != null;
						attachments.add(new Attachment(nodeAttachmentPC, bbt, pstFile));
					}
					continue;
				}
			}
		}
		this.nodeAttachmentTable = nodeAttachmentTable;
		this.attachments = (attachments != null) ? attachments : new java.util.Vector<Attachment>(0);

		int messageFlags = (Integer)contentsTable.get(messageRow, PropertyTags.MessageFlags);
		assert (nodeAttachmentTable == null) == ((messageFlags & MSG_FLAG_ATTACHMENT) == 0);

		this.nodeRecipientTable = nodeRecipientTable;
		recipients = null;
	}

	/**	Return the requested attachment as specified by the index.
	*	This is for use by other classes in this package; client code is expected to use {@link #attachmentIterator} when processing attachments.
	*	@param	index	The index of the attachment to retrieve. Note that this must be a valid index into the attachment table.
	*	@return	The Attachment object for the given attachment.
	*	@see	#attachmentIterator
	*	@see	#numAttachments
	*/
	Attachment attachment(final int index)
	{
		assert attachments != null;
		return attachments.get(index);
	}

	/**	Return an iterator over the message's attachments.
	*	@return	An iterator object which may be used to go through the message attachments.
	*	@see	#attachment
	*	@see	#numAttachments
	*/
	public java.util.Iterator<Attachment> attachmentIterator()
	{
		return attachments.iterator();
	}

	/**	Retrieve the message object property context.
	*	@param	pst		The PST file
	*	@return	The message object property context, required as a parameter for other functions in the class.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@see	#body
	*	@see	MessageObjectWithBody#bodyHtml
	*	@see	#transportHeaders
	*/
	@Override
	public PropertyContext getMessage(final PST pst)
	throws
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		final PropertyContext pc = super.getMessage(pst);

		if (nodeRecipientTable != null && !nodeRecipientTable.bidData.isNull()) {
			final TableContext tcRecipients = new TableContext(nodeRecipientTable, pst.blockBTree, pst);
			recipients = new java.util.Vector<Recipient>(tcRecipients.getRowCount());
			for (int i = 0; i < tcRecipients.getRowCount(); ++i)
				recipients.add(new Recipient(tcRecipients, i, pst.unicode()));
		}

		return pc;
	}

	/**	Return the number of attachments for this message.
	*	@return	The number of attachments to this message.
	*	@see	#attachment
	*	@see	#attachmentIterator
	*/
	int numAttachments()
	{
		if (nodeAttachmentTable == null)
			return 0;
		assert attachments != null;
		return attachments.size();
	}

	/**	Retrieve the requested recipient as specified by the index.
	*	@param	index	The index of the recipient to retrieve. Note that this must be a valid index into the recipient table.
	*	@return	The Recipient object for the given recipient.
	*/
	Recipient recipients(final int index)
	{
		assert recipients != null;
		return recipients.get(index);
	}

	/**	Provide a String describing the message (used primarily for testing)
	*	@return	A String describing the message
	*/
	@Override
	public String toString()
	{
		return String.format("%s, %s (%s)", subject, sentRepresentingName, messageDeliveryTime.toString());
	}

	/**	Extract the Message Transport Headers from the message PC.
	*	@param	pc	The message object property context, as retrieved by getMessage.
	*	@return	The message body in HTML, if present, as a string.
	*	@see	#body
	*	@see	MessageObjectWithBody#bodyHtml
	*	@see	#getMessage
	*	@see	#transportHeaders
	*/
	public String transportHeaders(final PropertyContext pc)
	{
		return (String)pc.get(PropertyTags.TransportMessageHeaders);
	}

	/**	Test the Message class by iterating through the messages.
	* 	@param args	The files to test
	*/
	public static void main(final String[] args)
	{
		test("io.github.jmcleodfoss.pst.Message", args);
	}
}
