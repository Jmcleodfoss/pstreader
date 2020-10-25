package io.github.jmcleodfoss.pstExtractor;

/**	The StickyNoteBean class represents a PST sticky note.
*	Note that this is not a "full" bean, in that it does not have any setters; its contents are set by other classes within the same package.
*/
public class StickyNoteBean
{
	/**	The sticky note title. */
	String title;

	/**	The sticky note. */
	String	note;

	/**	Retrieve the title of the sticky note.
	*	@return	The title of the sticky note message object.
	*/
	public String getTitle()
	{
		return title;
	}

	/**	Return the body of the sticky note.
	*	@return	The body of the sticky note message object.
	*/
	public String getNote()
	{
		return note;
	}
}
