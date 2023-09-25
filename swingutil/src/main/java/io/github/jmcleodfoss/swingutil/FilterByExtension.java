package io.github.jmcleodfoss.swingutil;

/**	Filter for extensions (Java 6.0 has this built in). 
 *	@deprecated
 *	This is superseded by <a href="https://docs.oracle.com/javase/7/docs/api/javax/swing/filechooser/FileNameExtensionFilter.html">javax.swing.filechooser.FileNameExtensionFilter</a>
 */
@Deprecated
public class FilterByExtension extends javax.swing.filechooser.FileFilter {

	/**	The extensions to accept. */
	private final java.util.Vector<String> extensions;

	/**	The file type description. */
	private final String descr;

	/**	Create a filter by extension.
	*	@param	descr		The description of the file type.
	*	@param	extensions	The list of extensions to accept.
	*/
	public FilterByExtension(String descr, String... extensions)
	{
		this.extensions = new java.util.Vector<String>(extensions.length);
		for (int i = 0; i < extensions.length; ++i)
			this.extensions.add(extensions[i]);
		this.descr = descr;
	}

	/**	Is the given file passed by the filter?
	*	@param	file	The file to check to see if its extension is contained in the list of filters.
	*	@return	true if the given file's extension is in the list of extensions, false otherwise.
	*/
	@Override
	public boolean accept(java.io.File file)
	{
		if (file.isDirectory())
			return true;

		int extSep = (int)'.';
		String filePath = file.getPath();
		int indexSep = filePath.lastIndexOf(extSep);
		if (indexSep == -1)
			return extensions.size() == 0;

		++indexSep;
		String fileExt = filePath.substring(indexSep);
		if (fileExt.length() == 0)
			return extensions.size() == 0;

		return extensions.contains(fileExt);
	}

	/**	Return the description of the file type.
	*	@return	A description of the file type this filter applies to.
	*/
	@Override
	public String getDescription()
	{
		return descr;
	}
}
