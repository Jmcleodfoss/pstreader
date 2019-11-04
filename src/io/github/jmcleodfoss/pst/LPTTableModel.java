package io.github.jmcleodfoss.pst;

/**	The LPTTableModel is the base class for the table models used to represent a property context and a single row of a table context. */
class LPTTableModel extends javax.swing.table.DefaultTableModel {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	The Comparator class compares the names corresponding to the row keys in the array of entries so the rows can be
	*	sorted alphabetically by property name. Note that this cannot be defined in terms of generics since the underlying
	*	array it operates on is an array of Object.
	*/
	private class Comparator implements java.util.Comparator<java.util.Map.Entry<Integer, Object>> {

		/**	The namedProperties object is required to look up the names for the property IDs. */
		final NameToIDMap namedProperties;

		/**	Create a Comparator object. */
		Comparator(final NameToIDMap namedProperties)
		{
			this.namedProperties = namedProperties;
		}

		/**	Compare two entries, returning a value to indicate whether the first is less than, equal to, or greater than the second. */
		@SuppressWarnings("unchecked")
		public int compare(final java.util.Map.Entry<Integer, Object> e1, final java.util.Map.Entry<Integer, Object> e2)
		{
			String s1 = namedProperties.name(e1.getKey());
			String s2 = namedProperties.name(e2.getKey());
			return s1.compareTo(s2);
		}

		/**	Determine whether the two entries are equal. */
		@SuppressWarnings("unchecked")
		public boolean equals(final java.util.Map.Entry<Integer, Object> e1, final java.util.Map.Entry<Integer, Object> e2)
		{
			return e1.getKey() == e2.getKey();
		}
	}

	/**	Create the table model with the given ValueDelegate object. */
	@SuppressWarnings("unchecked")
	LPTTableModel(final java.util.Map.Entry<Integer, Object>[] entries, final NameToIDMap namedProperties)
	{
		super();

		setRowCount(entries.length);
		setColumnCount(3);
		java.util.Arrays.sort(entries, new Comparator(namedProperties));
		for (int i = 0; i < entries.length; ++i) {
			java.util.Map.Entry<Integer, Object> entry = entries[i];
			int key = entry.getKey();
			setValueAt(Integer.toHexString(key), i, 0);
			setValueAt(namedProperties.name(key), i, 1);
			setValueAt(getValueString(key, entry.getValue()), i, 2);
		}
	}

	/**	Get the heading for the given column.
	*
	*	@param	column	The column index of the heading to return.
	*
	*	@return	The column heading for the requested column.
	*/
	@Override
	public String getColumnName(final int column)
	{
		switch (column) {
		case 0: return "Tag";
		case 1: return "Property";
		case 2: return "Data";
		}
		
		return "";
	}

	/**	Retrieve the string representation of the value column (column 3).
	*
	*	@param	tag	The tag for this column.
	*	@param	value	The object contained in the cell.
	*
	*	@return	A String representation of the cell.
	*/
	String getValueString(final int tag, final Object value)
	{
		return value.toString();
	}

	/**	No cells are editable.
	*
	*	@param	row	The row index of the cell to retrieve the value of.
	*	@param	column	The column index of the cell to retrieve the value of.
	*
	*	@return	false, always.
	*/
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
