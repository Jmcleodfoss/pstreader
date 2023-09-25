package io.github.jmcleodfoss.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.EventListenerList;

import io.github.jmcleodfoss.pst.PST;

/**	The pstExplorer class provides a GUI for viewing PST files.
*/
@SuppressWarnings({"serial", "PMD.ClassNamingConventions"})
public class pstExplorer extends JFrame
{
	/**	This governs logging of some internal debugging information. */
	private boolean fDebug = true;

	/**	The list of components to notify when a new file is loaded */
	private EventListenerList listeners;

	/**	The PST file */
	private PST pst;

	/**	The PST header information. */
	private Header headerTab;

	/**	The PST named properties. */
	private AppTable namedPropertiesTab;

	/**	The PST message store. */
	private AppTable messageStoreTab;

	/**	The PST node B-tree. */
	private NodeBTreeDisplay nodeTab;

	/**	The PST block B-tree. */
	private BlockBTreeDisplay blockTab;

	/**	The PST folder tree. */
	private FolderTree folderTab;

	/**	Create the singleton pstExplorer object.
	*	@param	args	If any arguments are passed attempt to open the last argument as a PST file.
	*/
	pstExplorer(String[] args)
	{
		super("PST Explorer");

		listeners = new EventListenerList();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		Rectangle bounds = gc.getBounds();
		bounds.x += 20;
		bounds.y += 20;
		bounds.width -= 40;
		bounds.height -= 40;
		setBounds(bounds);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu;
		JMenuItem item;

		menu = new JMenu("File");
		menuBar.add(menu);
		item = new JMenuItem("Open...");
		menu.add(item);
		item.addActionListener(new FileOpenActionListener(this));

		item = new JMenuItem("Close...");
		menu.add(item);
		item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					reset();
				}
		});

		item = new JMenuItem("Exit");
		menu.add(item);
		item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					dispose();
				}
		});

		JTabbedPane tabbedPane = new JTabbedPane();
		this.add(tabbedPane);

		headerTab = new Header();
		tabbedPane.add(headerTab, "Header");
		addNewFileListener(headerTab);

		namedPropertiesTab = new AppTable();
		tabbedPane.add(namedPropertiesTab, "Properties");

		messageStoreTab = new AppTable();
		tabbedPane.add(messageStoreTab, "Msg Store");

		nodeTab = new NodeBTreeDisplay(this);
		tabbedPane.add(nodeTab, "Nodes");
		addNewFileListener(nodeTab);
		
		blockTab = new BlockBTreeDisplay(this);
		tabbedPane.add(blockTab, "Blocks");
		addNewFileListener(blockTab);

		folderTab = new FolderTree(this);
		tabbedPane.add(folderTab, "Folders");
		addNewFileListener(folderTab);
		addNewFileListener(folderTab.folderContentsDisplay);

		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		if (args.length > 0)
			openFile(new File(args[args.length-1]));
	}

	/**	Add a NewFileListener to the list of listeners.
	*	@param	listener	The NewFileListener object to add to the list.
	*/
	void addNewFileListener(NewFileListener listener)
	{
		listeners.add(NewFileListener.class, listener);
	}

	/**	Broadcast the new file to all NewFileListener objects. */
	private void fireNewFileListenerEvent()
	{
		NewFileListener[] newFileListeners = listeners.getListeners(NewFileListener.class);
		for(NewFileListener l : newFileListeners)
			l.fileLoaded(pst);
	}

	/**	Read in the given PST file.
	*	@param	file	The File object indicating the PST file to load.
	*/
	void openFile(final File file)
	{
		reset();

		new io.github.jmcleodfoss.swingutil.ProgressBar(this, "Reading PST File") {
			@Override
			public void run()
			{
				try {
					pst = new PST(file.getAbsolutePath());
					setVisible(false);
				} catch (final Exception e) {
					setVisible(false);
					JOptionPane.showMessageDialog(null, e.toString(), "Could not open PST File", JOptionPane.ERROR_MESSAGE);
					if (fDebug)
						e.printStackTrace(System.out);
					pst = null;
				}
			}
		};

		if (pst != null) {
			setTitle(file.getAbsolutePath());
			fireNewFileListenerEvent();

			namedPropertiesTab.setModel(pst.namedPropertiesTableModel());
			messageStoreTab.setModel(pst.messageStoreTableModel());
		}
	}

	/**	Convenience function to provide a reference to the PST object currently being processed.
	*	@return	The PST object for the currently loaded file.
	*/
	PST pst()
	{
		return pst;
	}

	/**	Remove a NewFileListener to the list of listeners.
	*	@param	listener	The NewFileListener to remove from the list of NewFileListeners.
	*/
	void removeFileEventListener(NewFileListener listener)
	{
		listeners.remove(NewFileListener.class, listener);
	}

	/**	Reset all data. */
	void reset()
	{
		headerTab.reset();
		namedPropertiesTab.reset();
		messageStoreTab.reset();
		nodeTab.reset();
		blockTab.reset();
		folderTab.reset();

		if (pst != null) {
			try {
				pst.close();
			} catch (final IOException e) {
				// Can't do anything if PSTFile.close fails, so pass through and reset the variable
			}
		}

		pst = null;
	}

	/**	Simple mainline - create the pstExplorer object, which does all the work.
	*	@param	args	The command line arguments to the application (unused).
	*/
	public static void main(String args[])
	{
		new pstExplorer(args);
	}
}
