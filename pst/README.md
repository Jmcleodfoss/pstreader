# pst
A library for reading PST files, based on [[MS-PST]: Outlook Personal Folders (.pst) File Format](https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/141923d5-15ab-4ef1-a524-6dce75aae546).

## Example

It really helps to understand the structure of the PST file as described in the reference above when using this library, but here is a quick not-quite-java example.

    import io.github.jmcleodfoss.pst.*

    PST pst = new PST("path-to-file/filename.pst");
    Folder rootFolder = pst.getFolder(pst.nodeBTree.find(pst.messageStore.rootMailboxEntry.nid));
    TableContext rootHierarchyTable = new TableContext(rootFolder.nodeHierarchyTable, pst.blockBTree, pst);
    for (Iterator<Folder> folderIterator = rootFolder.subfolderIterator(); folderIterator.hasNext(); ) {
        final Folder f = folderIterator.next();

        if (IPF.isAppointment(f)) {
            // handle appointments in folder
        } else if (IPF.isContact(f)) {
            // handle contacts in folder
        } else if (IPF.isJournal(f)) {
            // handle journal entries in folder
        } else if (IPF.isStickyNote(f)) {
            // handle sticky notes in folder
        } else if (IPF.isTask(f)) {
            // handle tasks in folder
        } else {
            // handle message folders
        }
    }

For some more concrete examples, see [explorer](../explorer/README.md), a Swing application and [pstExtractor](../pstExtractor/README.md), a JSF-based web application.

## Documentation

The library is fully Javadoc'd (link tbd).

## Executable Classes
The following modules can process pst files directly via their main functions for exploratory and testing purposes.

The file [extras/test.sh](../../extras/test.sh) runs most of these tests on any pst files located in the directory pst/test-pst-files.

### BlockBTree.java
    java -cp xml.jar io.github.jmcleod.pst.BlockBTree pst-file

Display the block B-tree for the pst file, showing the following for each node:
* Block key
* Block ID
* Whether the block is "internal", i.e. metadata, or user data
* Block index
* Block size
* Block reference count

#### Output
> Block B-tree
> ____________
> BID key 0x00000004 0x00000001, IB 5800 bytes 108 ref count 205
> BID key 0x00000008 0x00000002, IB 5880 bytes 180 ref count 8
> BID key 0x0000000c 0x00000003, IB 5980 bytes 172 ref count 190
> ...

### BlockFinder.java
    java -cp xml.jar io.github.jmcleod.pst.BlockFinder pst-file

Confirm all block B-tree entries expected are found, or report discrepancies. 
#### Output
> Success: all 74011 BIDs found

### BTreeOnHeap.java
    java -cp xml.jar io.github.jmcleod.pst.BTreeOnHeap pst-file

Traverse the pst heap, showing:
* The node ID
* Whether the block is "internal", i.e. metadata, or user data
* The node index
* The block ID key and node index
* The sub-node block ID key and node index
* The parent node ID
* The heap node's index
* The key for the node
* The number of children the node has
* Information about each child, including
** The child's node key
** The child node's data

#### Output
> Node NID 0x00000021: Internal node index 0x00000001, BID(data) key 0x01d5416c 0x0075505b, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
> BTreeOnHeap
> ----------
> key 0x00000e34, 16 children
> bth:    key 0x00000e34, 16 children
> bth:    key 0x00000e34, data 02 01 80 01 00 00
> bth:    key 0x00000e38, data 03 00 00 00 00 00
> bth:    key 0x00000ff9, data 02 01 60 00 00 00
> bth:    key 0x00003001, data 1F 00 80 00 00 00
> bth:    key 0x00003416, data 02 01 A0 01 00 00
> bth:    key 0x000035df, data 03 00 FF 00 00 00
> bth:    key 0x000035e0, data 02 01 A0 00 00 00
> bth:    key 0x000035e2, data 02 01 E0 00 00 00
> bth:    key 0x000035e3, data 02 01 C0 00 00 00
> bth:    key 0x000035e4, data 02 01 00 01 00 00
> bth:    key 0x000035e5, data 02 01 20 01 00 00
> bth:    key 0x000035e6, data 02 01 40 01 00 00
> bth:    key 0x000035e7, data 02 01 60 01 00 00
> bth:    key 0x00006633, data 0B 00 01 00 00 00
> bth:    key 0x000066fa, data 03 00 0D 00 0E 00
> bth:    key 0x000067ff, data 03 00 00 00 00 00
> ...

