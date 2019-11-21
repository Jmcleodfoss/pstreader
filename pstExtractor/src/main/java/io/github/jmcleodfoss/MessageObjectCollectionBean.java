package io.github.jmcleodfoss.pstExtractor;

import java.util.ArrayList;
import java.util.List;

/**	The MessageObjectCollectionBean summarizes information about all appointment folders and the appointment request.
*	Note that this is not a "full" bean, in that it does not have any setters; its contents are set by other classes within the
*	same package.
*/
public class MessageObjectCollectionBean<B> {

	/**	The folders for this type of message object. */
	List<FolderBean<B>> folders;

	/**	Flag indicating whether objects of this type were requested. */
	boolean requested;

	public MessageObjectCollectionBean()
	{
		folders = new ArrayList<FolderBean<B>>();
		requested = false;
	}

	/**	Retrieve the list of folders from this collection of message object containers.
	*
	*	@return	The list of folders associated with this type of message object.
	*/
	public List<FolderBean<B>> getFolders()
	{
		return folders;
	}

	/**	Determine whether message objects of this type were requested. This suppression of output for unrequested message
	*	object types.
	*
	*	@return	true if there was a request for this type of message object, false if there was no request.
	*/
	public boolean isRequested()
	{
		return requested;
	}
}
