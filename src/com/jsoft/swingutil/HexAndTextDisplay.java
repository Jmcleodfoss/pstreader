package com.jsoft.swingutil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**	Display a block of raw data as both hex and ASCII. */
public class HexAndTextDisplay extends JScrollPane {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	The default number of columns of hexadecimal data to display. */
	private static final int NUM_COLUMNS = 16;

	/**	The TableModel for hex/text display */
	private class TableModel extends AbstractTableModel {

		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	The underlying data to be displayed. */
		private byte[] data;

		/**	Create a TableModel object. */
		private TableModel()
		{
			super();
		}

		/**	Get the class used by the requested column.
		*
		*	@param	columnIndex	The index of the column to return the class of.
		*
		*	@return	The class object for the type displayed in the requested column.
		*/
		public java.lang.Class getColumnClass(final int columnIndex)
		{
			if (columnIndex < NUM_COLUMNS)
				return String.class;
			if (columnIndex < 2*NUM_COLUMNS)
				return Character.class;

			assert columnIndex < 2*NUM_COLUMNS;
			return null;
		}

		/**	Get the number of columns in the table.
		*
		*	@return	The number of columns in the display.
		*/
		public int getColumnCount()
		{
			return 2*NUM_COLUMNS;
		}

		/**	Get the name of the requested column.
		*
		*	@param	columnIndex	The index of the column to get the name for.
		*
		*	@return	The name of the requested column.
		*/
		public String getColumnName(final int columnIndex)
		{
			return Integer.toHexString(columnIndex % NUM_COLUMNS);
		}

		/**	Get the number of rows in the table.
		*
		*	@return	The number of rows in the table.
		*/
		public int getRowCount()
		{
			if (data == null)
				return 0;
			return (data.length / NUM_COLUMNS) + (data.length % NUM_COLUMNS > 0 ? 1 : 0);
		}

		/**	Get the value of the given cell.
		*
		*	@param	rowIndex	The index of the row of the cell to return the contents of.
		*	@param	columnIndex	The index of the column of the cell to return the contents of.
		*
		*	@return	The contents of the requested cell.
		*/
		public Object getValueAt(final int rowIndex, final int columnIndex)
		{
			final int i = rowIndex * NUM_COLUMNS + (columnIndex % NUM_COLUMNS);
			if (i >= data.length)
				return " ";

			if (columnIndex < NUM_COLUMNS)
				return com.jsoft.util.ByteUtil.toHexString(data[i]);

			if (data[i] < 0x20 || data[i] >= 0x80)
				return new Character('.');

			return new Character((char)data[i]);
		}

		/**	Is this cell editable? THe answer is always no, as this application is read-only.
		*
		*	@param	rowIndex	The index of the row of the cell to check.
		*	@param	columnIndex	The index of the column of the cell to check.
		*
		*	@return	false, always, as this is a read-only display.
		*/
		public boolean isCellEditable(final int rowIndex, final int columnIndex)
		{
			return false;
		}

		/**	Set the given cell to the new value. Note that this is not supported!
		*
		*	@param	valueAt		The new value of the cell.
		*	@param	rowIndex	The index of the row of the cell to set.
		*	@param	columnIndex	The index of the column of the cell to set.
		*/
		public void setValueAt(final Object valueAt, final int rowIndex, final int columnIndex)
		{
			assert false;
		}

		/**	Read in a new block.
		*
		*	@param	byteBuffer	The data stream from which to read the new data. 
		*/
		private void read(java.nio.ByteBuffer byteBuffer)
		{
			data = new byte[byteBuffer.remaining()];
			byteBuffer.get(tableModel.data);
			fireTableDataChanged();
		}

		/**	Clear the data in the model. */
		private void reset()
		{
			data = null;
			fireTableDataChanged();
		}
	}

	/**	This is a special renderer to show label column (column 0) differently (and similarly to the column headers). */
	private class HeaderColumnCellRenderer implements TableCellRenderer {

		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	Render the cell using the table header renderer.
		*
		*	@param	table		The table whose cells are being rendered.
		*	@param	value		The value of the cell to render.
		*	@param	isSelected	A flag indicating whether the cell being rendered is selected.
		*	@param	hasFocus	A flag indicating whether the cell being rendered has keyboard focus.
		*	@param	rowIndex	The row of the cell being rendered.
		*	@param	columnIndex	The column of the cell being rendered.
		*
		*	@return	The component to use to render the cell.
		*/
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex)
		{
			TableCellRenderer renderer = table.getColumnModel().getColumn(0).getHeaderRenderer();
			if (renderer == null)
				renderer = table.getTableHeader().getDefaultRenderer();
			Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex);
			Font f = c.getFont();
			c.setFont(f.deriveFont(f.getSize()+1.0f));
			return c;
		}
	}

	/**	The table model */
	private TableModel tableModel;

	/**	The table display object. */
	private JTable table;

	/**	The row header */
	private JTable rowHeader;

	/**	Construct an object to display hex and text values. */
	public HexAndTextDisplay()
	{
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setBackground(Color.LIGHT_GRAY);

		table = new JTable();
		getViewport().add(table);
		getViewport().setBackground(Color.LIGHT_GRAY);
		table.getTableHeader().setReorderingAllowed(false);

		tableModel = new TableModel();
		table.setModel(tableModel);
		tableModel.addTableModelListener(table);

		rowHeader = new JTable(1, 1);
		rowHeader.setVisible(false);
		setRowHeaderView(rowHeader);

		JViewport rowViewport = getRowHeader();
		rowViewport.setBackground(Color.LIGHT_GRAY);
		updateRowHeaderViewportWidth();
	}

	/**	Set or update the width of the row header. */
	private void updateRowHeaderViewportWidth()
	{
		final int nRows = rowHeader.getRowCount();
		final Object contents = nRows == 0 ? null : rowHeader.getValueAt(nRows-1, 0);
		final String widest = contents == null ? " FFFF  " : ((String)contents + "   ");
		final int columnWidth = SwingUtilities.computeStringWidth(rowHeader.getFontMetrics(rowHeader.getFont()), widest);
		JViewport rowViewport = getRowHeader();
		Dimension d = rowViewport.getViewSize();
		if (d.width != columnWidth) {
			d.width = columnWidth;
			rowViewport.setPreferredSize(d);
		}
	}

	/**	Read and display the data.
	*
	*	@param	byteBuffer	The data stream from which to obtain the bytes to display.
	*/
	public void read(java.nio.ByteBuffer byteBuffer)
	{
		tableModel.read(byteBuffer);
		rowHeader.setVisible(true);
		getViewport().doLayout();

		rowHeader = new JTable(tableModel.getRowCount(), 1);
		TableColumn column = rowHeader.getColumnModel().getColumn(0);
		column.setCellRenderer(new HeaderColumnCellRenderer());
		for (int i = 0; i < tableModel.getRowCount(); ++i)
			rowHeader.setValueAt(Integer.toHexString(i*NUM_COLUMNS), i, 0);
		setRowHeaderView(rowHeader);
		updateRowHeaderViewportWidth();
	}

	/**	Reset the underlying table model. */
	public void reset()
	{
		tableModel.reset();
		rowHeader.setVisible(false);
		getViewport().doLayout();
	}
}