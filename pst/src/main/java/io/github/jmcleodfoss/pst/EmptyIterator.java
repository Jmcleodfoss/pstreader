package io.github.jmcleodfoss.pst;

/**	The EmptyIterator class is an iterator for empty lists. It is useful in some circumstances when one wishes to avoid checking
*	for null in client code.
*/
public class EmptyIterator implements java.util.Iterator<Object>
{
	/**	A generic EmptyIterator object which may be used by anyone, and has no state, so it can be shared.
	*/
	public static final EmptyIterator iterator = new EmptyIterator();

	/**	Are there any more elements to return? Note that there are never any more elements in an EmptyIterator.
	*	return	false, always (the EmptyIterator never contains any elements, by definition.
	*/
	public boolean hasNext()
	{
		return false;
	}

	/**	Get the next element. This function should never be called (since hasNext always returns true). In fact, calling this
	*	function is a programming error, and the UnsupportedOperationExcpetion will be thrown.
	*	returns	Nothing - calling this function results in an exception being thrown.
	*/
	public Object next()
	{
		throw new UnsupportedOperationException("remove not suported");
	}

	/**	The remove function is not supported by the EmptyIterator iterator.
	*/
	public void remove()
	{
		throw new UnsupportedOperationException("remove not suported");
	}
}
