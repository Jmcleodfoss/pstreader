package io.github.jmcleodfoss.pst;

/**	The BTreeOnHeap class represents a B-tree contained on a heap defined by a node in the node B-tree.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/2dd1a95a-c8b1-4ac5-87d1-10cb8de64053">MS-PST Section 2.3.2: BTree-on-Heap (BTH)</a>
*/
public class BTreeOnHeap extends BTree
{
	/**	The BTHContext class provides the context (data, metadata, and current location) for use when reading in a
	*	B-tree-on-heap structure.
	*/
	private static class BTHContext extends Context<BTree, BTreeLeaf>
	{
		/**	The heap-on-node containing this B-tree. */
		private HeapOnNode hon;

		/**	The header block for this B-tree on heap structure. */
		private final Header header;

		/**	This flag indicates whether the B-tree is empty. If it is empty, the value of index may not be used. */
		private final boolean fEmpty;

		/**	The HID corresponding to the current level of the B-tree. */
		private final HeapOnNode.HID hid;

		/**	The level of the nodes currently being built in the B-tree. */
		private final int level;

		/**	Construct a BTHContext object representing the root of the B-tree-on-heap structure, where the root index is the
		*	first entry in the heap-on-node.
		*	@param	hon	The heap-on-node on which this B-tree-on-heap is built.
		*	@param	pstFile	The PST file's {@link Header}, data stream, etc.
		*	@throws java.io.IOException	The PST file could not be read.
		*/
		private BTHContext(final HeapOnNode hon, PSTFile pstFile)
		throws
			java.io.IOException
		{
			this(hon, HeapOnNode.HID.BTreeOnHeapRoot, pstFile);
		}

		/**	Construct a BTHContext object representing the root of the B-tree-on-heap structure where the root is provided.
		*	@param	hon	The heap-on-node on which this B-tree-on-heap is built.
		*	@param	hid	The heap ID of the root of the B-tree-on-heap.
		*	@param	pstFile	The PST file's {@link Header}, data stream, etc.
		*	@throws java.io.IOException	The header could not be read from the PST file.
		*/
		private BTHContext(final HeapOnNode hon, final HeapOnNode.HID hid, PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(pstFile);
			this.hon = hon;
			header = new Header(PSTFile.makeByteBuffer(hon.heapData(hid)));
			fEmpty = !header.hidRoot.isHID();
			this.hid = header.hidRoot;
			level = header.levels;// + 1;
		}

		/**	Construct a BTHContext object representing the level below the passed in context for a B-tree-on-heap structure.
		*	@param	hid		The heap ID of the root of the B-tree-on-heap.
		*	@param	newLevel	The level of this entry in the B-tree (0 = leaf node)
		*	@param	context		The B-tree-in-heap construction context to use when building the next node.
		*	@throws	java.io.IOException	An I/O error was encountered while reading the B-tree header context.
		*/
		private BTHContext(final HeapOnNode.HID hid, final int newLevel, BTHContext context)
		throws
			java.io.IOException
		{
			super(context.pstFile);
			header = context.header;
			hon = context.hon;
			fEmpty = context.fEmpty;
			this.hid = hid;
			level = newLevel;
		}

		/**	The stream of data from which to read the intermediate or leaf node data.
		*	For the B-tree-on-heap structure, this is a stream created from an entry in the heap.
		*	@return	{@inheritDoc}
		*/
		@Override
		protected java.nio.ByteBuffer entryDataStream()
		throws
			java.io.IOException
		{
			return PSTFile.makeByteBuffer(hon.heapData(hid));
		}

		/**	The size of a single entry in the B-tree-on-heap structure. Note that the size depends on values in the BTH header.
		*	@return	{@inheritDoc}
		*/
		@Override
		protected int getEntrySize()
		{
			return header.keySize + (isLeafNode() ? header.dataSize : HeapOnNode.HID.SIZE);
		}

		/**	Obtain The number of entries contained within in this heap index.
		*	@return	{@inheritDoc}
		*/
		@Override
		protected int getNumEntries()
		{
			if (fEmpty || !hid.isHID() || hon.heapData(hid) == null)
				return 0;

			return hon.heapData(hid).length/getEntrySize();
		}

		/**	{@inheritDoc} */
		@Override
		protected BTreeOnHeap intermediateNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			final IntermediateRecord entry = new IntermediateRecord(entryStream, header.keySize);
			return new BTreeOnHeap(entry.key(), nextLevelContextFactory(entry.hidNextLevel));
		}

		/**	{@inheritDoc} */
		@Override
		protected boolean isLeafNode()
		{
			return level == 0;
		}

