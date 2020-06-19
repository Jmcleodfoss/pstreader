package io.github.jmcleodfoss.pst;

/**	The HeapOnNode class represents a heap stored on a node in the node B-Tree.
*	@see	io.github.jmcleodfoss.pst.BTree
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/77ce49a3-3772-4d8d-bb2c-2f7520a238a6">MS-PST Section 2.3.1: HN (Heap-on-Node)</a>
*/
public class HeapOnNode implements javax.swing.ListModel<Object>
{
	/**	The HID class is describes an index into the heap-on-node structure or a node (this is actually an HNID class).
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/85b9e985-ea53-447f-b70c-eb82bfbdcbc9">MS-PST Section 2.3.1.1: HID</a>
	*/
	static class HID extends NID
	{
		/**	The HID for the root of a B-Tree-on-Heap */
		static final HID BTreeOnHeapRoot = factory(0, 0);

		/**	The size of an HID or HNID element, in bytes. */
		static final int SIZE = 4;

		/**	The block index (index into one of the blocks whose contents make up the heap) of this HID.
		*	(i.e. which block, starting from 0, the block index should be counted into)
		*/
		private final int blockIndex;

		/**	The index to the node data within a block referred to by this HID */
		private final int index;

		/**	Build an HID using the given index and block index.
		*	@param	blockIndex	The block index (starting from 0) containing the HID.
		*	@param	index		The index within the block to the heap entry containing the HID.
		*	@see	#factory
		*	@see	#BTreeOnHeapRoot
		*/
		private HID(int blockIndex, int index)
		{
			super(NID.HID, (index << 16) | (blockIndex << 5));
			this.blockIndex = blockIndex;
			this.index = index + 1;
		}

		/**	Create an HID from the given integer.
		*	@param	rawData	A 32-bit little-endian value as read from the PST file to be translated into an HID.
		*/
		HID(int rawData)
		{
			super(rawData);
			if (type == NID.HID) {
				blockIndex = (short)(rawData >> 16) & 0xffff;
				index = (short)(rawData & 0xffff) >> 5;
			} else {
				index = 0;
				blockIndex = 0;
			}
		}

		/**	Is the given object equivalent to this NHD?
		*	@param	o	The object to check for equivalency with this rHD.
		*	returns	true if the passed object is an rHD and its key is equal to this object's key, false otherwise.
		*/
		public boolean equals(final Object o)
		{
			return o != null && this.getClass().isAssignableFrom(o.getClass()) && key == ((HID)o).key;
		}

		/**	Calculate hashcode.
		*	@return	Hashcode for the node ID.
		*/
		@Override
		public int hashCode()
		{
			return super.hashCode();
		}

		// Perhaps the HNID should be in a separate HNID class.
		/**	Determine whether this object represents an HID or an HNID.
		*	@return	true if this object is an HID, and false if it is an HNID.
		*/
		boolean isHID()
		{
			return type == NID.HID && index != 0;
		}

		/**	Obtain a description of this HID (typically used for debugging).
		*	@return	A description of this HID.
		*/
		@Override
		public String toString()
		{
			StringBuilder s = new StringBuilder();
			s.append ("type ");
			s.append(Integer.toHexString(type));
			s.append(" block index ");
			s.append(Integer.toHexString(blockIndex));
			s.append(" index ");
			s.append(Integer.toHexString(index));
			return s.toString();
		}

		/**	Create an HID from the given block index and index.
		*	@param	blockIndex	The block index (starting from 0) containing the HID.
		*	@param	index		The index within the block to the heap entry containing the HID.
		* 	@return	The HID for the given block index and index.
		*	@see	#BTreeOnHeapRoot
		*/
		private static HID factory(int blockIndex, int index)
		{
			return new HID(blockIndex, index);
		}
	}

	/**	The HNBitmapHeader class occurs the eighth block, and every 128 blocks thereafter (8, 136, 264, etc)
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/822e2327-b29d-4ec4-91be-45637a438d40">MS-PST Section 2.3.1.4: HNBITMAPHDR</a>
	*/
	static class HNBitmapHeader
	{
		/**	If the signature byte is anything other than this, this not a valid heap node */
		private static final byte HN_SIGNATURE = (byte)0xec;

