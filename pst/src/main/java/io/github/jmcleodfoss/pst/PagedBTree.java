package io.github.jmcleodfoss.pst;

/**	The PagedBTree class is the base class for B-trees stored on pages within the PST file (i.e. the Node and Block B-trees).
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/4f0cd8e7-c2d0-4975-90a4-d417cfca77f8">MS-PST Section 2.2.2.7.7.1: BTPAGE</a>
*/
public abstract class PagedBTree extends BTree
{
	/**	The size of a PST page. */
	static final int PAGE_SIZE = 512;

	/**	The PageContext class contains information required to keep track of the current position in the B-tree during input. */
	protected abstract static class PageContext<I extends BTree, L extends BTreeLeaf> extends BTree.Context<I, L>
	{
		private static final String nm_rgEntries = "rgEntries";
		private static final String nm_cEnt = "cEnt";
		private static final String nm_cbEnt = "cbEnt";
		private static final String nm_cEntMax = "cEntMax";
		private static final String nm_cLevel = "cLevel";

		/** The fields to read in for the various file formats.
		*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/4f0cd8e7-c2d0-4975-90a4-d417cfca77f8">MS-PST Section 2.2.2.7.7.1: BTPAGE</a>
		*	@see	<a href="https://blog.mythicsoft.com/ost-2013-file-format-the-missing-documentation/">OST 2013 file format the missing documentation blog entry</a>
		*/
		private static final DataDefinition fields[][] = {
			/* ANSI format-specific fields */
			{
				new DataDefinition(nm_rgEntries, new DataType.SizedByteArray(492), true),
				new DataDefinition("dwPadding", new DataType.SizedByteArray(4), true),
				new DataDefinition(nm_cEnt, DataType.integer8Reader, true),
				new DataDefinition(nm_cEntMax, DataType.integer8Reader, true),
				new DataDefinition(nm_cbEnt, DataType.integer8Reader, true),
				new DataDefinition(nm_cLevel, DataType.integer8Reader, true),
			},

			/* Unicode format-specific fields */
			{
				new DataDefinition(nm_rgEntries, new DataType.SizedByteArray(488), true),
				new DataDefinition(nm_cEnt, DataType.integer8Reader, true),
				new DataDefinition(nm_cEntMax, DataType.integer8Reader, true),
				new DataDefinition(nm_cbEnt, DataType.integer8Reader, true),
				new DataDefinition(nm_cLevel, DataType.integer8Reader, true),
			},

			/* OST 2013 format-specific fields */
			{
				new DataDefinition(nm_rgEntries, new DataType.SizedByteArray(4056), true),
				new DataDefinition(nm_cEnt, DataType.integer16Reader, true),
				new DataDefinition(nm_cEntMax, DataType.integer16Reader, true),
				new DataDefinition(nm_cbEnt, DataType.integer8Reader, true),
				new DataDefinition(nm_cLevel, DataType.integer8Reader, true),
			}
		};

		/**	Move to the start of this page so that the parent class can read in the header.
		*	@param	bref	The block reference for this page.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*	@return	The passed in pstFile object, for use as a parameter to the {@link #PageContext} constructor.
		*	@throws	java.io.IOException	An I/O error was encountered when seeking the new position in the file.
		*	@see	PageContext
		*/
		private static PSTFile gotoPage(final BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			pstFile.position(bref.ib.ib);
			return pstFile;
		}

		/**	Create a PageContext object form the given pstFile and bref.
		*	@param	bref	The block reference for this page.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*	@throws java.io.IOException	An I/O error was encountered while either seeking the page context's position in the file or while reading the page context data.
		*/
		protected PageContext(BREF bref, PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(gotoPage(bref, pstFile), fields[pstFile.header.fileFormat.index.getIndex()]);
		}

		/**	Obtain a data stream from which the B-tree entries may be read.
		*	@return	A data entry stream from which the page may be read.
		*/
		@Override
		protected java.nio.ByteBuffer entryDataStream()
		throws
			java.io.IOException
		{
			return PSTFile.makeByteBuffer((byte[])dc.get(nm_rgEntries));
		}

		/**	Retrieve the number of bytes including unread padding in an entry.
		*	@return	The number of bytes between the start of each entry in the B-tree.
		*/
		@Override
		protected int getEntrySize()
		{
			return dc.getUInt8(nm_cbEnt);
		}

		/**	Obtain the number of entries found in this page.
		*	@return	The number of B-Tree entries on this page.
		*/
		@Override
		protected int getNumEntries()
		{
			if (pstFile.header.fileFormat.index == FileFormat.Index.OST_2013)
				return (Short)dc.get(nm_cEnt);

			return dc.getUInt8(nm_cEnt);
		}

		/**	{@inheritDoc} */
		protected boolean isLeafNode()
		{
			return dc.getUInt8(nm_cLevel) == 0;
		}
	}

