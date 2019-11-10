# pstreader
Java library for reading Microsoft Outlook pst and ost files.

## Contents
### pst Library
A library for reading PST files, based on [[MS-PST]: Outlook Personal Folders (.pst) File Format](https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/141923d5-15ab-4ef1-a524-6dce75aae546).

### util Library
A utility library used by pst.jar primarily for:
* To create a hexadecimal strings from an array of bytes ultimately for display, e.g. io.github.jmcleodfoss.pst.BlockBase.toString() [BlockBase.java](https://github.com/Jmcleodfoss/pstreader/blob/master/src/io/github/jmcleodfoss/pst/BlockBase.java)
* To annotate unimplemented functionality, e.g. io.github.jmcleodfoss.pst.Attachment.main(String[]) in [Attachment.java](https://github.com/Jmcleodfoss/pstreader/blob/master/src/io/github/jmcleodfoss/pst/Attachment.java)
* To construct longs from byte, e.g. io.github.jmcleodfoss.BTreeOnHeap.pst.RecordBase.key() in [BTreeOnHeap.java](https://github.com/Jmcleodfoss/pstreader/blob/master/src/io/github/jmcleodfoss/pst/BTreeOnHeap.java)
* To create an appropriately-formatted separator for text output, e.g. io.github.jmcleod.pst.BTreeOnHeap.main(String[]) in [BTreeOnHeap.java](https://github.com/Jmcleodfoss/pstreader/blob/master/src/io/github/jmcleodfoss/pst/BTreeOnHeap.java)
* To provide a trivial iterator for iterating through zero items, e.g. io.github.jmcleod.pst.TableContext.iterator() in [TableContext.java](https://github.com/Jmcleodfoss/pstreader/blob/master/src/io/github/jmcleodfoss/pst/TableContext.java)
Time will tell whether this will be broken out into a completely separate libary or merged into pst.jar

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
`java -cp xml.jar io.github.jmcleodfoss.xml.PSTIPFFolderTypeToXML <pst-filename> <IPF Folder type, one of {Appointment, Contact, Journal, StickyNote, Task, Note}>`

#### PSTToXML
Extract contents of a PST file to an XML file
`java -cp xml.jar io.github.jmcleodfoss.xml.PSTToXML <pst-filename>`

### XSLTProcessor
Java native XSLT transformer
`java -cp xml.jar io.github.jmcleodfoss.xml.XSLTProcessor <xslt-file> <xml-file>`

## Structure of 3rd Party library files
This is temporary until I update this to use Maven. It is captured in the [lib.properties](https://github.com/Jmcleodfoss/pstreader/blob/master/lib.properties) file.

third-party/apache/commons-fileupload/commons-fileupload-1.2.2/lib:
commons-fileupload-1.2.2.jar

third-party/apache/commons-io/commons-io-2.1:
commons-io-2.1.jar

third-party/apache/jstl/jstl-1.2:
jstl-1.2.jar

third-party/apache/myfaces/myfaces-core-2.1.4-bin/lib:
commons-beanutils-1.8.3.jar
commons-codec-1.3.jar
commons-collections-3.2.jar
commons-digester-1.8.jar
commons-logging-1.1.1.jar
myfaces-api-2.1.4.jar
myfaces-bundle-2.1.4.jar
myfaces-impl-2.1.4.jar

third-party/apache/myfaces/myfaces-core-2.3.4-bin/lib:
commons-beanutils-1.9.3.jar
commons-collections-3.2.2.jar
commons-digester-1.8.jar
commons-logging-1.1.1.jar
myfaces-api-2.3.4.jar
myfaces-bundle-2.3.4.jar
myfaces-impl-2.3.4.jar

third-party/apache/tomahawk/tomahawk20-1.1.11/lib:
tomahawk20-1.1.11.jar

third-party/jquery/1.7.1:
jquery-1.7.1.min.js

third-party/junit:
junit-4.10.jar

third-party/oracle/glassfish:
el-impl-2.2.jar

third-party/privileged-accessor:
privilegedAccessor_1.0.2.jar
