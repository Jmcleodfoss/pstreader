package io.github.jmcleodfoss.pst;

/**	The NameIDToMap class wraps the PropertyContext node NID_NAME_ID_TO_MAP (0x61) to provide names for the properties therein.
*
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.2.2"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386230(v=office.12).aspx">Named Properties (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.7"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff387733(v=office.12).aspx">Named Property Lookup Map (MSDN)</a>
*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.5"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386696(v=office.12).aspx">Calculated Properties (MSDN)</a>
*/
public class NameToIDMap {

	/**	Logger for class debugging */
	java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.github.jmcleodfoss.pst.NameToIDMap");

	/**	The NameID class contains information about an individual entry in the NID_NAME_ID_TO_MAP node.
	*
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, section 2.4.7.1"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff385127(v=office.12).aspx">NAMEID (MSDN)</a>
	*/
	private static class NameID {

		/**	GUID index (type) {@value}: No GUID. */
		private static final short GUID_INDEX_NONE = 0;

		/**	GUID index (type) {@value}: GUID in PS_MAPI.
		*
		*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 1.3.2"
		*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee219487.aspx">Commonly Used Property Sets (MSDN)</a>
		*/
		private static final short GUID_INDEX_MAPI = 1;

		/**	GUID index (type) {@value}: GUID is in PS_PUBLIC_STRINGS.
		*
		*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 1.3.2"
		*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee219487.aspx">Commonly Used Property Sets (MSDN)</a>
		*/
		private static final short GUID_INDEX_PUBLIC_STRINGS = 2;

		/**	GUID index (type) {@value}: GUID is found at the "(n-1)*16" byte offset in the GUID stream. */
		private static final short GUID_INDEX_GUID = 3;

		/**	Names are stored in the PST file in this format. */
		private static final String CHARSET_WIDE = new String("UTF-16LE");

		/**	If fString, this is the offset into the string stream of the node at which the property name is found.
		*	Otherwise, this is the numeric identifier of the property.
		*/
		final int propertyID;

		/**	This indicates whether the propertyID corresponds to string or a numerical valus. */
		final boolean fString;

		/**	The GUID index */
		final short guidIndex;

		/**	The named property's index. */
		final short propertyIndex;

		/**	The property name, if any. */
		final String name;

		/**	The property set, if any. */
		final GUID guid;

		/**	Construct a NameID object from raw data.
		*
		*	@param	entryStream	The data stream from which to read the named ID information.
		*	@param	guidArray	The GUID as read from the GUID stream.
		*	@param	stringStream	The data stream from which to read the property name.
		*/
		NameID(java.nio.ByteBuffer entryStream, final byte[] guidArray, java.nio.ByteBuffer stringStream)
		throws
			java.io.UnsupportedEncodingException
		{
			propertyID = entryStream.getInt();

			final short fStringAndGuid = entryStream.getShort();
			fString = (fStringAndGuid & 0x0001) != 0;
			guidIndex = (short)(fStringAndGuid >>> 1);

			propertyIndex = entryStream.getShort();

			if (fString) {
				name = name(stringStream);
				guid = null;
			} else {
				name = null;
				guid = guid(guidArray);
			}
		}

		/**	Create the property set GUID from the array of GUIDs.
		*	Throw an exception if this NameID object does not have a GUID property set identifier.
		*
		*	@param	guidArray	The raw data from which to create the GUID.
		*
		*	@return	A io.github.jmcleodfoss.pst.GUID object corresponding to the passed raw GUID.
		*/
		private GUID guid(final byte[] guidArray)
		{
			if (fString)
				throw new RuntimeException("Not GUID!");

			if (guidIndex == GUID_INDEX_NONE)
				return GUID.PS_NULL;
			if (guidIndex == GUID_INDEX_MAPI)
				return GUID.PS_MAPI;
			if (guidIndex == GUID_INDEX_PUBLIC_STRINGS)
				return GUID.PS_PUBLIC_STRINGS;
			assert guidIndex == GUID_INDEX_GUID;

			final int offset = (guidIndex - 3) * 16;

			return new GUID(guidArray, offset);
		}

