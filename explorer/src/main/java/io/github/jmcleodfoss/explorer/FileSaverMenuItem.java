package io.github.jmcleodfoss.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**	The FileSaverMenuItem class implements a generic mechanism for saving files via a submenu menu item.
*/
abstract class FileSaverMenuItem implements ActionListener
{
	/**	The parent frame to display dialog boxes in */
	private final JFrame parentFrame;

	/**	Construct the underlying FileChooser and set required filter.
	*	@param	parentFrame	The frame in which to display the dialog box
	*/
	FileSaverMenuItem(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
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

		switch(fileChooser.showSaveDialog(parentFrame)) {
		case JFileChooser.APPROVE_OPTION:
			byte[] data = data();
			if (data != null) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(fileChooser.getSelectedFile());
					out.write(data);
				} catch (final FileNotFoundException ex) {
				} catch (final IOException ex) {
				} finally {
					try {
						if (out != null)
							out.close();
					} catch (final IOException ex) {
					}
				}
			}
			return;

		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
		default:
			return;
		}
	}
}

