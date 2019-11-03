package com.jsoft.pstExtractor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**	The TaskBean class represents a PST task entry.
*	Note that this is not a "full" bean, in that it does not have any setters; its contents are set by other classes within the
*	same package.
*/
public class TaskBean {

	/**	The format to use for time/date retrieval. */
	private static final SimpleDateFormat OUTPUT_FORMAT;
	static {
		SimpleDateFormat df = new java.text.SimpleDateFormat("MMMM dd, yyyy hh:mm:ss");
		df.setTimeZone(TimeZone.getDefault());
		OUTPUT_FORMAT = df;
	}

	/**	The appointment title. */
	String title;

	/**	The task due date. */
	Date dueDate;

	/**	Retrieve the due date and time for this task.
	*
	*	@return	The due date and time for this task.
	*/
	public String getDueDate()
	{
		return OUTPUT_FORMAT.format(dueDate);
	}

	/**	Retrieve the title for this task.
	*
	*	@return	The title for this task.
	*/
	public String getTitle()
	{
		return title;
	}
}
