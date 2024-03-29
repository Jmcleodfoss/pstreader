package io.github.jmcleodfoss.pst;

/**	The NameIDToMap class wraps the PropertyContext node NID_NAME_ID_TO_MAP (0x61) to provide names for the properties therein.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/60003704-dfa6-476f-b782-ce8bb52a2df3">MS-PST Section 2.4.2.2: Named Properties</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/e17e195d-0454-4b9b-b398-c9127a26a678">MS-PST Section 2.4.7: Named Property Lookup Map</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/86dd69f7-8bef-48f3-abab-671b54e00976">MS-PST Section 2.5: Calculated Properties</a>
*/
public class NameToIDMap
{
	/**	The list of named properties. Note that there will be only of of these per PST file. */
	private final java.util.HashMap<Integer, String> namedProperties;

	/**	The reverse look-up of canonical ID's to mapped ID's. */
	private final java.util.HashMap<Integer, Integer> canonicalIDToNPID;

	/**	The NameID class contains information about an individual entry in the NID_NAME_ID_TO_MAP node.
	*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/0d6b4781-92c5-4d49-b24b-b783557098d1">MS-PST Section 2.4.7.1: NAMEID</a>
	*/
	private static class NameID
	{
		/**	GUID index (type) {@value}: No GUID. */
		private static final short GUID_INDEX_NONE = 0;

		/**	GUID index (type) {@value}: GUID in PS_MAPI.
		*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/cc9d955b-1492-47de-9dce-5bdea80a3323">MS-OXPROPS Section 1.3.2: Commonly Used Property Sets</a>
		*/
		private static final short GUID_INDEX_MAPI = 1;

		/**	GUID index (type) {@value}: GUID is in PS_PUBLIC_STRINGS.
		*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/cc9d955b-1492-47de-9dce-5bdea80a3323">MS-OXPROPS Section 1.3.2: Commonly Used Property Sets</a>
		*/
		private static final short GUID_INDEX_PUBLIC_STRINGS = 2;

		/**	GUID index (type) {@value}: GUID is found at the "(n-1)*16" byte offset in the GUID stream. */
		private static final short GUID_INDEX_GUID = 3;

		/**	If {@link #fString} is trye, this is the offset into the string stream of the node at which the property name is found, otherwise, this is the numeric identifier of the property.
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
		*	@param	entryStream	The data stream from which to read the named ID information.
		*	@param	guidArray	The GUID as read from the GUID stream.
		*	@param	stringStream	The data stream from which to read the property name.
		*	@throws IncorrectNameIDStreamContentException	either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
		*	@throws	java.io.UnsupportedEncodingException	An unsupported encoding was found when creating a String from a data buffer.
		*/
		NameID(java.nio.ByteBuffer entryStream, final byte[] guidArray, java.nio.ByteBuffer stringStream)
		throws
			IncorrectNameIDStreamContentException,
			java.io.UnsupportedEncodingException
		{
			propertyID = entryStream.getInt();

			final short fStringAndGuid = entryStream.getShort();
			fString = (fStringAndGuid & 0x0001) != 0;
			guidIndex = (short)(0xffff & (fStringAndGuid >> 1));

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
		*	@param	guidArray	The raw data from which to create the GUID.
		*	@return	A io.github.jmcleodfoss.pst.GUID object corresponding to the passed raw GUID.
		*	@throws IncorrectNameIDStreamContentException	either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
		*/
		private GUID guid(final byte[] guidArray)
		throws
			IncorrectNameIDStreamContentException
		{
			if (fString)
				throw new IncorrectNameIDStreamContentException("GUID", "string");

			if (guidIndex == GUID_INDEX_NONE)
				return GUID.PS_NULL;
			if (guidIndex == GUID_INDEX_MAPI)
				return GUID.PS_MAPI;
			if (guidIndex == GUID_INDEX_PUBLIC_STRINGS)
				return GUID.PS_PUBLIC_STRINGS;

			final int offset = (guidIndex - GUID_INDEX_GUID) * 16;

			return new GUID(guidArray, offset);
		}

		/**	Retrieve the property name from the array of names, throwing an exception if this NameID object does not have a name.
		*	@param	stringStream	The input data stream from which to read the property name.
		*	@return	The next name in the stream of property names.
		*	@throws IncorrectNameIDStreamContentException	either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
		*	@throws	java.io.UnsupportedEncodingException	An unsupported encoding was found when creating a String from a data buffer.
		*/
		private String name(java.nio.ByteBuffer stringStream)
		throws
			IncorrectNameIDStreamContentException,
			java.io.UnsupportedEncodingException
		{
			if (!fString)
				throw new IncorrectNameIDStreamContentException("String", "binary");

			stringStream.position(propertyID);
			final long length = stringStream.getInt();

			final byte[] arr = new byte[(int)length];
			stringStream.get(arr);

			return new String(arr, DataType.CHARSET_WIDE);
		}

		/**	Create a string representation of this object (typically used for debugging).
		*	@return	A string describing this named property ID.
		*/
		@Override
		public String toString()
		{
			return String.format("ID 0x%08x, %s, GUID Index %d Property index 0x%04x %s", propertyID, fString ? "String" : "GUID", guidIndex, propertyIndex, fString ? name : guid.toString());
		}
	}

