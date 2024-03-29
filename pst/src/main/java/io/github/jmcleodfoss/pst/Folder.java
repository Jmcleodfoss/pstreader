package io.github.jmcleodfoss.pst;

/**	The Folder class represents a folder within a PST file.
*	Note that sub-folders are loaded as required, rather than on initial construction of the Folder object for the folder root.
*/
public class Folder extends ReadOnlyTreeModel implements TreeCustomNodeText, javax.swing.tree.TreeModel
{
	/**	The folder object node info. */
	public final NBTEntry nodeFolderObject;

	/**	The hierarchy table node info. */
	public final NBTEntry nodeHierarchyTable;

	/**	The contents table node info. */
	public final NBTEntry nodeContentsTable;

	/**	The associated contents table node info. */
	public final NBTEntry nodeAssociatedContentsTable;

	/**	The folder name. */
	public final String displayName;

	/**	The type of folder. */
	public final String containerClass;

	/**	The sub-folders of the folder. */
	private java.util.List<Folder> subfolders;

	/**	The content objects. */
	private java.util.List<MessageObject> contents;

	/**	Ths SubfolderLevelsToRead class tells how many sub-levels to read when processing a folder: none, one, or all. */
	private static class SubfolderLevelsToRead
	{
		/**	The SubFolderLevelsToReadEnum contains the possible choices for reading sub-folders. */
		private static enum Levels
		{
			/**	Read no sub-folders. */
			NONE,

			/**	Read a single level of sub-folders. */
			ONE,

			/**	Read the entire sub-folder hierarchy. */
			ALL
		};

		/**	The number of sub-levels to read. */
		private Levels levels;

		/**	Construct an object describing how many levels of sub-folders to read.
		*	@param	levels	The number of sub-folder levels to read.
		*/
		private SubfolderLevelsToRead(Levels levels)
		{
			this.levels = levels;
		}

		/**	The dec function reduces the value of the current number of sub-folders to read, if appropriate. */
		private void decrement()
		{
			if (levels == Levels.ONE)
				levels = Levels.NONE;
		}

		/**	The readSubfolders function indicates whether the sub-folders of this folder should be read.
		*	@return	true if the sub-folders should be read, false if they should not be.
		*/
		boolean readSubfolders()
		{
			return levels != Levels.NONE;
		}
	}

	/**	Create a folder object for the given Node B-Tree leaf node.
	*	@param	nodeFolderObject	The entry in the PST file's node B-tree for this folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file input stream, etc.
	*	@param	levelsToRead		The number of sub-levels to read.
	*	@param	fReadContents		A flag indicating whether the folder contents should be read in.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws	NotHeapNodeException			node which is not a heap node was found while reading the folder data.
	*	@throws NotPropertyContextNodeException		A node which does not hold a property context was found where a property context node was expected.
	*	@throws NotTableContextNodeException		A node which does not hold a table context was found where a table context node was expected.
	*	@throws	NullDataBlockException			A null data block was found while reading the folder data.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException 	An unknown client signature was found while reading the folder data.
	*	@throws UnknownPropertyTypeException		The property type was not recognized
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context was found while reading the folder data.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was found while reading the folder data.
	*	@throws java.io.IOException			An I/O exception was encountered while reading the folder data.
	*/
	private Folder(NBTEntry nodeFolderObject, BlockMap bbt, NodeMap nbt, PSTFile pstFile, SubfolderLevelsToRead levelsToRead, boolean fReadContents)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		this.nodeFolderObject = nodeFolderObject;
		PropertyContext folderObject = new PropertyContext(nodeFolderObject, bbt, pstFile);

		displayName = (String)folderObject.get(pstFile.unicode() ? PropertyTags.DisplayNameW : PropertyTags.DisplayName);
		containerClass = (String)folderObject.get(pstFile.unicode() ? PropertyTags.ContainerClassW : PropertyTags.ContainerClass);

		Object hasSubfolders = folderObject.get(PropertyTags.Subfolders);
		if (hasSubfolders != null && (Boolean)hasSubfolders && levelsToRead.readSubfolders()) {
			NID nidHierarchyTable = new NID(nodeFolderObject.nid, NID.HIERARCHY_TABLE);
			nodeHierarchyTable = nbt.find(nidHierarchyTable);
			this.subfolders = readSubfolders(nodeHierarchyTable, bbt, nbt, pstFile, levelsToRead, fReadContents);
		} else {
			nodeHierarchyTable = null;
			this.subfolders = new java.util.ArrayList<Folder>();
		}