### Folder.java
    java -cp xml.jar io.github.jmcleod.pst.Folder pst-file

Go recursively through all the folders and display each item's subject and date received/created.

#### Output
> Top of Personal Folders
> |-Deleted Items
> Subject for message #1 (Fri Jun 13 12:10:26 EDT 2008)
> Subject for message #2 (Mon Jun 09 09:25:11 EDT 2008)
> Subject for message #3 (Fri Jun 06 18:28:02 EDT 2008)
> |-Inbox
> Subject for message #4 (Thu Dec 21 11:38:52 EST 2006)
> Subject for message #5 (Tue Jan 30 21:01:22 EST 2007)
> Subject for message #6 (Wed Jun 27 22:13:57 EDT 2007)
> ...

### GUID.java
    java -cp xml.jar io.github.jmcleod.pst.GUID pst-file

Display the PST GUIDs (these are fixed and defined in the PST file reference given above)

#### Output
> Name GUID
>            ____ ____
>            Null 00000000-0000-0000-0000-000000000000
>  Public Strings 00020329-0000-0000-c000-000000000046
>          Common 00062008-0000-0000-c000-000000000046
>         Address 00062004-0000-0000-c000-000000000046
>     Appointment 00062002-0000-0000-c000-000000000046
>         MEETING 6ed8da90-450b-1b10-98da-00aa003f1305
>            Task 00062003-0000-0000-c000-000000000046
>            Note 0006200e-0000-0000-c000-000000000046
>            MAPI 00020328-0000-0000-c000-000000000046
>        Internal c1843281-8505-d011-b290-00aa003cf676

### Header.java
    java -cp xml.jar io.github.jmcleod.pst.Header pst-file

Show the pst file's header information, including:

* The format
* The encoding type
* The block ID and block index of the root of the block B-tree
* The block ID and block index of the root of the node B-tree

#### Output
> Format Unicode, Encoding Permute, BBT BID 0x0011d482 IB 0x17c42600, NBT BID 0x0011d480 IB 0x17c41a00

### HeapOnNode.java
    java -cp xml.jar io.github.jmcleod.pst.HeapOnNode pst-file

#### Output
> Node NID 0x00000021: Internal node index 0x00000001, BID(data) key 0x01d5416c 0x0075505b, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
> dataBlock BID key 0x01d5416c 0x0075505b, IB 168a2a00 bytes 444 ref count 2
> HeapOnNode
> ----------
> Property Context User Root HID type 0 block index 0 index 1 ib 412
> 0:B5 02 06 00 40 00 00 00
> 1:34 0E 02 01 80 01 00 00 38 0E 03 00 00 00 00 00 F9 0F 02 01 60 00 00 00 01 30 1F 00 80 00 00 00 16 34 02 01 A0 01 00 00 DF 35 03 00 FF 00 00 00 E0 35 02 01 A0 00 00 00 E2 35 02 01 E0 00 00 00 E3 35 02 01 C0 00 00 00 E4 35 02 01 00 01 00 00 E5 35 02 01 20 01 00 00 E6 35 02 01 40 01 00 00 E7 35 02 01 60 01 00 00 33 66 0B 00 01 00 00 00 FA 66 03 00 0D 00 0E 00 FF 67 03 00 00 00 00 00
> 2:E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D
> 3:50 00 65 00 72 00 73 00 6F 00 6E 00 61 00 6C 00 20 00 46 00 6F 00 6C 00 64 00 65 00 72 00 73 00
> 4:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 22 80 00 00
> 5:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 42 80 00 00
> 6:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D A2 80 00 00
> 7:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D C2 80 00 00
> 8:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D E2 80 00 00
> 9:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 02 81 00 00
> 10:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 62 80 00 00
> 11:01 00 00 00 6F 8B 97 B7 FC 49 21 48 A7 35 E2 6C DB FD 10 B5 01 00 00 00
> 12:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 43 41 09 00
> ...

