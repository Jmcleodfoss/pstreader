package io.github.jmcleodfoss.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**	Handle file open requests. */
class FileOpenActionListener implements ActionListener
{
	/**	The filter used to look for PST files. */
	static final FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Outlook Personal Storage Table file", "nst", "ost", "pst");

	/**	The main application object, needed to display dialog boxes in and to load new files to */
	private final pstExplorer explorer;

	/**	The FileChooser - keep it as a member variable so we retain directory changes. */
	private JFileChooser fileChooser;

	/**	Construct the underlying FileChooser and set required filter. */
	FileOpenActionListener(pstExplorer explorer)
	{
		this.explorer = explorer;

		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(fileFilter);
	}

	/**	Take action to take when File -&gt; Open is selected: show a load file dialog box.
	*	@param	e	The menu event which triggers displaying the file open dialog.
	*/
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(fileChooser.showOpenDialog(explorer)) {
		case JFileChooser.APPROVE_OPTION:
			explorer.openFile(fileChooser.getSelectedFile());
			return;

		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
		default:
			return;
		}
	}
}
