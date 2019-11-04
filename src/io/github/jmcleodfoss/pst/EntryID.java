package io.github.jmcleodfoss.pst;

/**	The EntryID class contains a PST file Entry ID (ENTRYID structure).
*
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.2.2.4"
*/
public class EntryID {

	/**	The node ID of the entry */
	public final NID nid;

	/**	Create an EntryID object from raw bytes.
	*
	*	@param	rawData	The bytes from which to create the EntryID (a little-endian sequence of bytes).
	*/
	EntryID(byte[] rawData)
	{
		final int rawNID = PSTFile.makeByteBuffer(rawData, 20, 4).getInt();
		nid = new NID(rawNID);
	}

	/**	Return the folder and sub-folders for this nid contained in the given node database in this PST file.
	*
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree.
	*	@param	pstFile	The PST file's {@link Header}, input stream, etc.
	*
	*	@return	The folder object for this EntryID's {@link #nid}.
	*/
	Folder folder(BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		UnparseablePropertyContextException,
		UnknownClientSignatureException,
		NotHeapNodeException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return Folder.getFolderTree(nbt.find(nid), bbt, nbt, pstFile);
	}

	/**	Return the message object for this nid contained in the given node database in this PST file.
	*
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree.
	*	@param	pstFile	The PST file's {@link Header}, input stream, etc.
	*
	*	@return	The message object for this EntryID's {@link #nid}.
	*/
	PropertyContext propertyContext(BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		UnparseablePropertyContextException,
		UnknownClientSignatureException,
		NotHeapNodeException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return new PropertyContext(nbt.find(nid), bbt, pstFile);
	}

	/**	Obtain a string representation of this EntryID object (for debugging).
	*
	*	@return	A description of this EntryID object.
	*/
	@Override
	public String toString()
	{
		return nid.toString();
	}
}
