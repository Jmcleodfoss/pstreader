# Explorer
A Swing application for looking at PST files on any platform that supports Java.

The PST library is a memory hog; you may need to increase the available heap to read some PST files.

## Windows cmd
    java -cp target\explorer-1.0-SNAPSHOT.jar [pst-file]

## Linux, Cygwin
    java -cp target/explorer-1.0-SNAPSHOT.jar [pst-file]

The Explorer application lets you explore a pst file from several conceptual levels to view:
* The pst file header information
* The list of properties, including names or GUIDs as required
* The message store
* The node B-tree, as a tree with nodes that can be expanded to show sub-nodes.
  * The node's contents are shown if you select the node.
* The block B-tree, as a tree with nodes that can be expanded to show to show sub-node blocks.
  * The block's contents are shown if you select a block's node.
* The pst file's folders (e.g. Deleted Items, Inbox, Outbox, and Sent Items) as a tree. Expanding the tree shows the folder contents (mail items, contacts, calendar entries, sticky note entries, or tasks).
    * Folder information:
      * Raw data
      * The heap for the folder information
      * The B-Tree in the heap for the folder information
      * The property context for the folder information
    * Folder contents:
      * Raw data for the folder contents
      * The heap for the folder for the folder contents
      * The B-Tree in the heap for the folder for the folder contents
      * The table context for the folder contents
    * Folder associated data
      * Raw data for the folder associated data
      * The heap for the folder for the folder associated data
      * The B-Tree in the heap for the folder for the folder associated data
      * The table context for the folder associated data

