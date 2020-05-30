# pstreader
Java library for reading Microsoft Outlook pst and ost files, with ancillary libraries, a Swing application, and a servlet.

## Contents
### pst Library
A library for reading PST files, based on [[MS-PST]: Outlook Personal Folders (.pst) File Format](https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/141923d5-15ab-4ef1-a524-6dce75aae546).
See
* [pst Library README](pst/README.md)
* [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/pst/1.1.0/index.html)
* [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/pst/1.1.0/pom)
* [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/pst/1.1.0/)

### LocaleBean
A bean for localization of Java Server Faces web applications.
See [localebean README](localebean/README.md)
Note: this artifact is not in Maven Central so its Javadoc is not publicly available.

### swingutil Library
A library of useful Java Swing functions used by the pst library. It is separate because I wanted the pst library to be as independent of Swing as possibly, but still able to be used by Swing applications.
See
* [swingutil library README](swingutil/README.md)
* [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/swingutil/1.1.0/index.html)
* [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/swingutil/1.1.0/pom)
* [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/swingutil/1.1.0/)

### util Library
A utility library for with some useful functions used by related applications.
See
* [util library README](util/README.md)
* [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/util/1.1.0/index.html)
* [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/util/1.1.0/pom)
* [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/util/1.1.0/)

### XML Utilities
These are all in the xml.jar package.
See [xml library README](xml/README.md).
Note: this artifact is not in Maven Central so its Javadoc is not publicly available.

### Explorer Application
A stand-alone example application for browsing PST files
See
* [explorer README](explorer/README.md)
* [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/explorer/1.1.0/index.html)
* [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/explorer/1.1.0/pom)
* [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/explorer/1.1.0/)

### pstExtractor Web Servlet
A servlet which allows you to upload a file and then browse through it
See [pstExtractor README](pstExtractor/README.md).
Note: this artifact is not in Maven Central so its Javadoc is not publicly available.
