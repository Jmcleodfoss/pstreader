# pstreader
Java library for reading Microsoft Outlook pst and ost files.

## Contents
### pst Library
A library for reading PST files, based on [[MS-PST]: Outlook Personal Folders (.pst) File Format](https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/141923d5-15ab-4ef1-a524-6dce75aae546).
See [pst Library README](pst/README.md)

### LocaleBean
A bean for localization of Java Server Faces web applications.
See [localebean READM](localebean/README.md)

### swingutil Library
A library of useful Java Swing functions used by the pst library. It is separate because I wanted the pst library to be as independent of Swing as possibly, but still able to be used by Swing applications.
See [swingutil library README](swingutil/README.md)

### util Library
A utility library for with some useful functions used by related applications.
See [util library README](util/README.md)

### XML Utilities
These are all in the xml.jar package.
See [xml library README](xml/README.md)

### Explorer Application
A stand-alone example application for browsing PST files

### pstExtractor Web Servlet
A servlet which allows you to upload a file and then browse through it

### WhoLovesYou Web Servlet (under construction)
A servlet which allows you to upload a file and then returns all senders who have sent an e-mail where you are the only recipient, ordered by the number of e-mails from that sender.
See [wholovesyou README](wholovesyou/README.md)
