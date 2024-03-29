# Explorer
A Swing application for looking at PST files on any platform that supports Java. It includes all components necessary to run it; there is no need to
download any other libraries.

The PST library is a memory hog; you may need to increase the available heap to read some PST files.

## Windows cmd
    java -cp target\explorer-1.0-SNAPSHOT.jar [pst-file]

## Linux, Cygwin
    java -cp target/explorer-1.0-SNAPSHOT.jar [pst-file]

The Explorer application lets you explore a pst file from several conceptual levels to view:
*   The pst file header information

*   The list of properties, including names or GUIDs as required

*   The message store

*   The node B-tree, as a tree with nodes that can be expanded to show sub-nodes.
    *   The node's contents are shown if you select the node.

*   The block B-tree, as a tree with nodes that can be expanded to show to show sub-node blocks.
    *   The block's contents are shown if you select a block's node.

*   The pst file's folders (e.g. Deleted Items, Inbox, Outbox, and Sent Items) as a tree. Expanding the tree shows the folder contents (mail items, contacts, calendar entries, sticky note entries, or tasks).
    *   Folder information:
        *   Raw data
        *   The heap for the folder information
        *   The B-Tree in the heap for the folder information
        *   The property context for the folder information

    *   Folder contents:
        *   Raw data for the folder contents
        *   The heap for the folder for the folder contents
        *   The B-Tree in the heap for the folder for the folder contents
        *   The table context for the folder contents

    *   Folder associated data
        *   Raw data for the folder associated data
        *   The heap for the folder for the folder associated data
        *   The B-Tree in the heap for the folder for the folder associated data
        *   The table context for the folder associated data

## Versions
### 1.0.0
Initial version.
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/explorer/1.0.0/index.html)
*   [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/explorer/1.0.0/pom)
*   [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/explorer/1.0.0/)

### 1.1.0
*   Targets Java 11
*   Uses Version 1.1.0 of pst library
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/explorer/1.1.0/index.html)
*   [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/explorer/1.1.0/pom)
*   [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/explorer/1.1.0/)

### 1.1.1
No changes to functionality, but all code has undergone a thorough review and static analysis.
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/explorer/1.1.1/index.html)
*   [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/explorer/1.1.1/pom)
*   [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/explorer/1.1.1/)

### 1.1.2
Fixes several problems:
* [Issue #6: Popup menus (Save Attachment, Save HTML) not working in Explorer application](https://github.com/Jmcleodfoss/pstreader/issues/6)
* [Issue #7: Node and Block right-side displays don't clear on close or loading a new file](https://github.com/Jmcleodfoss/pstreader/issues/7)
* [Issue #8: Exception when saving attachment from Node display](https://github.com/Jmcleodfoss/pstreader/issues/8)
The application has also been run through several automated code scanners and security checkers, and all issues found have been resolved.
#### Release links
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/explorer/1.1.2/index.html)
*   [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/explorer/1.1.2/pom)
*   [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/explorer/1.1.2/)

### 1.1.3
* Provide structured view of contents of intermediate node and block B-tree entries
* Provide structured view of XBLOCK / XXBLOCK / SIENTRY / SLENTRY internal blocks
* Fix removal of heap-on-node display which was throwing an exception
* Only show B-Tree-on-Heap display for Property Context and Table Context objects
