![MIT License](https://img.shields.io/badge/license-MIT-green) 
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/60a39e709460445f8004a1a4603bdd98)](https://www.codacy.com/gh/Jmcleodfoss/pstreader/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Jmcleodfoss/pstreader&amp;utm_campaign=Badge_Grade)
![Java CI with Maven](https://github.com/Jmcleodfoss/pstreader/workflows/Java%20CI%20with%20Maven/badge.svg)
![Codacy Security Scan](https://github.com/Jmcleodfoss/msgreader/workflows/Codacy%20Security%20Scan/badge.svg) 
![CodeQL](https://github.com/Jmcleodfoss/msgreader/workflows/CodeQL/badge.svg) 
# pstreader
Java library for reading Microsoft Outlook pst and ost files, with ancillary libraries, a Swing application, and a servlet.

## Contents
### pst Library
A library for reading PST files, based on [[MS-PST]: Outlook Personal Folders (.pst) File Format](https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/141923d5-15ab-4ef1-a524-6dce75aae546).
See [pst Library README](pst/README.md)

### LocaleBean
A bean for localization of Java Server Faces web applications.
See [localebean README](localebean/README.md)
Note: this artifact is not in Maven Central so its Javadoc is not publicly available.

### swingutil Library
A library of useful Java Swing functions used by the pst library. It is separate because I wanted the pst library to be as independent of Swing as possibly, but still able to be used by Swing applications.
See [swingutil library README](swingutil/README.md)

### util Library
A utility library for with some useful functions used by related applications.
See [util library README](util/README.md)

### XML Utilities
These are all in the xml.jar package.
See [xml library README](xml/README.md).
Note: this artifact is not in Maven Central so its Javadoc is not publicly available.
See also the xslt template files in the xslt directory.

### Explorer Application
A stand-alone example application for browsing PST files
See [explorer README](explorer/README.md)

### pstExtractor Web Servlet
A JSF servlet which allows you to upload a file and then browse through it
See [pstExtractor README](pstExtractor/README.md).
Note: this artifact is not in Maven Central so its Javadoc is not publicly available.

### XSLT Templates
A set of xslt transformation templates to convert output from [io.github.jmcledofoss.xml.PSTIPFFolderTypeToXML](xml/src/main/io/github/jmcleodfoss/xml/PSTIPFFolderTypeToXML.java) to html.
See [xslt README](xslt/READM.md).
