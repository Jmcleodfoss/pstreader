package io.github.jmcleodfoss.pst;

/**	The BBTEntry class represents a block B-tree leaf node.
*	@see	io.github.jmcleodfoss.pst.BTree
*	@see	io.github.jmcleodfoss.pst.BlockBTree
*	@see	io.github.jmcleodfoss.pst.NBTEntry
*	@see	io.github.jmcleodfoss.pst.PagedBTree
*	@see	io.github.jmcleodfoss.pst.PagedBTree.BTEntry
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/53a4b926-8ac4-45c9-9c6d-8358d951dbcd">MS-PST Section 2.2.2.7.7.3: BBTEntry (Leaf BBT Entry)</a>
*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
*/
class BBTEntry implements BTreeLeaf
{
	private static final String nm_bref = "BREF";
	private static final String nm_cb = "cb";
	private static final String nm_cbInflated = "cbInflated";
	private static final String nm_cRef = "cRef";

	/**	The fields in the input stream which make up the block B-tree leaf entry, which are different depending on the file format type.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/53a4b926-8ac4-45c9-9c6d-8358d951dbcd">MS-PST Section 2.2.2.7.7.3: BBTEntry (Leaf BBT Entry)</a>
	*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
	*/
	private static final DataDefinition fields[][] = {
		/* ANSI fields and sizes */
		{
			new DataDefinition(nm_bref, DataType.brefAnsiReader, true),
			new DataDefinition(nm_cb, DataType.integer16Reader, true),
			new DataDefinition(nm_cRef, DataType.integer16Reader, true)
		},

		/* Unicode fields and sizes */
		{
			new DataDefinition(nm_bref, DataType.brefUnicodeReader, true),
			new DataDefinition(nm_cb, DataType.integer16Reader, true),
			new DataDefinition(nm_cRef, DataType.integer16Reader, true)
		},

		/* OST 2013 fields and sizes */
		{
			new DataDefinition(nm_bref, DataType.brefUnicodeReader, true),
			new DataDefinition(nm_cb, DataType.integer16Reader, true),
			new DataDefinition(nm_cbInflated, DataType.integer16Reader, true),
			new DataDefinition(nm_cRef, DataType.integer16Reader, true)
		}
	};

	/**	The block reference for this block of the B-tree. */
	final BREF bref;

	/**	The number of raw data bytes in the block. */
	final int numBytes;

	/**	The total number of data bytes in the block accounting for compression. */
	final int totalBytes;

	/**	The reference count of the block. */
	private final int refCount;

	/**	Create a block B-tree leaf entry from data read in from the input datastream.
	*	@param	entryStream	The stream of data from which to read the entry.
	*	@param	context		The context used to build the tree. This is not saved in the tree itself.
	*	@throws	java.io.IOException	An I/O error was encountered while reading the data for this block B-tree node.
	*/
	BBTEntry(java.nio.ByteBuffer entryStream, final PagedBTree.PageContext<BTree, BTreeLeaf> context)
	throws
		java.io.IOException
	{
		DataContainer dc = new DataContainer();
		dc.read(entryStream, fields[context.fileFormat.index.getIndex()]);
		bref = (BREF)dc.get(nm_bref);
		numBytes = 0xffff & (Short)dc.get(nm_cb);
		totalBytes = context.fileFormat.index == FileFormat.Index.OST_2013 ? (0xffff & (Short)dc.get(nm_cbInflated)) : numBytes;
		refCount = 0xffff & (Short)dc.get(nm_cRef);
	}

	/**	Obtain the actual size of a block B-tree leaf node as read in from the input datastream.
	*	@param	context	The context to use to find the size (this function uses only the file format information.)
	*	@return	The actual size of a block B-tree leaf node for this file type.
	*/
	public int actualSize(final BTree.Context<BTree, BTreeLeaf> context)
	{
		return DataDefinition.size(fields[context.fileFormat.index.getIndex()]);
	}

	/**	{@inheritDoc} */
	public javax.swing.table.TableModel getNodeTableModel()
	{
		final Object[] columnHeadings = {"", ""};
		final Object[][] cells = {
			new Object[]{"BID", bref.bid},
			new Object[]{"IB", bref.ib.ib},
			new Object[]{"Byte Count", numBytes},
			new Object[]{"Reference Count", refCount}
		};

		return new ReadOnlyTableModel(cells, columnHeadings);
	}

	/**	Retrieve the key (block ID) for this node.
	*	@return	The {@link BID} key for the block indicated by this leaf entry in the block B-tree.
	*/
	public long key()
	{
		return bref.bid.key();
	}

	/**	{@inheritDoc} */
	public String getNodeText()
	{
		return String.format("%s - %d bytes", bref.toString(), numBytes);
	}

	/**	{@inheritDoc} */
	public java.nio.ByteBuffer rawData(final BlockMap bbt, final PSTFile pstFile)
	throws
		CRCMismatchException,
		java.io.IOException
	{
		final SimpleBlock block = SimpleBlock.read(this, pstFile);
		return block.dataStream();
	}

	/**	Provide a description of a block B-tree leaf node (typically used for debugging).
	*	@return	A string describing this object.
	*/
	@Override
	public String toString()
	{
		if (numBytes == totalBytes)
			return String.format(bref.toString() + " bytes %d ref count %d", numBytes, refCount);
		return String.format(bref.toString() + " bytes %d (total %d) ref count %d", numBytes, totalBytes, refCount);
	}
}
