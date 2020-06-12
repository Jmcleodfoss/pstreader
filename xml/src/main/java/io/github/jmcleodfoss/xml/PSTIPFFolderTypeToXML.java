package io.github.jmcleodfoss.xml;

/**	The PSTIPFFolderTypeToXML class extracts the contents of a given folder type from a PST file, outputting it is XML.
*	<p><strong>Use</strong><p>
*	<code>java io.github.jmcleodfoss.pst.PSTIPFFolderTypeToXML pst-file.pst folder-class-name</code><p>
*	The list of known folder classes may be found by issuing the command without parameters:
*	<code>java io.github.jmcleodfoss.pst.PSTIPFFolderTypeToXML</code>
**/
class PSTIPFFolderTypeToXML extends PSTToXML
{
	/** The list of known folder types, as a mapping from human-readable strings to those found in the protocol. */
	static final java.util.Map<String, String> knownFolderTypes = new java.util.HashMap<String, String>();
	static {
		final int IPFPrefixLength = 4;
		for (java.util.Iterator<String> iterator = io.github.jmcleodfoss.pst.IPF.iterator(); iterator.hasNext(); ){
			String folderType = iterator.next();
			knownFolderTypes.put(folderType.substring(IPFPrefixLength).toLowerCase(), folderType);
		}
	}

	/**	The folder class to emit. */
	final String includedFolderClass;

	/**	Construct an object to extract folders of the given class from the PST file with the given name.
	*	@param	fn			The file name of the PST file to process.
	*	@param	includedFolderClass	The folder class to extract.
	*	@throws	io.github.jmcleodfoss.pst.NotHeapNodeException			A node which was not a heap node was found where a heap node was expected when reading the pst file.
	*	@throws	io.github.jmcleodfoss.pst.NotPSTFileException			The named file is not a pst file.
	*	@throws io.github.jmcleodfoss.pst.NotPropertyContextNodeException	A node which was not a property context node was found where a property context node was expected when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.NotTableContextNodeException		A node which was not a table context node was found where a table context node was expected when reading the pst file.
	*	@throws	io.github.jmcleodfoss.pst.NullDataBlockException		A null data block was found when reading the pst file.
	*	@throws	io.github.jmcleodfoss.pst.UnknownClientSignatureException	An unrecognized client signature was found when reading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnparseablePropertyContextException	A bad / corrupt property context was found whe nreading the pst file.
	*	@throws io.github.jmcleodfoss.pst.UnparseableTableContextException	A bad / corrupt table context was found when reading the pst file.
	*	@throws java.io.IOException						An I/O error was encoutnered while reading the pst file.
	*/
	PSTIPFFolderTypeToXML(final String fn, final String includedFolderClass)
	throws
		io.github.jmcleodfoss.pst.UnknownClientSignatureException,
		io.github.jmcleodfoss.pst.NotHeapNodeException,
		io.github.jmcleodfoss.pst.NotPSTFileException,
		io.github.jmcleodfoss.pst.NotPropertyContextNodeException,
		io.github.jmcleodfoss.pst.NotTableContextNodeException,
		io.github.jmcleodfoss.pst.NullDataBlockException,
		io.github.jmcleodfoss.pst.UnparseablePropertyContextException,
		io.github.jmcleodfoss.pst.UnparseableTableContextException,
		java.io.IOException//,
//		javax.xml.parsers.ParserConfigurationException
	{
		super(fn);
		this.includedFolderClass = includedFolderClass;
	}

	/**	Emit folders only of the desired type.
	*	@param	type	The folder class of the folder currently being processed.
	*	@return	true if folders with this class should be emitted, false if they should not
	*/
	@Override
	protected boolean folderFilter(final String type)
	{
		return !type.equals(includedFolderClass);
	}

	/**	List the known folder types. */
	private static void listKnownFolderTypes()
	{
		for (java.util.Iterator<String> iterator = knownFolderTypes.keySet().iterator(); iterator.hasNext(); )
			System.out.printf("\t%s\n", iterator.next());
	}

	/**	Extract the contents of any folders of the given type.
	*	@param	args	The command line arguments to the application.
	*/
	public static void main(final String[] args)
	{
		if (args.length < 2) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.xml.PSTIPFFolderTypeToXML pst-filename folder-type");
			System.out.println("\nKnown folder types are:");
			listKnownFolderTypes();
			System.exit(1);
		}

		final String filename = args[0];
		final String folderClass = args[1];

		if (!knownFolderTypes.containsKey(folderClass)) {
			System.out.printf("Folder class %s is not recognized. Known folder classes are:\n", folderClass);
			listKnownFolderTypes();
			System.exit(1);
		}

		try {
			final PSTIPFFolderTypeToXML pstToXml = new PSTIPFFolderTypeToXML(filename, knownFolderTypes.get(folderClass));
			pstToXml.createXML(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
