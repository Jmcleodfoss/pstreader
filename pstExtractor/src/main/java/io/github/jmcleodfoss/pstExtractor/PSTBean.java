package io.github.jmcleodfoss.pstExtractor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.Part;

import io.github.jmcleodfoss.pst.Appointment;
import io.github.jmcleodfoss.pst.BadXBlockLevelException;
import io.github.jmcleodfoss.pst.BadXBlockTypeException;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.DataOverflowException;
import io.github.jmcleodfoss.pst.Contact;
import io.github.jmcleodfoss.pst.Folder;
import io.github.jmcleodfoss.pst.IPF;
import io.github.jmcleodfoss.pst.IncorrectNameIDStreamContentException;
import io.github.jmcleodfoss.pst.NameIDStreamNotFoundException;
import io.github.jmcleodfoss.pst.JournalEntry;
import io.github.jmcleodfoss.pst.MessageObject;
import io.github.jmcleodfoss.pst.NotHeapNodeException;
import io.github.jmcleodfoss.pst.NotPSTFileException;
import io.github.jmcleodfoss.pst.NotPropertyContextNodeException;
import io.github.jmcleodfoss.pst.NotTableContextNodeException;
import io.github.jmcleodfoss.pst.NullDataBlockException;
import io.github.jmcleodfoss.pst.NullNodeException;
import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.pst.StickyNote;
import io.github.jmcleodfoss.pst.Task;
import io.github.jmcleodfoss.pst.UnimplementedPropertyTypeException;
import io.github.jmcleodfoss.pst.UnknownClientSignatureException;
import io.github.jmcleodfoss.pst.UnknownPropertyTypeException;
import io.github.jmcleodfoss.pst.UnparseablePropertyContextException;
import io.github.jmcleodfoss.pst.UnparseableTableContextException;

