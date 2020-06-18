# swingutil Library
A library of useful Java Swing functions used by the pst library. It is separate because I wanted the pst library to be as independent of Swing as possibly, but still able to be used by Swing applications. This is probably a dead end; when I wrote them, Swing was still popular but that is less true eight years later.

I will deprecate any of these I can find replacements for in active development/support.

## EmptyTreeModel.java
A substitute for Swing's default tree model for use when the tree is empty. Based on [io.github.jmcleodfoss.pst.ReadOnlyTreeModel](../pst/src/main/java/io/github/jmcleodfoss/ReadOnlyTreeModel.java).

## FilterByExtension.java
Convenience wrapper for filtering files by extension for a javax.swing.JFileChooser. Java 6 has this natively as [javax.swing.filechooser.FileNameExtensionFilter](https://docs.oracle.com/javase/7/docs/api/javax/swing/filechooser/FileNameExtensionFilter.html)

## HexAndTextDisplay.java
Display data in a javax.swing.JScrollPane as both hexadecimal byte values and as text.

## HTMLTableModel.java
Output a javax.swing.table.TableModel as HTML.

## ProgressBar.java
Modal popup extension of javax.swing.JProgressBar which runs in its own thread.

## TreeNodePopupListener.java
Enables popup listeners to be associated with the nodes of a javax.swing.JTree.

# Versions
## 1.0.0
Initial version.
* [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/swingutil/1.0.0/index.html)
* [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/swingutil/1.0.0/pom)
* [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/swingutil/1.0.0/)

## 1.1.0
* Targets Java 11
* Uses Version 1.1.0 of pst library
* [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/swingutil/1.1.0/index.html)
* [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/swingutil/1.1.0/pom)
* [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/swingutil/1.1.0/)