		Object contentCount = folderObject.get(PropertyTags.ContentCount);
		if (contentCount != null && (Integer)contentCount > 0 && fReadContents) {
			NID nidContentsTable = new NID(nodeFolderObject.nid, NID.CONTENTS_TABLE);
			nodeContentsTable = nbt.find(nidContentsTable);
			if (nodeContentsTable != null)
				contents = readContents(nodeContentsTable, bbt, nbt, pstFile);
			else
				contents = new java.util.ArrayList<MessageObject>();
		} else {
			nodeContentsTable = null;
			contents = new java.util.ArrayList<MessageObject>();
		}

		NID nidAssociatedContentsTable = new NID(nodeFolderObject.nid, NID.ASSOC_CONTENTS_TABLE);
		nodeAssociatedContentsTable = nbt.find(nidAssociatedContentsTable);
	}

	/**	Get an iterator through this folder's message objects.
	*	@return	An iterator through the contents.
	*/
	public java.util.Iterator<MessageObject> contentsIterator()
	{
		return contents.iterator();
	}

	/**	Get the requested child of this parent.
	*	@param	oParent	The tree node from which to retrieve the child.
	*	@param	index	The child to retrieve.
	*	@return	The given child of the tree parent node.
	*/
	@Override
	public Object getChild(Object oParent, int index)
	{
		if (oParent instanceof Message)
			return ((Message)oParent).attachment(index);

		Folder folder = (Folder)oParent;
		if (index < folder.subfolders.size())
			return folder.subfolders.get(index);

		int indexItem = index - folder.subfolders.size();
		if (indexItem < folder.contents.size())
			return folder.contents.get(indexItem);

		assert false: "getChild for " + oParent + " index " + index + " not found";
		return null;
	}

	/**	Get the number of children of this parent.
	*	@param	oParent	The parent tree node to retrieve the number of children of.
	*	@return	The number of children (sub-folders, message objects, or attachments) of the parent.
	*/
	@Override
	public int getChildCount(Object oParent)
	{
		if (oParent instanceof Folder)
			return ((Folder)oParent).subfolders.size() + ((Folder)oParent).contents.size();

		return ((Message)oParent).numAttachments();
	}

	/**	Factory method to get a folder and the first level of sub-folders, but no folder contents.
	*	@param	nodeFolderObject	The entry in the PST file's node B-tree for this folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file input stream, etc.
	*	@return	A folder and its immediate descendents.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws NotHeapNodeException			A node which was not a heap node was found when reading in the folders.
	*	@throws	NotPropertyContextNodeException		A node without the Property Context client signature was found when building a property context.
	*	@throws	NotTableContextNodeException		A node without the Table Context client signature was found when building a table context.
	*	@throws	NullDataBlockException			A null data block was found when building a property context.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unrecognized client signature was encountered.
	*	@throws UnknownPropertyTypeException		The property type was not recognized
	*	@throws UnparseablePropertyContextException	The property context could not be read.
	*	@throws UnparseableTableContextException	The table context could not be read.
	*	@throws java.io.IOException			There was a problem reading the PST file.
	*/
	public static Folder getFolder(NBTEntry nodeFolderObject, BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return new Folder(nodeFolderObject, bbt, nbt, pstFile, new SubfolderLevelsToRead(SubfolderLevelsToRead.Levels.ONE), false);
	}

	/**	Factory method to get a folder and all sub-folders and contents.
	*	@param	nodeFolderObject	The entry in the PST file's node B-tree for this folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file input stream, etc.
	*	@return	A folder and all its immediate descendents.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws NotHeapNodeException			A node which was not a heap node was found when reading in the folder tree.
	*	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found when building a property context.
	*	@throws NotTableContextNodeException		A node without the Table Context client signature was found when building a table context.
	*	@throws NullDataBlockException			A null data block was found when building a property context.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unrecognized client signature was encountered.
	*	@throws UnknownPropertyTypeException		The property type was not recognized
	*	@throws UnparseablePropertyContextException	The property context could not be read.
	*	@throws UnparseableTableContextException	The table context could not be read.
	*	@throws java.io.IOException			There was a problem reading the PST file.
	*/
	public static Folder getFolderTree(NBTEntry nodeFolderObject, BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return new Folder(nodeFolderObject, bbt, nbt, pstFile, new SubfolderLevelsToRead(SubfolderLevelsToRead.Levels.ALL), true);
	}

	/**	Get the index of the given child object of the parent tree node.
	*	@param	oParent	The tree node to look for the child in.
	*	@param	child	The child to look for.
	*	@return	The index of given child of the tree parent node, or -1 if it is not a child of this parent node.
	*/
	@Override
	public int getIndexOfChild(Object oParent, Object child)
	{
		if (oParent instanceof Folder) {
			Folder folder = (Folder)oParent;

			for (int i = 0; i < folder.subfolders.size(); ++i) {
				if (folder.subfolders.get(i) == child)
					return i;
			}

			for (int i = 0; i < folder.contents.size(); ++i) {
				if (folder.contents.get(i) == child)
					return i + folder.subfolders.size();
			}
		}

		if (oParent instanceof Message) {
			Message message = (Message)oParent;

			for (int i = 0; i < message.numAttachments(); ++i) {
				if (message.attachment(i) == child)
					return i;
			}
		}

		return -1;
	}

	/**	{@inheritDoc} */
	@Override
	public String getNodeText(Object o)
	{
		if (o instanceof Appointment)
			return ((Appointment)o).subject;

		if (o instanceof Attachment)
			return ((Attachment)o).name;

		if (o instanceof Folder)
			return ((Folder)o).displayName;

		if (o instanceof PersonMetadata)
			return ((PersonMetadata)o).internetMessageId;

		if (o instanceof StickyNote)
			return ((StickyNote)o).subject;

		if (o instanceof MessageObject)
			return ((MessageObject)o).subject;

		return o.toString();
	}

	/**	Obtain the root of the tree.
	*	@return	The root of the (Swing) tree (model).
	*/
	@Override
	public Object getRoot()
	{
		return this;
	}

	/**	Indicate whether the given object is an intermediate node or a leaf node.
	*	@param	oNode	The object to check for leafiness.
	*	@return	true if the oNode is a leaf node, false if it is an intermediate node.
	*/
	@Override
	public boolean isLeaf(Object oNode)
	{
		if (oNode instanceof Message)
			return ((Message)oNode).numAttachments() == 0;

		if (oNode instanceof Folder)
			return ((Folder)oNode).subfolders.size() == 0 && ((Folder)oNode).contents.size() == 0;

		return true;
	}

	/**	Read in the contents of the given folder.
	*	@param	nodeContentsTable	The description of the folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file's incoming data stream, header, etc.
	*	@return	A vector of the message contents.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws NotHeapNodeException			A node which was not a heap node was found when reading in the sub-folders.
	*	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found when building a property context.
	*	@throws NotTableContextNodeException		A node without the Table Context client signature was found when building a table context.
	*	@throws NullDataBlockException			A null data block was found when building a property context.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws UnknownClientSignatureException		An unrecognized client signature was encountered.
	*	@throws UnknownPropertyTypeException		The property type was not recognized
	*	@throws UnparseablePropertyContextException	The property context could not be read.
	*	@throws UnparseableTableContextException	The table context could not be read.
	*	@throws java.io.IOException			There was a problem reading the PST file.
	*/
	public static java.util.List<MessageObject> readContents(final NBTEntry nodeContentsTable, final BlockMap bbt, final NodeMap nbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		TableContext contentsTable = new TableContext(nodeContentsTable, bbt, pstFile);

		java.util.ArrayList<MessageObject> contents = new java.util.ArrayList<MessageObject>(contentsTable.getRowCount());
		for (int row = 0; row < contentsTable.getRowCount(); ++row)
			contents.add(MessageObject.factory(contentsTable, row, bbt, nbt, pstFile));
		return contents;
	}

	/**	Read in the sub-folders of the given folder.
	*	@param	nodeHierarchyTable	The description of the folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file's incoming data stream, header, etc.
	*	@param	levelsToRead		The number of sub-levels to read.
	*	@param	fReadContents		A flag indicating whether the folder contents should be read in.
	*	@return	A vector of subfolders.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws NotHeapNodeException			A node which was not a heap node was found when reading in the sub-folders.
	*	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found when building a property context.
	*	@throws NotTableContextNodeException		A node without the Table Context client signature was found when building a table context.
	*	@throws NullDataBlockException			A null data block was found when building a property context.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		An unrecognized client signature was encountered.
	*	@throws UnknownPropertyTypeException		The property type was not recognized
	*	@throws UnparseablePropertyContextException	The property context could not be read.
	*	@throws UnparseableTableContextException	The table context could not be read.
	*	@throws java.io.IOException			There was a problem reading the PST file.
	*/
	public static java.util.List<Folder> readSubfolders(final NBTEntry nodeHierarchyTable, final BlockMap bbt, final NodeMap nbt, PSTFile pstFile, SubfolderLevelsToRead levelsToRead, boolean fReadContents)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		DataOverflowException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NotTableContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		TableContext hierarchyTable = new TableContext(nodeHierarchyTable, bbt, pstFile);

		java.util.ArrayList<Folder> subfolders = new java.util.ArrayList<Folder>(hierarchyTable.getRowCount());
		levelsToRead.decrement();
		for (int row = 0; row < hierarchyTable.getRowCount(); ++row) {
			int nidSubfolder = (Integer)hierarchyTable.get(row, PropertyTags.LtpRowId);
			NBTEntry nodeSubfolder = nbt.find(new NID(nidSubfolder));
			subfolders.add(new Folder(nodeSubfolder, bbt, nbt, pstFile, levelsToRead, fReadContents));
		}
		return subfolders;
	}

	/**	Show this message object, and sub-folders and content objects, in an ASCII representation of a tree. Note that this
	*	is called recursively, typically as:
	*	<pre>
	*	{@code
	*	// Given an already constructed Folder object folder:
	*	folder.show("");
	*	}
	*	</pre>
	*	@param	prefix	The text with which to prefix the lines for the current tree.
	*	@throws	NotHeapNodeException			node which is not a heap node was found while reading a sub-folder's data.
	*	@throws UnknownClientSignatureException		An unknown client signature was found while reading a sub-folder's data.
	*	@throws UnparseablePropertyContextException	A bad / corrupt property context was found while reading a sub-folder's data.
	*	@throws	UnparseableTableContextException	A bad / corrupt table context was found while reading a sub-folder's data.
	*	@throws java.io.IOException			An I/O exception was encountered while reading a sub-folder's data.
	*/
	private void show(String prefix)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		System.out.printf("%s%s", prefix, displayName);
		for (int i = 0; i < subfolders.size(); ++i) {
			Folder f = subfolders.get(i);
			f.show("|" + prefix + "-");
		}

		for (int i = 0; i < contents.size(); ++i) {
			MessageObject m = contents.get(i);
			System.out.println(m);
		}
	}

	/**	Get an iterator through this folder's sub-folders.
	*	@return	An iterator through this folder's sub-folders.
	*/
	public java.util.Iterator<Folder> subfolderIterator()
	{
		return subfolders.iterator();
	}

	/**	Test the Folder class by iterating through the folders and displaying information about each folder and sub-folder.
	*	@param	args	The file(s) to display the folders of.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.Folder pst-file [pst-file ...]");
			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {
				java.io.FileInputStream stream = new java.io.FileInputStream(a);
				final PSTFile pstFile = new PSTFile(stream);
				try {
					final BlockBTree blockBTree = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
					final NodeBTree nodeBTree = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);
					final MessageStore messageStore = new MessageStore(blockBTree, nodeBTree, pstFile);
					messageStore.rootFolder.show("");
					pstFile.close();
				} catch (final	BadXBlockLevelException
					|	BadXBlockTypeException
					|	DataOverflowException
					|	NotHeapNodeException
					|	NotPropertyContextNodeException
					|	NotTableContextNodeException
					|	NullDataBlockException
					|	NullNodeException
					|	UnimplementedPropertyTypeException
					|	UnknownClientSignatureException
					|	UnknownPropertyTypeException
					|	UnparseablePropertyContextException
					|	UnparseableTableContextException e) {
					System.out.println(e);
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
