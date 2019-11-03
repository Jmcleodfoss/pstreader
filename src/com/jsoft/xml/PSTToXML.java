package com.jsoft.xml;

/**	The PSTToXML class converts a PST file into an XML file.
*
*	<p><strong>Use</strong><p>
*	<code>java com.jsoft.pst.PSTToXML pst-file.pst</code><p>
*/
class PSTToXML {

	/**	The XML document for this PST file. */
	private org.w3c.dom.Document document;

	/**	This contains named properties we know to be invalid XML, which have to be replaced for XML output. */
	private static final java.util.HashMap<String, String> xmlSubstitutes = new java.util.HashMap<String, String>();
	static {
		xmlSubstitutes.put("http://schemas.microsoft.com/exchange/junkemailmovestamp", "http-schemas-microsoft-com-exchange-junkemailmovestamp");
		xmlSubstitutes.put("x-exclaimer-onmessagepostcategorize-{c60356ac-630e-418d-9b33-3b793fa0d170}", "x-exclaimer-onmessagepostcategorize");
	};

	/**	The Named Property element names, in a format safe for XML output. */
	private final java.util.HashMap<Short, String> safeXMLNamedProperties;

	/**	The PST file contents. */
	com.jsoft.pst.PST pst;

	/**	Create a PST to XML translator for the given PST file
	*
	*	@param	fn	The filename of the PST file to translate.
	*/
	public PSTToXML(String fn)
	throws
		com.jsoft.pst.NotHeapNodeException,
		com.jsoft.pst.UnknownClientSignatureException,
		com.jsoft.pst.UnparseablePropertyContextException,
		com.jsoft.pst.UnparseableTableContextException,
		java.io.IOException,
		javax.xml.parsers.ParserConfigurationException
	{
		pst = new com.jsoft.pst.PST(fn);

		safeXMLNamedProperties = new java.util.HashMap<Short, String>();
		for (java.util.Iterator<java.util.Map.Entrt<Short, String>> iterator = pst.namedPropertiesIterator(); iterator.hasNext(); ) {
			@SuppressWarnings("unchecked") java.util.Map.Entry<Short, String> entry = (java.util.Map.Entry<Short, String>)iterator.next();
			String value = entry.getValue();
			if (xmlSubstitutes.containsKey(value))
				safeXMLNamedProperties.put(entry.getKey(), xmlSubstitutes.get(value));
			else {
				String safeTag = com.jsoft.util.XMLOutput.safeXMLElementTag(value);
				if (!value.equals(safeTag))
					safeXMLNamedProperties.put(entry.getKey(), safeTag);
			}
		}
	}

	/**	The addFolderContents function adds the contents of the given folder to the XML file. Entries are added first, then sub-folders
	*	(including their contents) are added recursively.
	*
	*	@param	xml	The XML document being constructed.
	*	@param	folder	The folder being added.
	*	@param	pst	The PST object from which the XML document is being constructed.
	*/
	private void addFolderContents(com.jsoft.util.XMLOutput xml, com.jsoft.pst.Folder folder, com.jsoft.pst.PST pst)
	throws
		com.jsoft.pst.NotHeapNodeException,
		com.jsoft.pst.UnknownClientSignatureException,
		com.jsoft.pst.UnparseablePropertyContextException,
		com.jsoft.pst.UnparseableTableContextException,
		java.io.IOException,
		java.io.UnsupportedEncodingException
	{
		String type = folder.containerClass;
		if (type == null || !folderFilter(type)) {
			xml.addElement("folder-name", folder.displayName);
			xml.addElement("folder-type", folder.containerClass);
			for (java.util.Iterator<com.jsoft.pst.Message> contents = folder.contentsIterator(); contents.hasNext(); ) {
				com.jsoft.pst.Message message = contents.next();
	
				xml.openElement("object");
				addPropertiesToNode(xml, message.getMessage(pst.blockBTree, pst).iterator(), pst);
				xml.closeElement();
			}
		}

		for (java.util.Iterator<com.jsoft.pst.Folder> subfolders = folder.subfolderIterator(); subfolders.hasNext(); ) {
			com.jsoft.pst.Folder subfolder = subfolders.next();

			type = subfolder.containerClass;
			if (type == null || !folderFilter(type)) {
				xml.openElement("folder");
				addFolderContents(xml, subfolder, pst);
				xml.closeElement();
			}
		}
	}