		/**	Retrieve the property name from the array of names. Throw an exception if this NameID object does not have a name.
		*
		*	@param	stringStream	The input data stream from which to read the property name.
		*
		*	@return	The next name in the stream of property names.
		*/
		private String name(java.nio.ByteBuffer stringStream)
		throws
			java.io.UnsupportedEncodingException
		{
			if (!fString)
				throw new RuntimeException("Not string!");

			stringStream.position(propertyID);
			final long length = stringStream.getInt();

			final byte[] arr = new byte[(int)length];
			stringStream.get(arr);

			return new String(arr, CHARSET_WIDE); 
		}

		/**	Create a string representation of this object (typically used for debugging).
		*
		*	@return	A string describing this named property ID.
		*/
		@Override
		public String toString()
		{
			return String.format("ID 0x%08x, %s, GUID Index %d Property index 0x%04x %s", propertyID, fString ? "String" : "GUID", guidIndex, propertyIndex, fString ? name : guid.toString());
		}
	}

	/**	This is an implementation of javax.swing.TableModel which may be used the name/property map. */
	public class TableModel extends javax.swing.table.DefaultTableModel {

		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;
	
		/**	Create the table model.
		*
		*	@param	namedProperties	The map of named property IDs and values.
		*/
		TableModel(final NameToIDMap namedProperties)
		{
			super();

			Object[] keyArray = namedProperties.namedProperties.keySet().toArray();
			java.util.Arrays.sort(keyArray);
			setRowCount(keyArray.length);
			setColumnCount(2);
			for (int i = 0; i < keyArray.length; ++i) {
				short key = (Short)keyArray[i];
				setValueAt(Integer.toHexString(key & 0x0000ffff), i, 0);
				setValueAt(namedProperties.name(key), i, 1);
			}
		}
	
		/**	Provide the header the given column.
		*
		*	@param	column	The index of the column to retrieve the header for.
		*
		*	@return	The name of the given column.
		*/
		public String getColumnName(final int column)
		{
			switch (column) {
			case 0: return "ID";
			case 1: return "Property";
			}
			
			return "";
		}

		/**	No cells are editable.
		*
		*	@param	row	The row index of the cell to retrieve the value of.
		*	@param	column	The column index of the cell to retrieve the value of.
		*
		*	@return	false, always.
		*/
		public boolean isCellEditable(final int row, final int column)
		{
			return false;
		}
	}

	/**	The list of named properties. Note that there will be only of of these per PST file. */
	private final java.util.HashMap<Short, String> namedProperties;

	/**	The reverse look-up of canonical ID's to mapped ID's. */
	private final java.util.HashMap<Short, Short> canonicalIDToNPID;

	/**	Construct a NameIDToMap object from the given node and block database and PST file object.
	*
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree.
	*	@param	pstFile	The PST file's input data stream, header, etc.
	*
	*	@throws UnknownClientSignatureException		The client signature for the block was not recognized.
	*	@throws NotHeapNodeException			A node which is not a heap node was found.
	*	@throws UnparseablePropertyContextException	The property context could not be parsed.
	*	@throws java.io.IOException			There was a problem reading the PST file.
	*/
	public NameToIDMap(final BlockMap bbt, final NodeMap nbt, PSTFile pstFile)
	throws
		UnknownClientSignatureException,
		NotHeapNodeException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		final PropertyContext pc = new PropertyContext(nbt.find(NID.NID_NAME_TO_ID_MAP), bbt, pstFile);
		logger.log(java.util.logging.Level.FINER, "Name to ID Map\n______________\n" + pc.toString());

		final byte[] entryRaw = getBinaryProperty(pc, PropertyTag.NameidStreamEntry);
		java.nio.ByteBuffer entryStream = PSTFile.makeByteBuffer(entryRaw);

		final byte[] stringRaw = getBinaryProperty(pc, PropertyTag.NameidStreamString);
		java.nio.ByteBuffer stringStream = PSTFile.makeByteBuffer(stringRaw);

		final byte[] guidRaw = getBinaryProperty(pc, PropertyTag.NameidStreamGuid);

