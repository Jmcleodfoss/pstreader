package io.github.jmcleodfoss.pst;

/**	The ReadOnlyTableModel is a version of DefaultTableModel which does not permit cells to be edited. */
public class ReadOnlyTableModel extends javax.swing.table.DefaultTableModel {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Create a ReadOnlyTableModel object.
	*	@param	cells	The table contents.
	*	@param	columnHeaders	The column headers.
	*/
	public ReadOnlyTableModel(Object[][] cells, Object[] columnHeaders)
	{
		super(cells, columnHeaders);
	}

	/**	Is the given cell editable?
	*	@param	row	The row containing the cell to check.
	*	@param	col	The column containing the cell to check.
	*	@return	false, always.
	*/
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
}
