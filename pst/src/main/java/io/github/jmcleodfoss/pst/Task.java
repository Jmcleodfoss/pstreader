package io.github.jmcleodfoss.pst;

/**	The Task class holds information about a task entry.
*	@see	io.github.jmcleodfoss.pst.Appointment
*	@see	io.github.jmcleodfoss.pst.Contact
*	@see	io.github.jmcleodfoss.pst.JournalEntry
*	@see	io.github.jmcleodfoss.pst.Message
*	@see	io.github.jmcleodfoss.pst.StickyNote
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">MS-OXCMSG: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">MS-PST Section 2.4.5: Message Objects</a>
*/
public class Task extends MessageObject
{
	/**	The property ID under which to look up the TaskDueDate property. */
	private static int TaskDueDateLID = PropertyLIDs.UNKNOWN;

	/**	The due date of the task in UTC. */
	public final java.util.Date dueDate;

	/**	Create a task for the given row in the folder contents table.
	*	@param	contentsTable	The containing folder's contents table.
	*	@param	row	The row of the contents table from which to create the appointment.
	*	@param	nbt		The PST file's node B-Tree.
	*	@param	pstFile		The PST file's header, input stream, etc.
	*	@throws	NotHeapNodeException			A node which is not a heap node was encountered while reading this task.
	*	@throws	UnknownClientSignatureException		An unknown client signature was found while reading this task.
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while reading this task.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was found while reading this task.
	*	@throws	java.io.IOException			An I/O error was encountered when reading the data for this task.
	*/
	Task(final TableContext contentsTable, final int row, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(contentsTable, row, nbt, pstFile);

		dueDate = (java.util.Date)contentsTable.get(row, TaskDueDateLID);
	}

	/**	Save named property IDs for IDs of interest.
	*	@param	namedProperties	The list of named properties.
	*/
	static void initConstants(NameToIDMap namedProperties)
	{
		TaskDueDateLID = namedProperties.id(PropertyLIDs.TaskDueDate, DataType.TIME);
	}

	/**	Provide a String representation of the task (used primarily for testing)
	*	@return	A String representation of the task
	*/
	@Override
	public String toString()
	{
		return String.format("%s due %s", subject, dueDate);
	}

	/**	Test the Task class by iterating through the tasks.
	* 	@param args	The files to test
	*/
	public static void main(final String[] args)
	{
		test("io.github.jmcleodfoss.pst.Task", args);
	}
}
