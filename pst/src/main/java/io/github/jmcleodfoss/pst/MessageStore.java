package io.github.jmcleodfoss.pst;

/**	The MessageStore class is a (thin) wrapper around the message store PC, with a few convenience functions.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/aa0539bd-e7bf-4cec-8bde-0b87c2a86baf">MS-PST Section 2.4.3: Message Store</a>
*/
public class MessageStore
{
	/**	The tag for the the root node of the PST file.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/5493a0eb-0356-4e88-b4f5-0433ce0a93fa">MS-PST Section 2.4.3.1: Minimum Set of Required Properties</a>
	*/
	private static final int PROPID_ROOT_ENTRY_ID = PropertyTags.IpmSubTreeEntryId;

	/**	The tag for the password field.
	*	@see	#passwordHashed
	*	@see	#checkPassword
	*	@see	#hasPassword
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/48468b1e-cc81-4e2b-82a7-9bf61adc948e">MS-PST Section 2.4.3.3: PST Password Security</a>
	*/
	private static final int PROPID_PASSWORD = PropertyTags.LtpPstPassword;

	/**	The actual message store PC. */
	private final PropertyContext messageStore;

	/**	The block B-Tree of the underlying PST file.
	*	@see	#rootFolder
	*/
	private final BlockMap bbt;

	/**	The node B-Tree of the underlying PST file.
	*	@see	#rootFolder
	*/
	private final NodeMap nbt;

	/**	The dat input stream, {@link Header header}, etc, of the underlying PST file.
	*	@see	#rootFolder
	*/
	private final PSTFile pstFile;

	/**	The hashed password as found in the PST file (if this is 0, the PST file has no password). */
	private final int passwordHashed;

	/**	The root folder. */
	@Deprecated
	public final EntryID rootMailboxEntry;

	/**	The root folder. */
	public final Folder rootFolder;

	/**	Create a message store object by reading in the message store node.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree.
	*	@param	pstFile	The PST file input data stream, {@link Header header}, etc.
	*	@throws NotHeapNodeException			A node which was not a heap node was found while creating the message store.
	*	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found while building a property context.
	*	@throws NotTableContextNodeException		A node without the Table Context client signature was found while building a table context.
	*	@throws NullDataBlockException			A null data block was found while building a property context.
	*	@throws UnknownClientSignatureException		The client signature of one of the blocks in the message store was not recognized.
	*	@throws UnparseablePropertyContextException	The property context could not be interpreted.
	*	@throws UnparseableTableContextException	The table context could not be interpreted.
	*	@throws java.io.IOException			The PST file could not be read.
	*/
	public MessageStore(final BlockMap bbt, final NodeMap nbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		messageStore = new PropertyContext(nbt.find(NID.NID_MESSAGE_STORE), bbt, pstFile);

		this.bbt = bbt;
		this.nbt = nbt;
		this.pstFile = pstFile;

		passwordHashed = (Integer)messageStore.get(PROPID_PASSWORD);
		rootMailboxEntry = new EntryID((byte[])messageStore.get(PROPID_ROOT_ENTRY_ID));
		rootFolder = Folder.getFolderTree(nbt.find(rootMailboxEntry.nid), bbt, nbt, pstFile);
	}

	/**	Check whether the given password matches the stored password.
	*	@param	testPassword	The password to check.
	*	@return	true if the passed password matches the password in the PST file, false otherwise.
	*	@see	#passwordHashed
	*	@see	#hasPassword
	*/
	public boolean checkPassword(final String testPassword)
	{
		final int testPasswordHashed = CRC.crc(java.nio.ByteBuffer.wrap(testPassword.getBytes()).asReadOnlyBuffer(), 0, testPassword.length());
		return testPasswordHashed == passwordHashed;
	}

	/**	Determine whether this PST file requires a password.
	*	@return	true if the PST file is password-protected, false if it is not password-protected.
	*	@see	#passwordHashed
	*	@see	#checkPassword
	*/
	public boolean hasPassword()
	{
		return passwordHashed != 0;
	}

	/**	Get the PST root folder, which contains all PST subfolders and messages.
	*	@return	The root folder of the PST file, as a Folder object.
	* 	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found while building a property context.
	* 	@throws NotTableContextNodeException		A node without the Table Context client signature was found while building a table context.
	* 	@throws NullDataBlockException			A null data block was found while building a property context.
	*	@throws NotHeapNodeException			A node which was not a heap node was found while reading the root folder.
	*	@throws UnknownClientSignatureException		The client signature of one of the blocks in the root folder was not recognized.
	*	@throws UnparseablePropertyContextException	The property context could not be interpreted.
	*	@throws UnparseableTableContextException	The table context could not be interpreted.
	*	@throws java.io.IOException			The PST file could not be read.
	*/
	@Deprecated
	public Folder rootFolder()
	throws
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return rootFolder;
	}

	/**	Obtain a javax.swing.table.TableModel describing the message store.
	*	@param	namedProperties	The named properties for this PST file.
	*	@return	A javax.swing.tableTableModel containing the named properties.
	*/
	public javax.swing.table.TableModel tableModel(final NameToIDMap namedProperties)
	{
		return messageStore.tableModel(namedProperties);
	}
}
