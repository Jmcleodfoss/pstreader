package io.github.jmcleodfoss.localebean;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**	The LocaleBean provides a mechanism to retrieve the locale-related information for use in JSF pages. */
@Named("localeBean")
@ViewScoped
public class LocaleBean implements Serializable {

	/**     The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Determine whether a given URL refers to something which exists (by attempting to open a stream to it).
	*	@param	urlName	The name of the URL to look for.
	*	@return	true if the given URL could be opened as a java.io.InputStream, false if it could not.
	*/
	private boolean urlExists(final String urlName)
	{
		try {
			URI uri = new URI(urlName);
			uri.toURL().openStream();
			return true;
		} catch (IOException e) {
			return false;
		} catch (URISyntaxException e) {
			return false;
		}
	}

	/**	Return the most-specific filename which exists for the given locale, using the ResourceBundle rules to create the
	*	filename using the current locale's country, script, language, and variant (note that the script is not yet supported).
	*	@param	fn	The filename to create a localized version of.
	*	@return	The most specific localized version of the given filename, if present, otherwise the file named by the filename.
	*/
	@SuppressWarnings("PMD.NPathComplexity")
	public String getLocalizedFilename(final String fn)
	{
		final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale.equals(Locale.getDefault()))
			return fn;

		final int iExtension = fn.lastIndexOf('.');
		final String fnPathAndFile = iExtension >= 0 ? fn.substring(0, iExtension) : fn;
		final String fnExtension = iExtension >= 0 ? fn.substring(iExtension) : "";

		final String language = locale.getLanguage();
		final String script = locale.getScript();
		final String country = locale.getCountry();
		final String variant = locale.getVariant();
		
		if (language.equals("") && script.equals("") && country.equals("") && variant.equals(""))
			return fn;
		
		if (script.equals("")) {
			if (variant.equals("")) {
				StringBuilder sb = new StringBuilder(fnPathAndFile);
				sb.append('_').append(language);
				sb.append('_').append(script);
				sb.append('_').append(country);
				sb.append('_').append(variant);
				sb.append(fnExtension);
				final String qualifiedName = sb.toString();
				if (urlExists(qualifiedName))
					return qualifiedName;
			}

			if (country.equals("")) {
				StringBuilder sb = new StringBuilder(fnPathAndFile);
				sb.append('_').append(language);
				sb.append('_').append(script);
				sb.append('_').append(country);
				sb.append(fnExtension);
				final String qualifiedName = sb.toString();
				if (urlExists(qualifiedName))
					return qualifiedName;
			}

			if (language.equals("")) {
				StringBuilder sb = new StringBuilder(fnPathAndFile);
				sb.append('_').append(language);
				sb.append('_').append(script);
				sb.append(fnExtension);
				final String qualifiedName = sb.toString();
				if (urlExists(qualifiedName))
					return qualifiedName;
			}
		} else {
			if (variant.equals("")) {
				StringBuilder sb = new StringBuilder(fnPathAndFile);
				sb.append('_').append(language);
				sb.append('_').append(country);
				sb.append('_').append(variant);
				sb.append(fnExtension);
				final String qualifiedName = sb.toString();
				if (urlExists(qualifiedName))
					return qualifiedName;
			}

			if (country.equals("")) {
				StringBuilder sb = new StringBuilder(fnPathAndFile);
				sb.append('_').append(language);
				sb.append('_').append(country);
				sb.append(fnExtension);
				final String qualifiedName = sb.toString();
				if (urlExists(qualifiedName))
					return qualifiedName;
			}

			if (language.equals("")) {
				StringBuilder sb = new StringBuilder(fnPathAndFile);
				sb.append('_').append(language);
				sb.append(fnExtension);
				final String qualifiedName = sb.toString();
				if (urlExists(qualifiedName))
					return qualifiedName;
			}
		}

		return fn;
	}
}
