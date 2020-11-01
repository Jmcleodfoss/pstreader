package io.github.jmcleodfoss.xml;

/**	Perform XSLT Processing.
*	<p><strong>Use</strong><p>
*	<code>java io.github.jmcleodfoss.pst.XSLTProcess xslt-file.xml xml-file.xml</code>
*/
public class XSLTProcessor implements javax.xml.transform.ErrorListener
{
	/**	Action to take when an recoverable error is encountered
	*	@param	exception	The recoverable error
	*/
	public void error(javax.xml.transform.TransformerException e)
	{
		System.out.println(e);
	}

	/**	Action to take when a fatal error is encountered
	*	@param	exception	The fatal error
	*/
	public void fatalError(javax.xml.transform.TransformerException e)
	{
		System.out.println(e);
	}

	/**	Action to take when a warning is encountered
	*	@param	exception	The warning
	*/
	public void warning(javax.xml.transform.TransformerException e)
	{
		System.out.println(e);
	}

	/**	Convert an XML file using an XSLT stylesheet, sending result to System.out.
	*	@param	args	The XSLT style sheet and the XML file to transform
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(String[] args)
	{
		if (args.length != 2) {
			System.out.printf("Use:%n%n\tjava %s xslt-file xml-file%n", XSLTProcessor.class.getName());
			System.exit(1);
		}

		try {
			XSLTProcessor errorListener = new XSLTProcessor();

			javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
			tFactory.setErrorListener(errorListener);

			javax.xml.transform.stream.StreamSource xslt = new javax.xml.transform.stream.StreamSource(args[0]);
			javax.xml.transform.Transformer transformer = tFactory.newTransformer(xslt);

			javax.xml.transform.stream.StreamSource source = new javax.xml.transform.stream.StreamSource(args[1]);
			javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(System.out);
			transformer.transform(source, result);
		} catch (javax.xml.transform.TransformerConfigurationException e) {
			System.out.printf("One or more errors were encountered in stylesheet %s; could not transform %s%n", args[0], args[1]);
		} catch (javax.xml.transform.TransformerException e) {
			System.out.printf("An error occurred while attempting to transform %s%n", args[1]);
		}
	}
}
