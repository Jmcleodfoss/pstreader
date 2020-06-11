package io.github.jmcleodfoss.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**	The FileSaverMenuItem class implements a generic mechanism for saving files via a submenu menu item.
*/
abstract public class FileSaverMenuItem implements ActionListener
{
	/**	The extensions permitted by the save dialog. */
	final FileNameExtensionFilter[] extensionFilters;

	/**	Construct a FileSaveMenuItem with no extension filters. */
	FileSaverMenuItem()
	{
		this(null);
	}

	/**	Construct a FileSaveMenuItem with the given extension filters.
	*	@param	extensionFilters	A list of filters indicating extensions accepted in the dialog box.
	*/
	FileSaverMenuItem(FileNameExtensionFilter[] extensionFilters)
	{
		this.extensionFilters = extensionFilters;
	}

	/**	Obtain the initial filename suggestion for the save dialog box.
	*	@return	This (base class) version of the function returns null.
	*/
	String initialFilenameSuggestion()
	{
		return null;
	}

	/**	Obtain the title for the save dialog box.
	*	@return	Text to be used as a title for the dialog box.
	*/
	String dialogTitle()
	{
		return "Save";
	}

	/**	The data function returns the bytes to be written to the new file.
	*	@return	The data bytes to be written to the new file.
	*/
	abstract byte[] data();

	/**	Action to take when File / Open is selected. Note that this is called after the TreeSelectionListener which updates
	*	the Node content display.
	*	@param	e	The event which triggered this action.
	*/
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(dialogTitle());

		final String filename = initialFilenameSuggestion();
		if (filename != null)
			fileChooser.setSelectedFile(new File(filename));

		switch(fileChooser.showSaveDialog(pstExplorer.explorer)) {
		case JFileChooser.CANCEL_OPTION:
			return;

		case JFileChooser.APPROVE_OPTION:
			byte[] data = data();
			if (data != null) {
				try {
					FileOutputStream out = new FileOutputStream(fileChooser.getSelectedFile());
					out.write(data);
					out.close();
				} catch (FileNotFoundException ex) {
				} catch (IOException ex) {
				}
			}
			return;

		case JFileChooser.ERROR_OPTION:
			return;
		}
	}
}

