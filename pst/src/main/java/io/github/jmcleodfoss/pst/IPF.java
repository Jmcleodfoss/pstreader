package io.github.jmcleodfoss.pst;

/**	The IPF holds the list of IPF folder types. */
public class IPF
{
	/**	The string used for the "container class" for folders containing appointments.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String APPOINTMENT = "IPF.Appointment";

	/**	The string used for the "container class" for folders used to persist conversation actions.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String CONVERSATION_ACTION_SETTINGS = "IPF.Configuration";

	/**	The string used for the "container class" for folders containing contacts.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String CONTACT = "IPF.Contact";

	/**	The string used for the "container class" for folders containing documents to be uploaded to a shared location.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String DOCUMENT_LIBRARIES = "IPF.ShortcutFolder";

	/**	The string used for the "container class" for RSS feeds.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String HOMEPAGE = "IPF.Note.OutlookHomepage";

	/**	The string used for the "container class" for folders containing the IM contact list
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String IM_CONTACT_LIST = "IPF.Contact.MOC.IMContactList";

	/**	The string used for the "container class" for folders containing journals.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String JOURNAL = "IPF.Journal";

	/**	The string used for the "container class" for folders containing notes.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String NOTE = "IPF.Note";

	/**	The string used for the "container class" for folders containing favourite contacts
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String QUICK_CONTACTS = "IPF.Contact.MOC.QuickContacts";

	/**	The string used for the "container class" for folders supporting reminder searches.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String REMINDER = "Outlook.Reminder";

	/**	The string used for the "container class" for folders containing sticky notes.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String STICKYNOTE = "IPF.StickyNote";

	/**	The string used for the "container class" for folders containing tasks.
	*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxosfld/68a85898-84fe-43c4-b166-4711c13cdd61">MS-OXOSFLD Section 2.2.1: List of Special Folders</a>
	*/
	public static final String TASK = "IPF.Task";

	/**	The list of known folder class types. */
	static final java.util.Vector<String> knownClasses = new java.util.Vector<String>();
	static {
		knownClasses.add(APPOINTMENT);
		knownClasses.add(CONVERSATION_ACTION_SETTINGS);
		knownClasses.add(CONTACT);
		knownClasses.add(DOCUMENT_LIBRARIES);
		knownClasses.add(HOMEPAGE);
		knownClasses.add(IM_CONTACT_LIST);
		knownClasses.add(JOURNAL);
		knownClasses.add(NOTE);
		knownClasses.add(QUICK_CONTACTS);
		knownClasses.add(REMINDER);
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
	public static boolean isConversationActionSettings(Folder folder)
	{
		return CONVERSATION_ACTION_SETTINGS.equals(folder.containerClass);
	}

	/**	Is the given folder a list of contacts?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Contact.
	*/
	public static boolean isContact(Folder folder)
	{
		return CONTACT.equals(folder.containerClass);
	}

	/**	Is the given folder one in contains documents to be uploaded to a shared location?
	*	@param	folder	The folder to check.
	*	@return	true if the given folder's class is IPF.Shortcut
	*/
	public static boolean isDocumentLibrary(Folder folder)
	{
		return DOCUMENT_LIBRARIES.equals(folder.containerClass);
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

	/**	Test the IPF class by iterating through the root folders and displaying the type of each and whether it is known.
	*	@param	args	The file(s) to show the folder information for.
	*/
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:");
			System.out.println("\tjava io.github.jmcleodfoss.pst.IPF pst-file [pst-file ...]");
			System.out.println("\nTo get the list of recognized folder types:");
			System.out.println("\tjava io.github.jmcleodfoss.pst.IPF --list");
			System.exit(1);
		}

		if (args[0].equals("--list")) {
			System.out.println("Known folder types");
			for (java.util.Iterator<String> iter = iterator(); iter.hasNext(); )
				System.out.println(iter.next());
			System.exit(1);
		}

		final String fmtOutput = "%-25s %-25s %-10s%n";

		for (String a: args) {
			System.out.println(a);
			try {
				PST pst = new PST(a);
				System.out.printf(fmtOutput, "Folder Name", "Container Class", "Known Container Class?");
				for (java.util.Iterator<Folder> folderIterator = pst.messageStore.rootFolder.subfolderIterator(); folderIterator.hasNext(); ) {
					final Folder f = folderIterator.next();
					System.out.printf(fmtOutput, f.displayName, f.containerClass, isKnownClass(f));
				}
			} catch (final NotHeapNodeException e) {
				e.printStackTrace(System.out);
			} catch (final NotPropertyContextNodeException e) {
				e.printStackTrace(System.out);
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final NotTableContextNodeException e) {
				e.printStackTrace(System.out);
			} catch (final NullDataBlockException e) {
				e.printStackTrace(System.out);
			} catch (final UnknownClientSignatureException e) {
				e.printStackTrace(System.out);
			} catch (final UnparseablePropertyContextException e) {
				e.printStackTrace(System.out);
			} catch (final UnparseableTableContextException e) {
				e.printStackTrace(System.out);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				System.out.printf("Could not read %s%n", a);
				e.printStackTrace(System.out);
			}
		}
	}
}
