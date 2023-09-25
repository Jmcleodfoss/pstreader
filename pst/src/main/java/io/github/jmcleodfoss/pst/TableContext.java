package io.github.jmcleodfoss.pst;

/**	The TableContext class represents a PST Table Context, which is a structure on a B-tree-on-heap.
*	The class itself is not publicly available, but it extends javax.swing.table.AbstractTableModel, which provides a usable public interface.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/5e48be0d-a75a-4918-a277-50408ff96740">MS-PST Section 2.3.4: Table Context (TC)</a>
*/
public class TableContext extends javax.swing.table.AbstractTableModel
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	The PST file's namedProperties object (needed to provide the column name) */
	private static NameToIDMap namedProperties;

	/**	The TCINFO (Table Context Info) structure for this table context */
	private final TCInfo info;

	/**	The RowIndex for this table context. */
	private final BTreeOnHeap rowIndex;

	/**	The row data */
	private Object[][] rows;

	/**	The TCInfo class represents the PST file TCINFO structure, and contains table context info for a table context.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/45b3a0c5-d6d6-4e02-aebf-13766ff693f0">MS-PST Section 2.3.4.1: TCINFO</a>
	*/
	private static class TCInfo
	{
		/**	Index into rgib table of the ending offset of the Cell Existence Block. */
		private static final int TCI_bm = 3;

		private static final String nm_bType = "bType";
		private static final String nm_cCols = "cCols";
		private static final String nm_rgib = "rgib";
		private static final String nm_hidRowIndex = "hidRowIndex";
		private static final String nm_hnidRows = "hnidRows";
		private static final String nm_hidIndex = "hidIndex";

		/**	The fields in the input stream which make up the Table Context Info. */
		private static final DataDefinition[] fields = {
			new DataDefinition(nm_bType, DataType.integer8Reader, true),
			new DataDefinition(nm_cCols, DataType.integer8Reader, true),
			new DataDefinition(nm_rgib, new DataType.SizedInt16Array(4), true),
			new DataDefinition(nm_hidRowIndex, DataType.hidReader, true),
			new DataDefinition(nm_hnidRows, DataType.hidReader, true),
			new DataDefinition(nm_hidIndex, DataType.hidReader, false) // deprecated
		};

		/**	The heap index of the row ID B-tree-on-heap. */
		private final HeapOnNode.HID hidRowIndex;

		/**	The rows of the table. */
		private final HeapOnNode.HID hnidRows;

		/**	The column descriptions. */
		private final TColDescr[] columnDescription;

		/**	Ending offsets of 4/8, 2, and 1 byte values and the cell existence block. */
		private final short[] endingOffsets;

		/**	Description of each field. */
		private final DataDefinition[] rowFields;

		/**	The data types of the columns stored in HNIDs. */
		private final DataType[] hnidTypes;

		/**	Create a TCInfo object by reading in the required information from the data inputstream.
		*	@param	stream	The input data stream from which to read the TCINFO structure.
		*	@throws	NotTableContextNodeException	A node which is not a table context node was found while building the table context information object.
		*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
		*	@throws UnknownClientSignatureException	An unknown client signature was found while building the table context information object.
		*	@throws UnknownPropertyTypeException	The property type was not recognized
		*	@throws java.io.IOException		An I/O exception was encountered while reading the data for the table context information obkect.
		*/
		@SuppressWarnings("unchecked")
		private TCInfo(java.nio.ByteBuffer stream)
		throws
			NotTableContextNodeException,
			UnimplementedPropertyTypeException,
			UnknownClientSignatureException,
			UnknownPropertyTypeException,
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, fields);

			ClientSignature clientSignature = new ClientSignature((Byte)dc.get(nm_bType));
			if (!clientSignature.equals(ClientSignature.TableContext))
				throw new NotTableContextNodeException(clientSignature);

			int numColumns = dc.getUInt8(nm_cCols);
			columnDescription = new TColDescr[numColumns];
			for (int i = 0; i < numColumns; ++i)
				columnDescription[i] = new TColDescr(stream);

			java.util.Arrays.sort(columnDescription, new TColDescr.Comparator());

			hidRowIndex = (HeapOnNode.HID)dc.get(nm_hidRowIndex);
			hnidRows = (HeapOnNode.HID)dc.get(nm_hnidRows);
			endingOffsets = (short[])dc.get(nm_rgib);

			rowFields = new DataDefinition[numColumns+1];
			hnidTypes = new DataType[numColumns];
			for (int i = 0; i < numColumns; ++i) {
				if (storedInHNID(columnDescription[i])){
					rowFields[i] = new DataDefinition(fieldName(i), DataType.hidReader, true);
					hnidTypes[i] = DataType.definitionFactory(columnDescription[i].propertyType());
				} else
					rowFields[i] = new DataDefinition(fieldName(i), DataType.definitionFactory(columnDescription[i].propertyType()), true);
			}
			rowFields[numColumns] = new DataDefinition(cellExistenceBitmapFieldName(), new DataType.SizedByteArray((numColumns+7)/8), true);
		}

		/**	Create the name of the field containing the Cell Existence Bitmap (which is the last field in the row).
		*	Note that this provides the field name in a format consistent with those of the other fields, which allows simple
		*	construction of all field names knowing the number of columns.
		*	@return	The name of the cell existence bitmap field.
		*/
		private String cellExistenceBitmapFieldName()
		{
			return fieldName(columnDescription.length);
		}

		/**	Create a generic field name with the given value as a suffix.
		*	@param	i	The field index for which to create the field name.
		*	@return	A generic synthesized field name based on the given field index.
		*/
		private String fieldName(int i)
		{
			return "fld_" + i;
		}

		/**	Provide a representation describing this TCInfo object.
		*	This is typically used for debugging.
		*	@return	A string describing this TCInfo object, which includes descriptions of all columns.
		*/
		@Override
		public String toString()
		{
			StringBuilder s = new StringBuilder();
			s.append("hidRowIndex ");
			s.append(hidRowIndex);
			s.append(" columns ");
			s.append(Integer.toString(columnDescription.length));
			s.append(" hnidRows ");
			s.append(hnidRows);
			for (TColDescr tcd : columnDescription) {
				s.append('\n');
				s.append('\t');
				s.append(tcd);
			}
			s.append("\nData block ending offsets:");
			for (int i : endingOffsets) {
				s.append(' ');
				s.append(0xffff & i);
			}
			return s.toString();
		}

		/**	Is the data described by the given column description object stored directly in the table, or in the
		*	HNID stored in the table? Note that any field with a size of less than 4 is stored directly in the table.
		*	@param	cd	The column description to check.
		*	@return	true if the field referred to by the given column description object is stored in an HID, false if it
		*		is stored directly in the table.
		*/
		static boolean storedInHNID(TColDescr cd)
		{
			if (cd.width <= 4)
				return storedInHID(cd.propertyType());

			return false;
		}
	}

	/**	The TColDescr class holds a single table column description object.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/3a2f63cf-bb40-4559-910c-e55ec43d9cbb">MS-PST Section 2.3.4.2: TCOLDESC</a>
	*/
	static class TColDescr
	{

		private static final String nm_tag = "tag";
		private static final String nm_ibData = "ibData";
		private static final String nm_cbData = "cbData";
		private static final String nm_iBit = "iBit";

		/**	The fields in the input stream which make up the table column description. */
		private static final DataDefinition[] fields = {
			new DataDefinition(nm_tag, DataType.integer32Reader, true),
			new DataDefinition(nm_ibData, DataType.integer16Reader, true),
			new DataDefinition(nm_cbData, DataType.integer8Reader, true),
			new DataDefinition(nm_iBit, DataType.integer8Reader, true)
		};

		/**	Each column has an associated 32-bit tag. */
		private final int tag;

		/**	The offset of the data for this column within a row. */
		private final int columnOffset;

		/**	The number of bytes of data in this column. */
		private final int width;

		/**	The cell existence bitmap. */
		private final int cellExistenceBitmapIndex;

		/**	The Comparator class permits the list of fields to sorted by row offset.
		*/
		static class Comparator implements java.util.Comparator<TColDescr>, java.io.Serializable
		{
			/**	The serialVersionUID is required because the base class is serializable. */
			private static final long serialVersionUID = 1L;

			/**	Compare the two TColDescr objects.
			*	@param	a	One TColDescr object for comparison.
			*	@param	b	The other TColDescr object for comparison.
			*	@return	The difference in the column offsets of a and b, to ensure that the item with the larger
			*		offset is sorted later.
			*/
			@Override
			public int compare(TColDescr a, TColDescr b)
			{
				return a.columnOffset - b.columnOffset;
			}
		}
		/**	Create a TColDescr object from date read in from the input datastream.
		*	@param	stream	The input data stream from which to read the column description.
		*	@throws	java.io.IOException	An I/O error was encounted while reading the data for this column description.
		*/
		private TColDescr(java.nio.ByteBuffer stream)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, fields);
			tag = (Integer)dc.get(nm_tag);
			columnOffset = 0xffff & (Short)dc.get(nm_ibData);
			width = dc.getUInt8(nm_cbData);
			cellExistenceBitmapIndex = 0xff & (Byte)dc.get(nm_iBit);
		}

		/**	Get the data type for this column description tag.
		*	@return	The property type as masked off from the column tag.
		*/
		private short propertyType()
		{
			return (short)(tag & 0xffff);
		}

		/**	Provide a String representation of this TCInfo object. This is typically used for debugging.
		*	@return	A description of this TColDescr object.
		*/
		@Override
		public String toString()
		{
			String propertyName = PropertyTags.name(tag);
			return String.format("tag 0x%08x (%s) offset into row %d width %d CEB index %d", tag, propertyName, columnOffset, width, cellExistenceBitmapIndex);
		}
	}

	/**	Create a table context from the given BID.
	*	@param	nodeDescr	Description of the node as found in the block or sub-node B-tree.
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The PST file data stream, header, etc.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	* 	@throws	NotHeapNodeException			The leaf is not a heap node
	* 	@throws NotTableContextNodeException		A node without the Table Context client signature was found while building the table context.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	* 	@throws UnknownClientSignatureException		The Client Signature was not recognized
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	* 	@throws UnparseableTableContextException	The table content could not be interpreted
	* 	@throws java.io.IOException			There was an I/O error reading the table.
	*/
	public TableContext(LPTLeaf nodeDescr, BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotTableContextNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseableTableContextException,
		java.io.IOException
	{
		this(nodeDescr, new HeapOnNode(bbt.find(nodeDescr.bidData), bbt, pstFile), bbt, pstFile);
	}

	/**	Create a TableContext object from the given heap-on-node.
	*	This should only be used when the Heap-On-Node has already been found for purposes other than building the Table context.
	*	@param	nodeDescr	Description of the node as found in the block or sub-node B-tree.
	*	@param	hon		The heap-on-node on which this table context is defined.
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The PST file data stream, header, etc.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	* 	@throws NotTableContextNodeException		A node without the Table Context client signature was found while building the table context.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	* 	@throws UnknownClientSignatureException		The Client Signature was not recognized
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	* 	@throws UnparseableTableContextException	The table content could not be interpreted
	* 	@throws java.io.IOException			There was an I/O error reading the table.
	*/
	TableContext(LPTLeaf nodeDescr, HeapOnNode hon, BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotTableContextNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseableTableContextException,
		java.io.IOException
	{
		info = new TCInfo(PSTFile.makeByteBuffer(hon.userRootHeapData()));

		rowIndex = new BTreeOnHeap(hon, info.hidRowIndex, pstFile);
		int numRows = rowIndex.numLeafNodes();
		rows = new Object[numRows][];

		if (numRows == 0)
			return;

		if (info.columnDescription.length == 0)
			return;

		SubnodeBTree sbt = nodeDescr.bidSubnode.isNull() ? null : new SubnodeBTree(nodeDescr.bidSubnode, bbt, pstFile);
		if (info.hnidRows.type == NID.HID) {
			if (hon.heapData(info.hnidRows).length != expectedSize())
				throw new UnparseableTableContextException("Not enough bytes for row data: found " + hon.heapData(info.hnidRows).length + ", expected " + expectedSize());

			readRows(hon, info.columnDescription.length, hon.heapData(info.hnidRows), sbt, bbt, pstFile);
		} else if (info.hnidRows.type == NID.LTP) {
			SLEntry slEntry = (SLEntry)sbt.find(info.hnidRows.key());
			assert slEntry != null;

			BBTEntry bbtEntry = bbt.find(slEntry.bidData);
			assert bbtEntry != null;

			BlockBase b = BlockBase.read(bbtEntry, bbt, pstFile);
			readRows(hon, info.columnDescription.length, b.iterator(), sbt, bbt, pstFile);
		} else {
			assert false: "Unknown HNID node type " + info.hnidRows;
		}
	}

	/**	Returns true if the Cell Existence Bitmap indicates this column exists, and false otherwise.
	*	@param	ceb	The cell existence block for the row currently being processed.
	*	@param	column	The column of the table in which the field being processed lies.
	*	@return	true if the cell existence bitmap indicates that the field is present, false if the cell existens bitmap
	*			indicates it is absent.
	*/
	private boolean cellExists(byte[] ceb, int column)
	{
		int cebIndex = info.columnDescription[column].cellExistenceBitmapIndex;
		return (ceb[cebIndex/8] & (1 << (7 - cebIndex % 8))) != 0;
	}

	/**	Get the column index for the given tag, if present.
	*	@param	tag	The tag to look for in the table context's column list.
	*	@return	The column index for the given tag, if present, or -1 if the tag was not found.
	*/
	private int columnIndex(int tag)
	{
		for (int i = 0; i < info.columnDescription.length; ++i) {
			if (tag == info.columnDescription[i].tag)
				return i;
		}
		return -1;
	}

	/**	The expected number of bytes in the row data heap entry.
	*	@return	The expected number of bytes in a row data heap entry, based on the number of rows and the number of sum of the
	*		number of bytes in all columns.
	*/
	private int expectedSize()
	{
		return rows.length * DataDefinition.size(info.rowFields);
	}

	/**	Get the value for the given tag, if it exists, for the given row.
	*	@param	row	The row to return information for.
	*	@param	tag	The tag indicates the column to return.
	*	@return	The value for the given tag in the given row, if any, otherwise null.
	*/
	public Object get(int row, int tag)
	{
		if (row > getRowCount())
			return null;

		final int column = columnIndex(tag);

		if (column == -1)
			return null;
		return rows[row][column];
	}

	/**	Get the number of data columns in the table.
	*	@return	The number of data columns (i.e. excluding the cell existence bitmap) in this table context.
	*/
	@Override
	public int getColumnCount()
	{
		// Note: don't include the Cell Existence Bitmap!
		return (info == null || info.columnDescription == null) ? 0 : info.columnDescription.length - 1;
	}

	/**	Get the name of the given column for use as a table header.
	*	@param	column	The column to retrieve the header for.
	*	@return	The column name, as a property ID. Note that this function returns a generic name for named properties.
	*/
	@Override
	public String getColumnName(int column)
	{
		return namedProperties.name(info.columnDescription[column].tag);
	}

	/**	Obtain a ByteBuffer from which the raw data for the given propertyID may be read.
	*	@param	propertyTag	The property ID of the tag to read.
	*	@param	data		The raw data for this cell in the table.
	*	@param	hon		The heap-on-node in which to look up data stored in an HID.
	*	@return	A ByteBuffer form which the data may be read.
	*	@throws	java.io.UnsupportedEncodingException	An unsupported text encouding was found while reading in a String for this table.
	*	@see	io.github.jmcleodfoss.pst.BTreeOnHeap#getData
	*	@see	io.github.jmcleodfoss.pst.PropertyContext#getData
	*/
	static java.nio.ByteBuffer getData(int propertyTag, byte[] data, HeapOnNode hon)
	throws
		java.io.UnsupportedEncodingException
	{
		java.nio.ByteBuffer dataBuffer = PSTFile.makeByteBuffer(data);

		if (storedInHID(propertyTag & 0xffff)) {
			DataType hidReader = DataType.hidReader;
			HeapOnNode.HID hid = (HeapOnNode.HID)hidReader.read(dataBuffer);
			if (!hon.validHID(hid))
				return null;

			if (hid.isHID()) {
				if (hon.heapData(hid) == null)
					return null;

				return java.nio.ByteBuffer.wrap(hon.heapData(hid)).asReadOnlyBuffer();
			}
		}

		return java.nio.ByteBuffer.wrap(data).asReadOnlyBuffer();
	}

	/**	Get the number of rows in the table.
	*	@return	The number of rows in this table context.
	*/
	@Override
	public int getRowCount()
	{
		return rows.length;
	}

	/**	Return the value of the specified cell.
	*	@param	row	The row index of the cell to retrieve the value of.
	*	@param	column	The column index of the cell to retrieve the value of.
	*	@return	The value of the given cell, or null if the row for the given row index is empty.
	*/
	@Override
	public Object getValueAt(int row, int column)
	{
		if (rows[row] == null || rows[row][column] == null)
			return null;

		return DataType.makeString(info.columnDescription[column].tag, rows[row][column]);
	}

	/**	No cells are editable.
	*	@param	row	The row index of the cell to retrieve the value of.
	*	@param	column	The column index of the cell to retrieve the value of.
	*	@return	false, always.
	*/
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}

	/**	Is this table context object empty?
	*	@return	true if the number of rows in this table context is 0, false if it is non-zero.
	*/
	private boolean isEmpty()
	{
		return rows.length == 0;
	}

	/**	Read data for all rows from a block of bytes of raw data.
	*	This is used to read HID table contexts.
	*	@param	hon		The heap-on-node containing this table context.
	*	@param	numColumns	The number of columns in this table context (excluding the cell existence bitmap).
	*	@param	data		The raw row data.
	*	@param	sbt		The sub-node B-tree for the table context (where the HID data is to be found).
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The PST file's input data stream, header, etc.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the rows for the table context.
	*/
	private void readRows(HeapOnNode hon, int numColumns, byte[] data, SubnodeBTree sbt, BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		UnimplementedPropertyTypeException,
		UnknownPropertyTypeException,
		java.io.IOException
	{
		java.nio.ByteBuffer rowStream = PSTFile.makeByteBuffer(data);

		int rowWidth = 0xffff & info.endingOffsets[TCInfo.TCI_bm];
		final int maxDataSize = BlockBase.maxBlockSize(pstFile.header.fileFormat) - BlockTrailer.size(pstFile.header.fileFormat);
		final int rowsPerBlock = maxDataSize/rowWidth;
		final int nPaddingBytes = maxDataSize - rowsPerBlock*rowWidth;
		int r;
		for (r = 0; r < rows.length; ++r) {
			if (r > 0 && r % rowsPerBlock == 0)
				rowStream.position(rowStream.position() + nPaddingBytes);
			rows[r] = readRow(rowStream, numColumns, r, sbt, bbt, hon, pstFile);

		}
	}

	/**	Read data for all rows from blocks of bytes returned by an iterator.
	*	This is used to read LTP table contexts.
	*	@param	hon		The heap-on-node containing this table context.
	*	@param	numColumns	The number of columns in this table context (excluding the cell existence bitmap).
	*	@param	iterator	An iterator through the blocks which comprise an LTP table context.
	*	@param	sbt		The sub-node B-tree for the table context (where the HID data is to be found).
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The PST file's input data stream, header, etc.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	java.io.IOException	An I/O error was encountered while reading in the rows for the table context.
	*/
	private void readRows(HeapOnNode hon, int numColumns, java.util.Iterator<java.nio.ByteBuffer> iterator, SubnodeBTree sbt, BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		UnimplementedPropertyTypeException,
		UnknownPropertyTypeException,
		java.io.IOException
	{
		int rowWidth = 0xffff & info.endingOffsets[TCInfo.TCI_bm];
		int r = 0;

		while (iterator.hasNext()) {
			java.nio.ByteBuffer rowStream = iterator.next();
			while (rowStream.remaining() >= rowWidth) {
				if (r >= rows.length)
					throw new DataOverflowException(rows.length);
				rows[r++] = readRow(rowStream, numColumns, r, sbt, bbt, hon, pstFile);
			}
		}
	}

	/**	Read a single row from the TableContext
	*	@param	rowStream	The raw data for this row, as a ByteBuffer with the correct endianness.
	*	@param	numColumns	The number of columns in this table context (excluding the cell existence bitmap)
	*	@param	r		The index of this row (used only for diagnostic logging).
	*	@param	sbt		This table context's sub-node B-tree.
	*	@param	bbt		The PST file's block B-tree.
	*	@param	hon		The heap-on-node containing this table context.
	*	@param	pstFile		The PST file input data stream, header, etc.
	*	@return	The data in the row given by rowStream, parsed into the appropriate PST data types.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws	java.io.IOException	An I/O error was encountered while reading the data for this table context row.
	*/
	@SuppressWarnings({"PMD.UnusedFormalParameter", "unused"})
	private Object[] readRow(java.nio.ByteBuffer rowStream, int numColumns, int r, SubnodeBTree sbt, BlockMap bbt, HeapOnNode hon, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		UnimplementedPropertyTypeException,
		UnknownPropertyTypeException,
		java.io.IOException
	{
		DataContainer dc = new DataContainer(info.rowFields.length);

		dc.read(rowStream, info.rowFields);
		byte[] cellExistenceBitmap = (byte[])dc.get(info.rowFields[info.rowFields.length-1].name);

		Object[] row = new Object[numColumns];
		for (int c = 0; c < numColumns; ++c) {
			Object fieldData = dc.get(info.rowFields[c].name);

			if (!cellExists(cellExistenceBitmap, c))
				continue;

			if (!(fieldData instanceof HeapOnNode.HID)) {
				row[c] = fieldData;
				continue;
			}

			HeapOnNode.HID hid =  (HeapOnNode.HID)fieldData;

			if (!hon.validHID(hid)) {
				row[c] = null;
				continue;
			}

			if (!hid.isHID()) {
				BBTEntry bbtEntry = (BBTEntry)sbt.find(((NID)hid).key());
				BlockBase block = BlockBase.read(bbtEntry, bbt, pstFile);
				java.nio.ByteBuffer bBlock = PSTFile.makeByteBuffer(block.data());
				row[c] = info.rowFields[c].description.read(bBlock);
				continue;
			}

			byte[] data = hon.heapData(hid);
			if (data == null) {
				row[c] = null;
				continue;
			}

			DataType dataReader = DataType.getActualDataType(info.columnDescription[c].tag, data, info.hnidTypes[c]);
			row[c] = dataReader.read(PSTFile.makeByteBuffer(data));
		}
		return row;
	}

	/**	Dependency injection for namedProperties.
	*	@param	namedProperties	The PST file's named properties
	*	@see	#getColumnName
	*/
	static void setNamedProperties(NameToIDMap namedProperties)
	{
		TableContext.namedProperties = namedProperties;
	}

	/**	Are objects of the given property type stored within the tree itself, or in an HID denoted by the leaf element?
	*	@param	propertyType	The property type to check to see whether it is stored directly in the table or in an HID.
	*	@return	true if the given property type is stored in an HID, false if it store in directly in the table.
	*/
	static boolean storedInHID(int propertyType)
	{
		switch (propertyType & 0xffff) {
		case DataType.OBJECT:
		case DataType.STRING:
		case DataType.STRING_8:
		case DataType.BINARY:
		case DataType.GUID:
		/* The PST document implies that MULTIPLE_INTEGER_32 is not kept in an HID. */
		case DataType.MULTIPLE_INTEGER_32:
		case DataType.MULTIPLE_STRING:
		case DataType.MULTIPLE_BINARY:
			return true;
		default: return false;
		}
	}

	/**	Obtain a String representation of this table context.
	*	@return	A string representation of the table context, showing property ID and value.
	*/
	@Override
	public String toString()
	{
		if (isEmpty())
			return "Empty TableContext";

		StringBuilder s = new StringBuilder(info + "\n" + rowIndex + "\n");
		for (Object[] row : rows) {
			if (row != null)
			for (int c = 0; c < row.length; ++c) {
				s.append("\n" + PropertyTags.name(info.columnDescription[c].tag) + ": ");
				if (row[c] == null)
					s.append("empty");
				else if (row[c] instanceof Byte)
					s.append("0x" + Integer.toHexString((Byte)row[c]));
				else if (row[c] instanceof Short)
					s.append("0x" + Integer.toHexString((Short)row[c]));
				else if (row[c] instanceof Integer)
					s.append("0x" + Integer.toHexString((Integer)row[c]));
				else if (row[c] instanceof Long)
					s.append("0x" + Long.toHexString((Long)row[c]));
				else if (row[c] instanceof byte[])
					s.append(ByteUtil.createHexByteString((byte[])row[c]));
				else
					s.append(row[c]);
			}
			s.append("\n");
		}
		return s.toString();
	}

	/**	Test the TableContext class by reading in the first node containing a table context, extracting the table, and printing
	*	it out.
	*	@param	args	The file(s) to show the TableConext for.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.TableContext pst-file [pst-file ...]`");
			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {
				java.io.FileInputStream stream = new java.io.FileInputStream(a);
				final PSTFile pstFile = new PSTFile(stream);
				try {
					final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
					final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

					final OutputSeparator separator = new OutputSeparator();
					java.util.Iterator<BTreeNode> iterator = nbt.iterator();
					while (iterator.hasNext()) {
						final NBTEntry nodeDescr = (NBTEntry)iterator.next();
						if (nodeDescr.nid.type == NID.INTERNAL)
							continue;
						final BBTEntry dataBlock = bbt.find(nodeDescr.bidData);
							if (dataBlock != null) {
							try {
								final HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
								if (!hon.containsData())
									continue;
								if (hon.clientSignature().equals(ClientSignature.TableContext)) {
									separator.emit(System.out);
									final TableContext tc = new TableContext(nodeDescr, hon, bbt, pstFile);
									System.out.printf("Node %s%nTableContext%n------------%n%s%n", nodeDescr.toString(), tc.toString());
									if (tc.isEmpty())
										continue;
									tc.rowIndex.outputString(System.out, new StringBuilder("rowIndex"));
								}
							} catch (final	DataOverflowException
								|	NotHeapNodeException
								|	NotTableContextNodeException
								|	UnimplementedPropertyTypeException
								|	UnknownClientSignatureException
								|	UnknownPropertyTypeException
								|	UnparseableTableContextException e) {
								System.out.printf("%s%n\t%s", nodeDescr.toString(), e.toString());
								e.printStackTrace(System.out);
							}
						}
					}
				} finally {
					try {
						pstFile.close();
					} catch (final java.io.IOException e) {
						System.out.printf("There was a problem closing file %s%n", a);
					}
				}
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final	BadXBlockLevelException
				|	BadXBlockTypeException e) {
				System.out.println(e);
				e.printStackTrace(System.out);
			} catch (final CRCMismatchException e) {
				System.out.printf("File %s is corrupt (Calculated CRC does not match expected value)%n", a);
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final java.io.IOException e) {
				System.out.printf("Could not read %s%n", a);
				e.printStackTrace(System.out);
			}
		}
	}
}
