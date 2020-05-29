package io.github.jmcleodfoss.util;

/**	This is a utility class for simple client-managed XML output of strings. */
public class XMLOutput {

	/**	The output stream to which to write the XML. */
	java.io.PrintStream printStream;

	/**	The stack of open elements used by addElement(String)/closeElement() */
	java.util.Stack<String> openElements = new java.util.Stack<String>();

	/**	Construct an XMLOutput object which writes to printStream, showing the XML declaration if showDeclaration is true, and
	*	using the given encoding, if non-null.
	*
	*	@param	printStream	The PrintStream to which the XML should be written.
	*	@param	showDeclaration	This determines whether the XML declaration should be written, for example, "UTF-8".
	*	@param	encoding	This indicates the XML encoding.
	*/
	public XMLOutput(java.io.PrintStream printStream, final boolean showDeclaration, final String encoding)
	{
		this.printStream = printStream;

		if (showDeclaration) {
			StringBuilder declaration = new StringBuilder("<?xml version=\"1.0\"");
			if (encoding != null)
				declaration.append(String.format(" encoding=\"%s\"", encoding));
			declaration.append("?>");
			printStream.println(declaration);
		}
	}

	/**	Add appropriate indentation (each child tag is indented by one tab from its parent element). */
	private void indent()
	{
		if (openElements.size() == 0)
			return;
		char[] indentation = new char[openElements.size()];
		java.util.Arrays.fill(indentation, '\t');
		printStream.print(indentation);
	}

	/**	Add an empty element.
	*
	*	@param	name	The name of the empty element to add.
	*/
	public void addElement(final String name)
	{
		indent();
		printStream.append('<');
		printStream.print(name);
		printStream.println("/>");
	}

	/**	Add an element containing a single string value.
	*
	*	@param	name	The element name.
	*	@param	value	The element value.
	*/
	public void addElement(String name, String value)
	{
		indent();
		printStream.append('<');
		printStream.print(name);
		printStream.append('>');

		printStream.print(value);

		printStream.print("</");
		printStream.print(name);
		printStream.append('>');

		printStream.println();
	}

	/**	Close an element opened by openElement(String). */
	public void closeElement()
	{
		String name = openElements.pop();
		indent();
		printStream.print("</");
		printStream.print(name);
		printStream.append('>');
		printStream.println();
	}

	/**	Open element which may contain children.
	*
	*	@param	name	The name of the element to add.
	*/
	public void openElement(String name)
	{
		indent();
		printStream.append('<');
		printStream.print(name);
		printStream.append('>');
		printStream.println();

		openElements.push(name);
	}

	/**	Replace invalid characters in an element tag with valid ones.
	*
	*	@param	string	The String to replace the invalid characters in.
	*
	*	@return	A translated version of string, with the invalid characters [:/{} " replaced with sequences of alphabetic
	*		characters
	*/
	public static String safeXMLElementTag(String string)
	{
		StringBuilder dest = new StringBuilder(string.length()+16);
		
		char[] chars = new char[string.length()];
		string.getChars(0, string.length(), chars, 0);
		if (chars[0] >= '0' && chars[0] <= '9')
			dest.append("property-");

		for (char c : chars) {
			switch (c) {
			case ':': dest.append("_col_"); break;
			case '/': dest.append("_sl_"); break;
			case '{': dest.append("_lbr_"); break;
			case '}': dest.append("_rbr_"); break;
			case '(': dest.append("_lpar_"); break;
			case ')': dest.append("_rpar_"); break;
			case ' ': dest.append("_"); break;
			default: dest.append(c); break;
			}
		}

		return dest.toString();
	}

	/**	Replace non-UTF8 sequences with corresponding hexadecimal XML entities.
	*
	*	@param	string	The string to encode.
	*
	*	@return	The original string, with non-printable characters replaced with XML entities.
	*/
	public static String safeUTF8String(String string)
	throws
		java.io.UnsupportedEncodingException
	{
		StringBuilder dest = new StringBuilder(2*string.length());

		char[] chars = new char[string.length()];
		string.getChars(0, string.length(), chars, 0);
		for (char c : chars) {
			if (c >= 0x00 && c <= 0x1f || c >= 0x80) {
				dest.append("&amp;#");
				if (c < 0x10)
					dest.append('0');
				dest.append(Integer.toHexString(c));
				dest.append(";");
				continue;
			}

			switch (c) {
			case '&': dest.append("&amp;"); break;
			case '<': dest.append("&lt;"); break;
			case '>': dest.append("&gt;"); break;
			default: dest.append(c); break;
			}
		}

		return new String(dest);//dest.toString().getBytes("UTF-8"), "UTF-8");
	}
}
