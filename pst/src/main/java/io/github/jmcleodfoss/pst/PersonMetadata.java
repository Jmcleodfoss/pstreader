package io.github.jmcleodfoss.pst;

/**	The PersonMetadata class represents an OST person metadata message object. 
*	Note that this object is not documented.
*	The values set in the constructor are those which come from the folder Contents Table; retrieval of other fields require that the client provide the
*	message property context from which the	fields may be extracted.
*	@see	io.github.jmcleodfoss.pst.Appointment
*	@see	io.github.jmcleodfoss.pst.Contact
*	@see	io.github.jmcleodfoss.pst.JournalEntry
*	@see	io.github.jmcleodfoss.pst.Message
*	@see	io.github.jmcleodfoss.pst.StickyNote
*	@see	io.github.jmcleodfoss.pst.Task
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">MS-PST Section 2.4.5: Message Objects</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">MS-OXCMSG: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">MS-PST files, Section 2.4.5: Message Objects</a>
*/
public class PersonMetadata extends MessageObject
{
	/**	The creation time of the object */
	public final java.util.Date creationTime;

	/**	This is about the only recognizable text value which changes for each entry. */
	public final String internetMessageId;

	/**	Create an object representing the person metadata object for the given row in the folder contents table.
	*	@param	contentsTable	The containing folder's contents table.
	*	@param	row		The row of the contents table from which to create the contact.
	*	@param	nbt		The PST file's node B-Tree.
	*	@param	pstFile		The PST file's header, input stream, etc.
	*	@throws	NotHeapNodeException			A node which is not a heap node was found while building this sticky note.
	*	@throws	UnknownClientSignatureException		An unknown client signature was found while building this sticky note.
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while building this sticky note.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was found while building this sticky note.
	*	@throws java.io.IOException			An I/O error was encountered when reading the data for this sticky note.
	*/
	PersonMetadata(final TableContext contentsTable, final int row, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(contentsTable, row, nbt, pstFile);

		creationTime = (java.util.Date)contentsTable.get(row, PropertyTags.CreationTime);
		internetMessageId = (String)contentsTable.get(row, PropertyTags.InternetMessageIdW);
	}

	/**	Create a String representation of a person metadata object (used primarily for testing)
	*	@return	A String giving some information about this appointment.
	*/
	@Override
	@SuppressWarnings("JavaUtilDate") // We aren't doing anything fancy or complicated with java.util.date, and the way we are using it works.
	public String toString()
	{
		return String.format("%s, created %s", internetMessageId, creationTime.toString());
	}

	/**	Test the Appointment class by iterating through the appointments
	/**	Test the PersonMetadata class by iterating through the person metadata objects.
	* 	@param args	The files to test
	*/
	public static void main(final String[] args)
	{
		test("io.github.jmcleodfoss.pst.PersonMetadata", args);
	}
}
