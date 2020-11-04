package io.github.jmcleodfoss.pst;

/**	The PropertyContext class contains a PST file property context.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/294c83c6-ff92-42f5-b6b6-876c29fa9737">MS-PST Section 2.3.3: Property Context (PC)</a>
*/
public class PropertyContext
{
	/**	The location in which to store the properties. */
	protected final java.util.HashMap<Integer, Object> properties;

	/**	The PSTDataPointer permits saving a reference to an object which is large enough that it should only be retrieved on demand. */
	static protected class PSTDataPointer
	{
		/**	The reader for the given property. */
		final DataType reader;

		/**	The entry of the PST file's block BTree to read the data from. */
		final BBTEntry entry;

		/**	The PST file's block B-Tree. */
		final BlockMap bbt;

		/**	The PST file's input stream, etc. */
		final PSTFile pstFile;

		/**	Create a reference which may be used to retreive information from a PST file at a later time.
		*	@param	propertyType	The type of the property to read.
		*	@param	entry		The entry in the block B-Tree containing the data.
		*	@param	bbt		The PST file's block B-Tree.
		*	@param	pstFile		The PST file's input stream, etc.
		*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
		*	@throws UnknownPropertyTypeException	The property type was not recognized
		*/
		PSTDataPointer(final short propertyType, final BBTEntry entry, final BlockMap bbt, final PSTFile pstFile)
		throws
			UnimplementedPropertyTypeException,
			UnknownPropertyTypeException
		{
			reader = DataType.definitionFactory(propertyType);
			this.entry = entry;
			this.bbt = bbt;
			this.pstFile = pstFile;
		}

		/**	Retrieve the data we deferred reading.
		*	@return	The data read in from the given reference to the PST file.
		*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
		*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
		*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
		*/
		Object data()
		throws
			BadXBlockLevelException,
			BadXBlockTypeException,
			CRCMismatchException
		{
			try {
				BlockBase block = BlockBase.read(entry, bbt, pstFile);
				return reader.read(block.dataStream());
			} catch (final java.io.IOException e) {
			}

			return null;
		}
	}

	/**	A TableModel which may be used to represent a PropertyContext object. */
	static private class TableModel extends LPTTableModel
	{
		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;
	
		/**	Create the table model.
		*	@param	properties	The properties in the PropertyContext object.
		*	@param	namedProperties	The known property names
		*/
		TableModel(final java.util.Map<Integer, Object> properties, final NameToIDMap namedProperties)
		{
			super(properties, namedProperties);
		}
	
		/**	Retrieve the string representation of the value.
		*	@param	key	The property key, required to convert the data value into a String.
		*	@param	value	The value to convert.
		*	@return	A string representation of the value
		*/
		@Override
		@SuppressWarnings("PMD.AvoidReassigningParameters")
		String getValueString(final int key, Object value)
		{
			try {
				if (value instanceof PSTDataPointer)
					value = ((PSTDataPointer)value).data();

				return value == null ? "" : DataType.makeString(key, value);
			} catch (final BadXBlockLevelException e) {
				return "";
			} catch (final BadXBlockTypeException e) {
				return "";
			} catch (CRCMismatchException e) {
				return "";
			}
		}

		/**	No cells are editable.
		*	@param	rowIndex	The row of the cell to check.
		*	@param	columnIndex	The column of the cell to check.
		*	@return	false, as the table can never be edited.
		*/
		@Override
		public boolean isCellEditable(final int rowIndex, final int columnIndex)
		{
			return false;
		}
	}

	/**	Create an empty PropertyContext object. */
	private PropertyContext()
	{
		properties = new java.util.HashMap<Integer, Object>();
	}