	/**	Append all the properties contained in the given iterator to the output XML file.
	*
	*	@param	xml		The XML document being constructed.
	*	@param	iterator	The property/value iterator to add to the XML document.
	*	@param	pst		The PST object from which the XML document is being constructed.
	*/ 
	private void addPropertiesToNode(com.jsoft.util.XMLOutput xml, java.util.Iterator iterator, com.jsoft.pst.PST pst)
	throws
		java.io.UnsupportedEncodingException
	{
		while (iterator.hasNext()) {
			final java.util.Map.Entry keyAndValue = (java.util.Map.Entry)iterator.next();

			final Object value = keyAndValue.getValue();
			if (value == null)
				continue;

			final Integer tag = (Integer)keyAndValue.getKey();
			String propertyName = safeXMLNamedProperties.get(tag >>> 16);
			if (propertyName == null) {
				int propertyID = tag;
				propertyName = pst.propertyName(propertyID);
				if (propertyName.charAt(0) == '8')
					propertyName = "property-" + propertyName;
			}
			addPropertyToNode(xml, propertyName, value);
		}
	}

	/**	Add the given element to the output XML file using the given property name as tag.
	*
	*	@param	xml		The XML document being constructed.
	*	@param	propertyName	The name of the property to add.
	*	@param	element		The value of the property to add.
	*/
	private void addPropertyToNode(com.jsoft.util.XMLOutput xml, final String propertyName, final Object element)
	throws
		java.io.UnsupportedEncodingException
	{
		if (element.getClass().isArray()) {
			if (element instanceof byte[]) {
				final String s = com.jsoft.util.ByteUtil.createHexByteString((byte[])element);
				xml.addElement(propertyName, s);
			} else if (element instanceof String[]) {
				xml.openElement(propertyName + "-list");
				for(String item: (String[])element)
					xml.addElement(propertyName, com.jsoft.util.XMLOutput.safeUTF8String((String)item));
				xml.closeElement();
			} else if (element instanceof Object[]) {
				xml.openElement(propertyName + "-list");
				for(Object item: (Object[])element) {
					if (item == null)
						xml.addElement(propertyName);
					else if (item.getClass().isArray()) {
						addPropertyToNode(xml, propertyName, item);
					} else
						xml.addElement(propertyName, item.toString());
				}
				xml.closeElement();
			} else {
				xml.openElement(propertyName + "-list");
				final int l = java.lang.reflect.Array.getLength(element);
				for (int i = 0; i < l; ++i) 
					xml.addElement(propertyName, java.lang.reflect.Array.get(element, i).toString());
				xml.closeElement();
			}
		} else 
			xml.addElement(propertyName, element instanceof String ? com.jsoft.util.XMLOutput.safeUTF8String((String)element) : element.toString());
	}

	/**	Output the XML for this object to the given printstream
	*
	*	@param	printstream	The printstream to which the XML file should be written as it is built.
	*/
	void createXML(java.io.PrintStream printstream)
	throws
		com.jsoft.pst.NotHeapNodeException,
		com.jsoft.pst.UnknownClientSignatureException,
		com.jsoft.pst.UnparseablePropertyContextException,
		com.jsoft.pst.UnparseableTableContextException,
		java.io.IOException,
		java.io.UnsupportedEncodingException
	{
		com.jsoft.util.XMLOutput xml = new com.jsoft.util.XMLOutput(printstream, true, "UTF-8");
		xml.openElement("pst");
		addFolderContents(xml, pst.root, pst);
		xml.closeElement();
	}

	/**	Should this type of folder be filtered out?
	*
	*	@param	type	The folder class to check (note that not all folders have a class associated with them, but null folder
	*			classes are never passed to this function).
	*
	*	@return	true if folders of this class should be filtered out, false if they should be included.
	*/
	protected boolean folderFilter(final String type)
	{
		return (false);
	}

	/**	Convert a PST file into XML.
	*
	*	@param	args	The command line arguments to the application.
	*/
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava com.jsoft.xml.PSTToXML pst-filename");
			System.exit(1);
		}

		try {
			final PSTToXML pstToXml = new PSTToXML(args[0]);
			pstToXml.createXML(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}