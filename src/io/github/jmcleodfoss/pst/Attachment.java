package io.github.jmcleodfoss.pst;

/**	The Attachment class represents an attachment object within a PST message object. Note that the attachment
*	{@link PropertyContext} object is not saved, as it can be very large - the anticipated use case requires retrieving the PC,
*	typically via:
*
*	<pre>
*	{@code
*	// Given Message object message and PST object pst
*	for (java.util.Iterator<Attachment> iter = message.attachmentIterator(); iter.hasNext(); ) }&#x7b;
*		{@code
*		Attachment attachment = iter.next();
*		PropertyContext pcAttachment = pst.propertyContext(attachment.nodeInfo);
*		byte[] data = attachment.data(pcAttachment);
*		System.out.printf("attachment %s mime-type %s size %d\n", attachment.name, attachment.mimeType, data.length);
*	}&#x7d;
*	{@code
*	}
*	</pre>
*
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">{MX-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*	@see	"[MS-OXCMSG] Message and Attachment Object Protocol Specification v20101027"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc463900(v=EXCHG.80).aspx">[MS-OXCMSG]: Message and Attachment Object Protocol Specification (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.6"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385914(v=office.12).aspx">Attachment Objects (MSDN)</a>
*/
public class Attachment {

	/**	Attachment Flag value: The attachment has not yet been created (the file contains a placeholder but no attachment
	*	data). Per the MC-OXCXMSG document, its value is {@value}.
	*
	*	@see	"[MS-OXCXMSG] Message and Attachment Object Protocol Specifications v20101027, Section 2.2.2.9"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157901(v=exchg.80).aspx">PidTagAttachMethod Property (MSDN)</a>
	*	@see	#AF_BY_VALUE
	*	@see	#AF_BY_REFERENCE
	*	@see	#AF_BY_REFERENCE_ONLY
	*	@see	#AF_EMBEDDED_MESSAGE
	*	@see	#AF_STORAGE
	*	@see	#propidData
	*	@see	#data
	*/
	private static final int AF_NONE = 0x00;

	/**	Attachment Flag value: The attachment is stored in the AttachDataBinary field. Per the MC-OXCXMSG document, its value
	*	is {@value}.
	*
	*	@see	"[MS-OXCXMSG] Message and Attachment Object Protocol Specifications v20101027, Section 2.2.2.9"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157901(v=exchg.80).aspx">PidTagAttachMethod Property (MSDN)</a>
	*	@see	#AF_NONE
	*	@see	#AF_BY_REFERENCE
	*	@see	#AF_BY_REFERENCE_ONLY
	*	@see	#AF_EMBEDDED_MESSAGE
	*	@see	#AF_STORAGE
	*	@see	#propidData
	*	@see	#data
	*/
	private static final int AF_BY_VALUE = 0x01;

	/**	Attachment Flag value: The attachment is stored in the AttachLongPathname for recipients with access to the shared drive
	*	on which it is stored. Per the MC-OXCXMSG document, its value is {@value}.
	*
	*	@see	#AF_NONE
	*	@see	#AF_BY_VALUE
	*	@see	#AF_BY_REFERENCE_ONLY
	*	@see	#AF_EMBEDDED_MESSAGE
	*	@see	#AF_STORAGE
	*	@see	#propidData
	*	@see	#data
	*/
	private static final int AF_BY_REFERENCE = 0x02;

	/**	Attachment Flag value: The AttachLongPathname contains a fully-qualified path to the attachment. Per the MC-OXCXMSG
	*	document, its value is {@value}.
	*
	*	@see	"[MS-OXCXMSG] Message and Attachment Object Protocol Specifications v20101027, Section 2.2.2.9"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157901(v=exchg.80).aspx">PidTagAttachMethod Property (MSDN)</a>
	*	@see	#AF_NONE
	*	@see	#AF_BY_VALUE
	*	@see	#AF_BY_REFERENCE
	*	@see	#AF_EMBEDDED_MESSAGE
	*	@see	#AF_STORAGE
	*	@see	#propidData
	*	@see	#data
	*/
	private static final int AF_BY_REFERENCE_ONLY = 0x04;

	/**	Attachment Flag value: The attachment is an embedded message. Per the MC-OXCXMSG document, its value is {@value}.
	*
	*	@see	"[MS-OXCXMSG] Message and Attachment Object Protocol Specifications v20101027, Section 2.2.2.9"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157901(v=exchg.80).aspx">PidTagAttachMethod Property (MSDN)</a>
	*	@see	#AF_NONE
	*	@see	#AF_BY_VALUE
	*	@see	#AF_BY_REFERENCE
	*	@see	#AF_BY_REFERENCE_ONLY
	*	@see	#AF_STORAGE
	*	@see	#propidData
	*	@see	#data
	*/
	private static final int AF_EMBEDDED_MESSAGE = 0x05;

