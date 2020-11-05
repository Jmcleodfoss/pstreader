package io.github.jmcleodfoss.xml;

/**	The PSTToXML class converts a PST file into an XML file.
*	<p><strong>Use</strong><p>
*	<code>java io.github.jmcleodfoss.pst.PSTToXML pst-file.pst</code>
*/
class PSTToXML
{
	/**	This contains named properties we know to be invalid XML, which have to be replaced for XML output. */
	private static final java.util.HashMap<String, String> xmlSubstitutes = new java.util.HashMap<String, String>();
	static {
		xmlSubstitutes.put("http://schemas.microsoft.com/exchange/junkemailmovestamp", "http-schemas-microsoft-com-exchange-junkemailmovestamp");
		xmlSubstitutes.put("x-exclaimer-onmessagepostcategorize-{c60356ac-630e-418d-9b33-3b793fa0d170}", "x-exclaimer-onmessagepostcategorize");
	};

	/**	The Named Property element names, in a format safe for XML output. */
	private final java.util.HashMap<Integer, String> safeXMLNamedProperties;

	/**	The PST file contents. */
	private final io.github.jmcleodfoss.pst.PST pst;

	/**	Create a PST to XML translator for the given PST file
	*	@param	fn	The filename of the PST file to translate.
	*	@throws io.github.jmcleodfoss.pst.BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws io.github.jmcleodfoss.pst.BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws	io.github.jmcleodfoss.pst.CRCMismatchException			The header's calculated CRC does not match the expected value.
	*	@throws	io.github.jmcleodfoss.pst.DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws io.github.jmcleodfoss.pst.IncorrectNameIDStreamContentException	either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
	*	@throws	io.github.jmcleodfoss.pst.NameIDStreamNotFoundException	The requested Name ID mapping stream could not be found
	*	@throws	io.github.jmcleodfoss.pst.NotHeapNodeException			A node which was not a heap node was found where a heap node was expected when reading the pst file.
	*	@throws	io.github.jmcleodfoss.pst.NotPSTFileException			The named file is not a pst file.
	*	@throws io.github.jmcleodfoss.pst.NotPropertyContextNodeException	A node which was not a property context node was found where a property context node was expected when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NotTableContextNodeException		A node which was not a table context node was found where a table context node was expected when reading the pst file.
	*	@throws	io.github.jmcleodfoss.pst.NullDataBlockException		A null data block was found when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	io.github.jmcleodfoss.pst.UnimplementedPropertyException	Handling for the property type has not been implemented
	*	@throws	io.github.jmcleodfoss.pst.UnknownClientSignatureException	An unrecognized client signature was found when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnknownPropertyTypeException	The property type was not recognized
	*	@throws io.github.jmcleodfoss.pst.UnparseablePropertyContextException	A bad / corrupt property context was found whe nreading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnparseableTableContextException	A bad / corrupt table context was found when reading the pst file.
	*	@throws java.io.IOException						An I/O error was encoutnered while reading the pst file.
	*/
	public PSTToXML(String fn)
	throws
		io.github.jmcleodfoss.pst.BadXBlockLevelException,
		io.github.jmcleodfoss.pst.BadXBlockTypeException,
		io.github.jmcleodfoss.pst.CRCMismatchException,
		io.github.jmcleodfoss.pst.DataOverflowException,
		io.github.jmcleodfoss.pst.IncorrectNameIDStreamContentException,
		io.github.jmcleodfoss.pst.NameIDStreamNotFoundException,
		io.github.jmcleodfoss.pst.NotHeapNodeException,
		io.github.jmcleodfoss.pst.NotPSTFileException,
		io.github.jmcleodfoss.pst.NotPropertyContextNodeException,
		io.github.jmcleodfoss.pst.NotTableContextNodeException,
		io.github.jmcleodfoss.pst.NullDataBlockException,
		io.github.jmcleodfoss.pst.NullNodeException,
		io.github.jmcleodfoss.pst.UnimplementedPropertyTypeException,
		io.github.jmcleodfoss.pst.UnknownClientSignatureException,
		io.github.jmcleodfoss.pst.UnknownPropertyTypeException,
		io.github.jmcleodfoss.pst.UnparseablePropertyContextException,
		io.github.jmcleodfoss.pst.UnparseableTableContextException,
		java.io.IOException
	{
		pst = new io.github.jmcleodfoss.pst.PST(fn);

		safeXMLNamedProperties = new java.util.HashMap<Integer, String>();
		for (java.util.Iterator<java.util.Map.Entry<Integer, String>> iterator = pst.namedPropertiesIterator(); iterator.hasNext(); ) {
			java.util.Map.Entry<Integer, String> entry = iterator.next();
			String value = entry.getValue();
			if (xmlSubstitutes.containsKey(value))
				safeXMLNamedProperties.put(entry.getKey(), xmlSubstitutes.get(value));
			else {
				String safeTag = io.github.jmcleodfoss.util.XMLOutput.safeXMLElementTag(value);
				if (!value.equals(safeTag))
					safeXMLNamedProperties.put(entry.getKey(), safeTag);
			}
		}
	}

