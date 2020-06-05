package io.github.jmcleodfoss.pst;

/**	The Recipient class represents a single entry in the Recipient table.
*
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.5.3"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385128(v=office.12).aspx">Recipient Table</a>
*/
class Recipient {

	/**	The e-mail address of the recipient. */
	public final String emailAddress;

	/**	The given (display) name of the recipient. */
	public final String displayName;

	/**	Construct the Recipient object.
	*
	*	@param	tc		The recipient table.
	*	@param	row		The row in the recipient table to create the Recipient object from.
	*	@param	fUnicode	A flag indicating whether the underlying PST file is Unicode or ANSI.
	*
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
