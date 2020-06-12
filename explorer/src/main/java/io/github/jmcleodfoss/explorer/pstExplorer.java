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

import io.github.jmcleodfoss.pst.NodeSubnodeBTree;
import io.github.jmcleodfoss.pst.PST;

/**	The pstExplorer class provides a GUI for viewing PST files.
*/
@SuppressWarnings("serial")
class pstExplorer extends JFrame
{
	/**	This governs logging of some internal debugging information. */
	private boolean fDebug = true;

	/**	The pstExplorer object, for convenient reference by other classes. */
	static pstExplorer explorer;

	/**	The list of NewFileEventListeners */
	private EventListenerList listeners;

	/**	The file we are examining. */
	private File file;

	/**	The PST file */
	private PST pst;

	/**	The full node + sub-node B-tree. */
	private NodeSubnodeBTree nodeBTree;

	/**	The overall data pane which holds the PST "top-level" components. */
	private JTabbedPane tabbedPane;

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
		explorer = this;

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
		item.addActionListener(new FileOpenActionListener());

		item = new JMenuItem("Close...");
		menu.add(item);
		item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					reset();
				}
		});

		item = new JMenuItem("Exit");
		menu.add(item);
		item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					dispose();
				}
		});

		tabbedPane = new javax.swing.JTabbedPane();
		this.add(tabbedPane);

		headerTab = new Header();
		tabbedPane.add(headerTab, "Header");
		addNewFileListener(headerTab);

		namedPropertiesTab = new AppTable();
		tabbedPane.add(namedPropertiesTab, "Properties");

		messageStoreTab = new AppTable();
		tabbedPane.add(messageStoreTab, "Msg Store");

		nodeTab = new NodeBTreeDisplay();
		tabbedPane.add(nodeTab, "Nodes");
		addNewFileListener(nodeTab);
		
		blockTab = new BlockBTreeDisplay();
		tabbedPane.add(blockTab, "Blocks");
		addNewFileListener(blockTab);

		folderTab = new FolderTree();
		tabbedPane.add(folderTab, "Folders");
		addNewFileListener(folderTab);

		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		if (args.length > 0)
			openFile(new File(args[args.length-1]));
	}

	/**	Add a NewFileListener to the list of listeners.
	*	@param	listener	The NewFileListener object to add to the list.
	*/
	static void addNewFileListener(NewFileListener listener)
	{
		explorer.listeners.add(NewFileListener.class, listener);
	}

	/**	Broadcast the new file to all NewFileListener objects. */
	private void fireNewFileListenerEvent()
	{
		NewFileEvent e = new NewFileEvent(this, pst);
		NewFileListener[] newFileListeners = listeners.getListeners(NewFileListener.class);
		for(NewFileListener l : newFileListeners)
			l.fileLoaded(e);
	}

	/**	Get the node subnode BTree
	*	@return	the root of the node subnode B-tree
	*/
	NodeSubnodeBTree getNodeBTree()
	{
		return 	nodeBTree;
	}

	/**	Read in the given PST file.
	*	@param	file	The File object indicating the PST file to load.
	*/
	void openFile(final java.io.File file)
	{
		reset();

		io.github.jmcleodfoss.swingutil.ProgressBar pb = new io.github.jmcleodfoss.swingutil.ProgressBar(this, "Reading PST File") {
			public void run()
			{
				try {
					pst = new io.github.jmcleodfoss.pst.PST(file.getAbsolutePath());
					nodeBTree = pst.nodeBTreeRoot();
					setVisible(false);
				} catch (final Exception e) {
					setVisible(false);
					JOptionPane.showMessageDialog(null, e.toString(), "Could not open PST File", JOptionPane.ERROR_MESSAGE);
					if (fDebug)
						e.printStackTrace(System.out);
					pst = null;
					nodeBTree = null;
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
	static io.github.jmcleodfoss.pst.PST pst()
	{
		return explorer.pst;
	}

	/**	Remove a NewFileListener to the list of listeners.
	*	@param	listener	The NewFileListener to remove from the list of NewFileListeners.
	*/
	static void removeFileEventListener(NewFileListener listener)
	{
		explorer.listeners.remove(NewFileListener.class, listener);
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
			}
		}

		pst = null;
		nodeBTree = null;

		System.gc();
	}

	/**	Simple mainline - create the pstExplorer object, which does all the work.
	*	@param	args	The command line arguments to the application (unused).
	*/
	public static void main(String args[])
	{
		pstExplorer ex = new pstExplorer(args);
	}
}
