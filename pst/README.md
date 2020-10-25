# pst
A library for reading PST files, based on [[MS-PST]: Outlook Personal Folders (.pst) File Format](https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/141923d5-15ab-4ef1-a524-6dce75aae546).
See
*   [pst Library README](pst/README.md)
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/pst)

## Example
It really helps to understand the structure of the PST file as described in the reference above when using this library, but here is a quick not-quite-java example.
```Java
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
```
For some more concrete examples, see [explorer](../explorer/README.md), a Swing application and [pstExtractor](../pstExtractor/README.md), a JSF-based web application.

## Documentation
The library is [fully Javadoc'd](https://javadoc.io/doc/io.github.jmcleodfoss/pst)

## Executable Classes
The following modules can process pst files directly via their main functions for exploratory and testing purposes. All command lines assume the pst jar is in the CLASSPATH.

The file [extras/test.sh](../../extras/test.sh) runs most of these tests on any pst files located in the directory pst/test-pst-files.

### Appointment
Show information about all appointments in the pst file
```bash
java io.github.jmcleodfoss.pst.Appointment pst-file [pst-file ...]
```
#### Output
```Text
pst-file
Appointment Title 1, Start time, duration
Appointment Title 2, Start time, duration,
...
```

### Attachment
Show information about all attachments found in the pst file
```bash
java io.github.jmcleodfoss.pst.Attachment pst-file [pst-file ...]
```
#### Output
```text
pst-file
Message subject
	attachment Attachment-1-name mime-time Attachment-1-mime-type size Attachment-1-size
	attachment Attachment-2-name mime-time Attachment-2-mime-type size Attachment-2-size
	...
...
```

### BlockBTree
Display the block B-tree for the pst file, showing the following for each node:
*   Block key
*   Block ID
*   Whether the block is "internal", i.e. metadata, or user data
*   Block index
*   Block size
*   Block reference count
```bash
java io.github.jmcleodfoss.pst.BlockBTree pst-file [pst-file ...]
```
#### Output
```text
pst-file
Block B-tree
\_\_\_\_\_\_\_\_\_\_\_\_
BID key 0x00000004 0x00000001, IB 5800 bytes 108 ref count 205
BID key 0x00000008 0x00000002, IB 5880 bytes 180 ref count 8
BID key 0x0000000c 0x00000003, IB 5980 bytes 172 ref count 190
...
```

### BlockFinder
Confirm all block B-tree entries expected are found, or report discrepancies. 
```bash
java io.github.jmcleodfoss.pst.BlockFinder pst-file [pst-file ...]
````
#### Output
```text
pst-file
Success: all 74011 BIDs found
```

### BTreeOnHeap
Traverse the pst heap B-tree, showing:
*   The node ID

*   Whether the block is "internal", i.e. metadata, or user data

*   The node index

*   The block ID key and node index for this node's data B-tree

*   The block ID key and node index for this node's sub-node B-tree

*   The parent node ID and index

*   The node's key and number of children 

*   Information about each child node:
    *   The child's node key
    *   The child node's data

````bash
java io.github.jmcleodfoss.pst.BTreeOnHeap pst-file [ost-file ...]
````
#### Output
```text
pst-file
Node NID 0x00000021: Internal node index 0x00000001, BID(data) key 0x01d5416c 0x0075505b, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
BTreeOnHeap
\-\-\-\-\-\-\-\-\-\-
key 0x00000e34, 16 children
bth:    key 0x00000e34, 16 children
bth:    key 0x00000e34, data 02 01 80 01 00 00
bth:    key 0x00000e38, data 03 00 00 00 00 00
bth:    key 0x00000ff9, data 02 01 60 00 00 00
bth:    key 0x00003001, data 1F 00 80 00 00 00
bth:    key 0x00003416, data 02 01 A0 01 00 00
bth:    key 0x000035df, data 03 00 FF 00 00 00
bth:    key 0x000035e0, data 02 01 A0 00 00 00
bth:    key 0x000035e2, data 02 01 E0 00 00 00
bth:    key 0x000035e3, data 02 01 C0 00 00 00
bth:    key 0x000035e4, data 02 01 00 01 00 00
bth:    key 0x000035e5, data 02 01 20 01 00 00
bth:    key 0x000035e6, data 02 01 40 01 00 00
bth:    key 0x000035e7, data 02 01 60 01 00 00
bth:    key 0x00006633, data 0B 00 01 00 00 00
bth:    key 0x000066fa, data 03 00 0D 00 0E 00
bth:    key 0x000067ff, data 03 00 00 00 00 00
...
```

### Contact
Display information about each contact in the pst file(s)
```bash
java io.github.jmcleodfoss.pst.Contact pst-file [pst-file ...]
```
#### Output
```text
pst-file
/Top of Personal Folders/Contacts/
Contact-1 h:Contact-1-home-phone W:Contact-1-work-phone Contact-1-email
Contact-2 h:Contact-2-home-phone W:Contact-2-work-phone Contact-2-email
...
```

### DistributionList
Display information about each distribution list in the pst file(s)
```bash
java io.github.jmcleodfoss.pst.DistributionList pst-file [pst-file ...]
```
#### Output
```text
pst-file
/Top of Personal Folders/Contacts/
Distribution-list-1-name
Distribution-list-2-name
...
```

### FileFormat
Show the file format (Unicode or ANSI)
```bash
java io.github.jmcleodfoss.pst.FileFormat pst-file [pst-file ...]
```
#### Output
```text
pst-file: Unicode
```

### Folder
Go recursively through all the folders and display each item's subject and date received/created.
```bash
java io.github.jmcleodfoss.pst.Folder pst-file [pst-file ...]
```
#### Output
```text
pst-file
Top of Personal Folders
|-Deleted Items
Subject for message #1 (Fri Jun 13 12:10:26 EDT 2008)
Subject for message #2 (Mon Jun 09 09:25:11 EDT 2008)
Subject for message #3 (Fri Jun 06 18:28:02 EDT 2008)
|-Inbox
Subject for message #4 (Thu Dec 21 11:38:52 EST 2006)
Subject for message #5 (Tue Jan 30 21:01:22 EST 2007)
Subject for message #6 (Wed Jun 27 22:13:57 EDT 2007)
...
```

### GUID
Display the PST GUIDs (these are fixed and defined in the PST file reference given above, so this should be the same for all PST files).
```bash
java io.github.jmcleodfoss.pst.GUID
```
#### Output
```text
                     Name       GUID
                     ____       ____
                     Null       00000000-0000-0000-0000-000000000000
           Public Strings       00020329-0000-0000-c000-000000000046
                   Common       00062008-0000-0000-c000-000000000046
                  Address       00062004-0000-0000-c000-000000000046
              Appointment       00062002-0000-0000-c000-000000000046
       Calendar Assistant       11000e07-b51b-40d6-af21-caa85edab1d0
                  Meeting       6ed8da90-0b45-1b10-98da-00aa003f1305
                  Journal       0006200a-0000-0000-c000-000000000046
                Messaging       41f28f13-83f4-4114-a584-eedb5a6b0bff
                     Task       00062003-0000-0000-c000-000000000046
        Unified Messaging       4442858e-a9e3-4e80-b900-317a210cc15b
                     Note       0006200e-0000-0000-c000-000000000046
                     MAPI       00020328-0000-0000-c000-000000000046
                 Air Sync       71035549-0739-4dcb-9163-00f0580dbbdf
                  Sharing       00062040-0000-0000-c000-000000000046
   XML Extracted Entities       23239608-685d-4732-9c55-4c95cb4e8e33
               Attachment       96357f7f-59e1-47d0-99a7-46515c183b54
                  PostRss       00062041-0000-0000-c000-000000000046
                 Internal       c1843281-8505-d011-b290-00aa003cf676

```

### Header
Show the pst file's header information, including:
*   The format
*   The encoding type
*   The block ID and block index of the root of the block B-tree
*   The block ID and block index of the root of the node B-tree
```bash
java io.github.jmcleodfoss.pst.Header pst-file [pst-file ...]
```
#### Output
```text
pst-file
Format Unicode, Encoding Permute, BBT BID 0x0011d482 IB 0x17c42600, NBT BID 0x0011d480 IB 0x17c41a00
````

### HeapOnNode
Traverse the PST's heap, showing info for each node:
*   Node ID, type, and index
*   Block key and index for the node's data block
*   Block key and index for the node's sub-node B-tree
*   The parent node ID, type, and index
*   The data block's ID, index, byte index into the PST file, size, and reference count
*   Info about the data block (type, heap ID, block index, index, and byte index into the PST file)
*   The contents of this node's heap as it appears in each data block
```bash
java io.github.jmcleodfoss.pst.HeapOnNode pst-file [pst-file ...]
```
#### Output
````text
pst-file
Node NID 0x00000021: Internal node index 0x00000001, BID(data) key 0x01d5416c 0x0075505b, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
dataBlock BID key 0x01d5416c 0x0075505b, IB 168a2a00 bytes 444 ref count 2
HeapOnNode
\-\-\-\-\-\-\-\-\-\-
Property Context User Root HID type 0 block index 0 index 1 ib 412
0:B5 02 06 00 40 00 00 00
1:34 0E 02 01 80 01 00 00 38 0E 03 00 00 00 00 00 F9 0F 02 01 60 00 00 00 01 30 1F 00 80 00 00 00 16 34 02 01 A0 01 00 00 DF 35 03 00 FF 00 00 00 E0 35 02 01 A0 00 00 00 E2 35 02 01 E0 00 00 00 E3 35 02 01 C0 00 00 00 E4 35 02 01 00 01 00 00 E5 35 02 01 20 01 00 00 E6 35 02 01 40 01 00 00 E7 35 02 01 60 01 00 00 33 66 0B 00 01 00 00 00 FA 66 03 00 0D 00 0E 00 FF 67 03 00 00 00 00 00
2:E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D
3:50 00 65 00 72 00 73 00 6F 00 6E 00 61 00 6C 00 20 00 46 00 6F 00 6C 00 64 00 65 00 72 00 73 00
4:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 22 80 00 00
5:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 42 80 00 00
6:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D A2 80 00 00
7:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D C2 80 00 00
8:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D E2 80 00 00
9:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 02 81 00 00
10:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 62 80 00 00
11:01 00 00 00 6F 8B 97 B7 FC 49 21 48 A7 35 E2 6C DB FD 10 B5 01 00 00 00
12:00 00 00 00 E8 23 EA 11 66 05 98 41 90 E3 21 F2 7C 97 0A 7D 43 41 09 00
...
````

### IPF
Show all folders, their classes, and whether the class is recognized.
```bash
java io.github.jmcleodfoss.pst.IPF pst-file [pst-file ...]
```
Get a list of all known folder classes
```bash
java io.github.jmcleodfoss.pst.IPF --list
```
#### Output (on pst files)
```text
pst-file
Folder Name               Container Class           Known Container Class?
Deleted Items             null                      false
Inbox                     null                      false
Outbox                    null                      false
Sent Items                null                      false
Calendar                  IPF.Appointment           true
Contacts                  IPF.Contact               true
Journal                   IPF.Journal               true
Notes                     IPF.StickyNote            true
Tasks                     IPF.Task                  true
Drafts                    IPF.Note                  true
Junk E-mail               IPF.Note                  true
```
#### Output (--list option)
```text
Known folder types
IPF.Appointment
IPF.Configuration
IPF.Contact
IPF.ShortcutFolder
IPF.Note.OutlookHomepage
IPF.Contact.MOC.IMContactList
IPF.Journal
IPF.Note
IPF.Contact.MOC.QuickContacts
Outlook.Reminder
IPF.StickyNote
IPF.Task
```

### IPM
Show all messages, with their message type and whether the class is recognized
```bash
java io.github.jmcleodfoss.pst.IPM pst-file [pst-file ...]
```
Get a list of all known types classes
```bash
java io.github.jmcleodfoss.pst.IPM --list
```
#### Output (on pst files)
```text
pst-file
Subject                   Container Class           Known Container Class?
Email Subject 1         IPM.Note                  true
Email Subject 2         IPM.Note                  true
Email Subject 3         IPM.Note                  true
```
#### Output (--list option)
```text
Known message types
IPM.Activity
IPM.Appointment
IPM.Contact
IPM.DistList
IPM.Document
IPM.Journal
IPM.Note
IPM.Note.IMC.Notification
IPM.Note.Rules.OOfTemplate.Microsoft
IPM.Post
IPM.StickyNote
IPM.Recall.Report
IPM.Outlook.Recall
IPM.Remote
IPM.Resend
IPM.Note.Rules.ReplyTemplate.Microsoft
IPM.Schedule.Meeting.Cancelled
IPM.Schedule.Meeting.Request
IPM.Schedule.Meeting.Resp.Neg
IPM.Schedule.Meeting.Resp.Pos
IPM.Schedule.Meeting.Resp.Tent
IPM.Note.Secure
IPM.Note.Secure.Sign
IPM.Task
IPM.TaskRequest.Accept
IPM.TaskRequest.Decline
IPM.TaskRequest
IPM.TaskRequest.Update
```

### JournalEntry
Show the journal entries in the given pst file(s)/
```bash
java io.github.jmcleodfoss.pst.JournalEntry pst-file [pst-file ...]
```
#### Output
```text
pst-file
Journal-entry-1-subject
Journal-entry-2-subject
...
```

### Message
Show the messages in the given pst file(s).
```bash
java io.github.jmcleodfoss.pst.Message pst-file [pst-file ...]
```
#### Output
```text
pst-file
/Top of Personal Folders/Inbox/
Message-1-subject (Message-1-data)
Message-2-subject (Message-2-data)
...
```

### NameToIDMap
Show the named property IDs and their names or GUIDs (the order and contents depends on the history of the PST file).
```bash
java io.github.jmcleodfoss.pst.NameToIDMap pst-file [pst-file ...]
```
#### Output
```text
pst-file
0x8000=content-class
0x8001=ReminderSet
0x8002=Recurring
0x8003=SideEffects
0x8004=8578-00062008-0000-0000-c000-000000000046
0x8005=858d-00062008-0000-0000-c000-000000000046
0x8006=8f05-00062014-0000-0000-c000-000000000046
0x8007=SmartNoAttach
...
```

### NodeBTree
Display the entire node B-tree, for each entry showing:
*   The node ID
*   Whether the node is internal (metadata) or user data
*   The node index
*   The key and ID for this node's data block
*   The key and ID for this node's sub-node B-tree
*   This node's parent node ID and index
```bash
java io.github.jmcleodfoss.pst.NodeBTree pst-file [pst-file ...]
```
#### Output
``text
pst-file
\_\_\_\_\_\_\_\_\_\_\_
NID 0x00000021: Internal node index 0x00000001, BID(data) key 0x01d5416c 0x0075505b, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
NID 0x00000061: Internal node index 0x00000003, BID(data) key 0x01d5b1de 0x00756c77 (internal), BID(subnode) key 0x01d5b1ee 0x00756c7b (internal) Parent NID 0x00000000: Heap node index 0x00000000
NID 0x00000122: Normal Folder node index 0x00000009, BID(data) key 0x01d52d40 0x00754b50, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000122: Normal Folder node index 0x00000009
...
``

### NodeFinder
Confirm all node B-tree entries expected are found, or report discrepancies. 
```bash
java io.github.jmcleodfoss.pst.NodeFinder pst-file [pst-file ...]
```
#### Output
```text
pst-file
Success: all 8497 NIDs found
```

### PropertyContext
Show the property names and values associated with each node in the node B-tree.
*   The node ID
*   The folder type and index
*   The key and index for this node's data block
*   The key and index for this node's sub-node B-tree
*   The parent node ID, type, and index, and parent's child node IDs
*   All properties and values for this node (only simple types are shown; complex types and arrays are given as internal pointers)
```bash
java io.github.jmcleodfoss.pst.PropertyContext pst-file [pst-file ...]
```
#### Output
````text
pst-file
Node NID 0x00000122: Normal Folder node index 0x00000009, BID(data) key 0x01d52d40 0x00754b50, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000122: Normal Folder node index 0x00000009, 122
PropertyContext
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-
    
0x36030003 ContentUnreadCount "0"
0x36020003 ContentCount "0"
0x360a000b Subfolders "true"
0x36e41102 FreeBusyEntryIds "[[B@19f7e29"
0x36d20102 IpmJournalEntryId "[B@185612c"
0x36d30102 IpmNoteEntryId "[B@17050dc"
0x36d00102 IpmAppointmentEntryId "[B@1c29bfd"
0x36d10102 IpmContactEntryId "[B@1aec35a"
0x36d70102 IpmDraftsEntryId "[B@1424e82"
0x36d40102 IpmTaskEntryId "[B@110406"
0x36d50102 RemindersOnlineEntryId "[B@11d72ca"
0x36d81102 AdditionalRenEntryIds "[[B@d8cfe0"
0x36d90102 propertyTag-36d90102 "[B@19bb367"
0x3001001f DisplayNameW "null"
...
````

### PropertyLIDs
Display all known property Long IDs.
```bash
java io.github.jmcleodfoss.pst.PropertyLIDs
```
#### Output
```text
0x00001000: DayOfMonth
0x00009000: Categories
0x00000001: AttendeeCriticalChange
0x00001001: ICalendarDayOfWeekMask
0x00000002: Where
0x00000003: GlobalObjectId
0x00000004: IsSilent
0x00000005: IsRecurring
0x00001005: Occurrences
0x00008005: FileUnder
...
```

### PropertyTags
Display all known property IDs
```bash
java io.github.jmcleodfoss.pst.PropertyTags
```
#### Output
```text
0x70030003: ViewDescriptorFlags
0x000a000b: IsException
0x40020003: EndEmbed
0x800c000d: AddressBookOwner
0x0009000b: DelegateMail
0x40010003: StartEmbed
0x68010003: VoiceMessageDuration
...
```

### SimpleBlock
Show the block B-tree contents. For each block, show:
*   The block ID key and index
*   The byte index (index into the PST file) for this block
*   The block size in bytes
*   The block's reference count
*   The block data in hex
```bash
java io.github.jmcleodfoss.pst.SimpleBlock pst-file [pst-file ...]
```
#### Output
```text
pst-file
BID key 0x00000004 0x00000001, IB 5800 bytes 108 ref count 205: 62 00 EC 7C 40 00 00 00 00 00 00 00 B5 04 04 00 00 00 00 00 7C 07 18 00 18 00 19 00 1A 00 20 00 00 00 00 00 00 00 00 00 00 00 1F 00 01 30 08 00 04 02 03 00 02 36 0C 00 04 03 03 00 03 36 10 00 04 04 0B 00 0A 36 18 00 01 05 1F 00 13 36 14 00 04 06 03 00 F2 67 00 00 04 00 03 00 F3 67 04 00 04 01 02 00 00 00 0C 00 14 00 62 00
BID key 0x00000008 0x00000002, IB 5880 bytes 180 ref count 8: AA 00 EC 7C 40 00 00 00 00 00 00 00 B5 04 04 00 00 00 00 00 7C 10 3C 00 3C 00 3E 00 40 00 20 00 00 00 00 00 00 00 00 00 00 00 03 00 17 00 14 00 04 05 1F 00 1A 00 0C 00 04 03 03 00 36 00 34 00 04 0E 1F 00 37 00 1C 00 04 07 1F 00 42 00 18 00 04 06 0B 00 57 00 3C 00 01 0C 0B 00 58 00 3D 00 01 0D 1F 00 03 0E 30 00 04 0B 1F 00 04 0E 2C 00 04 0A 40 00 06 0E 20 00 08 08 03 00 07 0E 10 00 04 04 03 00 08 0E 28 00 04 09 03 00 17 0E 08 00 04 02 03 00 97 10 38 00 04 0F 03 00 F2 67 00 00 04 00 03 00 F3 67 04 00 04 01 02 00 00 00 0C 00 14 00 AA 00
...
```

### StickyNote
Show information for all sticky notes in the given pst file(s).
```bash
java io.github.jmcleodfoss.pst.StickyNote pst-file [pst-file ...]
```
#### Output
```text
pst-file
/Top of Personal Folders/Notes/
StickNote-1-subject
StickNote-2-subject
StickNote-3-subject
...
```

### SubnodeBTree
Show information about each node in the subnode B-tree. For each node, show:
*   The node ID
*   Whether the node is internal (metadata) or user data
*   The node index
*   The block ID key, Index, and type (internal/metadata or user data) for the data block for this node
*   The block ID key, Index, and type for the sub-node B-tree for this node
*   The parent node ID of this node
*   This node's index on the heap
```bash
java io.github.jmcleodfoss.pst.SubnodeBTree pst-file [pst-file ...]
```
#### Output
````text
pst-file
Subnode BTree for NID 0x00000061: Internal node index 0x00000003, BID(data) key 0x01d5b1de 0x00756c77 (internal), BID(subnode) key 0x01d5b1ee 0x00756c7b (internal) Parent NID 0x00000000: Heap node index 0x00000000
key 0x0000831f, 2 children
1: NID 0x0000831f: LTP node index 0x00000418, BID(data) key 0x01d5b1e4 0x00756c79, BID(subnode) key 0x00000000 0x00000000; BID key 0x01d5b1e4 0x00756c79, IB 169e9800 bytes 5824 ref count 2
block: 00 00 00 00 07 00 ...
...
````

### TableContext
Show information about the data associated with each node in the node B-Tree:
*   The node ID
*   The node type
*   The node index
*   The block ID key and index for this node's data
*   The block ID key and index for this node's sub-node B-tree
*   The parent node ID and index
*   The tags, property names, offsets, widths, types, and index for each field in this node. 
```bash
java io.github.jmcleodfoss.pst.TableContext pst-file [pst-file ...]
```
#### Output
```text
pst-file
Node NID 0x0000012d: Hierarchy Table node index 0x00000009, BID(data) key 0x01d53d84 0x00754f61, BID(subnode) key 0x00000000 0x00000000 Parent NID 0x00000000: Heap node index 0x00000000
TableContext
\-\-\-\-\-\-\-\-\-\-\-\-
hidRowIndex type 0 block index 0 index 1 columns 9 hnidRows type 0 block index 0 index 4
        tag 0x67f20003 (LtpRowId) offset into row 0 width 4 CEB index 0
        tag 0x67f30003 (LtpRowVer) offset into row 4 width 4 CEB index 1
        tag 0x3001001f (DisplayNameW) offset into row 8 width 4 CEB index 2
        tag 0x36020003 (ContentCount) offset into row 12 width 4 CEB index 3
        tag 0x36030003 (ContentUnreadCount) offset into row 16 width 4 CEB index 4
        tag 0x3613001f (ContainerClassW) offset into row 20 width 4 CEB index 6
        tag 0x66350003 (PstHiddenCount) offset into row 24 width 4 CEB index 7
        tag 0x66360003 (PstHiddenUnread) offset into row 28 width 4 CEB index 8
        tag 0x360a000b (Subfolders) offset into row 32 width 1 CEB index 5
Data block ending offsets: 32 32 33 35
key 0x00000020, 9 children

LtpRowId: 0x2223
LtpRowVer: 0x7
DisplayNameW: empty
ContentCount: empty
ContentUnreadCount: empty
ContainerClassW: empty
PstHiddenCount: empty
PstHiddenUnread: empty
Subfolders: empty
...
```

### Task
Show information for all tasks in the given pst file(s).
```bash
java io.github.jmcleodfoss.pst.Task pst-file [pst-file ...]
```
#### Output
```text
pst-file
/Top of Personal Folders/Tasks/
Task-1-Subject due Task-1-DueDate
Task-2-Subject due Task-2-DueDate
...
```

### XBlock
Traverse the node B-tree, giving the structure of each node:
*   The node size
*   The number of data blocks in the node
*   The block key and index for each data block
```bash
java io.github.jmcleodfoss.pst.XBlock pst-file [pst-file ...]
```
#### Output
```text
pst-file
8600 bytes in 2 data blocks:
key 0x01d5b1e8 0x00756c7a
key 0x01d5b1e0 0x00756c78
42362 bytes in 6 data blocks:
key 0x01d5d9ec 0x0075767b
key 0x01d5d9fc 0x0075767f
key 0x01d5d280 0x007574a0
...
```

## Versions
### 1.0.0
Initial version.
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/pst/1.0.0/index.html)
*   [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/pst/1.0.0/pom)
*   [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/pst/1.0.0/)

### 1.1.0
*   Targets Java 11
*   Fixes Bug #1, Bug #2, Bug #3
*   Includes all known tags and LIDs
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/pst/1.1.0/index.html)
*   [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/pst/1.1.0/pom)
*   [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/pst/1.1.0/)

### 1.1.1
No changes to functionality, but all code has undergone a thorough review and static analysis.
*   [Javadoc](https://javadoc.io/doc/io.github.jmcleodfoss/pst/1.1.1/index.html)
*   [pom file and dependency inclusion info](https://search.maven.org/artifact/io.github.jmcleodfoss/pst/1.1.1/pom)
*   [Download from Sonatype OSS Maven Repository](https://repo1.maven.org/maven2/io/github/jmcleodfoss/pst/1.1.1/)
