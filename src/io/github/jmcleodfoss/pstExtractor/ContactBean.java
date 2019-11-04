package io.github.jmcleodfoss.pstExtractor;

import java.util.ArrayList;
import java.util.List;

/**	The ContactBean class represents a PST contact entry.
*	Note that this is not a "full" bean, in that it does not have any setters; its contents are set by other classes within the
*	same package.
*/
public class ContactBean {

	/**	The contact's name. */
	String name;

	/**	The contact's email addresses. */
	List<String> emailAddresses;

	/**	The contact's phone numbers. */
	List<String> telephoneNumbers;

	/**	Construct a ContactBean object. */
	public ContactBean()
	{
		emailAddresses = new ArrayList<String>();
		telephoneNumbers = new ArrayList<String>();
	}

	/**	Retrieve the name of the contact.
	*
	*	@return The name of the contact.
	*/
	public String getName()
	{
		return name;
	}

	/**	Retrieve the contact's e-mail addresses.
	*
	*	@return	A list of e-mail addresses for this contact.
	*/
	public List<String> getEmailAddresses()
	{
		return emailAddresses;
	}

	/**	Retrieve the contact's telephone numbers.
	*
	*	@return	A list of telephone numbers for this contact.
	*/
	public List<String> getTelephoneNumbers()
	{
		return telephoneNumbers;
	}
}
