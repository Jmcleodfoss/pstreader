package com.jsoft.pst;

/**	The NID class represents a PST file node ID.
*
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.2.2.1"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385251(v=office.12).aspx">NID (Node ID) (MSDN)</a>
*/
public class NID implements NodeKey {

	/**	Logger for NID creation. */
	private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("com.jsoft.pst.NID");

	/**	Whether to check for known node types. This should be left disabled, since undocumented node types do occur in PST
	*	files.
	*/
	private static final boolean fStrict = false;

	/**	Node type: Heap node.
	*
	*	@see	com.jsoft.pst.HeapOnNode
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.3.1.1"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386694(v=office.12).aspx">HID (MSDN)</a>
	*/
	static final byte HID = 0x00;

	/**	Node type: Internal node.
	*
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.1"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385012(v=office.12).aspx">Special Internal NIDs (MSDN)</a>
	*/
	static final byte INTERNAL = 0x01;

	/**	Node type: Normal folder object (PC).
	*
	*	@see	com.jsoft.pst.Folder
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.4.1"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385548(v=office.12).aspx">Folder Object PC (MSDN)</a>
	*/
	static final byte NORMAL_FOLDER = 0x02;

	/**	Node type: Search folder object (PC). */
	static final byte SEARCH_FOLDER = 0x03;

	/**	Node type: Normal message object (PC).
	*
	*	@see	com.jsoft.pst.Message
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385158(v=office.12).aspx">Message Objects (MSDN)</a>
	*/
	static final byte NORMAL_MESSAGE = 0x04;

	/**	Node type: Attachment object (PC).
	*
	*	@see	com.jsoft.pst.Attachment
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.6.2"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff387207(v=office.12).aspx">Attachment Object PC (MSDN)</a>
	*/
	static final byte ATTACHMENT = 0x05;

	/**	Node type: Queue of changed objects for search folder objects. */
	static final byte SEARCH_UPDATE_QUEUE = 0x06;

	/**	Node type: Search criteria for search folders. */
	static final byte SEARCH_CRITERIA_OBJECT = 0x07;

	/**	Node type: Folder associated information message object (PC). */
	static final byte ASSOC_MESSAGE = 0x08;

	/**	Node type: An internal, persisted view-related object. */
	static final byte CONTENTS_TABLE_INDEX = 0x0a;

	/**	Node type: The in box (TC). */
	static final byte RECEIVE_FOLDER_TABLE = 0x0b;

	/**	Node type: The out box (TC). */
	static final byte OUTGOING_QUEUE_TABLE = 0x0c;

	/**	Node type: Hierarchy Table (TC).
	*
	*	@see	com.jsoft.pst.Folder
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.4.4"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386271(v=office.12).aspx">Hierarchy Table (MSDN)</a>
	*/
	static final byte HIERARCHY_TABLE = 0x0d;

	/**	Node type: Contents Table (TC).
	*
	*	@see	#NORMAL_MESSAGE
	*	@see	com.jsoft.pst.Folder
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.4.5"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386083(v=office.12).aspx">Contents Table (MSDN)</a>
	*/
	static final byte CONTENTS_TABLE = 0x0e;

	/**	Node type: Folder associated information contents table (TC).
	*
	*	@see	#ASSOC_MESSAGE
	*	@see	com.jsoft.pst.Folder
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.4.6"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385054(v=office.12).aspx">FAI Contents Table</a>
	*/
	static final byte ASSOC_CONTENTS_TABLE = 0x0f;

	/**	Node type: Contents table of a search folder (TC).
	*
	*	@see	com.jsoft.pst.Folder
	*/
	static final byte SEARCH_CONTENTS_TABLE = 0x10;

	/**	Node type: Contents table of a search folder (TC).
	*
	*	@see	#ATTACHMENT
	*	@see	com.jsoft.pst.Message
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.6.1"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386915(v=office.12).aspx">Attachment Objects</a>
	*/
	static final byte ATTACHMENT_TABLE = 0x11;

	/**	Node type: Recipients Table (TC).
	*
	*	@see	com.jsoft.pst.Message
	*	@see	com.jsoft.pst.Recipient
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5.3"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385128(v=office.12).aspx">Recipient Table (MSDN)</a>
	*/
	static final byte RECIPIENT_TABLE = 0x12;

	/**	Node type: search table index (an internal, persisted, view-related object. */
	static final byte SEARCH_TABLE_INDEX = 0x13;

	/**	Node type: LTP (data for Property Context or Table Context stored in HID). */
	static final byte LTP = 0x1f;

	/**	Special NID: The Message Store PC contains general PST file settings.
	*
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.3"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff387186(v=office.12).aspx">Message Store</a>
	*/
	static final NID NID_MESSAGE_STORE = new NID(NID.INTERNAL, 0x0001);

	/**	Special NID: The Name to ID Map PC contains the descriptions of the named properties.
	*
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.7"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff387733(v=office.12).aspx"></a>
	*/
	static final NID NID_NAME_TO_ID_MAP = new NID(NID.INTERNAL, 0x0003);

	/**	The key (type and index) of this node ID. */
	final int key;

	/**	The type of this node index. */
	final byte type;

	/**	The node index. */
	final int nid;

	/**	A description of this node ID type (used primarily in toString). */
	final String description;

