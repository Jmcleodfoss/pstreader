package io.github.jmcleodfoss.pstExtractor;

/**	The JournalEntryBean class represents a PST journal entry.
*	Note that this is not a "full" bean, in that it does not have any setters; its contents are set by other classes within the same package.
*/
public class JournalEntryBean
{
	/**	The journal entry title. */
	String title;

	/**	The journal entry. */
	String	note;

	/**	Retrieve the title of the journal entry.
	*	@return	The title of the journal entry message object.
	*/
	public String getTitle()
	{
		return title;
	}

	/**	Return the body of the journal entry.
	*	@return	The body of the journal entry message object.
	*/
	public String getNote()
	{
		return note;
	}
}
