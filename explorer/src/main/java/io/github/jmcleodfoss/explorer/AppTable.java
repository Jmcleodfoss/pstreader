package io.github.jmcleodfoss.explorer;

import java.awt.Dimension;
import java.awt.FontMetrics;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**	This is the standard table for the application. It lives in a JScrollPane, automatically resets column sizes as required, and
*	provides wrappers for useful table functions.
*/
@SuppressWarnings("serial")
class AppTable extends JScrollPane
{
	/**	Padding to apply when calculating width of text. */
	private final String PADDING = "  ";

	/**	The actual table. */
	protected JTable table;

	/**	Create the class. */
	AppTable()
	{
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getViewport().add(table);
	}

	/**	Clear the model */
	void reset()
	{
		table.setModel(new DefaultTableModel()); 
	}

	/**	Set the model and update the column widths.
	*	@param	tm	The table model describing the table displayed by this AppTable object.
	*/
	void setModel(TableModel tm)
	{
		table.setModel(tm);

		JTableHeader header = table.getTableHeader();
		FontMetrics fmHeader = header.getFontMetrics(header.getFont());

		TableColumnModel tcm = table.getColumnModel();
		FontMetrics fm = table.getFontMetrics(table.getFont());

		for (int c = 0; c < tm.getColumnCount(); ++c) {

			Object contents = tm.getColumnName(c);

			int maxWidth = contents == null ? 0 : SwingUtilities.computeStringWidth(fmHeader, contents.toString() + PADDING);

			for (int r = 0; r < tm.getRowCount(); ++r) {
				contents = tm.getValueAt(r, c);
				if (contents == null)
					continue;
				int newWidth = SwingUtilities.computeStringWidth(fm, contents.toString() + PADDING);
				if (newWidth > maxWidth)
					maxWidth = newWidth;
			}

			TableColumn tc = tcm.getColumn(c); 
			tc.setPreferredWidth(maxWidth);
			tc.setMinWidth(maxWidth);
		}

		Dimension dViewport = table.getPreferredSize();
		dViewport.height += header.getPreferredSize().height;
		getViewport().setMinimumSize(dViewport);
	}
}
