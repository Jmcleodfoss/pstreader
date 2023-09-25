package io.github.jmcleodfoss.explorer;

import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import io.github.jmcleodfoss.pst.PST;
import io.github.jmcleodfoss.swingutil.HexAndTextDisplay;

/**	Display information about the PST header. */
@SuppressWarnings("serial")
class Header extends JSplitPane implements NewFileListener
{
	// Constants for placement of header information
	private static final int COL_NAME = 0;
	private static final int COL_VALUE = 1;
	private static final int COL_NUM_COLUMNS = 2;

	private static final int ROW_FILE_FORMAT = 0;
	private static final int ROW_ENCRYPTION_METHOD = 1;
	private static final int ROW_NBT_ROOT = 2;
	private static final int ROW_BBT_ROOT = 3;
	private static final int ROW_NUM_ROWS = 4;

	/**	The raw data display component of the Header display. */
	private HexAndTextDisplay rawData;

	/**	The human-readable values available from the pst.Header object. */
	private JTable table;

	/**	Construct a header display object. */ 
	Header()
	{
		super(JSplitPane.VERTICAL_SPLIT);

		setTopComponent(table = new JTable(ROW_NUM_ROWS, COL_NUM_COLUMNS));
		table.setMinimumSize(new Dimension(300,150));
		table.setValueAt("FileFormat", ROW_FILE_FORMAT, COL_NAME);
		table.setValueAt("Encryption Method", ROW_ENCRYPTION_METHOD, COL_NAME);
		table.setValueAt("Node BTree Root", ROW_NBT_ROOT, COL_NAME);
		table.setValueAt("Block BTree Root", ROW_BBT_ROOT, COL_NAME);

		setBottomComponent(rawData = new HexAndTextDisplay());
		rawData.setMinimumSize(new Dimension(300,150));
	}

	/**	Update with information from the new file.
	*	@param	pst	The PST object loaded.
	*/
	@Override
	public void fileLoaded(final PST pst)
	{
		try {
			rawData.read(pst.read(0, pst.header.size()));
			table.setValueAt(pst.header.fileFormat, ROW_FILE_FORMAT, COL_VALUE);
			table.setValueAt(pst.header.encryption, ROW_ENCRYPTION_METHOD, COL_VALUE);
			table.setValueAt(pst.header.nbtRoot, ROW_NBT_ROOT, COL_VALUE);
			table.setValueAt(pst.header.bbtRoot, ROW_BBT_ROOT, COL_VALUE);
		} catch (java.io.IOException e) {
			e.printStackTrace(System.out);
			table.setValueAt("", ROW_FILE_FORMAT, COL_VALUE);
			table.setValueAt("", ROW_ENCRYPTION_METHOD, COL_VALUE);
			table.setValueAt("", ROW_NBT_ROOT, COL_VALUE);
			table.setValueAt("", ROW_BBT_ROOT, COL_VALUE);
		}
	}

	/**	Reset the contents of the header table. */
	void reset()
	{
		for (int i = 0; i < ROW_NUM_ROWS; ++i)
			table.setValueAt("", i, COL_VALUE);
		rawData.reset();
	}
}
