package io.github.jmcleodfoss.pst;

/**	The DataDefinition class encapsulates definitions used for reading values from a data stream. */
class DataDefinition {

	/**	Logger for data reading */
	static java.util.logging.Logger logger = Debug.getLogger("io.github.jmcleodfoss.pst.DataDefinition");

	/**	The IncompleteFieldException is used to signal that it was not possible to read in all the required bytes for a field */
	static class IncompleteFieldException extends RuntimeException {

		/**	The serial version UID is required since the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	Create an IncompleteFieldException describing the incomplete field.
		*
		*	@param	component	The name of the field which was not read fully.
		*	@param	expected	The number of bytes expected to be read.
		*	@param	found		The number of bytes actually read.
		*/
		public IncompleteFieldException(final String component, final int expected, final int found){
			super(component + " should have " + expected + " bytes, but " + found + " were found");
		}
	}

	/**	The name under which the data for this field is to be stored. */
	final String name;

	/**	An object describing how to read and display the data for this field. */
	final DataType description;

	/**	Whether the data should be saved or discarded. */
	private final boolean fSave;

	/**	Create an object to read in data with the given description, saving it under the given name if fSave is true.
	*
	*	@param	name		The field name with which the data will be stored and retrieved.
	*	@param	description	The description of how to read in the field.
	*	@param	fSave		A flag indicating whether the data should be saved or skipped.
	*/
	public DataDefinition(final String name, final DataType description, final boolean fSave)
	{
		this.name = name;
		this.description = description;
		this.fSave = fSave;
	}

	/**	Create an object to skip over data with the given description.
	*
	*	@param	name		The field name with which the data would be stored and retrieved (used for logging only in this
	*				case).
	*	@param	description	The description of how to read in the field.
	*/
	public DataDefinition(final String name, final DataType description)
	{
		this(name, description, false);
	}

	/**	Read in or skip a value described by description from stream, storing the result in data if necessary.
	*
	*	@param	description	The description of how to read in the field.
	*	@param	byteBuffer	The input data stream from which to read the field.
	*	@param	data		The location in which to store the field.
	*/
	static void read(final DataDefinition description, java.nio.ByteBuffer byteBuffer, java.util.IdentityHashMap<String, Object> data)
	throws
		java.io.IOException
	{
		final boolean f_debug = logger.isLoggable(java.util.logging.Level.FINER);
		if (description.fSave || f_debug) {
			logger.log(java.util.logging.Level.FINEST, (description.fSave ? "read" : "skip") + " " + description);
			final Object value = description.description.read(byteBuffer);
			logger.log(java.util.logging.Level.FINER, (description.fSave ? "read" : "skip") + " " + description.name + ": " + description.description.makeString(value));
			if (description.fSave)
				data.put(description.name, value);
		} else {
			byteBuffer.position(byteBuffer.position() + description.description.size());
		}
	}

	/**	Get the aggregate size in bytes of the data represented by the data array.
	*
	*	@param	data	The list of data definitions describing the data for which to return the size.
	*
	*	@return	The size, in bytes, of the data described by the given data description array.
	*/
	static int size(final DataDefinition[] data)
	{
		int s = 0;
		for (final DataDefinition d : data)
			s += d.description.size();
		return s;
	}

	/**	Provide a text description of this object.
	*
	*	@return	A String describing this data definition object.
	*/
	@Override
	public String toString()
	{
		return String.format("%s %d %ssaved", name, description.size(), fSave ? "" : "not ");
	}
}
