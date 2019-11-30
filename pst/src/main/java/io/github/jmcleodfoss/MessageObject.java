package io.github.jmcleodfoss.pst;

/**	The MessageObject class is the base class for all content objects (appointments, contacts, journal entries, messages, etc.)
*
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">{MX-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*	@see	"[MS-OXCXMSG] Message and Attacment Object Protocol Specifications v20101027"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc463900(v=EXCHG.80).aspx">[MS-OXCMSG]: Message and Attachment Object Protocol Specification (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385158(v=office.12).aspx">Message Objects (MSDN)</a>
*/
public class MessageObject {

	/**	The message PC node information. */
	public final NBTEntry nodeMessageObject;

	/**	Whether the containing PST file is Unicode or ANSI. This value is stored, rather than the property IDs for each case,
	*	to reduce memory requirements.
	*/
	protected final boolean fUnicode;

	/**	The subject. */
	public final String subject;

	/**	Create a message object for the given row in the folder contents table.
	*
	*	@param	contentsTable	The containing folder's contents table
	*	@param	messageRow	The row of the contents table from which to create the message
	*	@param	nbt		The PST file's node B-Tree
	*	@param	pstFile		The PST file's header, input stream, etc.
	*	@see	Folder
	*	@see	Attachment
	*	@see	Recipient
	*/
	MessageObject(final TableContext contentsTable, final int messageRow, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		fUnicode = pstFile.unicode();
		subject = (String)contentsTable.get(messageRow, fUnicode ? PropertyTag.SubjectW : PropertyTag.Subject);

		final int nidMessageObject = (Integer)contentsTable.get(messageRow, PropertyTag.LtpRowId);
		nodeMessageObject = nbt.find(new NID(nidMessageObject));
	}

	/**	Create a message object of the appropriate derived type.
	*
	*	@param	contentsTable	The containing folder's contents table
	*	@param	row		The row of the contents table from which to create the message
	*	@param	bbt		The PST file's block B-Tree
	*	@param	nbt		The PST file's node B-Tree
	*	@param	pstFile		The PST file's header, input stream, etc.
	*
	*/
	static MessageObject factory(TableContext contentsTable, final int row, final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		final String messageType = (String)contentsTable.get(row, pstFile.unicode() ? PropertyTag.MessageClassW : PropertyTag.MessageClass);

		if (IPM.isAppointment(messageType))
			return new Appointment(contentsTable, row, bbt, nbt, pstFile);

		if (IPM.isContact(messageType))
			return new Contact(contentsTable, row, bbt, nbt, pstFile);

		if (IPM.isDistributionList(messageType))
			return new DistributionList(contentsTable, row, bbt, nbt, pstFile);

		if (IPM.isStickyNote(messageType))
			return new StickyNote(contentsTable, row, bbt, nbt, pstFile);

		if (IPM.isTask(messageType))
			return new Task(contentsTable, row, bbt, nbt, pstFile);


		return new Message(contentsTable, row, bbt, nbt, pstFile);
	}

	/**	Retrieve the message object property context.
	*
	*	@param	bbt		The PST file's block B-Tree
	*	@param	pstFile		The PST file's header, input stream, etc.
	*
	*	@return	The message object property context, required as a parameter for other functions in the class.
	*
	*	@see	Message#body
	*	@see	MessageObjectWithBody#bodyHtml
	*	@see	Message#transportHeaders
	*
	*	@throws NotHeapNodeException			A node which is not a heap node was found in the purported heap.
	*	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found when building the property context.
	*	@throws NotTableContextNodeException		A node without the Table Context client signature was found when building the table context.
	*	@throws NullDataBlockException			A null data block was found when building the property context.
	*	@throws UnknownClientSignatureException		An unrecognized client signature was found when reading a block.
	*	@throws UnparseablePropertyContextException	The property context for this message could not be interpreted.
	*	@throws UnparseableTableContextException	The table context for this message could not be interpreted.
	*	@throws java.io.IOException			The PST file could not be read.
	*/
	public PropertyContext getMessage(final BlockMap bbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return new PropertyContext(nodeMessageObject, bbt, pstFile);
	}
}
