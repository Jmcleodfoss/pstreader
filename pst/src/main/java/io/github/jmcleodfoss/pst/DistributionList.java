package io.github.jmcleodfoss.pst;

/**	The DistributionList class represents a PST contact distribution list message object. The values set in the constructor are
*	those which come from the folder Contents Table; retrieval of other fields require that the client provide the message property
*	context from which the fields may be extracted.
*
*	An example of the expected use case is:
*	<pre>
*	{@code
*	// given the contents table object contentsTable and the PST object pst:
*
*	DistributionList distributionList = new DistributionList(contentsTable, 0, pst.blockBTree, pst.nodeBTree, pst);
*
*	PropertyContext distributionListPC = distributionList.getMessageObject(pst.blockBTree, pst);
*	}
*	</pre>
*
*	@see	io.github.jmcleodfoss.pst.Appointment
*	@see	io.github.jmcleodfoss.pst.Contact
*	@see	io.github.jmcleodfoss.pst.Message
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">{MX-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*	@see	"[MS-OXCXMSG] Message and Attacment Object Protocol Specifications v20101027"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc463900(v=EXCHG.80).aspx">[MS-OXCMSG]: Message and Attachment Object Protocol Specification (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385158(v=office.12).aspx">Message Objects (MSDN)</a>
*/
public class DistributionList extends MessageObject {

	/**	The property ID of the DistributionListMembers property. */
	private static int distributionListMembersLID = PropertyLIDs.UNKNOWN;

	/**	The property ID of the DistributionListOneOffMembers property. */
	private static int distributionListOneOffMembersLID = PropertyLIDs.UNKNOWN;

	/**	The property ID of the "Email 1 Address" property. */
	private static int email1AddressLID = PropertyLIDs.UNKNOWN;

	/**	The Entry class flags an object as being a distribution list entry. */
	public static class Entry {

		/**	The display name of the distribution list member. */
		protected String displayName;

		/**	The e-mail address of the distribution list member. */
		protected String emailAddress;

		/**	Retrieve the display name of this distribution list entry.
		*
		*	@return	The display name for this distribution list entry.
		*/
		public String displayName()
		{
			return displayName;
		}

		/**	Retrieve the email address of this distribution list entry.
		*
		*	@return	The email address for this distribution list entry.
		*/
		public String emailAddress()
		{
			return emailAddress;
		}

		/**	Create a String describing the distribution list entry.
		*
		*	@return	A String describing the one-off distribution list entry.
		*/
		@Override
		public String toString()
		{
			StringBuilder s = new StringBuilder();
			s.append(emailAddress);
			if (displayName.length() > 0) {
				s.append(" (");
				s.append(displayName);
				s.append(')');
			}
			return s.toString();
		}
	}

	/**	The OneOffEntry class describes a "one-off" entry in a distribution list, i.e. an entry which is not stored in the
	*	PST contact files.
	*/
	private static class OneOffEntry extends Entry {

		/**	When this bit is set in the second flag byte, the strings for the display name, address type, and e-mail
		*	address are stored in Unicode and delimited by double-NULs; when this bit is cleared, the strings are stored in
		*	ANSI and delimited by single NULs.
		*/
		private static short FLAG_U_MASK = 0x80;
	
		private static final String nm_Version = "wVersion";
		private static final String nm_Flags1 = "bitmapFlag1";
		private static final String nm_Flags2 = "bitmapFlag2";
		private static final String nm_StringData = "stringData";

		/**	The fields comprising the one-off entry data. */
		private static final DataDefinition[] fields = new DataDefinition[] {
			new DataDefinition(nm_Version, DataType.integer16Reader, false),
			new DataDefinition(nm_Flags1, DataType.integer8Reader, false),
			new DataDefinition(nm_Flags2, DataType.integer8Reader, true),
		};

		/**	Construct a OneOffEntry object from the data in the given ByteBuffer.
		*
		*	@param	byteBuffer	The ByteBuffer from which to read the data
		*
	*	@throws	java.io.IOException	An I/O problem was encoutered when reading in the address book entry.
		*/
		OneOffEntry(java.nio.ByteBuffer byteBuffer)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();

			dc.read(byteBuffer, fields);
			byte flags = (Byte)dc.get(nm_Flags2);
			boolean fUnicode = (flags & FLAG_U_MASK) != 0;

			DataDefinition d = new DataDefinition(nm_StringData, DataType.definitionFactory(fUnicode ? DataType.STRING : DataType.STRING_8), true);
			dc.read(byteBuffer, d);

			String s = (String)dc.get(nm_StringData);

			int ofs = 0;
			displayName = s.substring(ofs, ofs = s.indexOf(0, ofs));
			if (fUnicode)
				++ofs;

			// Skip the address type
			ofs = s.indexOf(0, ofs);
			if (fUnicode)
				++ofs;

