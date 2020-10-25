package io.github.jmcleodfoss.swingutil;

/**	The HTMLTableModel class creates an HTML table from a Swing TableModel. */
public class HTMLTableModel
{
	/**	Create an HTML table from a Swing TableModel.
	*	@param	tm	The Swing TableModel to convert to HTML.
	*	@return	A String containing the HTML table.
	*/
	public static String html(javax.swing.table.TableModel tm)
	{
		StringBuilder s = new StringBuilder();
		s.append("<table>\n");

		s.append("<thead>\n");
		s.append("</thead>\n");

		s.append("<tbody>\n");
		for (int r = 0; r < tm.getRowCount(); ++r) {
			s.append("<tr>");
			for (int c = 0; c < tm.getColumnCount(); ++c)
				s.append("\t<td>" + tm.getValueAt(r, c) + "</td>\n");
			s.append("</tr>");
		}
		s.append("</tbody>\n");

		s.append("</table>\n");

		return s.toString();
	}
}
