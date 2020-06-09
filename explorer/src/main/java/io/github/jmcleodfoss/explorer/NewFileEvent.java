package io.github.jmcleodfoss.explorer;

import java.util.EventObject;

import io.github.jmcleodfoss.pst.PST;

/**	This event used to notify objecs which implement the NewFileListener interface that a new file has been loaded. */
@SuppressWarnings("serial")
class NewFileEvent extends EventObject {

	/**	The new PST object for the newly-loaded file. */
	PST pst;

	/**	Create a NewFileEvent object.
	*	@param	source	The object from which the event originated.
	*	@param	pst	The new PST object.
	*/
	NewFileEvent(Object source, PST pst)
	{
		super(source);
		this.pst = pst;
	}
}
