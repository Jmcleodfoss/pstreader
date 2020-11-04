package io.github.jmcleodfoss.pst;

/**	The DistributionList class represents a PST contact distribution list message object. The values set in the constructor are
*	those which come from the folder Contents Table; retrieval of other fields require that the client provide the message property
*	context from which the fields may be extracted.
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
*	@see	io.github.jmcleodfoss.pst.Appointment
*	@see	io.github.jmcleodfoss.pst.Contact
*	@see	io.github.jmcleodfoss.pst.Message
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">MS-OXCMSG: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/1042af37-aaa4-4edc-bffd-90a1ede24188">MS-PST Section 2.4.5: Message Objects</a>
*/
public class DistributionList extends MessageObject
{
	/**	The property ID of the DistributionListMembers property. */
	private static int distributionListMembersLID = PropertyLIDs.UNKNOWN;

	/**	The property ID of the DistributionListOneOffMembers property. */
	@SuppressWarnings("PMD.UnusedPrivateField") // Allow this; I'll add support for it when I have a PST file which uses it.
	private static int distributionListOneOffMembersLID = PropertyLIDs.UNKNOWN;

	/**	The property ID of the "Email 1 Address" property. */
	private static int email1AddressLID = PropertyLIDs.UNKNOWN;

	static final String nm_ProviderUID = "ProviderUID";

	/**	The descriptions of the data common to all distribution list entries. */
	private static final DataDefinition[] DISTRIBUTION_LIST_COMMON_FIELDS = new DataDefinition[] {
		new DataDefinition("flags", DataType.integer32Reader, false),
		new DataDefinition(nm_ProviderUID, DataType.guidReader, true)
	};

	private static final String nm_EntryID = "entryID";

	/**	The fields comprising the one-off entry data. */
	private static final DataDefinition ADDRESS_BOOK_ENTRY_FIELDS[] = new DataDefinition[] {
		new DataDefinition("skip", new DataType.SizedByteArray(1), false),
		new DataDefinition(nm_EntryID, new DataType.SizedByteArray(24), true)
	};

	/**	When this bit is set in the second flag byte, the strings for the display name, address type, and email
	*	address are stored in Unicode and delimited by double-NULs; when this bit is cleared, the strings are stored in
	*	ANSI and delimited by single NULs.
	*/
	private static final short FLAG_U_MASK = 0x80;

	private static final String nm_Version = "wVersion";
	private static final String nm_Flags1 = "bitmapFlag1";
	private static final String nm_Flags2 = "bitmapFlag2";
	private static final String nm_StringData = "stringData";

	/**	The fields comprising the one-off entry data. */
	private static final DataDefinition[] ONE_OFF_ENTRY_FIELDS = new DataDefinition[] {
		new DataDefinition(nm_Version, DataType.integer16Reader, false),
		new DataDefinition(nm_Flags1, DataType.integer8Reader, false),
		new DataDefinition(nm_Flags2, DataType.integer8Reader, true),
	};

	/**	The Entry class flags an object as being a distribution list entry. */
	public static class Entry
	{
		/**	The display name of the distribution list member. */
		public final String displayName;

		/**	The email address of the distribution list member. */
		public final String emailAddress;

		/**	Create an Entry object
		*	@param	displayName	The display name for the distribution list
		*	@param	emailAddress	The email address for the distribution list
		*/
		private Entry(String displayName, String emailAddress)
		{
			this.displayName = displayName;
			this.emailAddress = emailAddress;
		}

		/**	Create a String describing the distribution list entry.
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

	/**	Create a distribution list object for the given row in the folder contents table.
	*	@param	contentsTable	The containing folder's contents table.
	*	@param	row		The row of the contents table from which to create the contact.
	*	@param	nbt		The PST file's node B-Tree.
	*	@param	pstFile		The PST file's header, input stream, etc.
	*	@throws NotHeapNodeException			A node which is not a heap node was encountered while reading in the data for the distribution list.
	*	@throws UnknownClientSignatureException		An unknown client signature was encountered while reading in the data for the distribution list.
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context was found while reading in the data for the distribution list.
	*	@throws UnparseableTableContextException	A bad / corrupt table context was found while reading in the data for the distribution list.
	*	@throws	java.io.IOException	An I/O problem was encoutered when reading in the address book.
	*/
	DistributionList(final TableContext contentsTable, final int row, final NodeMap nbt, final PSTFile pstFile)
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
	*	@param	namedProperties	The list of named properties.
	*	@param	fUnicode	Whether the current PST file is a Unicode file or not.
	*/
	static void initConstants(NameToIDMap namedProperties, boolean fUnicode)
	{
		email1AddressLID = namedProperties.id(PropertyLIDs.Email1EmailAddress, fUnicode ? DataType.STRING : DataType.STRING_8);

		distributionListMembersLID = namedProperties.id(PropertyLIDs.DistributionListMembers, DataType.MULTIPLE_BINARY);
		distributionListOneOffMembersLID = namedProperties.id(PropertyLIDs.DistributionListOneOffMembers, DataType.MULTIPLE_BINARY);
	}

