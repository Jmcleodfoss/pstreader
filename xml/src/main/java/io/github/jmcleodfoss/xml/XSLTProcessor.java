package io.github.jmcleodfoss.xml;

/**	Perform XSLT Processing.
*	<p><strong>Use</strong><p>
*	<code>java io.github.jmcleodfoss.pst.XSLTProcess xslt-file.xml xml-file.xml</code>
*/
public class XSLTProcessor
{
	/**	Convert an XML file using an XSLT stylesheet, sending result to System.out.
	*	@param	args	The XSLT style sheet and the XML file to transform
	*/
	public static void main(String[] args)
	{
		if (args.length != 2) {
			System.out.printf("Use:%n%n\tjava %s xslt-file xml-file%n", XSLTProcessor.class.getName());
			System.exit(1);
		}

		try {
			javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

			javax.xml.transform.stream.StreamSource xslt = new javax.xml.transform.stream.StreamSource(args[0]);
			javax.xml.transform.Transformer transformer = tFactory.newTransformer(xslt);

			javax.xml.transform.stream.StreamSource source = new javax.xml.transform.stream.StreamSource(args[1]);
			javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(System.out);
			transformer.transform(source, result);
		} catch (javax.xml.transform.TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (javax.xml.transform.TransformerException e) {
			e.printStackTrace();
		}
	}
}
