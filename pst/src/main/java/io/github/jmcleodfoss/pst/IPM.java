package io.github.jmcleodfoss.pst;

/**	The IPM holds the list of IPM message types. */
class IPM
{
	/**	An alternate string for journal message objects
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String ACTIVITY = "IPM.Activity";

	/**	The string used for appointment message objects.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String APPOINTMENT = "IPM.Appointment";

	/**	The string used for contact message objects.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String CONTACT = "IPM.Contact";

	/**	The string used for distribution lists.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String DISTRIBUTION_LIST = "IPM.DistList";

	/**	The string used for documents
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String DOCUMENT = "IPM.Document";

	/**	The string used for journal message objects.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String JOURNAL = "IPM.Journal";

	/**	The string used for note message objects.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String NOTE = "IPM.Note";

	/**	The string used for reports from the Internet Mail Connect (Exchange Server gateway to the internet)
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String IMC_NOTIFICATION = "IPM.Note.IMC.Notification";

	/**	The string used for out-of-office tempates
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String OOO_TEMPLATE = "IPM.Note.Rules.OOfTemplate.Microsoft";

	/**	The string used for person metadata entries.
	*	This is used in OST 2013 files, and does not seem to be documented anywhere. */
	private static final String PERSON_METADATA = "IPM.AbchPerson";

	/**	The string used for notes posted in a folder.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String POST = "IPM.Post";

	/**	The string used for sticky note mesasge objects.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String STICKYNOTE = "IPM.StickyNote";

	/**	The string used for recall reports
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String RECALL_REPORT = "IPM.Recall.Report";

	/**	The string used for recall requests
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String OUTLOOK_RECALL = "IPM.Outlook.Recall";

	/**	Remote mail message headers
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String REMOTE_MAIL_MESSAGE_HEADERS = "IPM.Remote";

	/**	Messages to be resent when previous send attempt failed
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String RESEND = "IPM.Resend";

	/**	Editing rule reply template
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String RULE_REPLY_TEMPLATE = "IPM.Note.Rules.ReplyTemplate.Microsoft";

	/**	Meeting Cancellations
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String MEETING_CANCELLATION = "IPM.Schedule.Meeting.Cancelled";

	/**	Meeting requests
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String MEETING_REQUEST = "IPM.Schedule.Meeting.Request";

	/**	Meeting declined responses
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String MEETING_DECLINED = "IPM.Schedule.Meeting.Resp.Neg";

	/**	Meeting accepted responses
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String MEETING_ACCEPTED = "IPM.Schedule.Meeting.Resp.Pos";

	/**	Meeting tentatively accepted responses
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String MEETING_TENTATIVE = "IPM.Schedule.Meeting.Resp.Tent";

	/**	Encrypted notes to others
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String SECURE_NOTE = "IPM.Note.Secure";

	/**	Digitally signed notes to others
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String SIGNED_NOTE = "IPM.Note.Secure.Sign";

	/**	The string used for task message objects.
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String TASK = "IPM.Task";

	/**	Task accepted responses
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String TASK_ACCEPTED = "IPM.TaskRequest.Accept";

	/**	Task declined responses
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String TASK_DECLINED = "IPM.TaskRequest.Decline";

	/**	Task Requests
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String TASK_REQUEST = "IPM.TaskRequest";

	/**	Task Update messages
	*	@see <a href="https://docs.microsoft.com/en-us/office/vba/outlook/concepts/forms/item-types-and-message-classes">Iten Types and Messages</a>
	*/
	private static final String TASK_UPDATE = "IPM.TaskRequest.Update";