		private static final String nm_ibHnpm = "ibHnpm";
		private static final String nm_rgbFillLevel = "rgb FillLevel";

		/**	The fields comprising the heap node bitmap header */
		private static final DataDefinition[] fields = {
			new DataDefinition(nm_ibHnpm, DataType.integer16Reader, true),
			new DataDefinition(nm_rgbFillLevel, new DataType.SizedByteArray(64))
		};

		/**	The size of the heap node bitmap header fields in bytes */
		private static final int size = DataDefinition.size(fields);

		/**	The offset to the page map. */
		private final int ibHnpm;

		/**	Build a heap-on-node bitmap header object from the input data stream.
		*	@param	stream	The input data stream from which to read the HNBITMAPHDR object.
		* 	@throws	java.io.IOException	There was an I/O error reading the heap node bitmap header.
		*/
		private HNBitmapHeader(java.nio.ByteBuffer stream)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, fields);
			ibHnpm = (Short)dc.get(nm_ibHnpm);
		}
	}

	/**	The Header class represents the header of a heap-on-node structure.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/8e4ae05c-3c24-4103-b7e5-ffef6f244834">MS-PST Section 2.3.1.2: HNHDR</a>
	*/
	static class Header
	{
		/**	If the signature byte is anything other than this, this not a valid heap node */
		private static final byte HN_SIGNATURE = (byte)0xec;

		private static final String nm_ibHnpm = "ibHnpm";
		private static final String nm_bSig = "bSig";
		private static final String nm_bClientSig = "bClientSig";
		private static final String nm_hidUserRoot = "hidUserRoot";
		private static final String nm_rgbFillLevel = "rgb FillLevel";

		/**	The fields comprising the Header. */
		private static final DataDefinition[] fields = {
			new DataDefinition(nm_ibHnpm, DataType.integer16Reader, true),
			new DataDefinition(nm_bSig, DataType.integer8Reader, true),
			new DataDefinition(nm_bClientSig, DataType.integer8Reader, true),
			new DataDefinition(nm_hidUserRoot, DataType.integer32Reader, true),
			new DataDefinition(nm_rgbFillLevel, DataType.integer32Reader, true)
		};

		/**	The size of the header fields in bytes */
		private static final int size = DataDefinition.size(fields);

		/**	The offset to the page map. */
		private final int ibHnpm;

		/**	The heap index of the root of the higher-level structure stored in this heap-on-node. */
		private final HeapOnNode.HID hidUserRoot;

		/**	The client signature of the heap-on-node. */
		private final ClientSignature clientSignature;

		/**	Build a heap-on-node header object from the input data stream.
		*	@param	stream	The data stream from which to read the HNHDR object.
		* 	@throws	NotHeapNodeException	A node which was not a heap node was found while bulding the heap.
		* 	@throws	UnknownClientSignatureException	A node with an unrecognized client signature was found while building the heap.
		* 	@throws java.io.IOException	An I/O error was encountered while trying to build the heap.
		*/
		private Header(java.nio.ByteBuffer stream)
		throws
			NotHeapNodeException,
			UnknownClientSignatureException,
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, fields);
			ibHnpm = 0x0000ffff & (Short)dc.get(nm_ibHnpm);
			byte blockSignature = (Byte)dc.get(nm_bSig);
			if (blockSignature != HN_SIGNATURE && Options.strictHeapNodes)
				throw new NotHeapNodeException(blockSignature);

			clientSignature = new ClientSignature((Byte)dc.get(nm_bClientSig));
			hidUserRoot = new HID((Integer)dc.get(nm_hidUserRoot));
		}

		/**	Obtain a description of the heap-on-node header object (typically used for debugging).
		*	@return	A string describing the Header object.
		*/
		@Override
		public String toString()
		{
			return String.format("%s User Root HID %s ib %d", clientSignature.toString(), hidUserRoot, ibHnpm);
		}
	}

	/**	The PageMap contains the page information found at end of each heap page.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/8e4ae05c-3c24-4103-b7e5-ffef6f244834">MS-PST Section 2.3.1.5: HNHPAGEMAP</a>
	*/
	private static class PageMap
	{
		private static final String nm_cAlloc = "cAlloc";
		private static final String nm_cFree = "cFree";
		private static final String nm_rgibAlloc = "rgibAlloc";

		/**	The data which makes up the Page Map */
		private static final DataDefinition[] fixed_fields = {
			new DataDefinition(nm_cAlloc, DataType.integer16Reader, true),
			new DataDefinition(nm_cFree, DataType.integer16Reader, false)
		};

		/**	The number of entries on this page of the heap data. */
		private final int numEntries;

		/**	The offsets from the beginning of the block to each entry of the heap. */
		private final short[] heapOffset;

		/**	Construct a PageMap object from the input datastream.
		*	@param	stream	The data stream from which to read the HNPAGEMAP object.
		* 	@throws	java.io.IOException	There was an I/O error while reading the data for the page map.
		*/
		private PageMap(java.nio.ByteBuffer stream)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, fixed_fields);
			numEntries = (Short)dc.get(nm_cAlloc);
			DataDefinition dataField = new DataDefinition(nm_rgibAlloc, new DataType.SizedInt16Array((numEntries+1)), true);
			dc.read(stream, dataField);

			heapOffset = (short[])dc.get(nm_rgibAlloc);
		}

		/**	Obtain a description of the PageMap object (typically used for debugging).
		*	@return	A string describing the PageMap object.
		*/
		@Override
		public String toString()
		{
			StringBuilder s = new StringBuilder();
			s.append(heapOffset.length);
			s.append(" entries:");
			for (int i = 0; i < heapOffset.length; ++i)
				s.append(heapOffset[i]);
			return s.toString();
		}
	}

	/**	The PageHeader contains the information found at the beginning of the 2nd-7th, 9th-15th, etc pages making up the heap-on-node structure.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/9c34ecf8-36bc-45a1-a2df-ee35c6dc840a">MS-PST Section 2.3.1.3: HNPAGEHDR</a>
	*/
	private static class PageHeader
	{
		private static final String nm_ibHnpm = "inHnpm";

		/**	The data which makes up the Page Header */
		private static final DataDefinition hdr_field = new DataDefinition(nm_ibHnpm, DataType.integer16Reader, true);

		/**	The size of the data which makes up the Page Header */
		private static final int hdrSize = hdr_field.description.size();

		/**	The offset from the begining of the block to the PageMap. */
		private final int ibHnpm;

		/**	Create a PageHeader object from the input data stream.
		* 	@param	stream	The stream to read the page header from.
		* 	@throws	java.io.IOException	An I/O exception was encountered while reading the data for the page header.
 		*/
		private PageHeader(java.nio.ByteBuffer stream)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, hdr_field);
			ibHnpm = (Short)dc.get(nm_ibHnpm);
		}
	};

	/**	The heap-on-node header for this heap-on-node object. This is used only in toString. */
	private final Header hnhdr;

	/**	The heap data for this heap-on-node object. */
	private final byte[][] heap;

	/**	The offsets into the heap corresponding to each block */
	private final int[] blockOffset;

	/**	Create a heap-on-node for the given block.
	*	@param	entry	The entry from the block B-tree from which to construct the heap-on-node.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	pstFile	The PST file {@link Header}, data stream, etc.
	* 	@throws	NotHeapNodeException	A node which was not a heap node was found while bulding the heap.
	* 	@throws	UnknownClientSignatureException	A node with an unrecognized client signature was found while building the heap.
	* 	@throws java.io.IOException	An I/O error was encountered while trying to build the heap.
	*/
	HeapOnNode(final BBTEntry entry, final BlockMap bbt, PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		java.io.IOException
	{
		final BlockBase blocks = BlockBase.read(entry, bbt, pstFile);
		java.util.Iterator<java.nio.ByteBuffer> blockIterator = blocks.iterator();

		Header hnhdr = null;

		// Get the number of entries in the heap
		int heapLength = 0;
		int iBlock = 0;
		java.util.ArrayList<PageMap> pageMaps = new java.util.ArrayList<PageMap>();
		java.util.ArrayList<java.nio.ByteBuffer> dataStreams = new java.util.ArrayList<java.nio.ByteBuffer>();
		while(blockIterator.hasNext()) {
			java.nio.ByteBuffer blockDataStream = blockIterator.next();
			if (blockDataStream == null)
				continue;
			blockDataStream.mark();

			int offsetData;
			int offsetPageMap;
			if (iBlock == 0) {
				hnhdr = new Header(blockDataStream);
				offsetData = Header.size;
				offsetPageMap = hnhdr.ibHnpm;
			} else if (iBlock - 8 % 128 == 0) {
				final HNBitmapHeader hnbmh = new HNBitmapHeader(blockDataStream);
				offsetData = HNBitmapHeader.size;
				offsetPageMap = hnbmh.ibHnpm;
			} else {
				final PageHeader ph = new PageHeader(blockDataStream);
				offsetData = PageHeader.hdrSize;
				offsetPageMap = ph.ibHnpm;
			}

			// read heap page map
			blockDataStream.position(blockDataStream.position() + offsetPageMap - offsetData);
			PageMap hnpm = new PageMap(blockDataStream);
			pageMaps.add(hnpm);

			// Skip to start of data for the next loop
			blockDataStream.reset();
			blockDataStream.position(blockDataStream.position() + offsetData);
			dataStreams.add(blockDataStream);

			heapLength += hnpm.numEntries;
			++iBlock;
		}

		this.hnhdr = hnhdr;

		heap = new byte[heapLength][];
		blockOffset = new int[heapLength];

		// Read in heap data
		int iHeap = 0;
		for (iBlock = 0; iBlock < dataStreams.size(); ++iBlock) {
			blockOffset[iBlock] = iHeap;
			final PageMap hnpm = pageMaps.get(iBlock);
			for (int i = 0; i < hnpm.numEntries; ++i) {
				int offset = hnpm.heapOffset[i];
				int size = hnpm.heapOffset[i+1] - offset;
				if (size > 0) {
					heap[iHeap] = new byte[size];
					dataStreams.get(iBlock).get(heap[iHeap]);
				}
				++iHeap;
			}
		}
	}

	/**	A convenience wrapper returning the client signature for this heap.
	*	@return	The client signature of this heap-on-node.
	*/
	ClientSignature clientSignature()
	{
		return hnhdr.clientSignature;
	}

	/**	Indicate whether this is a properly-formed HeapOnNode.
	*	@return	true if the hnhdr member is non-null (meaning the heap-on-node was read in correctly), false if it is null.
	*/
	boolean containsData()
	{
		return hnhdr != null;
	}

	/**	Return the data associated with the given HID
	*	@param	hid	The heap ID to retrieve the data from.
	*	@return	The data for the given heap index in this heap-on-node.
	*/
	byte[] heapData(final HID hid)
	{
		return heap[heapIndex(hid)];
	}

	/**	Obtain the index into the entire heap from an HID
	*	@param	hid	The heap ID to retrieve the over-all heap offset of.
	*	@return	The index into the data block list of the entire heap.
	*/
	private int heapIndex(final HID hid)
	{
		return blockOffset[hid.blockIndex] + hid.index - 1;
	}

	/**	Determine this heap-on-node contain a property context.
	*	@return	true if this heap-on-node contains a property context, false otherwise.
	*	@see	#isTableContext
	*	@see	BTreeOnHeap
	*/
	public boolean isPropertyContext()
	{
		return clientSignature().equals(ClientSignature.PropertyContext);
	}

	/**	Determine whether this heap-on-node contain a table context.
	*	@return	true if this heap-on-node contains a table context, false otherwise.
	*	@see	#isPropertyContext
	*	@see	BTreeOnHeap
	*/
	public boolean isTableContext()
	{
		return clientSignature().equals(ClientSignature.TableContext);
	}

	/**	Create the heap-on-node structure for this node to pass to the more general constructor.
	*	@param	nid	The node ID from which to construct the heap-on-node.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree.
	*	@param	pstFile	The PST file's data stream, etc.
	*	@return	The heap-on-node found at the given node ID.
	* 	@throws	NotHeapNodeException	A node which was not a heap node was found while building the heap.
	* 	@throws	UnknownClientSignatureException	An unrecognized client signature was found while building the heap.
	* 	@throws java.io.IOException	An I/O exception was found while reading the data to build the heap.
	*/
	static HeapOnNode makeHeapOnNode(NID nid, BlockMap bbt, NodeBTree nbt, PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		java.io.IOException
	{
		NBTEntry node = nbt.find(nid);
		return makeHeapOnNode(node.bidData, bbt, pstFile);
	}

	/**	Create the heap-on-node structure for the given BID pass to the more general constructor.
	*	@param	bid	The block ID from which to construct the heap-on-node.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	pstFile	The PST file's data stream, etc.
	*	@return	The heap-on-node found at the given block ID.
	*	@throws NotHeapNodeException		A node which was not a heap node was found while trying to build the heap.
	*	@throws UnknownClientSignatureException	An unknown client signature was found in one of the blocks in the heap.
	*	@throws java.io.IOException		There was a problem reading the PST file.
	*/
	public static HeapOnNode makeHeapOnNode(BID bid, BlockMap bbt, PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		java.io.IOException
	{
		BBTEntry dataBlock = bbt.find(bid);
		HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
		if (!hon.containsData())
			return null;
		return hon;
	}

	/**	Obtain a string representation of the heap on this node which includes all heap entries.
	*	@return	A string representing the heap-on-node.
	*/
	@Override
	public String toString()
	{
		if (hnhdr == null)
			return "Empty Heap-on-Node";

		StringBuilder s = new StringBuilder(hnhdr.toString());
		for (int i = 0; i < heap.length; ++i) {
			s.append('\n');
			s.append(i);
			s.append(':');
			s.append(heap[i] != null ? ByteUtil.createHexByteString(heap[i]) : "empty");
		}

		return s.toString();
	}

	/**	Retrieve the heap data corresponding to the header user root HNID.
	*	@return	The heap data for the user root entry in the heap-on-node.
	*/
	byte[] userRootHeapData()
	{
		return heapData(hnhdr.hidUserRoot);
	}

	/**	Does this HID refer to a heap entry which exists in this heap-on-node?
	*	@param	hid	The heap ID to check.
	*	@return	true if the given heap ID is an HID which belongs to this heap-on-node, false otherwise.
	*/
	boolean validHID(HID hid)
	{
		return hid.type == NID.HID && hid.blockIndex < blockOffset.length && hid.index > 0 && blockOffset[hid.blockIndex] + hid.index <= heap.length;
	}

	/**	The list presented by the HeapOnNode object is immutable, so it does not support adding ListDataListeners.
	*	@param	l	The listener (which will not be added).
	*	@see	#removeListDataListener
	*/
	public void addListDataListener(javax.swing.event.ListDataListener l)
	{
	}

	/**	Obtain the list element at the given index.
	*	@param	index	The heap entry to return.
	*	@return	A string containing the sequence of bytez hexidecimal bytes for the given heap node, if it isn't null, or null if the
	*		given heap node is null
	*/
	public Object getElementAt(int index)
	{
		if (heap[index] != null)
			return ByteUtil.createHexByteString(heap[index]);
		return null;
	}

	/**	Obtain the number of list elements.
	*	@return	The number of entries in the heap.
	*/
	public int getSize()
	{
		return heap.length;
	}

	/**	The list presented by the HeapOnNode object is immutable, so it is impossible to add or remove listeners.
	*	@param	l	The listener (which need not be removed because it could never have been added.
	*	@see	#addListDataListener
	*/
	public void removeListDataListener(javax.swing.event.ListDataListener l)
	{
	}

	/**	Test the HeapOnNode class by creating and printing out the first heap-on-node in the given PST file.
	*	@param	args	The file(s) to show the heap-on-nods od.
	*/
	public static void main(String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.HeapOnNode pst-file [pst-file ...]");
			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {
				final PSTFile pstFile = new PSTFile(new java.io.FileInputStream(a));
				final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
				final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

				final OutputSeparator separator = new OutputSeparator();
				java.util.Iterator<BTreeNode> iterator = nbt.iterator();
				while (iterator.hasNext()) {
					final NBTEntry node = (NBTEntry)iterator.next();
					if (!node.nid.isHeapOnNodeNID())
						continue;

					final BBTEntry dataBlock = bbt.find(node.bidData);
					if (dataBlock != null) {
						try {
							separator.emit(System.out);
							System.out.println("Node " + node + "\ndataBlock " + dataBlock);

							final HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
							System.out.println("HeapOnNode\n----------\n" + hon);
						} catch (final NotHeapNodeException e) {
							e.printStackTrace(System.out);
						} catch (final UnknownClientSignatureException e) {
							e.printStackTrace(System.out);
						}
					}
				}
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				e.printStackTrace(System.out);
			}
		}
	}
}
