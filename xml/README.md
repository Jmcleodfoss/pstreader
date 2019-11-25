# XML Utilities
All of the examples below assume that io.github.jmcleodfoss xml.jar, pst.jar, and util.jar are in the classpath.

## PSTIPFolderTypeToXML
Extract data matching a given folder type from a PST file to an XML file
`java io.github.jmcleodfoss.xml.PSTIPFFolderTypeToXML <pst-filename> <IPF Folder type, one of {appointment, contact, journal, stickynote, task, note}>`

## PSTToXML
Extract contents of a PST file to an XML file
`java io.github.jmcleodfoss.xml.PSTToXML <pst-filename>`

## XSLTProcessor
Java native XSLT transformer
`java io.github.jmcleodfoss.xml.XSLTProcessor <xslt-file> <xml-file>`

This is deprecated. I use [xlstproc](http://xmlsoft.org/XSLT/xsltproc.html) to apply xls stylesheets.
