package io.github.jmcleodfoss.pst;

/**	The LPTTableModel is the base class for the table models used to represent a property context and a single row of a table context. */
class LPTTableModel extends javax.swing.table.DefaultTableModel {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create the table model with the given ValueDelegate object.\
	*	@param	properties	The list of properties for this table row
	*	@param	namedProperties	The list of named properties for this PST file
	*/
	@SuppressWarnings("unchecked")
	LPTTableModel(final java.util.Map<Integer, Object> properties, final NameToIDMap namedProperties)
	{
		super();

		java.util.Set<Integer> keySet = properties.keySet();
		Integer keys[] = new Integer[keySet.size()];
		keys = keySet.toArray(keys);
		java.util.Arrays.sort(keys);

		setRowCount(keys.length);
		setColumnCount(3);
		for (int i = 0; i < keys.length; ++i) {
			setValueAt(Integer.toHexString(keys[i]), i, 0);
			setValueAt(namedProperties.name(keys[i]), i, 1);
			setValueAt(getValueString(keys[i], properties.get(keys[i])), i, 2);
		}
	}

	/**	Get the heading for the given column.
	*	@param	column	The column index of the heading to return.
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
	*	@param	tag	The tag for this column.
	*	@param	value	The object contained in the cell.
	*	@return	A String representation of the cell.
	*/
	String getValueString(final int tag, final Object value)
	{
		return value.toString();
	}

	/**	No cells are editable.
	*	@param	row	The row index of the cell to retrieve the value of.
	*	@param	column	The column index of the cell to retrieve the value of.
	*	@return	false, always.
	*/
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