	/**	The list of known message class types. */
	static final java.util.Vector<String> knownClasses = new java.util.Vector<String>();
	static {
		knownClasses.add(ACTIVITY);
		knownClasses.add(APPOINTMENT);
		knownClasses.add(CONTACT);
		knownClasses.add(DISTRIBUTION_LIST);
		knownClasses.add(DOCUMENT);
		knownClasses.add(JOURNAL);
		knownClasses.add(NOTE);
		knownClasses.add(IMC_NOTIFICATION);
		knownClasses.add(OOO_TEMPLATE);
		knownClasses.add(PERSON_METADATA);
		knownClasses.add(POST);
		knownClasses.add(STICKYNOTE);
		knownClasses.add(RECALL_REPORT);
		knownClasses.add(OUTLOOK_RECALL);
		knownClasses.add(REMOTE_MAIL_MESSAGE_HEADERS);
		knownClasses.add(RESEND);
		knownClasses.add(RULE_REPLY_TEMPLATE);
		knownClasses.add(MEETING_CANCELLATION);
		knownClasses.add(MEETING_REQUEST);
		knownClasses.add(MEETING_DECLINED);
		knownClasses.add(MEETING_ACCEPTED);
		knownClasses.add(MEETING_TENTATIVE);
		knownClasses.add(SECURE_NOTE);
		knownClasses.add(SIGNED_NOTE);
		knownClasses.add(TASK);
		knownClasses.add(TASK_ACCEPTED);
		knownClasses.add(TASK_DECLINED);
		knownClasses.add(TASK_REQUEST);
		knownClasses.add(TASK_UPDATE);
	}

	/**	Is the given message an appointments?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.Appointment.
	*/
	static boolean isAppointment(String messageClass)
	{
		return APPOINTMENT.equals(messageClass);
	}

	/**	Is the given message object a contact?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.Contact.
	*/
	static boolean isContact(String messageClass)
	{
		return CONTACT.equals(messageClass);
	}

	/**	Is the given message object a distribution list?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.DistributionList.
	*/
	static boolean isDistributionList(String messageClass)
	{
		return DISTRIBUTION_LIST.equals(messageClass);
	}

	/**	Is the given message a journal entry?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.Journal.
	*/
	static boolean isJournalEntry(String messageClass)
	{
		return JOURNAL.equals(messageClass);
	}

	/**	Is the class of the given message one of the known classes?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is one of the known classes, false otherwise.
	*/
	static boolean isKnownClass(String messageClass)
	{
		return knownClasses.contains(messageClass);
	}

	/**	Is the given message a note?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.Note.
	*/
	static boolean isNote(String messageClass)
	{
		return NOTE.equals(messageClass);
	}

	/**	Is the given message a person metadata object?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.Note.
	*/
	static boolean isPersonMetadata(String messageClass)
	{
		return PERSON_METADATA.equals(messageClass);
	}
	/**	Is the given folder a sticky note?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.StickyNote.
	*/
	static boolean isStickyNote(String messageClass)
	{
		return STICKYNOTE.equals(messageClass);
	}

	/**	Is the given message a task?
	*	@param	messageClass	The message class to check.
	*	@return	true if the given message's class is IPM.Task.
	*/
	static boolean isTask(String messageClass)
	{
		return TASK.equals(messageClass);
	}

	/**	Get an iterator through the known classes.
	*	@return	An iterator which may be used to step through the known folder classes.
	*/
	static java.util.Iterator<String> iterator()
	{
		return knownClasses.iterator();
	}

