package io.github.jmcleodfoss.pst;

/**	The IPF holds the list of IPF folder types. */
public class IPF {

	/**	The string used for the "container class" for folders containing appointments.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String APPOINTMENT = "IPF.Appointment";

	/**	The string used for the "container class" for folders used to perseist conversation actions.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String CONFIGURATION = "IPF.Configuration";

	/**	The string used for the "container class" for folders containing contacts.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String CONTACT = "IPF.Contact";

	/**	The string used for the "container class" for RSS feeds.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String HOMEPAGE = "IPF.Note.OutlookHomepage";

	/**	The string used for the "container class" for folders containing the IM contact list
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String IM_CONTACT_LIST = "IPF.Contact.MOC.IMContactList";

	/**	The string used for the "container class" for folders containing journals.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String JOURNAL = "IPF.Journal";

	/**	The string used for the "container class" for folders containing favourite contacts
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String QUICK_CONTACTS = "IPF.Contact.MOC.QuickContacts";

	/**	The string used for the "container class" for folders supporting reminder searches.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String REMINDER = "Outlook.Reminder";

	/**	The string used for the "container class" for folders containing shortcuts.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String SHORTCUT_FOLDER = "IPF.ShortcutFolder";

	/**	The string used for the "container class" for folders containing sticky notes.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String STICKYNOTE = "IPF.StickyNote";

	/**	The string used for the "container class" for folders containing tasks.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String TASK = "IPF.Task";

	/**	The string used for the "container class" for folders containing notes.
	*	@see <a href=https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	private static final String NOTE = "IPF.Note";

	/**	The list of known folder class types. */
	static final java.util.Vector<String> knownClasses = new java.util.Vector<String>();
	static {
		knownClasses.add(APPOINTMENT);
		knownClasses.add(CONFIGURATION);
		knownClasses.add(CONTACT);
		knownClasses.add(HOMEPAGE);
		knownClasses.add(IM_CONTACT_LIST);
		knownClasses.add(JOURNAL);
		knownClasses.add(NOTE);
		knownClasses.add(QUICK_CONTACTS);
		knownClasses.add(REMINDER);
		knownClasses.add(SHORTCUT_FOLDER);
		knownClasses.add(STICKYNOTE);
		knownClasses.add(TASK);
	}

	/**	Is the given folder a list of appointments?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Appointment.
	*/
	public static boolean isAppointment(Folder folder)
	{
		return APPOINTMENT.equals(folder.containerClass);
	}

	/**	Is the given folder the Conversation Action Settings?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Configuration.
	*/
	public static boolean isConfiguration(Folder folder)
	{
		return CONFIGURATION.equals(folder.containerClass);
	}

	/**	Is the given folder a list of contacts?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Contact.
	*/
	public static boolean isContact(Folder folder)
	{
		return CONTACT.equals(folder.containerClass);
	}

	/**	Is the given folder the list of RSS feeds?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Note.OutlookHomepage
	*/
	public static boolean isHomepage(Folder folder)
	{
		return HOMEPAGE.equals(folder.containerClass);
	}

	/**	Is the given folder the list of IM contacts?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Contact.MOC.ImContactList
	*/
	public static boolean isIMContactList(Folder folder)
	{
		return IM_CONTACT_LIST.equals(folder.containerClass);
	}

	/**	Is the given folder a list of journal entries?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Journal.
	*/
	public static boolean isJournal(Folder folder)
	{
		return JOURNAL.equals(folder.containerClass);
	}

	/**	Is the class of the given folder one of the known classes?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is one of the known classes, false otherwise.
	*/
	public static boolean isKnownClass(Folder folder)
	{
		return knownClasses.contains(folder.containerClass);
	}

	/**	Is the given folder a list of notes?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Note.
	*/
	public static boolean isNote(Folder folder)
	{
		return NOTE.equals(folder.containerClass);
	}

	/**	Is the given folder the list of quick (MFU) contacts?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Contact.MOC.QuickContacts
	*/
	public static boolean isQuickContacts(Folder folder)
	{
		return QUICK_CONTACTS.equals(folder.containerClass);
	}

	/**	Is the given folder the reminder search folder?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is Outlook.Reminder
	*/
	public static boolean isReminder(Folder folder)
	{
		return REMINDER.equals(folder.containerClass);
	}

	/**	Is the given folder a list of sticky notes?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.StickyNote.
	*/
	public static boolean isStickyNote(Folder folder)
	{
		return STICKYNOTE.equals(folder.containerClass);
	}

	/**	Is the given folder a list of tasks?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Task.
	*/
	public static boolean isTask(Folder folder)
	{
		return TASK.equals(folder.containerClass);
	}

	/**	Get an iterator through the known classes.
	*	@return	An iterator which may be used to step through the known folder classes.
	*/
	public static java.util.Iterator<String> iterator()
	{
		return knownClasses.iterator();
	}
}
