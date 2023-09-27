package io.github.jmcleodfoss.pst;

/**	The MessageObject class is the base class for all content objects (appointments, contacts, journal entries, messages, etc.)
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">MS-OXCMSG: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">MS-PST Section 2.4.5: Message Objects</a>
*/
public class MessageObject
{
	/**	The message PC node information. */
	public final NBTEntry nodeMessageObject;

	/**	Whether the containing PST file is Unicode or ANSI.
	*	This value is stored, rather than the property IDs for each case, to reduce memory requirements.
	*/
	protected final boolean fUnicode;

	/**	The subject. */
	public final String subject;

	/**	Create a message object for the given row in the folder contents table.
	*	@param	contentsTable	The containing folder's contents table
	*	@param	messageRow	The row of the contents table from which to create the message
	*	@param	nbt		The PST file's node B-Tree
	*	@param	pstFile		The PST file's header, input stream, etc.
	* 	@throws	NotHeapNodeException			A node which was not a heap node was found while building the message object.
	* 	@throws	UnknownClientSignatureException		A block with an unrecognized client signature was found while building the message object.
	* 	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while building the message object
	* 	@throws	UnparseableTableContextException	A bad / corrupt table context was found while building the message object
	* 	@throws java.io.IOException			An I/O exception was encountered while reading the data for the message object.
	*	@see	Folder
	*	@see	Attachment
	*	@see	Recipient
	*/
	MessageObject(final TableContext contentsTable, final int messageRow, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		fUnicode = pstFile.unicode();
		final int subjectTag = fUnicode ? PropertyTags.SubjectW : PropertyTags.Subject;
		subject = DataType.makeString(subjectTag, contentsTable.get(messageRow, subjectTag));

		final int nidMessageObject = (Integer)contentsTable.get(messageRow, PropertyTags.LtpRowId);
		nodeMessageObject = nbt.find(new NID(nidMessageObject));
	}