			emailAddress = s.substring(ofs, ofs = s.indexOf(0, ofs));
		}
	}

	/**	The AddressBookEntry class holds information about a distribution list member which is stored in the PST file's
	*	contacts.
	*/
	private static class AddressBookEntry extends Entry {

		private static final String nm_EntryID = "entryID";

		/**	The fields comprising the one-off entry data. */
		private static final DataDefinition d[] = new DataDefinition[] {
			new DataDefinition("skip", new DataType.SizedByteArray(1), false),
			new DataDefinition(nm_EntryID, new DataType.SizedByteArray(24), true)
		};

		/**	Extract and save the relevant information from the contact PC for this contact.
		*
		*	@param	byteBuffer	The data from which to read the entry ID.
		*	@param	bbt		The block B-tree for this PST file.
		*	@param	nbt		The node B-tree for this PST file.
		*	@param	pstFile		The PST file's data stream, header information, etc.
		*
		*	@throws	java.io.IOException	An I/O problem was encoutered when reading in the address book entry.
		*/
		AddressBookEntry(java.nio.ByteBuffer byteBuffer, BlockMap bbt, NodeMap nbt, PSTFile pstFile)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(byteBuffer, d);
			byte[] rawData = (byte[])dc.get(nm_EntryID);
			EntryID entryID = new EntryID(rawData);
			try {
				PropertyContext pc = entryID.propertyContext(bbt, nbt, pstFile);
				displayName = (String)pc.get(PropertyTags.DisplayNameW);
				emailAddress = (String)pc.get(email1AddressLID);
			} catch (final Exception e) {
			}
		}
	}

	/**	Create a distribution list object for the given row in the folder contents table.
	*
	*	@param	contentsTable	The containing folder's contents table.
	*	@param	row		The row of the contents table from which to create the contact.
	*	@param	bbt		The PST file's block B-Tree.
	*	@param	nbt		The PST file's node B-Tree.
	*	@param	pstFile		The PST file's header, input stream, etc.
	*
	*	@throws NotHeapNodeException			A node which is not a heap node was encountered while reading in the data for the distribution list.
	*	@throws UnknownClientSignatureException		An unknown client signature was encountered while reading in the data for the distribution list.
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context was found while reading in the data for the distribution list.
	*	@throws UnparseableTableContextException	A bad / corrupt table context was found while reading in the data for the distribution list.
	*	@throws	java.io.IOException	An I/O problem was encoutered when reading in the address book.
	*/
	DistributionList(final TableContext contentsTable, final int row, final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(contentsTable, row, nbt, pstFile);
	}

	/**	Save named property IDs for IDs of interest.
	*
	*	@param	namedProperties	The list of named properties.
	*/
	static void initConstants(NameToIDMap namedProperties)
	{
		email1AddressLID = namedProperties.id(PropertyLIDs.Email1EmailAddress);

		distributionListMembersLID = namedProperties.id(PropertyLIDs.DistributionListMembers);
		distributionListOneOffMembersLID = namedProperties.id(PropertyLIDs.DistributionListOneOffMembers);
	}

	static final String nm_ProviderUID = "ProviderUID";

	/**	The descriptions of the data common to all distribution list entries. */
	private static final DataDefinition[] distributionListCommonFields = new DataDefinition[] {
		new DataDefinition("flags", DataType.integer32Reader, false),
		new DataDefinition(nm_ProviderUID, DataType.definitionFactory(DataType.GUID), true)
	};

	/**	Extract the list of members from the message object property context.
	*
	*	@param	pc	The message object property context, as retrieved by getMessage.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree
	*	@param	pstFile	The PST file's data stream, header, etc.
	*
	*	@return	The list of members, as an array of strings.
	*
	*	@throws java.io.IOException	The distribution list contents could not be read.
	*/
	public java.util.Iterator<Entry> members(final PropertyContext pc, BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		java.io.IOException
	{
		final Object o = pc.get(distributionListMembersLID);
		if (o == null)
			return null;
		final byte[][] multipleBinary = (byte[][])o;
		java.util.ArrayList<Entry> entries = new java.util.ArrayList<Entry>(multipleBinary.length);

		DataContainer dc = new DataContainer();
		for (int i = 0; i < multipleBinary.length; ++i) {
			java.nio.ByteBuffer byteBuffer = PSTFile.makeByteBuffer(multipleBinary[i]);
			dc.read(byteBuffer, distributionListCommonFields);
			GUID providerUID = (GUID)dc.get(nm_ProviderUID);
			if (providerUID.equals(GUID.PROVIDER_UID_ONE_OFF))
				entries.add(new OneOffEntry(byteBuffer));
			else
				entries.add(new AddressBookEntry(byteBuffer, bbt, nbt, pstFile));
		}
		
		return entries.iterator();
	}
}
