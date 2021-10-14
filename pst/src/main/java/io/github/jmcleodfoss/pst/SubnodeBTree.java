package io.github.jmcleodfoss.pst;

/**	The SubnodeBTree class represents the B-tree for the sub-nodes of a node B-tree leaf node. */
public class SubnodeBTree extends BTree
{
	/**	The BlockContext class contains information about a block being added to the sub-node B-tree. */
	static class BlockContext extends BTree.Context<BTree, BTreeLeaf>
	{
		private static final String nm_bType = "bType";
		private static final String nm_cLevel = "cLevel";
		private static final String nm_cEnt = "cEnt";
		private static final String nm_dwPadding = "dwPadding";

		/**	The fields in the input stream which contain the sub-node block header common to both ANSI and Unicode files.
		*	@see <a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/5182eb24-4b0b-4816-aa3f-719cc6e6b018">MS-PST Section 2.2.2.8.3.3.1.2: SLBLOCK</a>
		*/
		private static final DataDefinition[] common_fields = {
			new DataDefinition(nm_bType, DataType.integer8Reader, true),
			new DataDefinition(nm_cLevel, DataType.integer8Reader, true),
			new DataDefinition(nm_cEnt, DataType.integer16Reader, true),
		};

		/**	The fields in the input stream which contain the sub-node block header specific to Unicode files. */
		private static final DataDefinition[] unicode_fields = {
			new DataDefinition(nm_dwPadding, new DataType.SizedByteArray(4), true)
		};

		/**	The datastream containing the sub-node B-tree nodes. */
		private java.nio.ByteBuffer snStream;

		/**	Construct a BlockContext object from the information at the given BID in this PST file.
		*	@param	bid	The block ID of the root of the sub-node B-tree.
		*	@param	bbt	The PST file's block B-tree.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
		*	@throws	java.io.IOException	An I/O error was encountered when reading in the data for this node's block context.
		*/
		BlockContext(final BID bid, final BlockMap bbt, PSTFile pstFile)
		throws
			CRCMismatchException,
			java.io.IOException
		{
			super(pstFile);
			final BBTEntry entry = bbt.find(bid);
			assert entry != null : "BID " + bid + " not found!";
			snStream = new SimpleBlock(entry, Encryption.NONE, pstFile).dataStream();

			for (DataDefinition f : common_fields)
				dc.read(snStream, f);
			if (unicode())
				dc.read(snStream, unicode_fields);
		}

		/**	{@inheritDoc} */
		@Override
		protected java.nio.ByteBuffer entryDataStream()
		{
			return snStream;
		}

		/**	{@inheritDoc} */
		@Override
		protected int getEntrySize()
		{
			return isLeafNode() ? SLEntry.size(fileFormat) : SIEntry.size(fileFormat);
		}

		/**	{@inheritDoc} */
		@Override
		protected int getNumEntries()
		{
			return (Short)dc.get(nm_cEnt);
		}

		/**	Create an intermediate sub-node B-tree entry using data read in from the input stream.
		*	@param	entryStream	The data stream from which to read the sub-node B-tree entry.
		*	@return	A sub-node B-tree containing all the children of this sub-node.
		*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
		*/
		@Override
		protected SubnodeBTree intermediateNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			CRCMismatchException,
			java.io.IOException
		{
			final SIEntry entry = new SIEntry(this, entryStream);
			return new SubnodeBTree(entry.nid.key(), this);
		}

		/**	{@inheritDoc} */
		@Override
		protected boolean isLeafNode()
		{
			return dc.getUInt8(nm_cLevel) == 0;
		}