	/**	Create a message object of the appropriate derived type.
	*	@param	contentsTable	The containing folder's contents table
	*	@param	row		The row of the contents table from which to create the message
	*	@param	bbt		The PST file's block B-Tree
	*	@param	nbt		The PST file's node B-Tree
	*	@param	pstFile		The PST file's header, input stream, etc.
	*	@return	The message object found at the given row of the content table.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	* 	@throws	NotHeapNodeException			A node which was not a heap node was found while building the message object.
	* 	@throws	NotPropertyContextNodeException		A node which was expected to be a property context node was found to be something else.
	* 	@throws	NotTableContextNodeException		A node which was expected to be a table context node was found to be something else.
	*	@throws	NullDataBlockException			A null data block was encountered while building the message object.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	* 	@throws	UnknownClientSignatureException		A block with an unrecognized client signature was found while building the message object.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	* 	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while building the message object
	* 	@throws	UnparseableTableContextException	A bad / corrupt table context was found while building the message object
	* 	@throws java.io.IOException			An I/O exception was encountered while reading the data for the message object.
	*/
	@SuppressWarnings("PMD.NPathComplexity")
	static MessageObject factory(TableContext contentsTable, final int row, final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
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
		java.io.IOException
	{
		final String messageType = (String)contentsTable.get(row, pstFile.unicode() ? PropertyTags.MessageClassW : PropertyTags.MessageClass);

		if (IPM.isAppointment(messageType))
			return new Appointment(contentsTable, row, nbt, pstFile);

		if (IPM.isContact(messageType))
			return new Contact(contentsTable, row, nbt, pstFile);

		if (IPM.isDistributionList(messageType))
			return new DistributionList(contentsTable, row, nbt, pstFile);

		if (IPM.isJournalEntry(messageType))
			return new JournalEntry(contentsTable, row, nbt, pstFile);

		if (IPM.isPersonMetadata(messageType))
			return new PersonMetadata(contentsTable, row, nbt, pstFile);

		if (IPM.isStickyNote(messageType))
			return new StickyNote(contentsTable, row, nbt, pstFile);

		if (IPM.isTask(messageType))
			return new Task(contentsTable, row, nbt, pstFile);

		return new Message(contentsTable, row, bbt, nbt, pstFile);
	}

	/**	Retrieve the message object property context.
	*	@param	pst	The PST file.
	*	@return	The message object property context, required as a parameter for other functions in the class.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws NotHeapNodeException			A node which is not a heap node was found in the purported heap.
	*	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found when building the property context.
	*	@throws NotTableContextNodeException		A node without the Table Context client signature was found when building the table context.
	*	@throws NullDataBlockException			A null data block was found when building the property context.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unrecognized client signature was found when reading a block.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	The property context for this message could not be interpreted.
	*	@throws UnparseableTableContextException	The table context for this message could not be interpreted.
	*	@throws java.io.IOException			The PST file could not be read.
	*	@see	Message#body
	*	@see	MessageObjectWithBody#bodyHtml
	*	@see	Message#transportHeaders
	*/
	public PropertyContext getMessage(final PST pst)
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
		java.io.IOException
	{
		return new PropertyContext(nodeMessageObject, pst.blockBTree, pst);
	}

	/**	Print out all the objects of a given class in a folder and all of its sub-folders.
	*	Used only by main (test) function.
	*	@param	folder	The folder to print out the objects in
	*	@param	path	The path to the folder
	*	@param	cl	The class of object to look for.
	*/
	@SuppressWarnings("rawtypes") // This is used only for testing / diagnostics
	private static void printFolderObjects(Folder folder, String path, final Class cl)
	{
		String newPath = path + folder.displayName + "/";
		for (java.util.Iterator<Folder> subfolders = folder.subfolderIterator(); subfolders.hasNext(); )
			printFolderObjects(subfolders.next(), newPath, cl);

		boolean fFolderShown = false;
		for (java.util.Iterator<MessageObject> messageObjects = folder.contentsIterator(); messageObjects.hasNext(); ){
			MessageObject mo = messageObjects.next();
			if (cl.isInstance(mo)) {
				if (!fFolderShown) {
					fFolderShown = true;
					System.out.println(newPath);
				}
				System.out.println(mo.toString());
			}
		}
	}

	/**	Provide a string describing the MessageObject (used primarily for testing)
	*	@return	A String describing the MessageObject
	*/
	@Override
	public String toString()
	{
		return subject;
	}

	/**	Test the given class by iterating through the messages.
	*	@param	clName	The name of the class of messages to be displayed.
	* 	@param	args	The file(s) to show the message objects for
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void test(final String clName, final String[] args)
	{
		final java.util.Set<String> ALLOWABLE_OBJECTS = java.util.Set.of(
			"io.github.jmcleodfoss.pst.Appointment",
			"io.github.jmcleodfoss.pst.Contact",
			"io.github.jmcleodfoss.pst.DistributionList",
			"io.github.jmcleodfoss.pst.JournalEntry",
			"io.github.jmcleodfoss.pst.Message",
			"io.github.jmcleodfoss.pst.PersonMetatdata",
			"io.github.jmcleodfoss.pst.StickyNore",
			"io.github.jmcleodfoss.pst.Task"
		);
		if (args.length == 0 || !ALLOWABLE_OBJECTS.contains(clName)) {

			System.out.printf("use:%n\tjava objectType pst-file [pst-file ...]%n");
			System.out.println("Valid object types are");
			for (final String s: ALLOWABLE_OBJECTS)
				System.out.println(s);

			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {
				final PST pst = new PST(a);
				printFolderObjects(pst.getFolderTree(), "/", Class.forName(clName));
				pst.close();
			} catch (final	BadXBlockLevelException
				|	BadXBlockTypeException
				|	DataOverflowException
				|	IncorrectNameIDStreamContentException
				|	NameIDStreamNotFoundException
				|	NotHeapNodeException
				|	NotPropertyContextNodeException
				|	NotTableContextNodeException
				|	NullDataBlockException
				|	NullNodeException
				|	UnimplementedPropertyTypeException
				|	UnknownPropertyTypeException
				|	UnknownClientSignatureException
				|	UnparseablePropertyContextException
				|	UnparseableTableContextException
				|	ClassNotFoundException e) {
				System.out.println(e.toString());
				e.printStackTrace(System.out);
			} catch (final CRCMismatchException e) {
				System.out.printf("File %s is corrupt (Calculated CRC does not match expected value)%n", a);
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				e.printStackTrace(System.out);
			}
		}
	}
}
