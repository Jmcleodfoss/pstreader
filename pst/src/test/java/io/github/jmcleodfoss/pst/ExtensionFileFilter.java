package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileFilter;

/** FileFilter for specific extensions */
class ExtensionFileFilter implements FileFilter
{
	/** The extension to filter by */
	private String extension;

	/** Create a FileFilter for the given extenstion
	*	@param	extension	The extension to filter by.
	*/
	ExtensionFileFilter(final String extension)
	{
		this.extension = extension;
	}

	/** Does the given File pass the filter?
	*	@param	pathname	The File to check
	*	@return	true if the file's extension matches the extension we are looking for, false otherwise.
	*/
	public boolean accept(File pathname)
	{
		String name = pathname.getName();
		int iExt = name.lastIndexOf('.');
		return iExt >= 0 && name.substring(iExt+1).equalsIgnoreCase(extension);
	}
}
