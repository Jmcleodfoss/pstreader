package io.github.jmcleodfoss.pst;

/**	The Folder class represents a folder within a PST file. Note that sub-folders are loaded as required, rather than on initial
*	construction of the Folder object for the folder root.
*/
public class Folder extends io.github.jmcleodfoss.swingutil.ReadOnlyTreeModel implements TreeCustomNodeText, javax.swing.tree.TreeModel {

	/**	Ths SubfolderLevelsToRead class tells how many sub-levels to read when processing a folder: none, one, or all. */
	private static class SubfolderLevelsToRead
	{
		/**	The SubFolderLevelsToReadEnum contains the possible choices for reading sub-folders. */
		private static enum Levels {
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
		*
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
		*
		*	@return	true if the sub-folders should be read, false if they should not be.
		*/
		boolean readSubfolders()
		{
			return levels != Levels.NONE;
		}
	}

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
	private java.util.Vector<Folder> subfolders;

	/**	The content objects. */
	private java.util.Vector<MessageObject> contents;

	/**	Create a folder object for the given Node B-Tree leaf node.
	*
	*	@param	nodeFolderObject	The entry in the PST file's node B-tree for this folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file input stream, etc.
	*	@param	levelsToRead		The number of sub-levels to read.
	*	@param	fReadContents		A flag indicating whether the folder contents should be read in.
	*/
	private Folder(NBTEntry nodeFolderObject, BlockMap bbt, NodeMap nbt, PSTFile pstFile, SubfolderLevelsToRead levelsToRead, boolean fReadContents)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		this.nodeFolderObject = nodeFolderObject;
		PropertyContext folderObject = new PropertyContext(nodeFolderObject, bbt, pstFile);

		displayName = (String)folderObject.get(pstFile.unicode() ? PropertyTag.DisplayNameW : PropertyTag.DisplayName);
		containerClass = (String)folderObject.get(pstFile.unicode() ? PropertyTag.ContainerClassW : PropertyTag.ContainerClass);

		if ((Boolean)folderObject.get(PropertyTag.Subfolders) && levelsToRead.readSubfolders()) {
			NID nidHierarchyTable = new NID(nodeFolderObject.nid, NID.HIERARCHY_TABLE);
			nodeHierarchyTable = nbt.find(nidHierarchyTable);
			subfolders = readSubfolders(nodeHierarchyTable, bbt, nbt, pstFile, levelsToRead, fReadContents);
		} else {
			nodeHierarchyTable = null;
			subfolders = new java.util.Vector<Folder>();
		}

		if ((Integer)folderObject.get(PropertyTag.ContentCount) > 0 && fReadContents) {
			NID nidContentsTable = new NID(nodeFolderObject.nid, NID.CONTENTS_TABLE);
			nodeContentsTable = nbt.find(nidContentsTable);
			contents = readContents(nodeContentsTable, bbt, nbt, pstFile);
		} else {
			nodeContentsTable = null;
			contents = new java.util.Vector<MessageObject>();
		}

		NID nidAssociatedContentsTable = new NID(nodeFolderObject.nid, NID.ASSOC_CONTENTS_TABLE);
		nodeAssociatedContentsTable = nbt.find(nidAssociatedContentsTable);
	}

	/**	Get an iterator through this folder's message objects.
	*
	*	@return	An iterator through the contents.
	*/
	public java.util.Iterator<MessageObject> contentsIterator()
	{
		return contents.iterator();
	}

	/**	Get the requested child of this parent.
	*
	*	@param	oParent	The tree node from which to retrieve the child.
	*	@param	index	The child to retrieve.
	*
	*	@return	The given child of the tree parent node.
	*/
	public Object getChild(Object oParent, int index)
	{
		if (oParent instanceof Message)
			return ((Message)oParent).attachment(index);

		Folder folder = (Folder)oParent;
		if (index < folder.subfolders.size())
			return folder.subfolders.get(index);

		index -= folder.subfolders.size();
		if (index < folder.contents.size())
			return folder.contents.get(index);

		assert false: "getChild for " + oParent + " index " + index + " not found";
		return null;
	}

	/**	Get the number of children of this parent.
	*
	*	@param	oParent	The parent tree node to retrieve the number of children of.
	*
	*	@return	The number of children (sub-folders, message objects, or attachments) of the parent.
	*/
	public int getChildCount(Object oParent)
	{
		if (oParent instanceof Folder)
			return ((Folder)oParent).subfolders.size() + ((Folder)oParent).contents.size();

		return ((Message)oParent).numAttachments();
	}

	/**	Get a folder and the first level of sub-folders, but no folder contents.
	*
	*
	*	@param	nodeFolderObject	The entry in the PST file's node B-tree for this folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file input stream, etc.
	*
	*	@return	A folder and its immediate descendents.
	*/
	public static Folder getFolder(NBTEntry nodeFolderObject, BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return new Folder(nodeFolderObject, bbt, nbt, pstFile, new SubfolderLevelsToRead(SubfolderLevelsToRead.Levels.ONE), false);
	}

