package io.github.jmcleodfoss.pst;

/** Exception indicating we ran out of memory doing a test.
*   This exception can be easily handled, given we can make sufficient memory available to the Java VM, so we treat it specially.
*   Use according to this paradigm:
*   <pre>
*   {@code
*   try {
*       // do stuff
*   } catch (IOException e) {
*   	if (e.toString().equals("java.io.IOException: Cannot allocate memory")) {
*   		throw new InsufficientMemoryException(e);
*   	}
*   	throw e;
*   }
*   }
*   </pre>
*/
@SuppressWarnings("serial")
class InsufficientMemoryException extends Exception {
	/** Create an InsufficientMemoryException
	*	@param	e	The exception which indicates the insufficient memory condition.
	*/
	InsufficientMemoryException(Exception e)
	{
		super(e);
	}
}
