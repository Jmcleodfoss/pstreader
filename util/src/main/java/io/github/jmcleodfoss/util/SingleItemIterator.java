package io.github.jmcleodfoss.util;

/**	The SingleItemIterator Iterator class is a trivial iterator for a list known to contain a single item. */
public class SingleItemIterator<T> implements java.util.Iterator<Object>
{
	/**	The data to return. */
	private final T data;

	/**	The fNext flag indicates whether there is another item. It is true until the item is returned, when it becomes false. */
	private boolean fNext;

	/**	Create a trivial iterator for the given block.
	*	@param	data	The date to "iterate" through
	*/
	public SingleItemIterator(T data)
	{
		fNext = true;
		this.data = data;
	}

	/**	Determine if there is another item to return.
	*	@return	true if the data hasn't been returned, false if it has.
	*/
	@Override public boolean hasNext()
	{
		return fNext;
	}

	/**	Retrieve the data.
	*	@return	The data for the single item in the list.
	*/
	@Override public T next()
	{
		if (!fNext)
			throw new java.util.NoSuchElementException();
		fNext = false;
		return data;
	}

	/**	The remove function is not supported by the SingleItemIterator iterator. */
	@Override public void remove()
	{
		throw new UnsupportedOperationException("remove not suported");
	}
}