	/**	The BTEntry class is an intermediate node in the B-trees stored in pages within the PST file.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/bc8052a3-f300-4022-be31-f0f408fffca0">MS-PST Section 2.2.2.7.7.2: BTEntry (Intermediate Entries)</a>
	*	@see	BBTEntry
	*	@see	NBTEntry
	*/
	static class BTEntry
	{
		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		private static final String nm_btKey = "btKey";
		private static final String nm_bref = "BREF";

		/**	The Unicode-specific fields in the input stream which make up an intermediate B-tree entry in a page. */
		private static final DataDefinition[] unicode_fields = {
			new DataDefinition(nm_btKey, DataType.integer64Reader, true),
			new DataDefinition(nm_bref, DataType.brefUnicodeReader, true),
		};

		/**	The size of the BTENTRY object in Unicode PST files. */
		private static final int UNICODE_SIZE = DataDefinition.size(unicode_fields);

		/**	The ANSI-specific fields in the input stream which make up an intermediate B-tree entry in a page. */
		private static final DataDefinition[] ansi_fields = {
			new DataDefinition(nm_btKey, DataType.integer32Reader, true),
			new DataDefinition(nm_bref, DataType.brefAnsiReader, true),
		};

		/**	The size of the BTENTRY object in ANSI PST files. */
		private static final int ANSI_SIZE = DataDefinition.size(ansi_fields);

		/**	The B-tree search key for this node. */
		final long key;

		/**	The block reference of this B-tree node's child. */
		final BREF bref;

		/**	Read an intermediate entry for a B-Tree stored in a page from the input stream using the given context.
		*	@param	context		The context from which to build the B-tree.
		*	@param	byteBuffer	The data stream from which to read the intermediate B-tree entry.
		*	@throws	java.io.IOException	An I/O error was encountered when reading the data for this B-tree entry.
		*/
		BTEntry(final Context<BTree, BTreeLeaf> context, java.nio.ByteBuffer byteBuffer)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(byteBuffer, context.unicode() ? unicode_fields : ansi_fields);
			key = context.unicode() ? (Long)dc.get(nm_btKey) : (long)(Integer)dc.get(nm_btKey);
			bref = (BREF)dc.get(nm_bref);
		}

		/**	Return the actual size of an intermediate B-tree entry as read in from the input datastream.
		*	@param	context	The construction context for this B-tree.
		*	@return	The size of an intermediate node or block B-tree entry.
		*/
		public static int actualSize(final Context<BTree, BTreeLeaf> context)
		{
			return context.unicode() ? BTEntry.UNICODE_SIZE : BTEntry.ANSI_SIZE;
		}

		/**	Obtain the key for this intermediate node.
		*	@return	The key for this node.
		*/
		public long key()
		{
			return key;
		}

		/**	Provide a description of this intermediate B-tree node (typically used for debugging).
		*	@return	A string describing this object.
		*/
		@Override
		public String toString()
		{
			return String.format("BTEntry: key 0x%08x BID 0x%08x IB 0x%08x", key, bref.bid.bid, bref.ib.ib);
		}
	}

	/**	The block reference to the page for this node of the block or node B-tree. */
	private final BREF bref;

	/**	Construct a PageBTree object with the given search key from the given position using the given context.
	*	@param	key	The key for this node.
	*	@param	bref	The block reference for this page.
	*	@param	context	The context from which to construct this B-Tree.
	*	@throws java.io.IOException	There was a problem reading the B-tree.
	*/
	protected PagedBTree(final long key, final BREF bref, final PageContext<BTree, BTreeLeaf> context)
	throws
		java.io.IOException
	{
		super(key, context);
		this.bref = bref;
	}

	/**	Return the actual size of an intermediate B-tree entry as read in from the input datastream.
	*	@param	context	The construction context for this B-tree.
	*	@return	The size of an intermediate node or block B-tree entry.
	*/
	public int actualSize(final Context<BTree, BTreeLeaf> context)
	{
		return BTEntry.actualSize(context);
	}

	/**	Get a table model which can be used to describe this node.
	*	@return	A DefaultTableModel describing this node.
	*/
	public javax.swing.table.TableModel getNodeTableModel()
	{
		Object[][] cells = {
			new Object[]{"Key", key()},
			new Object[]{"BID", bref.bid.key()},
			new Object[]{"IB", bref.ib}
		};

		return new javax.swing.table.DefaultTableModel(cells, new Object[]{"", ""});
	}

	/**	{@inheritDoc} */
	public java.nio.ByteBuffer rawData(final BlockMap bbt, final PSTFile pstFile)
	throws
		java.io.IOException
	{
		pstFile.position(bref.ib.ib);
		byte[] data = new byte[PAGE_SIZE];
		pstFile.mbb.get(data);
		return PSTFile.makeByteBuffer(data);
	}

	/**	{@inheritDoc} */
	@Override
	public String toString()
	{
		return bref.toString() + "; key " + Long.toHexString(key);
	}
}