	/**	Create a PropertyContext from the node ID, node database, and basic PST file object.
	*	@param	node	The node containing the property context.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	pstFile	The PST file data stream, etc.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	NotHeapNodeException			A node which was not a heap node was found while bulding the property context.
	*	@throws	NotPropertyContextNodeException		A node which is not part of a property context was found while building the property context.
	*	@throws	NullDataBlockException			A null data block was found while building the property context.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unknown client signature was found while building the property context.
	*	@throws UnknownPropertyTypeException		The property type was not recognized
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context block was found while building the property context.
	*	@throws	java.io.IOException			An I/O error was encountered while reading in the data for the property context.
	*/
	PropertyContext(final LPTLeaf node, final BlockMap bbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NullDataBlockException,
		UnparseablePropertyContextException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		java.io.IOException
	{
		this();

		if (node == null)
			throw new RuntimeException("node is null");

		BBTEntry dataBlock = bbt.find(node.bidData);
		if (dataBlock == null)
			throw new NullDataBlockException(node);

		HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
		if (!hon.clientSignature().equals(ClientSignature.PropertyContext))
			throw new NotPropertyContextNodeException(node, hon.clientSignature());

		BTreeOnHeap bth = new BTreeOnHeap(hon, pstFile);
		read(node, hon, bth, bbt, pstFile);
	}

	/**	Determine whether this property context contains the given tag.
	*	@param	tag	The tag to look for.
	*	@return	true if this property context contains the given tag, false if it doesn't.
	*/
	boolean containsKey(final int tag)
	{
		return properties.containsKey(tag);
	}

	/**	Retrieve a value from the property context.
	*	@param	tag	The tag to look for.
	*	@return	The object stored under the given tag.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*/
	Object get(final int tag)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException
	{
		final Object o = properties.get(tag);
		if (o instanceof PSTDataPointer)
			return ((PSTDataPointer)o).data();

		return o;
	}

	/**	Return a ByteBuffer holding the data for the given tag, taken from HeapOnNode hon if the data is stored in an HNID.
	*	@param	tag	The tag of the data to retrieve
	*	@param	data	The raw data to read; this contains either the data or a reference to an HNID.
	*	@param	hon	The heap-on-node to read from if the data is stored in an HNID.
	*	@return	A ByteBuffer from which the data may be read.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws	java.io.UnsupportedEncodingException	An unsupported text encoding was found when reading in String data for this property.
	*	@see	io.github.jmcleodfoss.pst.BTreeOnHeap#getData
	*	@see	io.github.jmcleodfoss.pst.TableContext#getData
	*/
	static java.nio.ByteBuffer getData(final int tag, final byte[] data, final HeapOnNode hon)
	throws
		UnimplementedPropertyTypeException,
		UnknownPropertyTypeException,
		java.io.UnsupportedEncodingException
	{
		java.nio.ByteBuffer dataBuffer = PSTFile.makeByteBuffer(data);

		DataType int16Reader = DataType.definitionFactory(DataType.INTEGER_16);
		short type = (Short)int16Reader.read(dataBuffer);

		if (storedInHNID(type)) {
			final DataType hidReader = DataType.hidReader;
			final HeapOnNode.HID hid = (HeapOnNode.HID)hidReader.read(dataBuffer);
			if (hon.validHID(hid) && hid.isHID() && hon.heapData(hid) != null)
				return java.nio.ByteBuffer.wrap(hon.heapData(hid)).asReadOnlyBuffer();
		}

		return java.nio.ByteBuffer.wrap(data).asReadOnlyBuffer();
	}

	/**	Retrieve an iterator traversing the values in the property context.
	*	@return	An interator through all the properties in the property context.
	*/
	public java.util.Iterator<java.util.Map.Entry<Integer, Object>> iterator()
	{
		return properties.entrySet().iterator();
	}

	/**	Retrieve the property corresponding to the given B-tree-on-heap leaf entry in a Property Context.
	*	@param	lr		The B-tree-on-heap record from which to read the property value.
	*	@param	tag		The property tag, needed to handle an inconsistency on the Outlook side.
	*	@param	propertyType	The data type of the property to read.
	*	@param	bData		A ByteBuffer containing the data to read.
	*	@param	sbt		The sub-node B-tree for the property context.
	*	@param	hon		The heap-on-node containing the property context
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The PST file data stream, etc.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context node was encountered while reading this property.
	*	@throws java.io.IOException			An I/O exception was encoutered while reading this property.
	*	@return	The object read in.
	*/
	private Object property(final BTreeOnHeap.LeafRecord lr, final int tag, final short propertyType, final java.nio.ByteBuffer bData, final SubnodeBTree sbt, final HeapOnNode hon, final BlockMap bbt, PSTFile pstFile)
	throws
		UnimplementedPropertyTypeException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		DataType dataReader = DataType.definitionFactory(propertyType);
		if ((dataReader.size() != 0 && dataReader.size() < lr.data.length) && (!storedInHNID(propertyType) || lr.data.length < 4))
			return dataReader.read(bData);
		final DataType hidReader = DataType.hidReader;
		final HeapOnNode.HID hnid = (HeapOnNode.HID)hidReader.read(bData);
		if (!hnid.isHID() && hnid.type != NID.HID) {
			// Must be an NID
			final SLEntry slEntry = (SLEntry)sbt.find(hnid.key());
			assert slEntry != null;

			final BBTEntry bbtEntry = bbt.find(slEntry.bidData);
			assert bbtEntry != null;

			return new PSTDataPointer(propertyType, bbtEntry, bbt, pstFile);
		}

		// This seems to usually mean the HID is 0x00000000, and I think it makes sense to treat this as null, since this is either an array of objects or a binary object or string.
		if (!hon.validHID(hnid))
			return null;

		byte[] data = hon.heapData(hnid);
		dataReader = DataType.getActualDataType(tag, data, dataReader);
		return dataReader.read(PSTFile.makeByteBuffer(data));
	}

