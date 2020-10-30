package io.github.jmcleodfoss.pst;

/**	The PST class is a convenience collection of entities describing a PST file. */
public class PST extends PSTFile
{
	/**	The block B-tree in this PST file. */
	public final BlockMap blockBTree;

	/**	The node B-tree in this PST file. */
	public final NodeMap nodeBTree;

	/**	The named properties */
	public final NameToIDMap namedProperties;

	/**	The PST MessageStore */
	public final MessageStore messageStore;

	/**	Create a "large footprint" PST object from the given filename.
	*	@param	fn		The file name of the PST file to read.
	*	@throws	CRCMismatchException			The header's calculated CRC does not match the expected value.
	*	@throws	NotHeapNodeException			An invalid or corrupt heap node was found.
	*	@throws NotPSTFileException			The file is not a PST file.
	*	@throws	NotPropertyContextNodeException		A node without the Property Context client signature was found while building a property context.
	*	@throws	NotTableContextNodeException		A node in this folder's B-tree does not contain a table context when it was expected to.
	*	@throws NullDataBlockException			A null data block was found while building a property context.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		The client signature of a node was not recognized.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	A property context block could not be read.
	*	@throws UnparseableTableContextException	A table context block could not be read.
	*	@throws java.io.IOException			There was an I/O error reading the file.
	*/
	public PST(final String fn)
	throws
		CRCMismatchException,
		NotHeapNodeException,
		NotPSTFileException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		this(fn, false);
	}

	/**	Create a PST object from the given filename.
	*	@param	fn		The file name of the PST file to read.
	*	@param	fSmallFootprint	If fSmallFootprint is true, the block and node B-trees are held in {@link BlockFinder} and
	*				{@link NodeFinder} objects, respectively, and the underlying PST file is re-read each time a
	*				node or blocksearch takes place. If it is false, the block and node B-trees are read in all-at-
	*				once in the constructore and held in {@link BlockBTree} and {@link NodeBTree} objects, respectively.
	*	@throws	CRCMismatchException			The header's calculated CRC does not match the expected value.
	*	@throws	NotHeapNodeException			An invalid or corrupt heap node was found.
	*	@throws NotPSTFileException			The file is not a PST file.
	*	@throws	NotPropertyContextNodeException		A node without the Property Context client signature was found while building a property context.
	*	@throws	NotTableContextNodeException		A node in this folder's B-tree does not contain a table context when it was expected to.
	*	@throws NullDataBlockException			A null data block was found while building a property context.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		The client signature of a node was not recognized.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	A property context block could not be read.
	*	@throws UnparseableTableContextException	A table context block could not be read.
	*	@throws java.io.IOException			There was an I/O error reading the file.
	*/
	public PST(final String fn, boolean fSmallFootprint)
	throws
		CRCMismatchException,
		NotHeapNodeException,
		NotPSTFileException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		this(new java.io.FileInputStream(fn), fSmallFootprint);
	}

	/**	Create a PST object from the given FileInputStream.
	*	@param	fis		The FileInputStream of the PST file to read.
	*	@param	fSmallFootprint	If fSmallFootprint is true, the block and node B-trees are held in {@link BlockFinder} and
	*				{@link NodeFinder} objects, respectively, and the underlying PST file is re-read each time a
	*				node or blocksearch takes place. If it is false, the block and node B-trees are read in all-at-
	*				once in the constructore and held in {@link BlockBTree} and {@link NodeBTree} objects, respectively.
	*	@throws	CRCMismatchException			The header's calculated CRC does not match the expected value.
	*	@throws	NotHeapNodeException			An invalid or corrupt heap node was found.
	*	@throws NotPSTFileException			The file is not a PST file.
	*	@throws	NotPropertyContextNodeException		A node without the Property Context client signature was found while building a property context.
	*	@throws	NotTableContextNodeException		A node in this folder's B-tree does not contain a table context when it was expected to.
	*	@throws	NullDataBlockException			A null data block was found while building a property context.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		The client signature of a node was not recognized.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	*	@throws UnparseablePropertyContextException	A property context block could not be read.
	*	@throws UnparseableTableContextException	A table context block could not be read.
	*	@throws java.io.IOException			There was an I/O error reading the file.
	*/
	public PST(final java.io.FileInputStream fis, boolean fSmallFootprint)
	throws
		CRCMismatchException,
		NotHeapNodeException,
		NotPSTFileException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		super(fis);

		blockBTree = fSmallFootprint ? new BlockFinder((PSTFile)this) : new BlockBTree(0, header.bbtRoot, (PSTFile)this);
		nodeBTree = fSmallFootprint ? new NodeFinder((PSTFile)this) : new NodeBTree(0, header.nbtRoot, (PSTFile)this);

		namedProperties = new NameToIDMap(blockBTree, nodeBTree, this);

		Appointment.initConstants(namedProperties, unicode());
		Contact.initConstants(namedProperties, unicode());
		DistributionList.initConstants(namedProperties, unicode());
		Task.initConstants(namedProperties);

		messageStore = new MessageStore(blockBTree, nodeBTree, this);
	}

	/**	Check whether the given password matches the stored password.
	*	@param	testPassword	The password to check.
	*	@param	charset		The charset Charset used to encode the string
	*	@return	true if the passed password matches the password in the PST file, false otherwise.
	*	@throws	java.io.UnsupportedEncodingException	The given charset is not supported / recognized
	*	@see	#hasPassword
	*/
	public boolean checkPassword(final String testPassword, java.nio.charset.Charset charset)
	throws
		java.io.UnsupportedEncodingException
	{
		return messageStore.checkPassword(testPassword, charset);
	}

	/**	Get a folder and its first set of descendent sub-folders.
	*	@param	nodeFolder	The description of the folder node.
	*	@return	A folder and its child folders and contents.
	*/
	@Deprecated
	public Folder getFolder(NBTEntry nodeFolder)
	{
		try {
			return Folder.getFolder(nodeFolder, blockBTree, nodeBTree, (PSTFile)this);
		} catch (final Exception e) {
			return null;
		}
	}

	/**	Get a folder and all sub-folders and contents.
	*	@return	The root folder and all its immediate descendents.
	*/
	public Folder getFolderTree()
	{
		return messageStore.rootFolder;
	}

	/**	Determine whether this PST file requires a password.
	*	@return	true if the PST file is password-protected, false if it is not password-protected.
	*	@see	#checkPassword
	*/
	public boolean hasPassword()
	{
		return messageStore.hasPassword();
	}

	/**	Convenience function to retrieve the heap-on-node for the given BID.
	*	@param	bid	The block ID to the heap-on-node of.
	*	@return	The heap-on-node for the given block ID.
	*/
	public HeapOnNode heapOnNode(BID bid)
	{
		try {
			return HeapOnNode.makeHeapOnNode(bid, blockBTree, this);
		} catch (final java.io.IOException e) {
		} catch (final CRCMismatchException e) {
		} catch (final NotHeapNodeException e) {
		} catch (final UnknownClientSignatureException e) {
		}

		return null;
	}

	/**	Convenience function to retrieve a table model for the PST file's message store.
	*	@return	A table model for the {@link #messageStore} memeber.
	*/
	public javax.swing.table.TableModel messageStoreTableModel()
	{
		return messageStore.tableModel(namedProperties);
	}

	/**	Convenience function to retrieve a table model for the PST file's named properties.
	*	@return	A table model for the {@link #namedProperties named properties} of the PST file.
	*	@see	io.github.jmcleodfoss.pst.NameToIDMap
	*/
	public javax.swing.table.TableModel namedPropertiesTableModel()
	{
		return namedProperties.tableModel();
	}

	/**	Convenience function to retrieve an iterator through the named properties.
	*	@return	An interator through the {@link #namedProperties named properties} of the PST file.
	*	@see	io.github.jmcleodfoss.pst.NameToIDMap
	*/
	public java.util.Iterator<java.util.Map.Entry<Integer, String>> namedPropertiesIterator()
	{
		return namedProperties.iterator();
	}

	/**	Convenience function to retrieve the root of the node B-tree.
	*	@return	The root of the node / sub-node B-tree
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws java.io.IOException	There was a problem reading the sub-node B-tree.
	*/
	public NodeSubnodeBTree nodeBTreeRoot()
	throws
		CRCMismatchException,
		java.io.IOException
	{
		return new NodeSubnodeBTree(0, header.nbtRoot, blockBTree, this);
	}

	/**	Convenience function to retrieve an iterator for the node B-tree.
	*	@return	An iterator over the leaves of the node B-tree.
	*	@see	NodeBTree
	*/
	java.util.Iterator<BTreeNode> nodeIterator()
	{
		return ((NodeBTree)nodeBTree).iterator();
	}

	/**	Convenience function to obtain the property context from the given NID
	*	@param	nid	The node ID to retrieve the property context iterator from.
	*	@return	An iterator through the properties in the property context for the node identified by nid.
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws NotHeapNodeException			The NID does not point to a node on the heap.
	* 	@throws	NotPropertyContextNodeException		A node without the Property Context client signature was found while building a property context.
	* 	@throws	NullDataBlockException			A null data block was found while building a property context.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unknown client signature was found while building the property context.
	*	@throws UnknownPropertyTypeException	The property type was not recognized
	* 	@throws UnparseablePropertyContextException	The property context for the node is bad
	*	@throws java.io.IOException			There was a problem reading the property context.
	*	@see	PropertyContext
	*/
	public java.util.Iterator<java.util.Map.Entry<Integer, Object>> pcPropertyIterator(final int nid)
	throws
		CRCMismatchException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NullDataBlockException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		final NBTEntry nbtEntry = nodeBTree.find(new NID(nid));
		return new PropertyContext(nbtEntry, blockBTree, this).iterator();
	}

	/**	Convenience function to obtain a table model for the property context for the given NID.
	*	@param	node	The node or sub-node B-tree leaf from which to retrieve the property context.
	*	@return	A table model containing the property tags, tag names, and values for the property context.
	*	@see	PropertyContext
	*	@see	LPTTableModel PropertyContext.tableModel(final NameToIDMap)
	*	@see	#tableModel
	*/
	public javax.swing.table.TableModel pcTableModel(LPTLeaf node)
	{
		PropertyContext pc = propertyContext(node);
		return pc == null ? new javax.swing.table.DefaultTableModel() : tableModel(pc);
	}

	/**	Convenience function to obtain the property context for the given node.
	*	@param	node	The node or sub-node B-tree leaf from which to retrieve the property context.
	*	@return	The property context from the given node.
	*	@see	#propertyContext(NID)
	*/
	public PropertyContext propertyContext(LPTLeaf node)
	{
		try {
			return new PropertyContext(node, blockBTree, this);
		} catch (final CRCMismatchException e) {
		} catch (final NotHeapNodeException e) {
		} catch (final NotPropertyContextNodeException e) {
		} catch (final NullDataBlockException e) {
		} catch (final UnimplementedPropertyTypeException e) {
		} catch (final UnknownPropertyTypeException e) {
		} catch (final UnknownClientSignatureException e) {
		} catch (final UnparseablePropertyContextException e) {
		} catch (final java.io.IOException e) {
		}

		return null;
	}

	/**	Convenience function to obtain the property context for the given node in the node B-tree.
	*	@param	nid	A node ID in the node B-tree.
	*	@return	The property context from the given node.
	* 	@throws	java.io.IOException	There was a problem reading the property context table in this node
	*	@see	#propertyContext(LPTLeaf)
	*/
	public PropertyContext propertyContext(NID nid)
	throws
		java.io.IOException
	{
		return propertyContext(nodeBTree.find(nid));
	}

	/**	Retrieve the name of the given tag.
	*	@param	tag	The tag for which to retrieve the name.
	*	@return	The name of the property of this tag.
	*/
	public String propertyName(int tag)
	{
		return namedProperties.name(tag);
	}

	/**	Convenience function to obtain sub-node B-Tree for the given node ID.
	*	@param	nid	The node ID in the node B-tree to construct the sub-node B-tree for.
	*	@return	The sub-node B-tree for the given node, if any, otherwise null.
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*/
	public SubnodeBTree subnodeBTree(NID nid)
	throws
		CRCMismatchException
	{
		try {
			NBTEntry nbtEntry = nodeBTree.find(nid);
			if (nbtEntry != null && nbtEntry.bidSubnode != null)
				return new SubnodeBTree(nbtEntry.bidSubnode, blockBTree, this);
		} catch (final java.io.IOException e) {
		}

		return null;
	}

	/**	Convenience function to obtain a javax.swing.table.TableModel for a PropertyContext object.
	*	@param	pc	The property context to create a table model for.
	*	@return	A javax.swing.table.TableModel object containing the property context's tags, tag names, and values.
	*/
	public javax.swing.table.TableModel tableModel(PropertyContext pc)
	{
		return pc.tableModel(namedProperties);
	}

	/**	Obtain a javax.swing.table.TableModel for the table context for the given node and heap-on-node. This function
	*	calls the {@link io.github.jmcleodfoss.pst.TableContext#TableContext(LPTLeaf, HeapOnNode, BlockMap, PSTFile) TableContext constructor} which
	*	requires the heap-on-node to have already been created and is intended  for use in situations where the heap-on-node
	*	has already been built; the alternative {@link #tcTableModel(LPTLeaf)} calls the
	*	{@link io.github.jmcleodfoss.pst.TableContext#TableContext(LPTLeaf, BlockMap, PSTFile) TableContext constructor} constructor which builds the
	*	heap-on-node itself.
	*	@param	node	A node from the node B-tree or a sub-node B-tree containing the TableContext.
	*	@param	hon	The heap-on-node containing the table context.
	*	@return	A javax.swing.table.TableModel representation of the table context.
	*	@see	io.github.jmcleodfoss.pst.TableContext
	*	@see	#tcTableModel(LPTLeaf)
	*	@see	io.github.jmcleodfoss.pst.TableContext#TableContext(LPTLeaf, HeapOnNode, BlockMap, PSTFile)
	*/
	public javax.swing.table.TableModel tcTableModel(LPTLeaf node, HeapOnNode hon)
	{
		try {
			return new TableContext(node, hon, blockBTree, this);
		} catch (final CRCMismatchException e) {
		} catch (final NotTableContextNodeException e) {
		} catch (final UnimplementedPropertyTypeException e) {
		} catch (final UnknownClientSignatureException e) {
		} catch (final UnknownPropertyTypeException e) {
		} catch (final UnparseableTableContextException e) {
		} catch (final java.io.IOException e) {
		}

		return new javax.swing.table.DefaultTableModel();
	}

	/**	Obtain a javax.swing.table.TableModel for the table context for the given node. This function calls the
	*	{@link io.github.jmcleodfoss.pst.TableContext#TableContext(LPTLeaf, BlockMap, PSTFile) TableContext constructor} constructor which builds the
	*	heap-on-node itself; if the heap-on-node for the node has already been created, the alternative function
	*	{@link #tcTableModel(LPTLeaf, HeapOnNode)} should be used instead.
	*	@param	node	A node from the node B-tree or a sub-node B-tree containing the TableContext.
	*	@return	A javax.swing.table.TableModel representation of the table context.
	*	@see	io.github.jmcleodfoss.pst.TableContext
	*	@see	#tcTableModel(LPTLeaf, HeapOnNode)
	*	@see	io.github.jmcleodfoss.pst.TableContext#TableContext(LPTLeaf, BlockMap, PSTFile)
	*/
	public javax.swing.table.TableModel tcTableModel(LPTLeaf node)
	{
		try {
			return new TableContext(node, blockBTree, this);
		} catch (final CRCMismatchException e) {
		} catch (final NotHeapNodeException e) {
		} catch (final NotTableContextNodeException e) {
		} catch (final UnimplementedPropertyTypeException e) {
		} catch (final UnknownClientSignatureException e) {
		} catch (final UnknownPropertyTypeException e) {
		} catch (final UnparseableTableContextException e) {
		} catch (final java.io.IOException e) {
		}

		return new javax.swing.table.DefaultTableModel();
	}
}
