package io.github.jmcleodfoss.pst;

/**	The Recipient class represents a single entry in the Recipient table.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MX-OXPROPS: Exchange Server Protocols Master Property List</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/0e6d7ebd-c850-4772-ba9d-f5a642c9ff85">MS-PST Section 2.4.5.3: Recipient Table: Message Objects</a>
*/
class Recipient {

	/**	The e-mail address of the recipient. */
	public final String emailAddress;

	/**	The given (display) name of the recipient. */
	public final String displayName;

	/**	Construct the Recipient object.
	*	@param	tc		The recipient table.
	*	@param	row		The row in the recipient table to create the Recipient object from.
	*	@param	fUnicode	A flag indicating whether the underlying PST file is Unicode or ANSI.
	*	@throws	NotHeapNodeException			A node which is not a heap node was found when reading in the data for this recipient.
	*	@throws	UnknownClientSignatureException		An unknown client signature was found while building the recipient.
	*	@throws	UnparseablePropertyContextException	A bad / corrupt property context was found while building the recipient.
	*	@throws	java.io.IOException			An I/O error was encoutered while reading the data for the recipient.
	*/
	Recipient(TableContext tc, int row, boolean fUnicode)
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		emailAddress = (String)tc.get(row, fUnicode ? PropertyTags.EmailAddressW : PropertyTags.EmailAddress);
		displayName = (String)tc.get(row, fUnicode ? PropertyTags.DisplayNameW : PropertyTags.DisplayName);
	}
}
