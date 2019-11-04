package io.github.jmcleodfoss.pst;

/**	The BBTEntry class represents a block B-tree leaf node.
*
*	@see	io.github.jmcleodfoss.pst.BTree
*	@see	io.github.jmcleodfoss.pst.BlockBTree
*	@see	io.github.jmcleodfoss.pst.NBTEntry
*	@see	io.github.jmcleodfoss.pst.PagedBTree
*	@see	io.github.jmcleodfoss.pst.PagedBTree.BTEntry
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608 Section 2.2.2.7.7.3"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386084(v=office.12).aspx">BBTEntry (Leaf BBT Entry) (MSDN)</a>
*/
class BBTEntry implements BTreeLeaf {

	private static final String nm_bref = "BREF";
	private static final String nm_cb = "cb";
	private static final String nm_cRef = "cRef";

	/**	The Unicode-specific fields in the input stream which make up the block B-tree leaf entry. */
	private static final DataDefinition[] unicode_fields = {
		new DataDefinition(nm_bref, DataType.brefUnicodeReader, true),
	};

	/**	The size in bytes of the Unicode-specific fields in a block B-tree leaf entry. */
	private static final int UNICODE_SIZE = DataDefinition.size(unicode_fields);

	/**	The ANSI-specific fields in the input stream which make up the block B-tree leaf entry. */
	private static final DataDefinition[] ansi_fields = {
		new DataDefinition(nm_bref, DataType.brefAnsiReader, true),
	};

	/**	The size in bytes of the Unicode-specific fields in a node B-tree leaf entry. */
	private static final int ANSI_SIZE = DataDefinition.size(ansi_fields);

	/**	The fields common to both ANSI and Unicode files in the input stream which make up the block B-tree leaf entry. */
	private static final DataDefinition[] common_fields = {
		new DataDefinition(nm_cb, DataType.integer16Reader, true),
		new DataDefinition(nm_cRef, DataType.integer16Reader, true)
	};

	/**	The size in bytes of the fields common to ANSI and Unicode files in a block B-tree leaf entry. */
	private static final int COMMON_SIZE = DataDefinition.size(common_fields);

	/**	The block reference for this block of the B-tree. */
	final BREF bref;

	/**	The number of raw data bytes in the block. */
	final int numBytes;

	/**	The reference count of the block. */
	private final int refCount;

	/**	Create a block B-tree leaf entry from data read in from the input datastream.
	*
	*	@param	entryStream	The stream of data from which to read the entry.
	*	@param	context		The context used to build the tree. This is not saved in the tree itself.
	*/
	BBTEntry(java.nio.ByteBuffer entryStream, final PagedBTree.PageContext<BTree, BTreeLeaf> context)
	throws
		java.io.IOException
	{
		DataContainer dc = new DataContainer();
		dc.read(entryStream, context.unicode() ? unicode_fields : ansi_fields, common_fields);
		bref = (BREF)dc.get(nm_bref);
		numBytes = (Short)dc.get(nm_cb);
		refCount = (Short)dc.get(nm_cRef);
	}

	/**	Obtain the actual size of a block B-tree leaf node as read in from the input datastream.
	*
	*	@param	context	The context to use to find the size (this function uses only the file format information.)
	*
	*	@return	The actual size of a block B-tree leaf node for this file type.
	*/
	public int actualSize(final BTree.Context<BTree, BTreeLeaf> context)
	{
		return COMMON_SIZE + (context.unicode() ? UNICODE_SIZE : ANSI_SIZE);
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

		return new io.github.jmcleodfoss.swingutil.ReadOnlyTableModel(cells, columnHeadings);
	}

	/**	Retrieve the key (block ID) for this node.
	*
	*	@return	The {@link BID} key for the block indicated by this leaf entry in the block B-tree.
	*/
	public long key()
	{
		return bref.bid.key();
	}

	/**	{@InheritDoc} */
	public String getNodeText()
	{
		return String.format("%s - %d bytes", bref.toString(), numBytes);
	}

	/**	{@inheritDoc} */
	public java.nio.ByteBuffer rawData(final BlockMap bbt, final PSTFile pstFile)
	throws
		java.io.IOException
	{
		final SimpleBlock block = SimpleBlock.read(this, pstFile);
		if (block != null)
			return block.dataStream();
		return null;
	}

	/**	Provide a description of a block B-tree leaf node (typically used for debugging).
	*
	*	@return	A string describing this object.
	*/
	@Override
	public String toString()
	{
		return String.format(bref.toString() + " bytes %d ref count %d", numBytes, refCount);
	}
}
