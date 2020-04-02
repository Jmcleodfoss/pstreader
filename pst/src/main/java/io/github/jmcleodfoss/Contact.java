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
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">{MX-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*	@see	"[MS-OXCXMSG] Message and Attacment Object Protocol Specifications v20101027"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc463900(v=EXCHG.80).aspx">[MS-OXCMSG]: Message and Attachment Object Protocol Specification (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385158(v=office.12).aspx">Message Objects (MSDN)</a>
*/
public class Contact extends MessageObject {

	/**	The tags under which to look up the email addresses. */
	private static final int[] emailAddressPropertyIDLookup = {
		PropertyTag.Email1EmailAddress,
		PropertyTag.Email2EmailAddress,
		PropertyTag.Email3EmailAddress
	};

	/**	The property ID of the "Email 1 Address" property. */
	private static int[] emailAddressPropertyIDs = { -1, -1, -1};

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

		String name = (String)contentsTable.get(row, fUnicode ? PropertyTag.DisplayNameW : PropertyTag.DisplayName);
		if (name == null)
			name = (String)contentsTable.get(row, fUnicode ? PropertyTag.CompanyNameW : PropertyTag.CompanyName);
		if (name == null)
			name = subject;
		if (name == null)
			name = (String)contentsTable.get(row, fUnicode ? PropertyTag.ConversationTopicW : PropertyTag.ConversationTopic);
		displayName = name;
		givenName = (String)contentsTable.get(row, fUnicode ? PropertyTag.GivenNameW : PropertyTag.GivenName);
		middleName = (String)contentsTable.get(row, fUnicode ? PropertyTag.MiddleNameW : PropertyTag.MiddleName);
		surname = (String)contentsTable.get(row, fUnicode ? PropertyTag.SurnameW : PropertyTag.Surname);
		homePhone = (String)contentsTable.get(row, fUnicode ? PropertyTag.HomeTelephoneNumberW : PropertyTag.HomeTelephoneNumber);
		homeFax = (String)contentsTable.get(row, fUnicode ? PropertyTag.HomeFaxNumberW : PropertyTag.HomeFaxNumberW);
		mobilePhone = (String)contentsTable.get(row, fUnicode ? PropertyTag.MobileTelephoneNumberW : PropertyTag.MobileTelephoneNumber);
		businessPhone = (String)contentsTable.get(row, fUnicode ? PropertyTag.BusinessTelephoneNumberW : PropertyTag.BusinessTelephoneNumber);
		businessFax = (String)contentsTable.get(row, fUnicode ? PropertyTag.BusinessFaxNumberW : PropertyTag.BusinessFaxNumber);
		postalAddress = (String)contentsTable.get(row, fUnicode ? PropertyTag.PostalAddressW : PropertyTag.PostalAddress);
		companyName = (String)contentsTable.get(row, fUnicode ? PropertyTag.CompanyNameW : PropertyTag.CompanyName);
		otherPhone = (String)contentsTable.get(row, fUnicode ? PropertyTag.OtherTelephoneNumberW : PropertyTag.OtherTelephoneNumber);

		emailAddresses = new java.util.ArrayList<String>(3);
		for (int propId : emailAddressPropertyIDs) {
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
		for (int i = 0; i < emailAddressPropertyIDLookup.length; ++i)
			emailAddressPropertyIDs[i] = namedProperties.id(emailAddressPropertyIDLookup[i]);
	}
}
