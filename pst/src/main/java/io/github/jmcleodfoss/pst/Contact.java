package io.github.jmcleodfoss.pst;

/**	The Contact class represents a PST contact message object. The values set in the constructor are those which come from the
*	folder Contents Table; retrieval of other fields require that the client provide the message property context from which the
*	fields may be extracted.
*
*	An example of the expected use case is:
*	<pre>
*	{@code
*	// given the contents table object contentsTable and the PST object pst:
*
*	Contact contact = new Contact(contentsTable, 0, pst.blockBTree, pst.nodeBTree, pst);
*	System.out.printf("contact: %s email %s\n", contact.subject, message.messageDeliveryTime);
*
*	PropertyContext contactPC = contact.getMessageObject(pst.blockBTree, pst);
*	System.out.printf("body %s\n", message.body(messagePC);
*	}
*	</pre>
*
*	@see	io.github.jmcleodfoss.pst.Appointment
*	@see	io.github.jmcleodfoss.pst.JournalEntry
*	@see	io.github.jmcleodfoss.pst.Message
*	@see	io.github.jmcleodfoss.pst.StickyNote
*	@see	io.github.jmcleodfoss.pst.Task
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">[MX-OXPROPS]: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">[MS-OXCMSG]: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">[MS-PST}: Outlook Personal Folders (.pst) files, Section 2.4.5: Message Objects</a>
*/
public class Contact extends MessageObject {

	/**	The tags under which to look up the email addresses. */
	private static final int[] emailAddressLIDLookup = {
		PropertyLIDs.Email1EmailAddress,
		PropertyLIDs.Email2EmailAddress,
		PropertyLIDs.Email3EmailAddress
	};

	/**	The property ID of the "Email 1 Address" property. */
	private static int[] emailAddressLIDs = { PropertyLIDs.UNKNOWN, PropertyLIDs.UNKNOWN, PropertyLIDs.UNKNOWN};

	/**	The display name for the contact. */
	public final String displayName;

	/**	The contact's first name. */
	public final String givenName;

	/**	The contact's middle name. */
	public final String middleName;

	/**	The contact's surname. */
	public final String surname;

	/**	The contact's home phone number. */
	public final String homePhone;

	/**	The contact's home fax number. */
	public final String homeFax;

	/**	The contact's primary mobile number. */
	public final String mobilePhone;

	/**	The contact's work phone number. */
	public final String businessPhone;

	/**	The contact's work fax number. */
	public final String businessFax;

	/**	The contact's postal address number. */
	public final String postalAddress;

	/**	The contact's work organization. */
	public final String companyName;

	/**	The contact's other/alternate phone number. */
	public final String otherPhone;

	/**	The contact's email addresses. */
	public final java.util.List<String> emailAddresses;
	
	/**	Create a Contact for the given row in the folder contents table.
	*
	*	@param	contentsTable	The containing folder's contents table.
	*	@param	row		The row of the contents table from which to create the contact.
	*	@param	bbt		The PST file's block B-Tree.
	*	@param	nbt		The PST file's node B-Tree.
	*	@param	pstFile		The PST file's header, input stream, etc.
	*
	*	@throws	NotHeapNodeException			A node which is not a heap node was found while reading the contact data.
	*	@throws	UnknownClientSignatureException		An unknown client signature was encoutered while reading the contact data.
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while readind the contact data.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was found while reading the contact data.
	*	@throws java.io.IOException			An I/O error was enountered while reading the data for this contact.
	*/
	Contact(final TableContext contentsTable, final int row, final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(contentsTable, row, nbt, pstFile);

		String name = (String)contentsTable.get(row, fUnicode ? PropertyTags.DisplayNameW : PropertyTags.DisplayName);
		if (name == null)
			name = (String)contentsTable.get(row, fUnicode ? PropertyTags.CompanyNameW : PropertyTags.CompanyName);
		if (name == null)
			name = subject;
		if (name == null)
			name = (String)contentsTable.get(row, fUnicode ? PropertyTags.ConversationTopicW : PropertyTags.ConversationTopic);
		displayName = name;
		givenName = (String)contentsTable.get(row, fUnicode ? PropertyTags.GivenNameW : PropertyTags.GivenName);
		middleName = (String)contentsTable.get(row, fUnicode ? PropertyTags.MiddleNameW : PropertyTags.MiddleName);
		surname = (String)contentsTable.get(row, fUnicode ? PropertyTags.SurnameW : PropertyTags.Surname);
		homePhone = (String)contentsTable.get(row, fUnicode ? PropertyTags.HomeTelephoneNumberW : PropertyTags.HomeTelephoneNumber);
		homeFax = (String)contentsTable.get(row, fUnicode ? PropertyTags.HomeFaxNumberW : PropertyTags.HomeFaxNumberW);
		mobilePhone = (String)contentsTable.get(row, fUnicode ? PropertyTags.MobileTelephoneNumberW : PropertyTags.MobileTelephoneNumber);
		businessPhone = (String)contentsTable.get(row, fUnicode ? PropertyTags.BusinessTelephoneNumberW : PropertyTags.BusinessTelephoneNumber);
		businessFax = (String)contentsTable.get(row, fUnicode ? PropertyTags.BusinessFaxNumberW : PropertyTags.BusinessFaxNumber);
		postalAddress = (String)contentsTable.get(row, fUnicode ? PropertyTags.PostalAddressW : PropertyTags.PostalAddress);
		companyName = (String)contentsTable.get(row, fUnicode ? PropertyTags.CompanyNameW : PropertyTags.CompanyName);
		otherPhone = (String)contentsTable.get(row, fUnicode ? PropertyTags.OtherTelephoneNumberW : PropertyTags.OtherTelephoneNumber);

		emailAddresses = new java.util.ArrayList<String>(3);
		for (int propId : emailAddressLIDs) {
			String emailAddress = (String)contentsTable.get(row, propId);
			if (emailAddress != null)
				emailAddresses.add(emailAddress);
		}
	}

	/**	Return a string describing the contact.
	*
	*	@return	A string describing the contact.
	*/
	@Override
	public String toString()
	{
		return String.format("%s", subject);
	}

	/**	Save named property IDs for IDs of interest.
	*
	*	@param	namedProperties	The list of named properties.
	*/
	static void initConstants(NameToIDMap namedProperties)
	{
		for (int i = 0; i < emailAddressLIDLookup.length; ++i)
			emailAddressLIDs[i] = namedProperties.id(emailAddressLIDLookup[i], DataType.STRING);
	}
}