		java.util.HashMap<Short, String> namedProperties = new java.util.HashMap<Short, String>();
		java.util.HashMap<Short, Short> canonicalIDToNPID = new java.util.HashMap<Short, Short>();
		while (entryStream.remaining() > 0) {
			final NameID nameID = new NameID(entryStream, guidRaw, stringStream);
			short id = (short)(PropertyID.NamedPropertyFirst | nameID.propertyIndex);
			if (nameID.fString)
				namedProperties.put(id, nameID.name);
			else {
				String name;

				if (PropertyIDByGUID.containsKey((short)nameID.propertyID, nameID.guid))
					name = PropertyIDByGUID.name((short)nameID.propertyID, nameID.guid);
				else 
					name = Integer.toHexString(nameID.propertyID & 0xffff) + "-" + nameID.guid.toString();

				namedProperties.put(id, name);
				canonicalIDToNPID.put((short)nameID.propertyID, id);
			}
		}
		this.namedProperties = namedProperties;
		this.canonicalIDToNPID = canonicalIDToNPID;
	}

	/**	Retrieve the bytes corresponding to the given property ID.
	*
	*	@param	pc		The property context containing the named property list.
	*	@param	propertyTag	The property ID to retrieve.
	*
	*	@return	The raw data saved for this property ID.
	*/
	byte[] getBinaryProperty(final PropertyContext pc, final int propertyTag)
	{
		final Object o = pc.get(propertyTag);
		if (o == null)
			throw new RuntimeException("Could not find " + PropertyTagName.name(propertyTag));
		final byte[] arr = (byte[])o;
		logger.log(java.util.logging.Level.INFO, PropertyTagName.name(propertyTag) + " (" + arr.length + " bytes): ", arr);
		return arr;
	}

	/**	Get the ID of a property named property list given its canonical tag.
	*
	*	@param	tag	The canonical tag of the property.
	*
	*	@return	The tag under which this property is stored in this PST file, if found, otherwise, -1.
	*/
	public int id(int tag)
	{
		short canonicalID = (short)(tag >>> 16);
		short dataType = (short)(tag & 0xffff);
		if (!canonicalIDToNPID.containsKey(canonicalID))
			return -1;
		short mappedID = canonicalIDToNPID.get(canonicalID);
		return (mappedID << 16 | dataType);
	}

	/**	Obtain an iterator through the named properties.
	*
	*	@return	An iterator which may be used to go through the named properties in the list.
	*/
	public java.util.Iterator<java.util.Map.Entry<Short, String>> iterator()
	{
		return namedProperties.entrySet().iterator();
	}

	/**	Retrieve the name of the given property ID.
	*
	*	@param	propertyID	The property ID to retrieve the name of.
	*
	*	@return	The name for this id, if found, or null if it is not known.
	*/
	public String name(final short propertyID)
	{
		if (propertyID >= PropertyID.NamedPropertyFirst && propertyID <= PropertyID.NamedPropertyLast) {
			final String name = namedProperties.get(propertyID);
			return name;
		}

		return null;
	}

	/**	Retrieve the name for the given property tag.
	*
	*	@param	propertyTag	The tag of the property ID + data type to retrieve.
	*
	*	@return	The name for this tag, if found, and a generic name based on the property Tag if not found.
	*/
	public String name(final int propertyTag)
	{
		final short propertyID = (short)(propertyTag >>> 16);
		final String name = name(propertyID);
		if (name != null)
			return name;

		if (propertyID >= PropertyID.NamedPropertyFirst && propertyID <= PropertyID.NamedPropertyLast)
			return String.format("namedPropertyID-%08x", propertyTag);

		return PropertyTagName.name(propertyTag);
	}

	/**	Retrieve a table model suitable for displaying the information in this class in a table.
	*
	*	@return	A javax.swing.table.TableModel suitable for displaying the data in this NameToIDMap object.
	*/
	public javax.swing.table.TableModel tableModel()
	{
		return new TableModel(this);
	}

	/**	Test the NameIDToMap class by reading the Named Properties map in and displaying the mapping.
	*
	*	@param	args	The command line arguments to the test application.
	*/
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.NameIDToMap pst-filename [log-level]");
			System.out.println("\nNote that log-level applies only to construction of the NameIDToMap object.");
			System.exit(1);
		}

		try {
			final java.util.logging.Level logLevel = args.length >= 2 ? Debug.getLogLevel(args[1]) : java.util.logging.Level.OFF;
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.github.jmcleodfoss.pst");

			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(args[0]));
			final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);
			final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);

			logger.setLevel(logLevel);
			final NameToIDMap nameToIDMap = new NameToIDMap(bbt, nbt, pstFile);

			Object[] keyArray = nameToIDMap.namedProperties.keySet().toArray();
			java.util.Arrays.sort(keyArray);

			for (Object key : keyArray)
				System.out.printf("0x%04x=%s\n", key, nameToIDMap.namedProperties.get((Short)key));
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
