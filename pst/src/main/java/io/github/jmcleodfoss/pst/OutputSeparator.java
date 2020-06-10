package io.github.jmcleodfoss.pst;

/**	The OutputSeparator class provides the frill of printing a separator line out for the second and subsequent calls to the emit
*	function.
*/
public class OutputSeparator
{
	/**	The default separator consists of twenty X the underscore character. */
	private static final String defaultSeparator = "____________________";

	/**	fFirst indicates whether this is the first invocation of emit or a subsequent one. */
	private boolean fFirst;

	/**	The array of bytes to write to the output stream as a separator. */
	final private byte[] separator;

	/**	Construct an OutputSeparator object from the sequence of bytes passed in.
	*	@param	separator	The array of bytes to use as a separator.
	*/
	private OutputSeparator(byte[] separator)
	{
		fFirst = true;
		this.separator = separator;
	}

	/**	Construct an OutputSeparator object from the string passed in.
	*	@param	separator	A string to use as the separator.
	*/
	public OutputSeparator(String separator)
	{
		this(separator.getBytes());
	}

	/**	Construct an OutputSeparator object from the default separator string.
	*	@see	#defaultSeparator
	*/
	public OutputSeparator()
	{
		this(defaultSeparator);
	}

	/**	If this is the first invocation of the emit function for this object, clear fFirst. If it is the second or later call,
	*	output the separator, followed by a newline.
	*	@param	stream	The java.io.OutputStream to which the separator should be written.
	*	@throws java.io.IOException	There was a problem writing the separator to the output stream.
	*/
	public void emit(java.io.OutputStream stream)
	throws
		java.io.IOException
	{
		if (fFirst)
			fFirst = false;
		else {
			stream.write(separator);
			stream.write('\n');
		}
	}
}