	/**	The Attachment Flag value: attachment is an application-specific object. Per the MC-OXCXMSG document, its value
	*	is {@value}.
	*
	*	@see	"[MS-OXCXMSG] Message and Attachment Object Protocol Specifications v20101027, Section 2.2.2.9"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee157901(v=exchg.80).aspx">PidTagAttachMethod Property (MSDN)</a>
	*	@see	#AF_NONE
	*	@see	#AF_BY_VALUE
	*	@see	#AF_BY_REFERENCE
	*	@see	#AF_BY_REFERENCE_ONLY
	*	@see	#AF_EMBEDDED_MESSAGE
	*	@see	#propidData
	*	@see	#data
	*/
	private static final int AF_STORAGE = 0x06;

	/**	The property ID under which the attachment data is stored. Note that, typically, the attachment data is much larger 
	*	than the metadata.
	*
	*	@see	#AF_NONE
	*	@see	#AF_BY_VALUE
	*	@see	#AF_BY_REFERENCE
	*	@see	#AF_BY_REFERENCE_ONLY
	*	@see	#AF_EMBEDDED_MESSAGE
	*	@see	#AF_STORAGE
	*	@see	#data
	*/
	private final int propidData;

	/**	The node containing the attachment PC. This may be used to retrieve the attachment's property context as follows:
	*
	*	<pre>
	*	{@code
	*	// Given Attachment object attachment and PST object pst
	*	PropertyContext pcAttachment = pst.propertyContext(attachment.nodeInfo);
	*	}
	*	</pre>
	*
	*	@see	io.github.jmcleodfoss.pst.PST#propertyContext
	*/
	public final SLEntry nodeInfo;

	/**	The name of the attachment. */
	public final String name;

	/**	The attachment filename extension. */
	public final String extension;

	/**	The attachment mime type. */
	public final String mimeType;

	/**	Construct the attachment object using publicly-accessible class {@link LPTLeaf}.
	*
	*	@param	nodeInfo	The sub-node B-tree entry holding the attachment information.
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The PST file input stream, etc.
	*/
	public Attachment(final LPTLeaf nodeInfo, final BlockMap bbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		this((SLEntry)nodeInfo, bbt, pstFile);
	}

	/**	Construct the Attachment object using the sub-node B-tree entry {@link SLEntry}, which is available only within the
	*	{@link io.github.jmcleodfoss.pst} package.
	*
	*	@param	nodeInfo	The sub-node B-tree entry holding the attachment information.
	*	@param	bbt		The PST file's block B-tree.
	*	@param	pstFile		The PST file input stream, etc.
	*/
	Attachment(final SLEntry nodeInfo, final BlockMap bbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		this.nodeInfo = nodeInfo;

		final PropertyContext pc = new PropertyContext(nodeInfo, bbt, pstFile);
		final int attachMethod = (Integer)pc.get(PropertyTag.AttachMethod);
		int propidData = -1;
		switch (attachMethod) {
		case AF_NONE:
			break;
	
		case AF_BY_VALUE:
			propidData = PropertyTag.AttachDataBinary;
			break;
	
		case AF_BY_REFERENCE:
		case AF_BY_REFERENCE_ONLY:
			propidData = PropertyTag.AttachLongPathname;
			break;

		case AF_EMBEDDED_MESSAGE:
		case AF_STORAGE:
			propidData = PropertyTag.AttachDataObject;
			break;
		}
		this.propidData = propidData;

		int tagAttachmentFilename = pstFile.unicode() ? PropertyTag.AttachFilenameW : PropertyTag.AttachFilename;
		name = pc.containsKey(tagAttachmentFilename) ? (String)pc.get(tagAttachmentFilename) : "unnamed-attachment";
		extension = (String)pc.get(PropertyTag.AttachExtension);
		final String mimeType = (String)pc.get(pstFile.unicode() ? PropertyTag.AttachMimeTagW : PropertyTag.AttachMimeTag);
		this.mimeType = mimeType == null ? "" : mimeType.toLowerCase();
	}

	/**	Retrieve the attachment data.
	*
	*	@param	pc	The attachment property context.
	*
	*	@return	A byte array containing the attachment data.
	*
	*	@see	#propidData
	*/
	public byte[] data(final PropertyContext pc)
	{
		return (byte[])pc.get(propidData);
	}

	/**	Test the Attachment class by iterating through the messages and displaying information about each attachment.
	*
	*	@param	arg	The command line arguments to the test application.
	*/
	@Unimplemented(priority = Unimplemented.Priority.LOW)
	public static void main(final String[] arg)
	{
		assert false: "Not implemented yet.";
		System.out.println("Not implemented yet.");
	}
}
