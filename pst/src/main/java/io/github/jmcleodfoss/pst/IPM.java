package io.github.jmcleodfoss.pst;

/**	The IPM holds the list of IPM message types. */
class IPM {

	/**	The string used for appointment message objects. */
	private static final String APPOINTMENT = "IPM.Appointment";

	/**	The string used for contact message objects. */
	private static final String CONTACT = "IPM.Contact";

	/**	The string used for journal message objects. */
	private static final String JOURNAL = "IPM.Journal";

	/**	The string used for sticky note mesasge objects. */
	private static final String STICKYNOTE = "IPM.StickyNote";

	/**	The string used for task message objects. */
	private static final String TASK = "IPM.Task";

	/**	The string used for note message objects. */
	private static final String NOTE = "IPM.Note";

	/**	The string used for the distribution list message objects. */
	private static final String DISTRIBUTION_LIST = "IPM.DistList";

	/**	The list of known message class types. */
	static final java.util.Vector<String> knownClasses = new java.util.Vector<String>();
	static {
		knownClasses.add(APPOINTMENT);
		knownClasses.add(CONTACT);
		knownClasses.add(DISTRIBUTION_LIST);
		knownClasses.add(JOURNAL);
		knownClasses.add(NOTE);
		knownClasses.add(STICKYNOTE);
		knownClasses.add(TASK);
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
}