### NameToIDMap.java
    java -cp xml.jar io.github.jmcleod.pst.NameToIDMap pst-file

Show the named properties and their names or GUIDs.

#### Output
> 0x8000=content-class
> 0x8001=ReminderSet
> 0x8002=Recurring
> 0x8003=SideEffects
> 0x8004=8578-00062008-0000-0000-c000-000000000046
> 0x8005=858d-00062008-0000-0000-c000-000000000046
> 0x8006=8f05-00062014-0000-0000-c000-000000000046
> 0x8007=SmartNoAttach
> ...

### NodeBTree.java
    java -cp xml.jar io.github.jmcleod.pst.NodeBTree pst-file

Display the entire node B-tree, for each entry showing:
* The node ID
* Whether the node is internal (metadata) or user data
* The node index
* The data block key and ID
* The subnode block key and ID
* The parent node ID
* The heap node index
#### Output
> ___________
> NID 0x00000021: Internal node index 0x00000001, BID(data) key 0x01d5416c 0x0075505b, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
> NID 0x00000061: Internal node index 0x00000003, BID(data) key 0x01d5b1de 0x00756c77 (internal), BID(subnode) key 0x01d5b1ee 0x00756c7b (internal) Parent NID 0x00000000: Heap node index 0x00000000
> NID 0x00000122: Normal Folder node index 0x00000009, BID(data) key 0x01d52d40 0x00754b50, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000122: Normal Folder node index 0x00000009
> ...

### NodeFinder.java
    java -cp xml.jar io.github.jmcleod.pst.NodeFinder pst-file

Confirm all node B-tree entries expected are found, or report discrepancies. 
#### Output
Success: all 8497 NIDs found

### PropertyContext.java
    java -cp xml.jar io.github.jmcleod.pst.PropertyContext pst-file

Show the properties associated with each node in the node B-tree.
#### Output
> Node NID 0x00000122: Normal Folder node index 0x00000009, BID(data) key 0x01d52d40 0x00754b50, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000122: Normal Folder node index 0x00000009, 122
> PropertyContext
> ---------------
> 
> 0x36030003 ContentUnreadCount "0"
> 0x36020003 ContentCount "0"
> 0x360a000b Subfolders "true"
> 0x36e41102 FreeBusyEntryIds "[[B@19f7e29"
> 0x36d20102 IpmJournalEntryId "[B@185612c"
> 0x36d30102 IpmNoteEntryId "[B@17050dc"
> 0x36d00102 IpmAppointmentEntryId "[B@1c29bfd"
> 0x36d10102 IpmContactEntryId "[B@1aec35a"
> 0x36d70102 IpmDraftsEntryId "[B@1424e82"
> 0x36d40102 IpmTaskEntryId "[B@110406"
> 0x36d50102 RemindersOnlineEntryId "[B@11d72ca"
> 0x36d81102 AdditionalRenEntryIds "[[B@d8cfe0"
> 0x36d90102 propertyTag-36d90102 "[B@19bb367"
> 0x3001001f DisplayNameW "null"
> ...

### SimpleBlock.java
    java -cp xml.jar io.github.jmcleod.pst.SimpleBlock pst-file