		/**	Create a sub-node B-tree leaf entry using data read in from the input stream.
		*	@param	entryStream	The data stream from which to read the sub-node B-tree entry.
		*	@return	A sub-node B-tree leaf entry.
		*/
		@Override
		protected SLEntry leafNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new SLEntry(this, entryStream);
		}
	}

	/**	The SIEntry is an intermediate node in the sub-node B-tree. */
	private static class SIEntry
	{
		private static final String nm_nid = "NID";
		private static final String nm_nid_padding = "NID-padding";
		private static final String nm_bid = "BID";

		/**	The size of the intermediate sub-node B-tree entry in ANSI files. */
		private static final int SIZE_ANSI = 8;

		/**	The size of the intermediate sub-node B-tree entry in Unicode files. */
		private static final int SIZE_UNICODE = 16;

		/**	The Unicode-specific fields in the input stream which make up the intermediate sub-node B-tree entry. */
		static final DataDefinition[] unicode_fields = {
			new DataDefinition(nm_nid, DataType.nidReader, true),
			new DataDefinition(nm_nid_padding, new DataType.SizedByteArray(4), false),
			new DataDefinition(nm_bid, DataType.bidUnicodeReader, true)
		};

		/**	The ANSI-specific fields in the input stream which make up the intermediate sub-node B-tree entry. */
		static final DataDefinition[] ansi_fields = {
			new DataDefinition(nm_nid, DataType.nidReader, true),
			new DataDefinition(nm_bid, DataType.bidAnsiReader, true)
		};

		/**	The key node ID of the child block of this node. */
		private final NID nid;

		/**	The block ID of the child block of this node. */
		private final BID bid;

		/**	Read an intermediate sub-node B-tree entry from the input stream using the given context.
		*	@param	context	The context in which to read the node.
		*	@param	stream	The data stream from which to read the node information.
		*	@throws	java.io.IOException	An I/O error was encountered when reading in the data for this node.
		*/
		SIEntry(final Context<BTree, BTreeLeaf> context, java.nio.ByteBuffer stream)
		throws
			java.io.IOException
		{
			DataContainer dc = new DataContainer();
			dc.read(stream, context.unicode() ? unicode_fields : ansi_fields);

			nid = (NID)dc.get(nm_nid);
			bid = (BID)dc.get(nm_bid);
		}

		/**	Obtain size of an intermediate sub-node B-tree entry for this file's format.
		*	@param	fileFormat	The PST's file format object.
		*	@return	The size of the intermediate sub-node entry in this file/
		*/
		static int size(FileFormat fileFormat)
		{
			return fileFormat.fUnicode ? SIZE_UNICODE : SIZE_ANSI;
		}

		/**	Obtain the B-tree search key for this node. All child nodes will have keys equal to or greater than this.
		*	@return	The key to this node.
		*/
		public long key()
		{
			return nid.key();
		}

		/**	Create a description of this intermediate sub-node B-tree entry.
		*	@return	A description of the intermediate sub-node B-tree entry.
		*/
		@Override
		public String toString()
		{
			return "NID " + nid + ", BID " + bid;
		}
	}

	/**	Create a SubnodeBTree object from the given position and context.
	*	@param	key	The key for this node in the sub-node B-tree.
	*	@param	context	The context to use when building the sub-node B-tree.
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	java.io.IOException	An I/O error was encountered when reading in the data for this sub-node B-tree.
	*/
	private SubnodeBTree(final long key, final BlockContext context)
	throws
		CRCMismatchException,
		java.io.IOException
	{
		super(key, context);
	}

	/**	Create a SubnodeBTree object from the given position and PST file.
	*	@param	bid	The block ID of the root of this sub-node B-tree.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	pstFile	The PST file's {@link Header}, data stream, etc.
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	* 	@throws java.io.IOException			There was an I/O error reading the sub-node B-tree.
	*/
	public SubnodeBTree(final BID bid, final BlockMap bbt, PSTFile pstFile)
	throws
		CRCMismatchException,
		java.io.IOException
	{
		this(0, new BlockContext(bid, bbt, pstFile));
	}

	/**	Return the actual size of an intermediate sub-node B-tree entry as read in from the input datastream.
	*	@param	context	The context in which the sub-node B-tree is being build.
	*/
	public int actualSize(final Context<BTree, BTreeLeaf> context)
	{
		return SIEntry.size(context.fileFormat);
	}

	/**	{@inheritDoc} */
	public String getNodeText()
	{
		return String.format("Node 0x%08x", key);
	}

	/**	Test the SubnodeBTree class by reading in traversing the PST file and displaying all the sub-node B-tree leaves.
	*	@param	args	The file(s) to display the sub-node B-Tree for.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(final String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.NodeBTree pst-file [pst-file ...]");
			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {

				java.io.FileInputStream stream = new java.io.FileInputStream(a);
				final PSTFile pstFile = new PSTFile(stream);
				try {
					final OutputSeparator separator = new OutputSeparator();

					final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
					final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

					java.util.Iterator<BTreeNode> iterator = nbt.iterator();
					while (iterator.hasNext()) {
						final NBTEntry entry = (NBTEntry)iterator.next();
						if (entry.bidSubnode.bid != 0) {
							separator.emit(System.out);

							System.out.printf("Subnode BTree for %s%n", entry.toString());
							final SubnodeBTree sbt = new SubnodeBTree(entry.bidSubnode, bbt, pstFile);
							System.out.println(sbt);
							java.util.Iterator<BTreeNode> sbtIterator = sbt.iterator();

							int i = 0;
							while (sbtIterator.hasNext()) {
								++i;
								final SLEntry sbtEntry = (SLEntry)sbtIterator.next();
								final BBTEntry bbtEntry = bbt.find(sbtEntry.bidData);
								if (bbtEntry != null) {
									System.out.printf("%d: %s; %s%n", i, sbtEntry.toString(), bbtEntry.toString());
									final SimpleBlock b = new SimpleBlock(bbtEntry, pstFile);
									System.out.printf("block: %s%n", b.toString());
								} else {
									System.out.printf("%d: %s; no block B-tree entry%n", i, sbtEntry.toString());
								}
							}
						}
					}
				} catch (final java.io.IOException e) {
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
				e.printStackTrace(System.out);
			}
		}
	}
}
