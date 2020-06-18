package io.github.jmcleodfoss.explorer;

import java.util.EventListener;

import io.github.jmcleodfoss.pst.PST;

/**	The NewFileListener interface is implemented by objects which wish to know when a new file has been loaded. */
interface NewFileListener extends EventListener
{
	/**	This function is called to notify NewFileListener objects that a new file has been loaded.
	*	@param	pst	The PST object loaded.
	*/
	void fileLoaded(final PST pst);
}
