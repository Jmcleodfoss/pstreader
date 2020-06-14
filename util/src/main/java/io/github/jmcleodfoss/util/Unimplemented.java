package io.github.jmcleodfoss.pst;

/**	The Unimplemented annotation flags methods which have not yet been implemented, and member variables which are not yet used.
*/
public @interface Unimplemented
{
	/**	The Priority enum indicates the priority with with the method or variable should be implemented.
	*	@see	#priority
	*/
	public enum Priority {
		NOT_NEEDED,
		LOW,
		MODERATE,
		HIGH,
		URGENT
	}

	/**	The priority member indicates the priority with which the specific function should be implemented.
	*	@see	Priority
	*	@return	The default Priority
	*/
	Priority priority() default Priority.MODERATE;
}
