package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileFilter;

/** FileFilter for specific extensions */
class ExtensionFileFilter implements FileFilter
{
	/** The extension to filter by */
	private String[] extensions;

	/** Create a FileFilter for the given extenstion
	*	@param	extensions	The extension to filter by.
	*/
	ExtensionFileFilter(final String... extensions)
	{
		this.extensions = extensions.clone();
	}

	/** Does the given File pass the filter?
	*	@param	pathname	The File to check
	*	@return	true if the file's extension matches the extension we are looking for, false otherwise.
	*/
	@Override
	public boolean accept(File pathname)
	{
		String name = pathname.getName();
		int iExt = name.lastIndexOf('.');
		if (iExt < 0)
			return false;

		for (String extension : extensions) {
			if (name.substring(iExt+1).equalsIgnoreCase(extension))
				return true;
		}
		return false;
	}
}
