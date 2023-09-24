package io.github.jmcleodfoss.pst;

/**	The SLEntry is a leaf node in the sub-node B-tree.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/85c4d943-0779-43c5-bd98-61dc9bb5dfd6">MS-PST Section 2.2.2.8.3.3.1.1: SLEntry (Leaf Block Entry)</a>
*/
class SLEntry extends LPTLeaf
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	The size of the SLEntry object in ANSI files. */
	private static final int SIZE_ANSI = 12;

	/**	The size of the SLEntry object in Unicode files. */
	private static final int SIZE_UNICODE = 24;

	/**	The Unicode-specific fields in the input stream which make up the sub-node B-tree leaf entry. */
	private static final DataDefinition[] unicode_fields = {
		new DataDefinition(nm_nid, DataType.nidReader, true),
		new DataDefinition(nm_nid_padding, new DataType.SizedByteArray(4), false),
		new DataDefinition(nm_bidData, DataType.bidUnicodeReader, true),
		new DataDefinition(nm_bidSubnode, DataType.bidUnicodeReader, true)
	};

	/**	The ANSI-specific fields in the input stream which make up the sub-node B-tree leaf entry. */
	private static final DataDefinition[] ansi_fields = {
		new DataDefinition(nm_nid, DataType.nidReader, true),
		new DataDefinition(nm_bidData, DataType.bidAnsiReader, true),
		new DataDefinition(nm_bidSubnode, DataType.bidAnsiReader, true)
	};

	/**	Read a sub-node B-tree leaf entry from the input stream using the given context.
	*	@param	context	The context from which to build the sub-node B-tree leaf entyr.
	*	@param	stream	The data stream from which to read the sub-node B-tree leaf entry.
	*	@throws	java.io.IOException	An I/O error was encounted when reading the data for this node.
	*/
	SLEntry(final SubnodeBTree.BlockContext context, java.nio.ByteBuffer stream)
	throws
		java.io.IOException
	{
		super(stream, context.unicode() ? unicode_fields : ansi_fields);
	}

	/**	Return the actual size of a sub-node B-tree leaf entry as read in from the input datastream.
	*	@param	context	The context in which the sub-node B-tree is being built.
	*	@return	The total size of the sub-node B-tree leaf entry, including padding.
	*/
	@Override
	public int actualSize(final BTree.Context<BTree, BTreeLeaf> context)
	{
		return size(context.fileFormat);
	}

	/**	The size of a sub-node B-tree leaf entry for this file's format.
	*	@param	fileFormat	The PST's file format object.
	*	@return	The size of the sub-node B-tree leaf entry, without padding.
	*/
	static int size(FileFormat fileFormat)
	{
		return fileFormat.fUnicode ? SIZE_UNICODE : SIZE_ANSI;
	}
}