	/**	This is an implementation of javax.swing.TableModel which may be used to display the name/property map. */
	static public class TableModel extends javax.swing.table.DefaultTableModel
	{
		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	Create the table model.
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
				int key = (Integer)keyArray[i];
				setValueAt(Integer.toHexString(key & 0x0000ffff), i, 0);
				setValueAt(namedProperties.name(key), i, 1);
			}
		}

		/**	Provide the header the given column.
		*	@param	column	The index of the column to retrieve the header for.
		*	@return	The name of the given column.
		*/
		@Override
		public String getColumnName(final int column)
		{
			switch (column) {
			case 0: return "ID";
			case 1: return "Property";
			default: return "";
			}
		}

		/**	No cells are editable.
		*	@param	row	The row index of the cell to retrieve the value of.
		*	@param	column	The column index of the cell to retrieve the value of.
		*	@return	false, always.
		*/
		@Override
		public boolean isCellEditable(final int row, final int column)
		{
			return false;
		}
	}

	/**	Construct a NameIDToMap object from the given node and block database and PST file object.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	nbt	The PST file's node B-tree.
	*	@param	pstFile	The PST file's input data stream, header, etc.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws IncorrectNameIDStreamContentException	either the Name ID GUID stream contains string values, or the Name ID Name stream contains binary data
	*	@throws	NameIDStreamNotFoundException	The requested Name ID mapping stream could not be found
	*	@throws NotHeapNodeException			A node which is not a heap node was found.
	* 	@throws NotPropertyContextNodeException		A node without the Property Context client signature was found while building a property context.
	*	@throws NullDataBlockException			A null data block was found while building a property context.
	*	@throws NullNodeException	The NullNodeException is thrown when a node is found to be null when building a PropertyContext.
	*	@throws	UnimplementedPropertyTypeException	Handling for the property type has not been implemented
	*	@throws	UnknownClientSignatureException		The client signature for the block was not recognized.
	*	@throws UnknownPropertyTypeException		The property type was not recognized
	*	@throws UnparseablePropertyContextException	The property context could not be parsed.
	*	@throws java.io.IOException			There was a problem reading the PST file.
	*/
	@SuppressWarnings("this-escape") // Use of this calls a function that sets a pointer to this; no unsafe use happens
	public NameToIDMap(final BlockMap bbt, final NodeMap nbt, PSTFile pstFile)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		IncorrectNameIDStreamContentException,
		NameIDStreamNotFoundException,
		NotHeapNodeException,
		NotPropertyContextNodeException,
		NullDataBlockException,
		NullNodeException,
		UnimplementedPropertyTypeException,
		UnknownClientSignatureException,
		UnknownPropertyTypeException,
		UnparseablePropertyContextException,
		java.io.IOException
	{
		final PropertyContext pc = new PropertyContext(nbt.find(NID.NID_NAME_TO_ID_MAP), bbt, pstFile);

		final byte[] entryRaw = getStream(pc, PropertyTags.NameidStreamEntry);
		java.nio.ByteBuffer entryStream = PSTFile.makeByteBuffer(entryRaw);

		final byte[] stringRaw = getStream(pc, PropertyTags.NameidStreamString);
		java.nio.ByteBuffer stringStream = PSTFile.makeByteBuffer(stringRaw);

		final byte[] guidRaw = getStream(pc, PropertyTags.NameidStreamGuid);

		java.util.HashMap<Integer, String> namedProperties = new java.util.HashMap<Integer, String>();
		java.util.HashMap<Integer, Integer> canonicalIDToNPID = new java.util.HashMap<Integer, Integer>();
		while (entryStream.remaining() > 0) {
			final NameID nameID = new NameID(entryStream, guidRaw, stringStream);
			int id = 0xffff & (PropertyTags.NamedPropertyFirst | nameID.propertyIndex);
			if (nameID.fString)
				namedProperties.put(id, nameID.name);
			else {
				namedProperties.put(id, PropertyLIDs.name(nameID.propertyID, nameID.guid));
				canonicalIDToNPID.put(nameID.propertyID, id);
			}
		}
		this.namedProperties = namedProperties;
		this.canonicalIDToNPID = canonicalIDToNPID;

		// Dependency injection into TableContext to show column names properly in client apps using Swing AbstractTableModel to show the data
		TableContext.setNamedProperties(this);
	}

	/**	Retrieve the bytes corresponding to the given property ID.
	*	@param	pc		The property context containing the named property list.
	*	@param	propertyTag	The property ID to retrieve.
	*	@return	The raw data saved for this property ID.
	*	@throws BadXBlockLevelException	The level must be 1 (for XBlock) or 2 (for XXBlock) but a different value was found
	*	@throws BadXBlockTypeException	The type must be 1 for XBlock and XXBlock
	*	@throws CRCMismatchException	The block's calculated CDC is not the same as the expected value.
	*	@throws	NameIDStreamNotFoundException	The requested Name ID mapping stream could not be found
	*/
	private byte[] getStream(final PropertyContext pc, final int propertyTag)
	throws
		BadXBlockLevelException,
		BadXBlockTypeException,
		CRCMismatchException,
		NameIDStreamNotFoundException
	{
		final Object o = pc.get(propertyTag);
		if (o == null)
			throw new NameIDStreamNotFoundException(PropertyTags.name(propertyTag));
		final byte[] arr = (byte[])o;
		return arr;
	}

	/**	Get the ID of a property named property list given its canonical tag.
	*	@param	canonicalId	The canonical tag of the property.
	*	@param	dataType	The type of the data (this is known a priori if this function is called)
	*	@return	The tag under which this property is stored in this PST file, if found, otherwise, -1.
	*/
	public int id(int canonicalId, int dataType)
	{
		if (!canonicalIDToNPID.containsKey(canonicalId))
			return PropertyLIDs.UNKNOWN;
		int mappedID = canonicalIDToNPID.get(canonicalId);
		return (mappedID << 16 | dataType);
	}

	/**	Obtain an iterator through the named properties.
	*	@return	An iterator which may be used to go through the named properties in the list.
	*/
	public java.util.Iterator<java.util.Map.Entry<Integer, String>> iterator()
	{
		return namedProperties.entrySet().iterator();
	}

	/**	Retrieve the name for the given property tag.
	*	@param	propertyTag	The tag of the property ID + data type to retrieve.
	*	@return	The name for this tag, if found, and a generic name based on the property Tag if not found.
	*/
	public String name(final int propertyTag)
	{
		if (PropertyTags.tags.containsKey(propertyTag))
			return PropertyTags.tags.get(propertyTag);

		int propertyId = propertyTag;
		if ((propertyTag & 0xffff0000) != 0) {
			// Were we passed a property ID + data type> Convert to a pure property ID and check for named properties
			propertyId = (propertyTag >>> 16);
		}

		if (propertyId >= PropertyTags.NamedPropertyFirst && propertyId <= PropertyTags.NamedPropertyLast && namedProperties.containsKey(propertyId)) {
			return namedProperties.get(propertyId);
		}

		return String.format("propertyTag-%08x", propertyTag);
	}

	/**	Retrieve a table model suitable for displaying the information in this class in a table.
	*	@return	A javax.swing.table.TableModel suitable for displaying the data in this NameToIDMap object.
	*/
	public javax.swing.table.TableModel tableModel()
	{
		return new TableModel(this);
	}

	/**	Test the NameIDToMap class by reading the Named Properties map in and displaying the mapping.
	*	@param	args	The name(s) of the file(s) to show the Named Properties of.
	*/
	@SuppressWarnings("PMD.DoNotCallSystemExit")
	public static void main(final String[] args)
	{
		if (args.length < 1) {
			System.out.println("use:\n\tjava io.github.jmcleodfoss.pst.NameIDToMap pst-file [pst-file ...]");
			System.exit(1);
		}

		for (final String a: args) {
			System.out.println(a);
			try {
				java.io.FileInputStream stream = new java.io.FileInputStream(a);
				final PSTFile pstFile = new PSTFile(stream);
				try {
					final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);
					final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);

					final NameToIDMap nameToIDMap = new NameToIDMap(bbt, nbt, pstFile);

					Object[] keyArray = nameToIDMap.namedProperties.keySet().toArray();
					java.util.Arrays.sort(keyArray);

					for (Object key : keyArray)
						System.out.printf("0x%04x=%s%n", (Integer)key, nameToIDMap.namedProperties.get((Integer)key));
				} catch (final	BadXBlockLevelException
					|	BadXBlockTypeException
					|	IncorrectNameIDStreamContentException
					|	NameIDStreamNotFoundException
					|	NotHeapNodeException
					|	NotPropertyContextNodeException
					|	NullDataBlockException
					|	NullNodeException
					|	UnimplementedPropertyTypeException
					|	UnknownClientSignatureException
					|	UnknownPropertyTypeException
					|	UnparseablePropertyContextException e) {
					System.out.println(e);
					e.printStackTrace(System.out);
				} finally {
					try {
						pstFile.close();
					} catch (final java.io.IOException e) {
						System.out.printf("There was a problem closing file %s%n", a);
					}
				}
			} catch (final NotPSTFileException e) {
				System.out.printf("File %s is not a pst file%n", a);
			} catch (final CRCMismatchException e) {
				System.out.printf("File %s is corrupt (Calculated CRC does not match expected value)%n", a);
			} catch (final java.io.FileNotFoundException e) {
				System.out.printf("File %s not found%n", a);
			} catch (final java.io.IOException e) {
				System.out.printf("Could not read %s%n", a);
				e.printStackTrace(System.out);
			}
		}
	}
}
