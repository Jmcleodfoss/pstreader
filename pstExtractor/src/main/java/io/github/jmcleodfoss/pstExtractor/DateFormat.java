package io.github.jmcleodfoss.pstExtractor;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/** Utility class for shared defeault date format creation */
public class DateFormat
{
	/** Get the default date format.
	*	@return	The default date format to use.
	*/
	static SimpleDateFormat defaultDateFormat()
	{
		SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss");
		df.setTimeZone(TimeZone.getDefault());
		return df;
	}
}