	/**	Loop through folder's subfolders and message objects displaying the type of each message object found.
	*	Used only for testing Used only for testing
	*	@param	folder	The folder to process
	*	@param	pst	The pst file to look in
	*	@param	fmtOutput	The format to use for output.
	*	@see	#main
	*/
	private static void showFolderMessageClasses(Folder folder, PST pst, String fmtOutput)
	{
		for (java.util.Iterator<Folder> folderIterator = folder.subfolderIterator(); folderIterator.hasNext(); )
			showFolderMessageClasses(folderIterator.next(), pst, fmtOutput);

		for (java.util.Iterator<MessageObject> msgIterator = folder.contentsIterator(); msgIterator.hasNext(); ){
			MessageObject msg = msgIterator.next();
			try {
				PropertyContext pc = msg.getMessage(pst);
				final String messageType = (String)pc.get(pst.unicode() ? PropertyTags.MessageClassW : PropertyTags.MessageClass);
				System.out.printf(fmtOutput, msg.subject, messageType, isKnownClass(messageType));
			} catch (final BadXBlockLevelException e) {
				System.out.println(e);
				e.printStackTrace(System.out);
			} catch (final BadXBlockTypeException e) {
				System.out.println(e);
				e.printStackTrace(System.out);
			} catch (final CRCMismatchException e) {
				e.printStackTrace(System.out);
			} catch (final DataOverflowException e) {
				e.printStackTrace(System.out);
			} catch (final NotHeapNodeException e) {
				e.printStackTrace(System.out);
			} catch (final NotPropertyContextNodeException e) {
				e.printStackTrace(System.out);
			} catch (final NotTableContextNodeException e) {
				e.printStackTrace(System.out);
			} catch (final NullDataBlockException e) {
				e.printStackTrace(System.out);
			} catch (final UnimplementedPropertyTypeException e) {
				e.printStackTrace(System.out);
			} catch (final UnknownClientSignatureException e) {
				e.printStackTrace(System.out);
			} catch (final UnknownPropertyTypeException e) {
				e.printStackTrace(System.out);
			} catch (final UnparseablePropertyContextException e) {
				e.printStackTrace(System.out);
			} catch (final UnparseableTableContextException e) {
				e.printStackTrace(System.out);
			} catch (final java.io.IOException e) {
				e.printStackTrace(System.out);
			}
		}
	}

	/**	Test the IPM class by iterating through all message objects and displaying the type of each and whether it is known.
	*	@param	args	The file(s) to show the folder information for.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:");
			System.out.println("\tjava io.github.jmcleodfoss.pst.IPM pst-file [pst-file ...]");
			System.out.println("\nTo get the list of recognized folder types:");
			System.out.println("\tjava io.github.jmcleodfoss.pst.IPM --list");
			System.exit(1);
		}

		if (args[0].equals("--list")) {
			System.out.println("Known message types");
			for (java.util.Iterator<String> iter = iterator(); iter.hasNext(); )
				System.out.println(iter.next());
			System.exit(1);
		}

		final String fmtOutput = "%-25s %-25s %-10s%n";

		for (final String a: args) {
			System.out.println(a);
			try {
				final PST pst = new PST(a);
				System.out.printf(fmtOutput, "Subject", "Container Class", "Known Container Class?");
				showFolderMessageClasses(pst.messageStore.rootFolder, pst, fmtOutput);
				pst.close();
			} catch (final BadXBlockLevelException e) {
				System.out.println(e);
				e.printStackTrace(System.out);
			} catch (final BadXBlockTypeException e) {
				System.out.println(e);
				e.printStackTrace(System.out);
			} catch (final CRCMismatchException e) {
				System.out.printf("File %s is corrupt (Calculated CRC does not match expected value)%n", a);
			} catch (final DataOverflowException e) {
				e.printStackTrace(System.out);
			} catch (final IncorrectNameIDStreamContentException e) {
				e.printStackTrace(System.out);
			} catch (final NameIDStreamNotFoundException e) {
				e.printStackTrace(System.out);
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
			} catch (final UnimplementedPropertyTypeException e) {
				e.printStackTrace(System.out);
			} catch (final UnknownClientSignatureException e) {
				e.printStackTrace(System.out);
			} catch (final UnknownPropertyTypeException e) {
				e.printStackTrace(System.out);
			} catch (final UnparseablePropertyContextException e) {
				e.printStackTrace(System.out);
			} catch (final UnparseableTableContextException e) {
				e.printStackTrace(System.out);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				e.printStackTrace(System.out);
			}
		}
	}
}