/**	The ContactFormBean shares the data from the contact upload form with the contact server. */
@Named("pstBean")
@SessionScoped
public class PSTBean implements Serializable
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	The maximum number of attempts to provide a password permitted. */
	private static final int MAX_PASSWORD_ATTEMPTS = 3;

	/**	Enumeration of extraction types. */
	enum ExtractionTypes {
		APPOINTMENTS,
		CONTACTS,
		JOURNAL_ENTRIES,
		STICKYNOTES,
		TASKS
	};

	/**	The resource names for the labels of the check boxes of the various types are not found. */
	private final static EnumMap<ExtractionTypes, String> LabelResourceName = new EnumMap<ExtractionTypes, String>(ExtractionTypes.class);
	static {
		LabelResourceName.put(ExtractionTypes.APPOINTMENTS, "appointments.text");
		LabelResourceName.put(ExtractionTypes.CONTACTS, "contacts.text");
		LabelResourceName.put(ExtractionTypes.JOURNAL_ENTRIES, "journalEntries.text");
		LabelResourceName.put(ExtractionTypes.STICKYNOTES, "stickyNotes.text");
		LabelResourceName.put(ExtractionTypes.TASKS, "tasks.text");
	}

	/**	The actual values selected in the set of check boxes indicating what to extract. */
	private List<ExtractionTypes> selectedExtractionTypes;

	/**	The names and values of the extraction checkbox. */
	private SelectItem[] extractionTypeChoices;

	/**	The uploaded PST file. */
	private transient Part uploadedFile;

	/**	The password for the PST file. */
	private transient String password;

	/**	The appointments in this PST file. */
	private transient MessageObjectCollectionBean<AppointmentBean> appointments;

	/**	The contacts in this PST file. */
	private transient MessageObjectCollectionBean<ContactBean> contacts;

	/**	The journal entries in this PST file. */
	private transient MessageObjectCollectionBean<JournalEntryBean> journalEntries;

	/**	The sticky notes in this PST file. */
	private transient MessageObjectCollectionBean<StickyNoteBean> stickyNotes;

	/**	The tasks in this PST file. */
	private transient MessageObjectCollectionBean<TaskBean> tasks;

	/**	The PST file. */
	private transient PST pst;

	/**	The number of attempts to provide a password. */
	private transient int numPasswordAttempts;

	/**	Create a bean for communication between the form and the servlet. */
	public PSTBean()
	{
		ResourceBundle rb = ResourceBundle.getBundle("text-resources");
		SelectItem[] extractionTypeChoices = new SelectItem[ExtractionTypes.values().length];
		for (ExtractionTypes t : ExtractionTypes.values())
			extractionTypeChoices[t.ordinal()] = new SelectItem(t, rb.getString(LabelResourceName.get(t)));
		this.extractionTypeChoices = extractionTypeChoices;

		selectedExtractionTypes = new ArrayList<ExtractionTypes>();

		appointments = new MessageObjectCollectionBean<AppointmentBean>();
		contacts = new MessageObjectCollectionBean<ContactBean>();
		journalEntries = new MessageObjectCollectionBean<JournalEntryBean>();
		stickyNotes = new MessageObjectCollectionBean<StickyNoteBean>();
		tasks = new MessageObjectCollectionBean<TaskBean>();
	}

	/**	Add the appointments in the given folder to the list of appointments..
	*	@param	folder	The folder from which to harvest appointments.
	*/
	private void addAppointments(Folder folder)
	{
		FolderBean<AppointmentBean> folderAppointments = new FolderBean<AppointmentBean>();
		folderAppointments.name = folder.displayName;

		for (Iterator<MessageObject> contents = folder.contentsIterator(); contents.hasNext(); ) {
			MessageObject mo = contents.next();
			if (!(mo instanceof Appointment))
				continue;

			Appointment a = (Appointment)mo;
			AppointmentBean b = new AppointmentBean();
			b.title = a.subject;
			b.start = a.startTime;
			b.end = a.endTime;
			folderAppointments.contents.add(b);
		}
		appointments.folders.add(folderAppointments);

		for (Iterator<Folder> folders = folder.subfolderIterator(); folders.hasNext(); )
			addAppointments(folders.next());
	}

	/**	Add the contacts in the given folder to the list of contacts.
	*	@param	folder	The folder from which to harvest contacts.
	*	@param	pst	The pst file to read the folder data from.
	*/
	private void addContacts(Folder folder, PST pst)
	{
		FolderBean<ContactBean> folderContacts = new FolderBean<ContactBean>();
		folderContacts.name = folder.displayName;

		for (Iterator<MessageObject> contents = folder.contentsIterator(); contents.hasNext(); ) {
			MessageObject mo = contents.next();
			if (!(mo instanceof Contact))
				continue;

			Contact c = (Contact)mo;
			ContactBean b = new ContactBean();
			b.name = c.displayName;
			for (String emailAddress : c.emailAddresses)
				b.emailAddresses.add(emailAddress);
			folderContacts.contents.add(b);
			if (c.homePhone != null)
				b.telephoneNumbers.add(c.homePhone);
			if (c.mobilePhone != null)
				b.telephoneNumbers.add(c.mobilePhone);
			if (c.businessPhone != null)
				b.telephoneNumbers.add(c.businessPhone);
			if (c.otherPhone != null)
				b.telephoneNumbers.add(c.otherPhone);
		}
		contacts.folders.add(folderContacts);

		for (Iterator<Folder> folders = folder.subfolderIterator(); folders.hasNext(); )
			addContacts(folders.next(), pst);
	}

	/**	Add the journal entries in the current folder to the list of journal entries.
	* 	@param	folder	The folder from which to harvest the journal entries.
	* 	@param	pst	The pst file from which to read the journal entries' data.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws	NotHeapNodeException			A node which was not a heap node was found where a heap node was expected when reading the journal entries.
	*	@throws NotPropertyContextNodeException		A node which was not a property context node was found where a property context node was expected when reading the journal entries. 
	*	@throws NotTableContextNodeException		A node which was not a table context node was found where a table context node was expected when reading the journal entries.
	*	@throws	NullDataBlockException			A null data block was found when reading the journal entries.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unrecognized client signature was found when reading the journal entries.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context was found whe nreading the journal entries.
	*	@throws UnparseableTableContextException	A bad / corrupt table context was found when reading the journal entries.
	*	@throws IOException			An I/O error was encoutnered while reading the journal entries.
	*/
	private void addJournalEntries(Folder folder, PST pst)
	throws
		IOException,
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException
	{
		FolderBean<JournalEntryBean> folderJournalEntry = new FolderBean<JournalEntryBean>();
		folderJournalEntry.name = folder.displayName;

		for (Iterator<MessageObject> contents = folder.contentsIterator(); contents.hasNext(); ) {
			MessageObject mo = contents.next();
			if (!(mo instanceof JournalEntry))
				continue;

			JournalEntry j = (JournalEntry)mo;
			JournalEntryBean b = new JournalEntryBean();
			b.title = j.subject;
			b.note = j.body(j.getMessage(pst));
			folderJournalEntry.contents.add(b);
		}
		journalEntries.folders.add(folderJournalEntry);

		for (Iterator<Folder> folders = folder.subfolderIterator(); folders.hasNext(); )
			addJournalEntries(folders.next(), pst);
	}

	/**	Add the sticky notes in the current folder to the list of sticky notes.
	*	@param	folder	The folder from which to harvest the sticky notes.
	*	@param	pst	The pst file from which to read the sticky notes data.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws	NotHeapNodeException			A node which was not a heap node was found where a heap node was expected when reading the sticky notes.
	*	@throws NotPropertyContextNodeException		A node which was not a property context node was found where a property context node was expected when reading the sticky notes. 
	*	@throws NotTableContextNodeException		A node which was not a table context node was found where a table context node was expected when reading the sticky notes.
	*	@throws	NullDataBlockException			A null data block was found when reading the sticky notes.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unrecognized client signature was found when reading the sticky notes.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context was found whe nreading the sticky notes.
	*	@throws UnparseableTableContextException	A bad / corrupt table context was found when reading the sticky notes.
	*	@throws IOException			An I/O error was encoutnered while reading the sticky notes.
	*/
	private void addStickyNotes(Folder folder, PST pst)
	throws
		IOException,
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException
	{
		FolderBean<StickyNoteBean> folderStickyNotes = new FolderBean<StickyNoteBean>();
		folderStickyNotes.name = folder.displayName;

		for (Iterator<MessageObject> contents = folder.contentsIterator(); contents.hasNext(); ) {
			MessageObject mo = contents.next();
			if (!(mo instanceof StickyNote))
				continue;

			StickyNote s = (StickyNote)mo;
			StickyNoteBean b = new StickyNoteBean();
			b.title = s.subject;
			b.note = s.body(s.getMessage(pst));
			folderStickyNotes.contents.add(b);
		}
		stickyNotes.folders.add(folderStickyNotes);

		for (Iterator<Folder> folders = folder.subfolderIterator(); folders.hasNext(); )
			addStickyNotes(folders.next(), pst);
	}

	/**	Add the tasks in the given folder to the list of tasks.
	*	@param	folder	The folder from which to harvest tasks.
	*/
	private void addTasks(Folder folder)
	{
		FolderBean<TaskBean> folderTasks = new FolderBean<TaskBean>();
		folderTasks.name = folder.displayName;

		for (Iterator<MessageObject> contents = folder.contentsIterator(); contents.hasNext(); ) {
			MessageObject mo = contents.next();
			if (!(mo instanceof Task))
				continue;

			Task t = (Task)mo;
			TaskBean b = new TaskBean();
			b.title = t.subject;
			b.dueDate = t.dueDate;
			folderTasks.contents.add(b);
		}
		tasks.folders.add(folderTasks);

		for (Iterator<Folder> folders = folder.subfolderIterator(); folders.hasNext(); )
			addTasks(folders.next());
	}

	/**	Check password, and process PST file if password is correct.
	*	@return	A String indicating the next view.
	*/
	public String checkPasswordAndProcess()
	{
		++numPasswordAttempts;
		if ((pst.hasPassword() && password.length() == 0) || (!pst.hasPassword() && password.length() > 0)) {
			if (numPasswordAttempts >= MAX_PASSWORD_ATTEMPTS) {
				resetForm();
				return "AccessDenied";
			}
	
			return "ResubmitPassword";
		}

		return doProcessPST();
	}

	/**	Get the required information from a PST file and handle any exceptions encountered during processing.
	*	@return	A String indicating the next view
	*/
	private String doProcessPST()
	{
		try {
			processPST();
			return "Results";
		} catch (final	IOException
			|	UnimplementedPropertyTypeException e) {
			e.printStackTrace(System.out);
			return "ProcessingProblem";
		} catch (final	BadXBlockLevelException
			|	BadXBlockTypeException
			|	CRCMismatchException
			|	DataOverflowException
			|	NotHeapNodeException
			|	NotPropertyContextNodeException
			|	NotTableContextNodeException
			|	NullDataBlockException
			|	NullNodeException
			|	UnknownClientSignatureException
			|	UnknownPropertyTypeException
			|	UnparseablePropertyContextException
			|	UnparseableTableContextException e) {
			e.printStackTrace(System.out);
			return "CorruptPST";
		}
	}

	/**	Get the list of appointments from this PST file.
	*	@return	The list of appointments found in this PST file.
	*/
	public MessageObjectCollectionBean<AppointmentBean> getAppointments()
	{
		return appointments;
	}

	/**	Get the list of contacts from this PST file.
	*	@return	The list of contacts found in this PST file.
	*/
	public MessageObjectCollectionBean<ContactBean> getContacts()
	{
		return contacts;
	}

	/**	Get the list of journal entries from this PST file.
	*	@return	The list of journal entires found in this PST file.
	*/
	public MessageObjectCollectionBean<JournalEntryBean> getJournalEntries()
	{
		return journalEntries;
	}

	/**	Get the array of extraction types choices.
	*	@return	The array of extraction types choices.
	*/
	public SelectItem[] getExtractionTypeChoices()
	{
		return extractionTypeChoices.clone();
	}

	/**	Retrieve the maximum number of password attempts permitted.
	*	@return	The maximum number of password attempts permitted.
	*	@see #MAX_PASSWORD_ATTEMPTS
	*/
	public int getMaxPasswordAttempts()
	{
		return MAX_PASSWORD_ATTEMPTS;
	}

	/**	Retrieve the number of password attempts so far.
	*	@return	The number of password attempts so far.
	*	@see #numPasswordAttempts
	*/
	public int getNumPasswordAttempts()
	{
		return numPasswordAttempts;
	}

	/**	Retrieve the password.
	*	@return	The password for the PST file, if any.
	*/
	public String getPassword()
	{
		return password;
	}

	/**	Get the array listing the extraction types selected.
	*	@return	An array containing the extraction types selected.
	*/
	public List<ExtractionTypes> getSelectedExtractionTypes()
	{
		return selectedExtractionTypes;
	}

	/**	Get the list of sticky notes from this PST file.
	*	@return	The list of sticky notes found in this PST file.
	*/
	public MessageObjectCollectionBean<StickyNoteBean> getStickyNotes()
	{
		return stickyNotes;
	}

	/**	Get the list of tasks from this PST file.
	*	@return	The list of tasks found in this PST file.
	*/
	public MessageObjectCollectionBean<TaskBean> getTasks()
	{
		return tasks;
	}

	/**	Retrieve the uploaded file.
	*	@return	The uploaded PST file
	*/
	public Part getUploadedFile()
	{
		return uploadedFile;
	}

	/**	Determine whether any results are available.
	*	@return	true if a PST file has been processed and is available, false otherwise.
	*/
	public boolean isResultAvailable()
	{
		return pst != null;
	}

	/**	Reset form data.
	*/
	public String resetForm()
	{
		pst = null;
		numPasswordAttempts = 0;
		selectedExtractionTypes = new ArrayList<ExtractionTypes>();
		return "ExtractionForm";
	}

	/**	Set the password.
	*	@param	password	The password for the PST file, if any.
	*/
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**	Get the required information from a PST file
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws	NotHeapNodeException			A node which was not a heap node was found where a heap node was expected when reading the pst file.
	*	@throws NotPropertyContextNodeException		A node which was not a property context node was found where a property context node was expected when reading the pst file.
	*	@throws NotTableContextNodeException		A node which was not a table context node was found where a table context node was expected when reading the pst file.
	*	@throws	NullDataBlockException			A null data block was found when reading the pst file.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unrecognized client signature was found when reading the pst file.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context was found whe nreading the pst file.
	*	@throws UnparseableTableContextException	A bad / corrupt table context was found when reading the pst file.
	*	@throws IOException			An I/O error was encoutnered while reading the pst file.
	*/
	private void processPST()
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		IOException
	{
		Folder rootFolder = pst.getFolderTree();

		appointments.requested = selectedExtractionTypes.contains(ExtractionTypes.APPOINTMENTS);
		contacts.requested = selectedExtractionTypes.contains(ExtractionTypes.CONTACTS);
		journalEntries.requested = selectedExtractionTypes.contains(ExtractionTypes.JOURNAL_ENTRIES);
		stickyNotes.requested = selectedExtractionTypes.contains(ExtractionTypes.STICKYNOTES);
		tasks.requested = selectedExtractionTypes.contains(ExtractionTypes.TASKS);

		for (Iterator<Folder> folderIterator = rootFolder.subfolderIterator(); folderIterator.hasNext(); ) {
			final Folder f = folderIterator.next();

			if (appointments.requested && IPF.isAppointment(f)) {
				Folder folderTree = Folder.getFolderTree(f.nodeFolderObject, pst.blockBTree, pst.nodeBTree, pst);
				addAppointments(folderTree);
			} else if (contacts.requested && IPF.isContact(f)) {
				Folder folderTree = Folder.getFolderTree(f.nodeFolderObject, pst.blockBTree, pst.nodeBTree, pst);
				addContacts(folderTree, pst);
			} else if (journalEntries.requested && IPF.isJournal(f)) {
				Folder folderTree = Folder.getFolderTree(f.nodeFolderObject, pst.blockBTree, pst.nodeBTree, pst);
				addJournalEntries(folderTree, pst);
			} else if (stickyNotes.requested && IPF.isStickyNote(f)) {
				Folder folderTree = Folder.getFolderTree(f.nodeFolderObject, pst.blockBTree, pst.nodeBTree, pst);
				addStickyNotes(folderTree, pst);
			} else if (tasks.requested && IPF.isTask(f)) {
				Folder folderTree = Folder.getFolderTree(f.nodeFolderObject, pst.blockBTree, pst.nodeBTree, pst);
				addTasks(folderTree);
			}
		}
	}

	/**	Set the uplaoded PST file.
	*	@param	uploadedFile	The uploaded PST file.
	*/
	public void setUploadedFile(Part uploadedFile)
	{
		this.uploadedFile = uploadedFile;
	}

	/**	Process submission from Incorrect Password form.
	*	@return	A String indicating the next view.
	*/
	public String submitPassword()
	{
		if (pst == null) {
			// Note that this is not an expected workflow.
			return submitPST();
		}

		return checkPasswordAndProcess();
	}

	/**	Process submission from main PST extraction form.
	*	@return	A String indicating the next view.
	*/
	public String submitPST()
	{
		try {
			InputStream is = uploadedFile.getInputStream();

			if (is instanceof FileInputStream) {
				try {
					pst = null;
					pst = new PST((FileInputStream)is, true);
					return checkPasswordAndProcess();
				} catch (final	IOException
					|	UnimplementedPropertyTypeException e) {
					// IO Exception creating or reading PST file
					e.printStackTrace(System.out);
					return "ProcessingProblem";
				} catch(final	BadXBlockLevelException
					|	BadXBlockTypeException
					|	CRCMismatchException
					|	DataOverflowException
					|	IncorrectNameIDStreamContentException
					|	NameIDStreamNotFoundException
					|	NotHeapNodeException
					|	NotPropertyContextNodeException
					|	NotTableContextNodeException
					|	NullDataBlockException
					|	NullNodeException
					|	UnknownClientSignatureException
					|	UnknownPropertyTypeException
					|	UnparseablePropertyContextException
					|	UnparseableTableContextException e) {
					e.printStackTrace(System.out);
					return "CorruptPST";
				} catch (final NotPSTFileException e) {
					e.printStackTrace(System.out);
					return "NotPST";
				}
			}
		} catch (final IOException e) {
			// IO Exception retrieving input stream.
			e.printStackTrace(System.out);
			return "ProcessingProblem";
		}

		// This is an unexpected condition. It can only occur if we raised an uncaught exception.
		return "ProcessingProblem";
	}
}