		/**	{@inheritDoc} */
		@Override
		protected LeafRecord leafNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new LeafRecord(entryStream, header.keySize, header.dataSize);
		}
	
		/**	Create a context object for use when reading the next level of the B-tree.
		*	@param	hid	The heap-on-node index of the next level nod of the B-tree
		*	@return	A new BTHContect object for use when reading the next child node of this node.
		*	@throws	java.io.IOException	An I/O error was encountered while reading the B-tree header context.
		*/
		private BTHContext nextLevelContextFactory(final HeapOnNode.HID hid)
		throws
			java.io.IOException
		{
			return new BTHContext(hid, level-1, this);
		}
	}

	/**	The header of a B-tree-on-heap structure. */
	private static class Header
	{
		private static final String nm_bType = "bType";
		private static final String nm_cbKey = "cbKey";
		private static final String nm_cbEnt = "cbEnt";
		private static final String nm_bIdxLevels = "bIdxLevels";
		private static final String nm_hidRoot = "hidRoot";

		/**	The fields in the input stream which make up the B-tree-on-heap header. */
		private static DataDefinition[] fields = {
			new DataDefinition(nm_bType, DataType.integer8Reader, true),
			new DataDefinition(nm_cbKey, DataType.integer8Reader, true),
			new DataDefinition(nm_cbEnt, DataType.integer8Reader, true),
			new DataDefinition(nm_bIdxLevels, DataType.integer8Reader, true),
			new DataDefinition(nm_hidRoot, DataType.hidReader, true)
		};

		/**	The size in bytes of a key in the B-tree-on-heap structure. */
		final byte keySize;

		/**	The size in bytes of the data entries in the B-tree-on-heap structure. */
		final byte dataSize;

		/**	The number of levels in the B-tree on heap structure. */
		final byte levels;

		/**	The index of the heap entry for root of the B-tree-on-heap structure. Note that these values start at
		*	0x00000020 rather than at 0.
		*/
		final HeapOnNode.HID hidRoot;

		/**	Read in B-tree-on-heap header information.
		*	@param	stream	The input stream from which to read the B-tree-on-heap header data.
		*	@throws	java.io.IOException	An I/O error was encountered while reading the B-tree header.
		*/
		Header(java.nio.ByteBuffer stream)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, fields);
			keySize = (Byte)dc.get(nm_cbKey);
			dataSize = (Byte)dc.get(nm_cbEnt);
			levels = (Byte)dc.get(nm_bIdxLevels);
			hidRoot = (HeapOnNode.HID)(dc.get(nm_hidRoot));
		}

		/**	Obtain a description of the BTH header. This is typically used for debugging.
		*	@return	A string describing this object.
		*/
		@Override
		public String toString()
		{
			return String.format("BTH Root HID %s Key size %d data size %d levels %d", hidRoot, keySize, dataSize, levels);
		}
	}

	/**	The RecordBase class contains information common to both intermediate and leaf node records. */
	private static class RecordBase
	{
		protected static final String nm_cbKey = "cbKey";

		/**	The data context used to read in data for the classes derived from RecordBase. */
		protected DataContainer dc;
	
		/**	The size in bytes of an entry at this level of the heap (intermediate and leaf nodes may have different sizes). */
		final int entrySize;

		/**	The size in bytes of a key. */
		final int keySize;

		/**	The raw data forming the key. */
		final byte[] key;

		/**	The constructor for the RecordBase class reads in the given fields from the datastream, and saves the key and
		*	the key and data sizes.
		*	@param	stream	The input stream from which to read the BTH data.
		*	@param	keySize	The size of the key object in this B-tree-on-heap.
		*	@param	fields	The fields to read in when constructing the node.
		*	@throws	java.io.IOException	An I/O error was encountered while reading in the B-tree.
		*/
		RecordBase(java.nio.ByteBuffer stream, byte keySize, DataDefinition[] fields)
		throws
			java.io.IOException
		{
			dc = new DataContainer();
			entrySize = DataDefinition.size(fields);
			dc.read(stream, fields);
			this.keySize = keySize;
			key = (byte[])dc.get(nm_cbKey);
		}

		/**	Return the key for this node. Note that as the key size depends on the data in the B-tree, this value is
		*	determined based on the key length.
		*	@return	key	The key for this B-tree-on-heap node entry.
		*/
		public long key()
		{
			switch (keySize) {
			case 1:
			case 2:
			case 4:
				return ByteUtil.makeLongLE(key, keySize);

			default:
				return ByteUtil.makeLongLE(key, 4);
			}
		}
	}

	/**	An intermediate entry in the B-tree-on-heap structure. */
	private static class IntermediateRecord extends RecordBase
	{
		private static final String nm_hidNextLevel = "hidNextLevel";

		/**	The field in the input stream which describes the next level. Note that this is read after the key, whose size
		*	depends on the contents of the B-tree.
		*/
		private static DataDefinition next_level_field = new DataDefinition(nm_hidNextLevel, DataType.hidReader, true);

		/**	The index of the next level in the B-tree-on-heap structure. */
		HeapOnNode.HID hidNextLevel;

		/**	Return the field definitions required to read in an intermediate record from the input stream.
		*	@param	keySize	The size of the key values in this B-tree-on-heap.
		*	@return	The field definitions for the record being read.
		*/
		private static DataDefinition[] fieldDefinitions(final byte keySize)
		{
			DataDefinition key_field = new DataDefinition(nm_cbKey, new DataType.SizedByteArray(keySize), true);
			DataDefinition[] fields = {
				key_field,
				next_level_field
			};

			return fields;
		}

		/**	Create an intermediate node record with data read from the input stream.
		*	@param	stream	{@inheritDoc}
		*	@param	keySize	{@inheritDoc}
		*/
		IntermediateRecord(java.nio.ByteBuffer stream, final byte keySize)
		throws
			java.io.IOException
		{
			super(stream, keySize, fieldDefinitions(keySize));
			hidNextLevel = (HeapOnNode.HID)dc.get(nm_hidNextLevel);
		}

		/**	Provide a description of an intermediate node in the B-tree-on-heap structure. This is typically used for
		*	debugging.
		*	@return	A string describing the intermediate node record.
		*/
		@Override
		public String toString()
		{
			return String.format("key 0x%x, hidNextLevel %s", key(), hidNextLevel.toString()); 
		}
	}

	/**	A leaf entry in the B-tree-on-heap structure. */
	static class LeafRecord extends RecordBase implements BTreeLeaf
	{
		private static final String nm_cbData = "cbData";

		/**	The contents of this leaf node in the B-tree-on-heap. */
		final byte[] data;

		/**	Return the field definitions required to read in a leaf record from the input stream.
		*	@param	keySize		The size of the key object in this B-tree-on-heap.
		*	@param	dataSize	The size of the data object in this B-tree-on-heap.
		*	@return	The DataDefinition array which should be read in to obtain a B-tree-on-heap leaf entry.
		*/
		private static DataDefinition[] fieldDefinitions(final byte keySize, final byte dataSize)
		{
			final DataDefinition[] fields = {
				new DataDefinition(nm_cbKey, new DataType.SizedByteArray(keySize), true),
				new DataDefinition(nm_cbData, new DataType.SizedByteArray(dataSize), true)
			};

			return fields;
		}

		/**	Create a leaf record with data read out of the input stream.
		*	@param	stream		{@inheritDoc}
		*	@param	keySize		{@inheritDoc}
		*	@param	dataSize	The size of the data object in this B-tree-on-heap.
		*/
		LeafRecord(java.nio.ByteBuffer stream, final byte keySize, final byte dataSize)
		throws
			java.io.IOException
		{
			super(stream, keySize, fieldDefinitions(keySize, dataSize));

			data = (byte[])dc.get(nm_cbData);
		}

		/**	{@inheritDoc} */
		public int actualSize(Context<BTree, BTreeLeaf> context)
		{
			return entrySize;
		}

		/**	{@inheritDoc} */
		public javax.swing.table.TableModel getNodeTableModel()
		{
			return null;
		}

		/**	{@inheritDoc} */
		public String getNodeText()
		{
			return String.format("0x%08x (%d, %d): %s", key(), keySize, entrySize, ByteUtil.createHexByteString(data));
		}

		/**	{@inheritDoc} */
		public java.nio.ByteBuffer rawData(final BlockMap bbt, final PSTFile pstFile)
		throws
			java.io.IOException
		{
			return java.nio.ByteBuffer.wrap(data).asReadOnlyBuffer();
		}

		/**	Provide a description of a leaf node in the B-tree-on-heap structure. This is typically used for debugging.
		*	@return	A string describing the B-tree-on-heap leaf record.
		*/
		@Override
		public String toString()
		{
			return String.format("key 0x%08x, data %s", key(), ByteUtil.createHexByteString(data));
		}
	}

	/**	Construct a B-tree-on-heap structure with the given key from the given context. This function may be called for the root 
	*	and for intermediate nodes in the B-tree.
	*	@param	key	{@inheritDoc}
	*	@param	context	{@inheritDoc}
	*/
	private BTreeOnHeap(final long key, final BTHContext context)
	throws
		java.io.IOException
	{
		super(key, context);
	}

	/**	Construct a B-tree-on-heap structure with from the given heap-on-node structure and PST file object. This function is 
	*	intended to create the root of the B-tree.
	*	@param	hon	The heap-on-node from which to derive this B-tree-on-heap.
	*	@param	pstFile	The PST file's {@link Header}, input stream, etc.
	*	@throws java.io.IOException	The PST file could not be read.
	*/
	public BTreeOnHeap(final HeapOnNode hon, PSTFile pstFile)
	throws
		java.io.IOException
	{
		this(0, new BTHContext(hon, pstFile));
	}

	/**	Construct a B-tree-on-heap structure with from the given heap-on-node structure and PST file object using a specified
	*	heap ID. This function is intended to create the root of the B-tree.
	*	@param	hon	The heap-on-node from which to derive this B-tree-on-heap.
	*	@param	hid	The index of heap-on-node which points to the B-tree-on-heap's root.
	*	@param	pstFile	The PST file's {@link Header}, input stream, etc.
	*	@throws	java.io.IOException	An I/O error was encountered while reading this B-tree.
	*/
	BTreeOnHeap(final HeapOnNode hon, final HeapOnNode.HID hid, PSTFile pstFile)
	throws
		java.io.IOException
	{
		this(hid.key(), new BTHContext(hon, hid, pstFile));
	}

	/**	Obtain the actual size of the B-tree-on-heap object.
	*	@param	context	The context from which to create the B-tree-on-heap.
	*	@return	The actual size of a B-tree-on-heap object for this B-tree-on-heap.
	*/
	public int actualSize(final Context<BTree, BTreeLeaf> context)
	{
		return ((BTHContext)context).header.keySize + HeapOnNode.HID.SIZE;
	}

	/**	Obtain data for the given leaf node, or null if the given object is not a leaf node.
	*	@param	o	The node to retrieve the data for.
	*	@param	hon	The heap-on-node from which the B-tree-on-heap is being built.
	*	@return	A read-only ByteBuffer containing the data in the leaf node.
	*	@throws java.io.UnsupportedEncodingException	The PST file could not be read.
	*/
	public static java.nio.ByteBuffer getData(final Object o, final HeapOnNode hon)
	throws
		java.io.UnsupportedEncodingException
	{
		if (o instanceof LeafRecord) {
			LeafRecord l = (LeafRecord)o;
			if (hon.isPropertyContext())
				return PropertyContext.getData((int)(l.key()), l.data, hon);
			if (hon.isTableContext())
				return TableContext.getData((int)(l.key()), l.data, hon);
			return java.nio.ByteBuffer.wrap(l.data).asReadOnlyBuffer();
		}

		return null;
	}

	/**	{@inheritDoc} */
	public String getNodeText()
	{
		return String.format("0x%08x", key);
	}

	/**	Test this class by displaying the first B-tree-on-heap in the given PST file.
	*	@param	args	The arguments to the test application.
	*/
	public static void main(String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.BTreeOnHeap pst-filename");
			System.exit(1);
		}
		try {
			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(args[0]));
			BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
			NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

			OutputSeparator separator = new OutputSeparator();

			java.util.Iterator<BTreeNode> iterator = nbt.iterator();
			while (iterator.hasNext()) {
				NBTEntry node = (NBTEntry)iterator.next();
				if (!node.nid.isHeapOnNodeNID())
					continue;

				BBTEntry dataBlock = bbt.find(node.bidData);
				if (dataBlock != null) {
					try {
						HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
						if (!hon.containsData() || (!hon.clientSignature().equals(ClientSignature.PropertyContext) && !hon.clientSignature().equals(ClientSignature.TableContext)))
							continue;

						try {
							BTreeOnHeap bth = new BTreeOnHeap(hon, pstFile);
	
							separator.emit(System.out);
							System.out.println("Node " + node + "\nBTreeOnHeap\n----------\n" + bth);
							bth.outputString(System.out, new StringBuilder("bth: "));
						} catch (final Exception e) {
							e.printStackTrace(System.out);
							System.out.print("node " + node);
							System.out.print("dataBlock " + dataBlock);
							System.out.print("hon " + hon);
							throw e;
						}
					} catch (final NotHeapNodeException e) {
						// Not every node in the block B-tree is a heap node, so this is benign.
					} catch (final UnknownClientSignatureException e) {
						System.out.println("Node " + node + ": " + e);
						e.printStackTrace(System.out);
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
