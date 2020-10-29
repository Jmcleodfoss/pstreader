package io.github.jmcleodfoss.pst;

/**	The UnimplementedPropertyTypeException indicates that a property type was encountered that the software cannot deal with
*	(typically because no examples of the property type have been encountered so it could not be tested effectively)
*	@see	DataType
*/
public class UnimplementedPropertyTypeException extends Exception
{
	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create an UnimplementedPropertyTypeException object
	*	@param	type	The name of the type which is not implemented.
	*/
	UnimplementedPropertyTypeException(final String type)
	{
		super("Unimplemented property type " + type);
	}
}
