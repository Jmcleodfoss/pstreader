package io.github.jmcleodfoss.pst;

/**	The Appointment class holds information about a calendar entry.
*
*	@see	io.github.jmcleodfoss.pst.Contact
*	@see	io.github.jmcleodfoss.pst.JournalEntry
*	@see	io.github.jmcleodfoss.pst.Message
*	@see	io.github.jmcleodfoss.pst.StickyNote
*	@see	io.github.jmcleodfoss.pst.Task
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">{MX-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*	@see	"[MS-OXCXMSG] Message and Attacment Object Protocol Specifications v20101027"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc463900(v=EXCHG.80).aspx">[MS-OXCMSG]: Message and Attachment Object Protocol Specification (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385158(v=office.12).aspx">Message Objects (MSDN)</a>
*/
public class Appointment extends MessageObject {

	/**	The property ID under which to look up the AppointmentStartWhole property. */
	private static int AppointmentStartWholePropertyID = -1;

	/**	The property ID under which to look up the AppointmentEndWhole property. */
	private static int AppointmentEndWholePropertyID = -1;

	/**	The property ID under which to look up the AppointmentDuration property. */
	private static int AppointmentDurationPropertyID = -1;

	/**	The property ID under which to look up the RecurrencePattern property. */
	private static int RecurrencePatternPropertyID = -1;

	/**	The property ID under which to look up the Recurring property. */
	private static int RecurringPropertyID = -1;

	/**	The property ID under which to look up the RecurrenceType property. */
	private static int RecurrenceTypePropertyID = -1;

	/**	The property ID under which to look up the ReminderDelta property. */
	private static int ReminderDeltaPropertyID = -1;

	/**	The start time of the appointment in UTC. */
	public final java.util.Date startTime;

	/**	The end time of the appointment in UTC. */
	public final java.util.Date endTime;

	/**	The duration of the appointment. */
	public final int duration;

	/**	The recurrence pattern of the appointment. */
	public final String recurrencePattern;
	
	/**	Is this a recurring appointment? */
	public final boolean fRecurring;

	/**	The recurrence type. */
	int recurrenceType;

	/**	How long before the appointment time to provide a reminder. */
	int reminderDelta;

	/**	Create a message object for the given row in the folder contents table.
	*
	*	@param	contentsTable	The containing folder's contents table.
	*	@param	row	The row of the contents table from which to create the appointment.
	*	@param	bbt		The PST file's block B-Tree.
	*	@param	nbt		The PST file's node B-Tree.
	*	@param	pstFile		The PST file's header, input stream, etc.
	*
	*	@throws	NotHeapNodeException			A node which is not a heap node was found while reading the appointment.
	*	@throws	UnknownClientSignatureException		An unknown client signature was found while reading the appointment.
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while reading the appointment.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was found while reading the appointment.
	*	@throws	java.io.IOException			An I/O error was encountered while reading the appointment.
	*/
	Appointment(final TableContext contentsTable, final int row, final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(contentsTable, row, nbt, pstFile);

		startTime = (java.util.Date)contentsTable.get(row, AppointmentStartWholePropertyID);
		endTime = (java.util.Date)contentsTable.get(row, AppointmentEndWholePropertyID);

		Object o = contentsTable.get(row, AppointmentDurationPropertyID);
		duration = o == null ? 0 : (Integer)o;

		recurrencePattern = (String)contentsTable.get(row, RecurrencePatternPropertyID);

		o = contentsTable.get(row, RecurringPropertyID);
		fRecurring = o == null ? false : (Boolean)o;

		o = contentsTable.get(row, RecurrenceTypePropertyID);
		recurrenceType = o == null ? 0 : (Integer)o;

		o = contentsTable.get(row, ReminderDeltaPropertyID);
		reminderDelta = o == null ? 0 : (Integer)o;
	}

	/**	Save named property IDs for IDs of interest.
	*
	*	@param	namedProperties	The list of named properties.
	*/
	static void initConstants(NameToIDMap namedProperties)
	{
		AppointmentStartWholePropertyID = namedProperties.id(PropertyTag.AppointmentStartWhole);
		AppointmentEndWholePropertyID = namedProperties.id(PropertyTag.AppointmentEndWhole);
		AppointmentDurationPropertyID = namedProperties.id(PropertyTag.AppointmentDuration);
		RecurrencePatternPropertyID = namedProperties.id(PropertyTag.RecurrencePattern);
		RecurringPropertyID = namedProperties.id(PropertyTag.Recurring);
		RecurrenceTypePropertyID = namedProperties.id(PropertyID.RecurrenceType);
		ReminderDeltaPropertyID = namedProperties.id(PropertyID.ReminderDelta);
	}
}
