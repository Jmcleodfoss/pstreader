#LocaleBean
Convenience bean for localization for Java Server Faces.

The model used is to have different versions of a file for different languates, with the locale information appended to the filename, as in the examples below:
*   filename.xhtml, the default (English US) file
*   filename\_FR.xhtml, the French translation of the file
*   filename\_FR\_CA.xhtml, the French (Canada) translation of the file
*   filename\_FR\_FR.xhtml, the French (France) translation of the file

Suffixes checked are Language, Scrupt, Country, and Variant.

The function `public static String getLocalizedFilename(final String fn)` looks for the most specified version of a file, ie.e with the most number of parts.