	/**	Read in a property context.
	*	@param	node	The node from which to read the property context.
	*	@param	hon	The heap-on-node containing the property context.
	*	@param	bth	The B-tree-on-heap containing the heap-on-node.
	*	@param	bbt	The PST file block B-tree.
	*	@param	pstFile	The PST file data stream, etc.
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context block was found while reading the property context.
	*	@throws	java.io.IOException			An I/O error was encountered while reading in the data for the property context.
	*/
	private void read(final LPTLeaf node, final HeapOnNode hon, final BTreeOnHeap bth, final BlockMap bbt, PSTFile pstFile)
	throws
		CRCMismatchException,
		UnimplementedPropertyTypeException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		final SubnodeBTree sbt = node.bidSubnode.key() != 0 ? new SubnodeBTree(node.bidSubnode, bbt, pstFile) : null;

		java.util.Iterator<BTreeNode> iterator = bth.iterator();
		while (iterator.hasNext()) {
			final BTreeOnHeap.LeafRecord lr = (BTreeOnHeap.LeafRecord)iterator.next();
			java.nio.ByteBuffer bData = PSTFile.makeByteBuffer(lr.data);
			final short propertyType = bData.getShort();
			final int tag = (int)lr.key() << 16 | propertyType;
			final Object property = property(lr, tag, propertyType, bData, sbt, hon, bbt, pstFile);
			properties.put(tag, property);
		}
	}

	/**	Return a table model for the property context for the given NID.
	*	@param	namedProperties	The property names for this PST file.
	*	@return	TableModel representation of this property context.
	*/
	LPTTableModel tableModel(final NameToIDMap namedProperties)
	{
		return new TableModel(properties, namedProperties);
	}

	/**	Are objects of the given property type stored within the tree itself, or in an HID denoted by the leaf element?
	*	@param	propertyType	The property type to check to see whether it is stored directly in the table or in an HID.
	*	@return	true if the given property type is stored in an HID, false if it store in directly in the property context.
	*/
	private static boolean storedInHNID(final int propertyType)
	{
		switch (propertyType) {
		case DataType.OBJECT:
		case DataType.STRING:
		case DataType.STRING_8:
		case DataType.BINARY:
		case DataType.GUID:
		case DataType.SERVER_ID:
		case DataType.MULTIPLE_INTEGER_32:
		case DataType.MULTIPLE_STRING:
		case DataType.MULTIPLE_BINARY:
			return true;
		default: return false;
		}
	}

	/**	Return a string representation of this Property Context which enumerates all properties, including tag name (if known),
	*	key,and value. This is typically used for debugging; it does not take into account named properties, or the 
	*	"bucket list" in the property name map.
	*	@return	A string represengint the PropertyContext object.
	*/
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		java.util.Iterator<java.util.Map.Entry<Integer, Object>> iterator = iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry<Integer, Object> entry = iterator.next();
			if (s.length() > 0)
				s.append('\n');
			int propertyTag = entry.getKey();
			s.append("0x");
			s.append(Integer.toHexString(propertyTag));
			s.append(' ');
			s.append(PropertyTags.name(propertyTag));
			s.append(' ');
			Object o = entry.getValue();
			if (o == null)
				s.append("null");
			else {
				if (o instanceof PSTDataPointer) {
					try {
						o = ((PSTDataPointer)o).data();
					} catch (final BadXBlockLevelException e) {
						o = "";
					} catch (final BadXBlockTypeException e) {
						o = "";
					} catch (CRCMismatchException e) {
						o = "";
					}
				}
				s.append(DataType.makeString(propertyTag, o));
			}
		}
		return s.toString();
	}

	/**	Test the PropertyContext class by reading in the first node containing a property context, extracting the properties,
	*	and printing them out.
	*	@param	args	The files to display the property contexts of.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.PropertyContext pst-file [pst-file ...]");
			System.exit(1);
		}


		for (final String a: args) {
			System.out.println(a);
			try {
				// Suppresing output can dramatically increase the speed of this function, while still showing any exceptions raised.
				// Medium-term goal is to set this based on a command line argument.
				final boolean fShowOutput = true;

				java.io.FileInputStream stream = new java.io.FileInputStream(a);
				final PSTFile pstFile = new PSTFile(stream);
				try {
					final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
					final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);
					final NameToIDMap namedProperties = new NameToIDMap(bbt, nbt, pstFile);

					final OutputSeparator separator = new OutputSeparator();

					java.util.Iterator<BTreeNode> iterator = nbt.iterator();
					while (iterator.hasNext()) {
						final NBTEntry node = (NBTEntry)iterator.next();
						if (node.nid.type == NID.INTERNAL)
							continue;

						final BBTEntry dataBlock = bbt.find(node.bidData);
						if (dataBlock == null)
							continue;

						// Check for valid property context. We expect to encounter quite a few non-PC blocks, so this is completely benign.
						final HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
						if (!hon.clientSignature().equals(ClientSignature.PropertyContext))
							continue;

						final PropertyContext pc = new PropertyContext(node,  bbt, pstFile);

						if (fShowOutput) {
							separator.emit(System.out);
							System.out.printf("Node i%s, 0x%08x \nPropertyContext\n---------------\n", node.toString(), node.key());
							java.util.Iterator<java.util.Map.Entry<Integer, Object>> propertyIterator = pc.iterator();
							while (propertyIterator.hasNext()) {
								final java.util.Map.Entry<Integer, Object> entry = propertyIterator.next();
								final int key = entry.getKey();
								final String name = namedProperties.name(key);
								final Object value = pc.get(key);
								final String s = value != null ? DataType.makeString(key, value) : null;
								System.out.printf("0x%08x %s \"%s\"%n", key, name, s);
							}
						}
					}
				} catch (final BadXBlockLevelException e) {
					System.out.println(e);
					e.printStackTrace(System.out);
				} catch (final BadXBlockTypeException e) {
					System.out.println(e);
					e.printStackTrace(System.out);
				} catch (final IncorrectNameIDStreamContentException e) {
					e.printStackTrace(System.out);
				} catch (final NameIDStreamNotFoundException e) {
					e.printStackTrace(System.out);
				} catch (final NotHeapNodeException e) {
					// Not every node in the block B-tree is a heap node, so this is benign.
				} catch (final NotPropertyContextNodeException e) {
					System.out.println(e.toString());
					e.printStackTrace(System.out);
				} catch (final NullDataBlockException e) {
					System.out.println(e.toString());
					e.printStackTrace(System.out);
				} catch (final UnimplementedPropertyTypeException e) {
					System.out.println(e.toString());
					e.printStackTrace(System.out);
				} catch (final UnknownPropertyTypeException e) {
					System.out.println(e.toString());
					e.printStackTrace(System.out);
				} catch (final UnknownClientSignatureException e) {
					System.out.println(e.toString());
					e.printStackTrace(System.out);
				} catch (final UnparseablePropertyContextException e) {
					System.out.printf(e.toString());
					e.printStackTrace(System.out);
				} finally {
					try {
						pstFile.close();
					} catch (final java.io.IOException e) {
						System.out.printf("There was a problem closing file %s%n", a);
					}
				}
			} catch (final CRCMismatchException e) {
				System.out.printf("File %s is corrupt (Calculated CRC does not match expected value)%n", a);
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				System.out.printf("Could not read %s%n", a);
				e.printStackTrace(System.out);
			}
		}
	}
}
