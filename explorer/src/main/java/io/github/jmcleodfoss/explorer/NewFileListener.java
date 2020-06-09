package io.github.jmcleodfoss.explorer;

import java.util.EventListener;

/**	The NewFileListener interface is implemented by objects which wish to know when a new file has been loaded. */
interface NewFileListener extends EventListener {

	/**	This function is called to notify NewFileListener objects that a new file has been loaded.
	*	@param	e	An event describing of the new file.
	*/
	void fileLoaded(NewFileEvent e);
}