	/**	Construct a node ID from a type and a node index. This is used to create the special node IDs defined here and in
	*	derived classes, and should not be used elsewhere.
	*
	*	@param	type	The node type.
	*	@param	nid	The node index.
	*
	*	@see	#NID_MESSAGE_STORE
	*	@see	#NID_NAME_TO_ID_MAP
	*/
	protected NID(final byte type, final int nid)
	{
		key = nid << 5 | type;
		this.type = type;
		this.nid = nid;
		description = description(type);
	}

	/**	Create a node ID from an NID key.
	*
	*	@param	key	The node B-tree key
	*/
	NID(final int key)
	{
		this.key = key;
		type = (byte)(key & 0x1f);
		nid = (key >> 5) & 0x07ffffff;
		description = description(type);
		logger.log(java.util.logging.Level.INFO, String.format("NID %08x => type 0x%02x nid %08x %s", key, type, nid, description));
	}

	/**	Create a node ID from the given node ID but with the new type.
	*
	*	@param	nid	The node ID to base the new NID on.
	*	@param	type	The type of the new node.
	*/
	NID(final NID nid, final byte type)
	{
		this(type, nid.nid);
	}

	/**	Get the description of this node ID type. Throw an exception if the node ID type is not found.
	*	Node type checking can be strict, in which case description will thrown an exception for unrecognized node types, or it
	*	can be relaxed, in which case an "Unknown type" is synthesized.
	*
	*	@param	type	The node type to create the description for.
	*
	*	@return	A string describing the node type based on known node types.
	*
	*	@see	#fStrict
	*/
	static private String description(final byte type)
	{
		switch(type) {
		case HID:
			return "Heap";

		case INTERNAL:
			return "Internal";

		case NORMAL_FOLDER:
			return "Normal Folder";

		case SEARCH_FOLDER:
			return "Search Folder";

		case NORMAL_MESSAGE:
			return "Normal Message";

		case ATTACHMENT:
			return "Attachment";

		case SEARCH_UPDATE_QUEUE:
			return "Search Update Queue";

		case SEARCH_CRITERIA_OBJECT:
			return "Search Criteria Object";

		case ASSOC_MESSAGE:
			return "Assoc Message";

		case CONTENTS_TABLE_INDEX:
			return "Contents Table Index";

		case RECEIVE_FOLDER_TABLE:
			return "Receive Folder Table";

		case OUTGOING_QUEUE_TABLE:
			return "Outgoing Queue Table";

		case HIERARCHY_TABLE:
			return "Hierarchy Table";

		case CONTENTS_TABLE:
			return "Contents Table";

		case ASSOC_CONTENTS_TABLE:
			return "Assoc Contents Table";

		case SEARCH_CONTENTS_TABLE:
			return "Search Contents Table";

		case ATTACHMENT_TABLE:
			return "Attachment Table";

		case RECIPIENT_TABLE:
			return "Recipient Table";

		case SEARCH_TABLE_INDEX:
			return "Search Table Index";


		case LTP:
			return "LTP";

		// Unknown node types appear in e.g. the Contents Table Index folder.
		default:
			assert !fStrict : "Unrecognized node type " + Integer.toHexString(type);
			return "Internal " + Integer.toHexString(type);
		}
	}

	/**	Is the given object equivalent to this NID?
	*
	*	@param	o	The object to check for equivalency with this NID.
	*
	*	returns	true if the passed object is an NID and its key is equal to this object's key, false otherwise.
	*/
	public boolean equals(final Object o)
	{
		return o instanceof NID && key == ((NID)o).key;
	}

	/**	Is this an attachment table NID?
	*
	*	@return	true if this NID is an attachment table, false otherwise.
	*
	*	@see	#ATTACHMENT_TABLE
	*/
	public boolean isAttachmentTable()
	{
		return type == ATTACHMENT_TABLE;
	}

	/**	Is this an Attachment object (PC) NID?
	*
	*	@return	true if this NID is an attachment object, false otherwise.
	*
	*	@see	#ATTACHMENT
	*/
	public boolean isAttachment()
	{
		return type == ATTACHMENT;
	}

	/**	Is this an internal node, or does it represent client-level data (i.e. a Property Context or Table Context).
	*
	*	@return	true if this NID is an internal node other than one known to be a PC, false otherwise.
	*
	*	@see	#INTERNAL
	*	@see	#NID_MESSAGE_STORE
	*	@see	#NID_NAME_TO_ID_MAP
	*/
	public boolean isHeapOnNodeNID()
	{
		return type != INTERNAL || this.equals(NID_MESSAGE_STORE) || this.equals(NID_NAME_TO_ID_MAP);
	}

	/**	Is this an recipient table (TC) NID?
	*
	*	@return	true if this NID is an attachment object, false otherwise.
	*
	*	@see	#RECIPIENT_TABLE
	*/
	public boolean isRecipientTable()
	{
		return type == RECIPIENT_TABLE;
	}

	/**	The B-tree search key for this Node ID object.
	*
	*	@return	The key for this NID.
	*
	*	@see	com.jsoft.pst.NodeBTree
	*/
	public long key()
	{
		return key;
	}

	/**	Provide a string describing this node ID. This function is typically used for debugging.
	*
	*	@return	A string describing this node ID object.
	*
	*	@see	#description
	*/
	@Override
	public String toString()
	{
		return String.format("0x%08x: %s node index 0x%08x", key, description, nid);
	}
}
