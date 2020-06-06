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
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/7fd7ec40-deec-4c06-9493-1bc06b349682">MS-OXCMSG: Message and Attachment Object Protocol Specification</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/46eb4828-c6a5-420d-a137-9ee36df317c1">MS-PST: Outlook Personal Folder (.pst) File Format Section 2.4.6: Attachment Objects</a>
*/
public class Attachment {

	/**	Attachment Flag value: The attachment has not yet been created (the file contains a placeholder but no attachment
	*	data). Per the MC-OXCXMSG document, its value is {@value}.
	*
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/252923d6-dd41-468b-9c57-d3f68051a516">MS-OXCXMSG: Message and Attachment Object Protocol Specifications, Section 2.2.2.9: PidTagAttachMethod Property</a>
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
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/252923d6-dd41-468b-9c57-d3f68051a516">MS-OXCXMSG: Message and Attachment Object Protocol Specifications, Section 2.2.2.9: PidTagAttachMethod Property</a>
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
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/252923d6-dd41-468b-9c57-d3f68051a516">MS-OXCXMSG: Message and Attachment Object Protocol Specifications, Section 2.2.2.9: PidTagAttachMethod Property</a>
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
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/252923d6-dd41-468b-9c57-d3f68051a516">MS-OXCXMSG: Message and Attachment Object Protocol Specifications, Section 2.2.2.9: PidTagAttachMethod Property</a>
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
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/252923d6-dd41-468b-9c57-d3f68051a516">MS-OXCXMSG: Message and Attachment Object Protocol Specifications, Section 2.2.2.9: PidTagAttachMethod Property</a>
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
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/252923d6-dd41-468b-9c57-d3f68051a516">MS-OXCXMSG: Message and Attachment Object Protocol Specifications, Section 2.2.2.9: PidTagAttachMethod Property</a>
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
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxcmsg/252923d6-dd41-468b-9c57-d3f68051a516">MS-OXCXMSG: Message and Attachment Object Protocol Specifications, Section 2.2.2.9: PidTagAttachMethod Property</a>
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
	*
	*	@throws NotHeapNodeException			A node which is not a heap node was found in the purported heap.
	*	@throws NotPropertyContextNodeException		A node was found in a PropertyContext which did not have the property context signature.
	*	@throws NullDataBlockException			A node with a null data block was found when building a PropertyContext.
	*	@throws UnknownClientSignatureException		An unrecognized client signature was found when reading a block.
	*	@throws UnparseablePropertyContextException	The property context for this attachement could not be interpreted.
	*	@throws java.io.IOException			The PST file could not be read.
	*/
	public Attachment(final LPTLeaf nodeInfo, final BlockMap bbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NullDataBlockException,
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
	*
	*	@throws NotHeapNodeException			A node which is not a heap node was found in the purported heap.
	*	@throws NotPropertyContextNodeException		A node was found in a PropertyContext which did not have the property context signature.
	*	@throws NullDataBlockException			A node with a null data block was found when building a PropertyContext.
	*	@throws UnknownClientSignatureException		An unrecognized client signature was found when reading a block.
	*	@throws UnparseablePropertyContextException	The property context for this attachement could not be interpreted.
	*	@throws java.io.IOException			The PST file could not be read.
	*/
	Attachment(final SLEntry nodeInfo, final BlockMap bbt, final PSTFile pstFile)
	throws
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NullDataBlockException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		this.nodeInfo = nodeInfo;

		final PropertyContext pc = new PropertyContext(nodeInfo, bbt, pstFile);
		final int attachMethod = (Integer)pc.get(PropertyTags.AttachMethod);
		int propidData = -1;
		switch (attachMethod) {
		case AF_NONE:
			break;
	
		case AF_BY_VALUE:
			propidData = PropertyTags.AttachDataBinary;
			break;
	
		case AF_BY_REFERENCE:
		case AF_BY_REFERENCE_ONLY:
			propidData = PropertyTags.AttachLongPathname;
			break;

		case AF_EMBEDDED_MESSAGE:
		case AF_STORAGE:
			propidData = PropertyTags.AttachDataObject;
			break;
		}
		this.propidData = propidData;

		int tagAttachmentFilename = pstFile.unicode() ? PropertyTags.AttachFilenameW : PropertyTags.AttachFilename;
		name = pc.containsKey(tagAttachmentFilename) ? (String)pc.get(tagAttachmentFilename) : "unnamed-attachment";
		extension = (String)pc.get(PropertyTags.AttachExtension);
		final String mimeType = (String)pc.get(pstFile.unicode() ? PropertyTags.AttachMimeTagW : PropertyTags.AttachMimeTag);
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