	/**	Extract the list of members from the message object property context.
	*	@param	pc	The message object property context, as retrieved by getMessage.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree
	*	@param	pstFile	The PST file's data stream, header, etc.
	*	@return	The list of members, as an array of strings.
	*	@throws	java.io.IOException						An I/O error was encountered reading data from a ByteBuffer
	*	@throws io.github.jmcleodfoss.pst.NotHeapNodeException			No valid heap node was found when creating the PropertyContext for an EntryID for an address book entry
	*	@throws io.github.jmcleodfoss.pst.NotPropertyContextNodeException	No valid PropertyContext was found for an EntryID for an address book entry
	*	@throws io.github.jmcleodfoss.pst.NullDataBlockException		An expected Block B-Tree entry was not found when creating the PropertyContext for an EntryID for an
	*										address book entry
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws io.github.jmcleodfoss.pst.UnparseablePropertyContextException	An invalid or corrupt PropertyContext was found for an EntryID for an address book entry.
	*	@throws io.github.jmcleodfoss.pst.UnparseableTableContextException	An invalid or corrupt TableContext was found for an EntryID for an address book entry.
	*	@throws io.github.jmcleodfoss.pst.UnknownClientSignatureException	An invalid ClientSignature was found for a heap entry when creating an EntryID for an address book entry.
	*/
	public java.util.Iterator<Entry> members(final PropertyContext pc, BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		java.io.IOException,
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NullDataBlockException,
		UnimplementedPropertyTypeException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		UnknownClientSignatureException
	{
		final Object o = pc.get(distributionListMembersLID);
		if (o == null)
			return null;
		final byte[][] multipleBinary = (byte[][])o;
		java.util.ArrayList<Entry> entries = new java.util.ArrayList<Entry>(multipleBinary.length);

		DataContainer dc = new DataContainer();
		for (int i = 0; i < multipleBinary.length; ++i) {
			java.nio.ByteBuffer byteBuffer = PSTFile.makeByteBuffer(multipleBinary[i]);
			dc.read(byteBuffer, DISTRIBUTION_LIST_COMMON_FIELDS);
			GUID providerUID = (GUID)dc.get(nm_ProviderUID);
			if (providerUID.equals(GUID.PROVIDER_UID_ONE_OFF)){
				dc.read(byteBuffer, ONE_OFF_ENTRY_FIELDS);

				final byte flags = (Byte)dc.get(nm_Flags2);
				final boolean fUnicode = (flags & FLAG_U_MASK) != 0;

				final DataDefinition d = new DataDefinition(nm_StringData, DataType.definitionFactory(fUnicode ? DataType.STRING : DataType.STRING_8), true);
				dc.read(byteBuffer, d);

				final String s = (String)dc.get(nm_StringData);

				int ofs = 0;
				final String displayName = s.substring(ofs, ofs = s.indexOf(0, ofs));
				if (fUnicode)
					++ofs;

				// Skip the address type
				ofs = s.indexOf(0, ofs);
				if (fUnicode)
					++ofs;

				final String emailAddress = s.substring(ofs, ofs = s.indexOf(0, ofs));

				entries.add(new Entry(displayName, emailAddress));
			} else {
				dc.read(byteBuffer, ADDRESS_BOOK_ENTRY_FIELDS);
				byte[] rawData = (byte[])dc.get(nm_EntryID);
				final EntryID entryID = new EntryID(rawData);
				final PropertyContext pcEntry = new PropertyContext(nbt.find(entryID.nid), bbt, pstFile);
				entries.add(new Entry((String)pcEntry.get(PropertyTags.DisplayNameW), (String)pcEntry.get(email1AddressLID)));
			}
		}

		return entries.iterator();
	}

	/**	Test the DistributionList class by iterating through the distribution list entries
	* 	@param args	The files to test
	*/
	public static void main(final String[] args)
	{
		test("io.github.jmcleodfoss.pst.DistributionList", args);
	}
}
