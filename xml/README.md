# XML Utilities
All of the examples below assume that io.github.jmcleodfoss xml.jar, pst.jar, and util.jar are in the classpath.

## PSTIPFolderTypeToXML
Extract data matching a given folder type from a PST file to an XML file
`java io.github.jmcleodfoss.xml.PSTIPFFolderTypeToXML pst-filename IPF-Folder-type`
where IPF-folder-type is one of:
* appointment
* contact
* contact.moc.imcontactlist
* contact.moc.quickcontacts
* journal
* note
* note.outlookhomepage
* outlook.reminder
* stickynote
* task
* note

## PSTToXML
Extract contents of a PST file to an XML file
`java io.github.jmcleodfoss.xml.PSTToXML pst-filename`

## XSLTProcessor
Java native XSLT transformer
`java io.github.jmcleodfoss.xml.XSLTProcessor xslt-file xml-file`

This works, but I use [xlstproc](http://xmlsoft.org/XSLT/xsltproc.html) to apply xls stylesheets.