	/**	Get a folder and all sub-folders and contents.
	*
	*	@param	nodeFolderObject	The entry in the PST file's node B-tree for this folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file input stream, etc.
	*
	*	@return	A folder and all its immediate descendents.
	*/
	public static Folder getFolderTree(NBTEntry nodeFolderObject, BlockMap bbt, NodeMap nbt, PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		return new Folder(nodeFolderObject, bbt, nbt, pstFile, new SubfolderLevelsToRead(SubfolderLevelsToRead.Levels.ALL), true);
	}

	/**	Get the index of the given child object of the parent tree node.
	*
	*	@param	oParent	The tree node to look for the child in.
	*	@param	child	The child to look for.
	*
	*	@return	The index of given child of the tree parent node, or -1 if it is not a child of this parent node.
	*/
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
	public String getNodeText(Object o)
	{
		if (o instanceof Appointment)
			return ((Appointment)o).subject;

		if (o instanceof Attachment)
			return ((Attachment)o).name;

		if (o instanceof Folder)
			return ((Folder)o).displayName;

		if (o instanceof MessageObject)
			return ((MessageObject)o).subject;// + " " + ((Message)o).messageDeliveryTime;

		if (o instanceof StickyNote)
			return ((StickyNote)o).subject;

		return o.toString();
	}

	/**	Obtain the root of the tree.
	*
	*	@return	The root of the (Swing) tree (model).
	*/
	public Object getRoot()
	{
		return this;
	}

	/**	Indicate whether the given object is an intermediate node or a leaf node.
	*
	*	@param	oNode	The object to check for leafiness.
	*
	*	@return	true if the oNode is a leaf node, false if it is an intermediate node.
	*/
	public boolean isLeaf(Object oNode)
	{
		if (oNode instanceof Message)
			return ((Message)oNode).numAttachments() == 0;

		if (oNode instanceof Folder)
			return ((Folder)oNode).subfolders.size() == 0 && ((Folder)oNode).contents.size() == 0;

		return true;
	}

	/**	Read in the contents of the given folder.
	*
	*	@param	nodeFolderObject	The description of the folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file's incoming data stream, header, etc.
	*
	*	@return	A vector of the message contents.
	*/
	public static java.util.Vector<MessageObject> readContents(final NBTEntry nodeContentsTable, final BlockMap bbt, final NodeMap nbt, PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		TableContext contentsTable = new TableContext(nodeContentsTable, bbt, pstFile);

		java.util.Vector<MessageObject> contents = new java.util.Vector<MessageObject>(contentsTable.getRowCount());
		for (int row = 0; row < contentsTable.getRowCount(); ++row)
			contents.add(MessageObject.factory(contentsTable, row, bbt, nbt, pstFile));
		return contents;
	}

	/**	Read in the sub-folders of the given folder.
	*
	*	@param	nodeFolderObject	The description of the folder.
	*	@param	bbt			The PST file's block B-tree.
	*	@param	nbt			The PST file's node B-tree.
	*	@param	pstFile			The PST file's incoming data stream, header, etc.
	*	@param	levelsToRead		The number of sub-levels to read.
	*	@param	fReadContents		A flag indicating whether the folder contents should be read in.
	*
	*	@return	A vector of subfolders.
	*/
	public static java.util.Vector<Folder> readSubfolders(final NBTEntry nodeHierarchyTable, final BlockMap bbt, final NodeMap nbt, PSTFile pstFile, SubfolderLevelsToRead levelsToRead, boolean fReadContents)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		TableContext hierarchyTable = new TableContext(nodeHierarchyTable, bbt, pstFile);

		java.util.Vector<Folder> subfolders = new java.util.Vector<Folder>(hierarchyTable.getRowCount());
		levelsToRead.decrement();
		for (int row = 0; row < hierarchyTable.getRowCount(); ++row) {
			int nidSubfolder = (Integer)hierarchyTable.get(row, PropertyTag.LtpRowId);
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
	*
	*	@param	prefix	The text with which to prefix the lines for the current tree.
	*/
	private void show(String prefix)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		System.out.println(prefix + displayName);
		for (int i = 0; i < subfolders.size(); ++i) {
			Folder f = subfolders.get(i);
			f.show("|" + prefix + "-");
		}

		for (int i = 0; i < contents.size(); ++i) {
			MessageObject m = contents.get(i);;
			System.out.println(m);
		}
	}

	/**	Get an iterator through this folder's sub-folders.
	*
	*	@return	An iterator through this folder's sub-folders.
	*/
	public java.util.Iterator<Folder> subfolderIterator()
	{
		return subfolders.iterator();
	}

	/**	Test the Folder class by iterating through the folders and displaying information about each folder and sub-folder.
	*
	*	@param	args	The arguments to the function
	*/
	public static void main(String[] args)
	{
		if (args.length == 0) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.Folder pst-file");
			System.exit(1);
		}

		try {
			java.util.logging.Level logLevel = args.length >= 2 ? Debug.getLogLevel(args[1]) : java.util.logging.Level.OFF;
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.github.jmcleodfoss.pst.BTree");
			logger.setLevel(logLevel);

			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(args[0]));
			BlockBTree blockBTree = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
			NodeBTree nodeBTree = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

			NameToIDMap namedProperties = new NameToIDMap(blockBTree, nodeBTree, pstFile);
			MessageStore messageStore = new MessageStore(blockBTree, nodeBTree, pstFile);
			Folder root = messageStore.rootFolder();
			root.show("");
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
}
