package io.github.jmcleodfoss.xml;

/**	The UnknownFolderTypeException is thrown when a folder type in io.github.jmcleodfoss.pst.IPF is not found in the list of known folder types
*	@see	PSTIPFFolderTypeToXML
*/
public class UnknownFolderTypeException extends RuntimeException
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a UnknownFolderTypeException. */
	UnknownFolderTypeException(String folderType)
	{
		super(String.format("IPF Folder Type %s not implemented in PSTIPFolderTypeToXML", folderType));
	}
}

