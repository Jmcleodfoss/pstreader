package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileFilter;

class ExtensionFileFilter implements FileFilter
{
	private String extension;

	ExtensionFileFilter(final String extension)
	{
		this.extension = extension;
	}

	public boolean accept(File pathname)
	{
		String name = pathname.getName();
		int iExt = name.lastIndexOf('.');
		return iExt >= 0 && name.substring(iExt+1).equalsIgnoreCase(extension);
	}
}