#### Output
> BID key 0x00000004 0x00000001, IB 5800 bytes 108 ref count 205: 62 00 EC 7C 40 00 00 00 00 00 00 00 B5 04 04 00 00 00 00 00 7C 07 18 00 18 00 19 00 1A 00 20 00 00 00 00 00 00 00 00 00 00 00 1F 00 01 30 08 00 04 02 03 00 02 36 0C 00 04 03 03 00 03 36 10 00 04 04 0B 00 0A 36 18 00 01 05 1F 00 13 36 14 00 04 06 03 00 F2 67 00 00 04 00 03 00 F3 67 04 00 04 01 02 00 00 00 0C 00 14 00 62 00
> BID key 0x00000008 0x00000002, IB 5880 bytes 180 ref count 8: AA 00 EC 7C 40 00 00 00 00 00 00 00 B5 04 04 00 00 00 00 00 7C 10 3C 00 3C 00 3E 00 40 00 20 00 00 00 00 00 00 00 00 00 00 00 03 00 17 00 14 00 04 05 1F 00 1A 00 0C 00 04 03 03 00 36 00 34 00 04 0E 1F 00 37 00 1C 00 04 07 1F 00 42 00 18 00 04 06 0B 00 57 00 3C 00 01 0C 0B 00 58 00 3D 00 01 0D 1F 00 03 0E 30 00 04 0B 1F 00 04 0E 2C 00 04 0A 40 00 06 0E 20 00 08 08 03 00 07 0E 10 00 04 04 03 00 08 0E 28 00 04 09 03 00 17 0E 08 00 04 02 03 00 97 10 38 00 04 0F 03 00 F2 67 00 00 04 00 03 00 F3 67 04 00 04 01 02 00 00 00 0C 00 14 00 AA 00
> ...

### SubnodeBTree.java
    java -cp xml.jar io.github.jmcleod.pst.SubnodeBTree pst-file

#### Output
> Subnode BTree for NID 0x00000061: Internal node index 0x00000003, BID(data) key 0x01d5b1de 0x00756c77 (internal), BID(subnode) key 0x01d5b1ee 0x00756c7b (internal) Parent NID 0x00000000: Heap node index 0x00000000
> key 0x0000831f, 2 children
> 1: NID 0x0000831f: LTP node index 0x00000418, BID(data) key 0x01d5b1e4 0x00756c79, BID(subnode) key 0x00000000 0x00000000; BID key 0x01d5b1e4 0x00756c79, IB 169e9800 bytes 5824 ref count 2
> block: 00 00 00 00 07 00 ...
> ...

### TableContext.java
    java -cp xml.jar io.github.jmcleod.pst.TableContext pst-file

#### Output
> Node NID 0x0000012d: Hierarchy Table node index 0x00000009, BID(data) key 0x01d53d84 0x00754f61, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
> TableContext
> ------------
> hidRowIndex type 0 block index 0 index 1 columns 9 hnidRows type 0 block index 0 index 4
>         tag 0x67f20003 (LtpRowId) offset into row 0 width 4 CEB index 0
>         tag 0x67f30003 (LtpRowVer) offset into row 4 width 4 CEB index 1
>         tag 0x3001001f (DisplayNameW) offset into row 8 width 4 CEB index 2
>         tag 0x36020003 (ContentCount) offset into row 12 width 4 CEB index 3
>         tag 0x36030003 (ContentUnreadCount) offset into row 16 width 4 CEB index 4
>         tag 0x3613001f (ContainerClassW) offset into row 20 width 4 CEB index 6
>         tag 0x66350003 (PstHiddenCount) offset into row 24 width 4 CEB index 7
>         tag 0x66360003 (PstHiddenUnread) offset into row 28 width 4 CEB index 8
>         tag 0x360a000b (Subfolders) offset into row 32 width 1 CEB index 5
> Data block ending offsets: 32 32 33 35
> key 0x00000020, 9 children
> 
> LtpRowId: 0x2223
> LtpRowVer: 0x7
> DisplayNameW: empty
> ContentCount: empty
> ContentUnreadCount: empty
> ContainerClassW: empty
> PstHiddenCount: empty
> PstHiddenUnread: empty
> Subfolders: empty
> ...

### XBlock.java
    java -cp xml.jar io.github.jmcleod.pst.XBlock pst-file

#### Output
> 8600 bytes in 2 data blocks:
> key 0x01d5b1e8 0x00756c7a
> key 0x01d5b1e0 0x00756c78
> 42362 bytes in 6 data blocks:
> key 0x01d5d9ec 0x0075767b
> key 0x01d5d9fc 0x0075767f
> key 0x01d5d280 0x007574a0