	/**	The addFolderContents function adds the contents of the given folder to the XML file. Entries are added first, then sub-folders
	*	(including their contents) are added recursively.
	*	@param	xml	The XML document being constructed.
	*	@param	folder	The folder being added.
	*	@param	pst	The PST object from which the XML document is being constructed.
	*	@throws io.github.jmcleodfoss.pst.BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws io.github.jmcleodfoss.pst.BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws	io.github.jmcleodfoss.pst.CRCMismatchException			The header's calculated CRC does not match the expected value.
	*	@throws	io.github.jmcleodfoss.pst.DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws io.github.jmcleodfoss.pst.IncorrectNameIDStreamContentException	either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
	*	@throws	io.github.jmcleodfoss.pst.NameIDStreamNotFoundException	The requested Name ID mapping stream could not be found
	*	@throws	io.github.jmcleodfoss.pst.NotHeapNodeException			A node which was not a heap node was found where a heap node was expected when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NotPropertyContextNodeException	A node which was not a property context node was found where a property context node was expected when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NotTableContextNodeException		A node which was not a table context node was found where a table context node was expected when reading the pst file.
	*	@throws	io.github.jmcleodfoss.pst.NullDataBlockException		A null data block was found when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	io.github.jmcleodfoss.pst.UnimplementedPropertyException	Handling for the property type has not been implemented
	*	@throws	io.github.jmcleodfoss.pst.UnknownClientSignatureException	An unrecognized client signature was found when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnknownPropertyTypeException	The property type was not recognized
	*	@throws io.github.jmcleodfoss.pst.UnparseablePropertyContextException	A bad / corrupt property context was found whe nreading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnparseableTableContextException	A bad / corrupt table context was found when reading the pst file.
	*	@throws java.io.IOException						An I/O error was encoutnered while reading the pst file.
	*	@throws	java.io.UnsupportedEncodingException				An unsupported encoding was found while reading a string in the pst file.
	*/
	private void addFolderContents(io.github.jmcleodfoss.util.XMLOutput xml, io.github.jmcleodfoss.pst.Folder folder, io.github.jmcleodfoss.pst.PST pst)
	throws
		io.github.jmcleodfoss.pst.BadXBlockLevelException,
		io.github.jmcleodfoss.pst.BadXBlockTypeException,
		io.github.jmcleodfoss.pst.CRCMismatchException,
		io.github.jmcleodfoss.pst.DataOverflowException,
		io.github.jmcleodfoss.pst.IncorrectNameIDStreamContentException,
		io.github.jmcleodfoss.pst.NameIDStreamNotFoundException,
		io.github.jmcleodfoss.pst.NotHeapNodeException,
		io.github.jmcleodfoss.pst.NotPropertyContextNodeException,
		io.github.jmcleodfoss.pst.NotTableContextNodeException,
		io.github.jmcleodfoss.pst.NullDataBlockException,
		io.github.jmcleodfoss.pst.NullNodeException,
		io.github.jmcleodfoss.pst.UnimplementedPropertyTypeException,
		io.github.jmcleodfoss.pst.UnknownClientSignatureException,
		io.github.jmcleodfoss.pst.UnknownPropertyTypeException,
		io.github.jmcleodfoss.pst.UnparseablePropertyContextException,
		io.github.jmcleodfoss.pst.UnparseableTableContextException,
		java.io.IOException,
		java.io.UnsupportedEncodingException
	{
		String type = folder.containerClass;
		if (type == null || !folderFilter(type)) {
			xml.addElement("folder-name", folder.displayName);
			xml.addElement("folder-type", folder.containerClass);
			for (java.util.Iterator<io.github.jmcleodfoss.pst.MessageObject> contents = folder.contentsIterator(); contents.hasNext(); ) {
				io.github.jmcleodfoss.pst.MessageObject message = contents.next();
	
				xml.openElement("object");
				addPropertiesToNode(xml, message.getMessage(pst).iterator(), pst);
				xml.closeElement();
			}
		}

		for (java.util.Iterator<io.github.jmcleodfoss.pst.Folder> subfolders = folder.subfolderIterator(); subfolders.hasNext(); ) {
			io.github.jmcleodfoss.pst.Folder subfolder = subfolders.next();

			type = subfolder.containerClass;
			if (type != null && !folderFilter(type)) {
				xml.openElement("folder");
				addFolderContents(xml, subfolder, pst);
				xml.closeElement();
			}
		}
	}

