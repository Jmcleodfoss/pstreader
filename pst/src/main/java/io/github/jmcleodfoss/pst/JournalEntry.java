package io.github.jmcleodfoss.pst;

/**	The JournalEntry class represents a PST sticky note message object. The values set in the constructor are those which come from the
*	folder Contents Table; retrieval of other fields require that the client provide the message property context from which the
*	fields may be extracted.
*
*	@see	io.github.jmcleodfoss.pst.Appointment
*	@see	io.github.jmcleodfoss.pst.Contact
*	@see	io.github.jmcleodfoss.pst.Message
*	@see	io.github.jmcleodfoss.pst.StickyNote
*	@see	io.github.jmcleodfoss.pst.Task
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">MS-OXCMSG: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">MS-PST: Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5: Message Objects</a>
*/
public class JournalEntry extends MessageObjectWithBody {
	
	/**	Create an object representing the journal entry for the given row in the folder contents table.
	*
	*	@param	contentsTable	The containing folder's contents table.
	*	@param	row		The row of the contents table from which to create the contact.
	*	@param	bbt		The PST file's block B-Tree.
	*	@param	nbt		The PST file's node B-Tree.
	*	@param	pstFile		The PST file's header, input stream, etc.
	*
	* 	@throws	NotHeapNodeException	A node which was not a heap node was found while bulding the journal entry.
	* 	@throws	UnknownClientSignatureException	An unknown client signature was found while building the journal entry.
	* 	@throws	UnparseablePropertyContextException	The property context for the journal entry was not parseable and is probably corrupt.
	* 	@throws	UnparseableTableContextException	The table context for the journal entry was not parseable and is probably corrupt.
	* 	@throws	java.io.IOException	An I/O exception was encountered while reading the data for the journal entry.
	*/
	JournalEntry(final TableContext contentsTable, final int row, final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(contentsTable, row, nbt, pstFile);
	}
}
