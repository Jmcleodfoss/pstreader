package io.github.jmcleodfoss.pst;

/**	The DataContainer class may be used to manage a set up DataDefinition objects, saving all values read in a map member.
*	It provides utility methods for retrieving various useful types.
*	Note that it is based on java.util.IdentityHashMap, so it is necessary to "get" a value using the identical argument used to "put" it.
*	Changing from HashMap to IdentityHashMap reduced run-time by ~33% for the original test PST file.
*/
class DataContainer extends java.util.IdentityHashMap<String, Object>
{
	/**	The IncompleteInitializationException indicates that a required component is not available at the time it is required. */
	static class IncompleteInitializationException extends RuntimeException
	{
		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	Create an IncompleteInitializationException for the given field.
		*	@param	component	The name of the component requested but not found.
		*/
		public IncompleteInitializationException(final String component)
		{
			super("Incomplete initialization: " + component + " not yet available");
		}
	}

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Constructor with default maximum size .*/
	DataContainer()
	{
		super();
	}

	/**	Constructor with a known expected maximum number of entries.
	*	@param	expectedMaxSize	The number of entries which will be put in the underlying collection.
	*/
	DataContainer(final int expectedMaxSize)
	{
		super(expectedMaxSize);
	}

	/**	Return an unsigned 8-bit value.
	*	@param	name	The name of the value to return.
	*	@return	An integer containing the unsigned 8-bit value.
	*/
	int getUInt8(String name)
	{
		return 0xff & (Byte)get(name);
	}

	/**	Read in all descriptions from the given data stream.
	*	@param	byteBuffer	The input stream from which to read the data.
	*	@param	description	The list of descriptions of data to be read.
	*	@throws	java.io.IOException	An I/O problem was encountered while reading in the requested data.
	*/
	void read(java.nio.ByteBuffer byteBuffer, final DataDefinition... description)
	throws
		java.io.IOException
	{
		for (DataDefinition d : description)
			DataDefinition.read(d, byteBuffer, this);
	}

	/**	Read in all descriptions from the given data stream.
	*	@param	byteBuffer	The input stream from which to read the data.
	*	@param	description	The list of descriptions of data to be read.
	*	@throws	java.io.IOException	An I/O problem was encountered while reading in the requested data.
	*/
	void read(java.nio.ByteBuffer byteBuffer, final DataDefinition[]... description)
	throws
		java.io.IOException
	{
		for (DataDefinition[] d : description)
			read(byteBuffer, d);
	}

	/**	Confirm the given field was read in; throw an exception if it wasn't.
	*	@param	nm_field	The name of the field to check.
	*/
	protected void validateFieldExists(final String nm_field)
	{
		if (!containsKey(nm_field))
			throw new IncompleteInitializationException(nm_field);
	}

	/**	Format and return a human-readable representation of this DataContainer with the property names and hex byte strings for each.
	*	@return	A String showing the contents of this DataContainer object.
	*/
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		java.util.Iterator<java.util.Map.Entry<String, Object>> iterator = entrySet().iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry<String, Object> entry = iterator.next();

			s.append('\n');
			s.append("\t");
			s.append(entry.getKey());
			s.append(':');
			s.append(' ');
			s.append(entry.getValue().toString());
		}
		return s.toString();
	}
}
