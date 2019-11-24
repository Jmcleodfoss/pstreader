# pstreader
Java library for reading Microsoft Outlook pst and ost files.

## Contents
### pst Library
A library for reading PST files, based on [[MS-PST]: Outlook Personal Folders (.pst) File Format](https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/141923d5-15ab-4ef1-a524-6dce75aae546).
See [pst Library README](pst/README.md)

### util Library
A utility library for with some useful functions used by related applications.
See [util library README](util/README.md)

### swingutil Library
A library of useful Java Swing functions used by the pst library. It is separate because I wanted the pst library to be as independent of Swing as possibly, but still able to be used by Swing applications.

### Explorer Application
A stand-alone example application for browsing PST files

### pstExtractor Web Servlet
A servlet which allows you to upload a file and then browse through it

### WhoLovesYou Web Servlet (under construction)
A servlet which allows you to upload a file and then returns all senders who have sent an e-mail where you are the only recipient, ordered by the number of e-mails from that sender.

### XML Utilitys
These are all in the xml.jar package. To run them, it is necessary to specify the class and arguments when invoking Java. The xml.jar package is self-contained; it includes the contents of pst.jar, swingutil.jar, and util.jar libraries.

#### PSTIPFolderTypeToXML
Extract data matching a given folder type from a PST file to an XML file
`java -cp xml.jar io.github.jmcleodfoss.xml.PSTIPFFolderTypeToXML <pst-filename> <IPF Folder type, one of {appointment, contact, journal, stickynote, task, note}>`

#### PSTToXML
Extract contents of a PST file to an XML file
`java -cp xml.jar io.github.jmcleodfoss.xml.PSTToXML <pst-filename>`

### XSLTProcessor
Java native XSLT transformer
`java -cp xml.jar io.github.jmcleodfoss.xml.XSLTProcessor <xslt-file> <xml-file>`
