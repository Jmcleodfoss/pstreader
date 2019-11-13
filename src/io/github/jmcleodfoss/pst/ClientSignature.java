package io.github.jmcleodfoss.pst;

/**	The ClientSignature of a heap-on-node indicates its use.
*
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.3.1.2"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386518(v=office.12).aspx">Heap-on-Node (MSDN)</a>
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386774(v=office.12).aspx">HNHDR (MSDN)</a>
*/
class ClientSignature {

	/**	The use of this client signature is not documented. */
	private static final byte RESERVED1 = 0x6c;

	/**	The heap contains a table context. */
	private static final byte TABLE_CONTEXT = 0x7c;

	/**	The use of this client signature is not documented. */
	private static final byte RESERVED2 = (byte)0x8c;

	/**	The use of this client signature is not documented. */
	private static final byte RESERVED3 = (byte)0x9c;

	/**	The use of this client signature is not documented. */
	private static final byte RESERVED4 = (byte)0xa5;

	/**	The use of this client signature is not documented. */
	private static final byte RESERVED5 = (byte)0xac;

	/**	The heap contains a B-tree. */
	private static final byte BTREE_ON_HEAP = (byte)0xb5;

	/**	The heap contains a property context. */
	private static final byte PROPERTY_CONTEXT = (byte)0xbc;

	/**	The use of this client signature is not documented. */
	private static final byte RESERVED6 = (byte)0xcc;

	/**	Provide a BTH type fo external comparison. */
	static final ClientSignature BTH = factory(BTREE_ON_HEAP);

	/**	Provide a TABLE_CONTEXT type for external comparison.
	*
	*	@see	HeapOnNode#isTableContext
	*/
	static final ClientSignature TableContext = factory(TABLE_CONTEXT);

	/**	Provide a PROPERTY_CONTEXT type for external comparison.
	*
	*	@see	HeapOnNode#isPropertyContext
	*/
	static final ClientSignature PropertyContext = factory(PROPERTY_CONTEXT);

	/**	The signature byte. */	
	private final byte signature;

	/**	A description of the signature. */	
	private final String description;

	/**	Construct a ClientSignature object from a client signature read in from disk.
	*
	*	@param	signature	The signature of this heap.
	*/
	ClientSignature(byte signature)
	throws
		UnknownClientSignatureException
	{
		this.signature = signature;
		description = description(signature);
	}

	/**	Compare two ClientSignature objects for equality.
	*
	*	@param	comp	The ClientSignature object to compare this ClientSignature to.
	*
	*	@return	true if the signature members of the two objects are the same, false otherwise.
	*/
	public boolean equals(ClientSignature comp)
	{
		return signature == comp.signature;
	}

	/**	Provide a description of the client signature.
	*
	*	@return	The description of this client signature.
	*/
	@Override
	public String toString()
	{
		return description;
	}

	/**	Create the description for this client signature, throwing an exception if it is unknown.
	*
	*	@param	signature	The client signature byte as read from the Heap-on-Node header.
	*
	*	@return	A string describing the client signature.
	*/
	static private String description(byte signature)
	throws
		UnknownClientSignatureException
	{
		switch (signature) {
		case RESERVED1:
			return "RESERVED1";

		case TABLE_CONTEXT:
			return "Table Context";

		case RESERVED2:
			return "RESERVED2";

		case RESERVED3:
			return "RESERVED3";

		case RESERVED4:
			return "RESERVED4";

		case RESERVED5:
			return "RESERVED5";

		case BTREE_ON_HEAP:
			return "BTree on Heap";

		case PROPERTY_CONTEXT:
			return "Property Context";

		case RESERVED6:
			return "RESERVED6";

		default:
			throw new UnknownClientSignatureException(signature);
		}
	}

	/**	Create a ClientSignature object of a known good type.
	*
	*	@param	signature	The signature of the ClientSignature object to create.
	*
	*	@return	A ClientSignature object with the given signature.
	*/
	static private ClientSignature factory(byte signature)
	{
		try {
			return new ClientSignature(signature);
		} catch (final UnknownClientSignatureException e) {
			e.printStackTrace(System.out);
			System.exit(1);
			return null;
		}
	}
}