	/**	Append all the properties contained in the given iterator to the output XML file.
	*	@param	xml		The XML document being constructed.
	*	@param	iterator	The property/value iterator to add to the XML document.
	*	@param	pst		The PST object from which the XML document is being constructed.
	*	@throws	java.io.UnsupportedEncodingException				An unsupported encoding was found while reading a string in the pst file.
	*/ 
	private void addPropertiesToNode(io.github.jmcleodfoss.util.XMLOutput xml, java.util.Iterator<java.util.Map.Entry<Integer, Object>> iterator, io.github.jmcleodfoss.pst.PST pst)
	throws
		java.io.UnsupportedEncodingException
	{
		while (iterator.hasNext()) {
			final java.util.Map.Entry<Integer, Object> keyAndValue = iterator.next();

			final Object value = keyAndValue.getValue();
			if (value == null)
				continue;

			final Integer tag = keyAndValue.getKey();
			String propertyName = safeXMLNamedProperties.get(0xffff & (tag >> 16));
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
	*	@param	xml		The XML document being constructed.
	*	@param	propertyName	The name of the property to add.
	*	@param	element		The value of the property to add.
	*	@throws	java.io.UnsupportedEncodingException				An unsupported encoding was found while reading a string in the pst file.
	*/
	private void addPropertyToNode(io.github.jmcleodfoss.util.XMLOutput xml, final String propertyName, final Object element)
	throws
		java.io.UnsupportedEncodingException
	{
		if (element.getClass().isArray()) {
			if (element instanceof byte[]) {
				final String s = io.github.jmcleodfoss.pst.ByteUtil.createHexByteString((byte[])element);
				xml.addElement(propertyName, s);
			} else if (element instanceof String[]) {
				xml.openElement(propertyName + "-list");
				for(String item: (String[])element)
					xml.addElement(propertyName, io.github.jmcleodfoss.util.XMLOutput.safeUTF8String(item));
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
			xml.addElement(propertyName, element instanceof String ? io.github.jmcleodfoss.util.XMLOutput.safeUTF8String((String)element) : element.toString());
	}

	/**	Output the XML for this object to the given printstream
	*	@param	printstream	The printstream to which the XML file should be written as it is built.
	*	@throws io.github.jmcleodfoss.pst.BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws io.github.jmcleodfoss.pst.BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws	io.github.jmcleodfoss.pst.CRCMismatchException			The header's calculated CRC does not match the expected value.
	*	@throws	io.github.jmcleodfoss.pst.DataOverflowException	More data was found than will fit into the number of rows allocated, indicating a probably-corrupt file.
	*	@throws io.github.jmcleodfoss.pst.IncorrectNameIDStreamContentException	either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
	*	@throws	io.github.jmcleodfoss.pst.NameIDStreamNotFoundException	The requested Name ID mapping stream could not be found
	*	@throws	io.github.jmcleodfoss.pst.NotHeapNodeException			A node which was not a heap node was found where a heap node was expected when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NotPropertyContextNodeException	A node which was not a property context node was found where a property context node was expected when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NotTableContextNodeException		A node which was not a table context node was found where a table context node was expected when reading the pst file.
	*	@throws	io.github.jmcleodfoss.pst.NullDataBlockException		A null data block was found when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	io.github.jmcleodfoss.pst.UnimplementedPropertyException	Handling for the property type has not been implemented
	*	@throws	io.github.jmcleodfoss.pst.UnknownClientSignatureException	An unrecognized client signature was found when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnknownPropertyTypeException	The property type was not recognized
	*	@throws io.github.jmcleodfoss.pst.UnparseablePropertyContextException	A bad / corrupt property context was found whe nreading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnparseableTableContextException	A bad / corrupt table context was found when reading the pst file.
	*	@throws java.io.IOException						An I/O error was encoutnered while reading the pst file.
	*	@throws	java.io.UnsupportedEncodingException				An unsupported encoding was found while reading a string in the pst file.
	*/
	void createXML(java.io.PrintStream printstream)
	throws
		io.github.jmcleodfoss.pst.BadXBlockLevelException,
		io.github.jmcleodfoss.pst.BadXBlockTypeException,
		io.github.jmcleodfoss.pst.CRCMismatchException,
		io.github.jmcleodfoss.pst.DataOverflowException,
		io.github.jmcleodfoss.pst.IncorrectNameIDStreamContentException,
		io.github.jmcleodfoss.pst.NameIDStreamNotFoundException,
		io.github.jmcleodfoss.pst.NotHeapNodeException,
		io.github.jmcleodfoss.pst.NotPropertyContextNodeException,
		io.github.jmcleodfoss.pst.NotTableContextNodeException,
		io.github.jmcleodfoss.pst.NullDataBlockException,
		io.github.jmcleodfoss.pst.NullNodeException,
		io.github.jmcleodfoss.pst.UnimplementedPropertyTypeException,
		io.github.jmcleodfoss.pst.UnknownClientSignatureException,
		io.github.jmcleodfoss.pst.UnknownPropertyTypeException,
		io.github.jmcleodfoss.pst.UnparseablePropertyContextException,
		io.github.jmcleodfoss.pst.UnparseableTableContextException,
		java.io.IOException,
		java.io.UnsupportedEncodingException
	{
		io.github.jmcleodfoss.util.XMLOutput xml = new io.github.jmcleodfoss.util.XMLOutput(printstream, true, "UTF-8");
		xml.openElement("pst");
		addFolderContents(xml, pst.getFolderTree(), pst);
		xml.closeElement();
	}

	/**	Should this type of folder be filtered out?
	*	@param	type	The folder class to check (note that not all folders have a class associated with them, but null folder
	*			classes are never passed to this function).
	*	@return	true if folders of this class should be filtered out, false if they should be included.
	*/
	protected boolean folderFilter(final String type)
	{
		return (false);
	}

	/**	Convert a PST file into XML.
	*	@param	args	The command line arguments to the application.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.xml.PSTToXML pst-filename");
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
