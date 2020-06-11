package io.github.jmcleodfoss.pstExtractor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**	The AppointmentBean class represents a PST appointment.
*	Note that this is not a "full" bean, in that it does not have any setters; its contents are set by other classes within the
*	same package.
*/
public class AppointmentBean
{
	/**	The format to use for time/date retrieval. */
	private static final SimpleDateFormat OUTPUT_FORMAT;
	static {
		SimpleDateFormat df = new java.text.SimpleDateFormat("MMMM dd, yyyy hh:mm:ss");
		df.setTimeZone(TimeZone.getDefault());
		OUTPUT_FORMAT = df;
	}

	/**	The appointment title. */
	String title;

	/**	The appointment start time. */
	Date start;

	/**	The appointment end time. */
	Date end;

	/**	Retrieve the ending date and time for this appointment.
	*	@return	The end date and time for this appointment.
	*/
	public String getEnd()
	{
		return OUTPUT_FORMAT.format(end);
	}

	/**	Retrieve the starting date and time for this appointment.
	*	@return	The start date and time for this appointment.
	*/
	public String getStart()
	{
		return OUTPUT_FORMAT.format(start);
	}

	/**	Retrieve the title for this appointment.
	*	@return	The title for this appointment.
	*/
	public String getTitle()
	{
		return title;
	}
}
