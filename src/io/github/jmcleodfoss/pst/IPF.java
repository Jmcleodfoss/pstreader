package io.github.jmcleodfoss.pst;

/**	The IPF holds the list of IPF folder types. */
public class IPF {

	/**	The string used for the "container class" for folders containing appointments. */
	private static final String APPOINTMENT = "IPF.Appointment";

	/**	The string used for the "container class" for folders containing contacts. */
	private static final String CONTACT = "IPF.Contact";

	/**	The string used for the "container class" for folders containing journals. */
	private static final String JOURNAL = "IPF.Journal";

	/**	The string used for the "container class" for folders containing sticky notes. */
	private static final String STICKYNOTE = "IPF.StickyNote";

	/**	The string used for the "container class" for folders containing tasks. */
	private static final String TASK = "IPF.Task";

	/**	The string used for the "container class" for folders containing notes. */
	private static final String NOTE = "IPF.Note";

	/**	The list of known folder class types. */
	static final java.util.Vector<String> knownClasses = new java.util.Vector<String>();
	static {
		knownClasses.add(APPOINTMENT);
		knownClasses.add(CONTACT);
		knownClasses.add(JOURNAL);
		knownClasses.add(NOTE);
		knownClasses.add(STICKYNOTE);
		knownClasses.add(TASK);
	}

	/**	Is the given folder a list of appointments?
	*
	*	@param	folder	The folder to check.
	*
	*	@return	true if the given folder's class is IPF.Appointment.
	*/
	public static boolean isAppointment(Folder folder)
	{
		return APPOINTMENT.equals(folder.containerClass);
	}

	/**	Is the given folder a list of contacts?
	*
	*	@param	folder	The folder to check.
	*
	*	@return	true if the given folder's class is IPF.Contact.
	*/
	public static boolean isContact(Folder folder)
	{
		return CONTACT.equals(folder.containerClass);
	}

	/**	Is the given folder a list of journal entries?
	*
	*	@param	folder	The folder to check.
	*
	*	@return	true if the given folder's class is IPF.Journal.
	*/
	public static boolean isJournal(Folder folder)
	{
		return JOURNAL.equals(folder.containerClass);
	}

	/**	Is the class of the given folder one of the known classes?
	*
	*	@param	folder	The folder to check.
	*
	*	@return	true if the given folder's class is one of the known classes, false otherwise.
	*/
	public static boolean isKnownClass(Folder folder)
	{
		return knownClasses.contains(folder.containerClass);
	}

	/**	Is the given folder a list of notes?
	*
	*	@param	folder	The folder to check.
	*
	*	@return	true if the given folder's class is IPF.Note.
	*/
	public static boolean isNote(Folder folder)
	{
		return NOTE.equals(folder.containerClass);
	}

	/**	Is the given folder a list of sticky notes?
	*
	*	@param	folder	The folder to check.
	*
	*	@return	true if the given folder's class is IPF.StickyNote.
	*/
	public static boolean isStickyNote(Folder folder)
	{
		return STICKYNOTE.equals(folder.containerClass);
	}

	/**	Is the given folder a list of tasks?
	*
	*	@param	folder	The folder to check.
	*
	*	@return	true if the given folder's class is IPF.Task.
	*/
	public static boolean isTask(Folder folder)
	{
		return TASK.equals(folder.containerClass);
	}

	/**	Get an iterator through the known classes.
	*
	*	@return	An iterator which may be used to step through the known folder classes.
	*/
	public static java.util.Iterator<String> iterator()
	{
		return knownClasses.iterator();
	}
}
