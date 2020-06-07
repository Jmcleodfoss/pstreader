package io.github.jmcleodfoss.pst;

/**	The EntryID class contains a PST file Entry ID (ENTRYID structure).
*
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/9378e8b9-7b6a-45bf-a51a-f21daf24d9ce">MS-PST Section 2.4.3.2: Mapping between EntryID and NID</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/MS-OXCDATA/57e8bcbf-11d0-40fe-8833-5558bb9c0c89">MS-OXCDATA Section 2.2: EntryID and Related Types</a>
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
	*
	*	@throws	NotHeapNodeException			A node which is not a heap node was found while creating the folder.
	*	@throws	NotPropertyContextNodeException		A node in this folder's B-tree does not contain a property context when it was expected to.
	*	@throws	NotTableContextNodeException		A node in this folder's B-tree does not contain a table context when it was expected to.
	*	@throws	NullDataBlockException			A null data block was encountered while building the folder.
	*	@throws UnknownClientSignatureException		An unknown client signature was encountered while building the folder.
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context was encountered.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was encountered.
	*	@throws	java.io.IOException			An I/O problem was encountered while reading the data for folder.
	*/
	Folder folder(BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
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
	*
	*	@throws	NotHeapNodeException			A node which is not a heap node was found while building the property context for this node.
	*	@throws	NotPropertyContextNodeException		The node does not contain a property context.
	*	@throws	NullDataBlockException			A null data block was encountered while building the property context.
	*	@throws UnknownClientSignatureException		An unknown client signature was encountered while building the property context.
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context was encountered.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was encountered.
	*	@throws	java.io.IOException			An I/O problem was encountered while reading the data for the property context.
	*/
	PropertyContext propertyContext(BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		NotPropertyContextNodeException,
		NullDataBlockException,
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
