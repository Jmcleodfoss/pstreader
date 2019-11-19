package io.github.jmcleodfoss.swingutil;

/**	The ProgressBar is a modal-dialog display of a JProgressBar of indeterminate duration which runs in its own thread.
*	Clients must override the Runnable.run function to perform any actions to be done while the progress bar is displayed.
*/
abstract public class ProgressBar extends javax.swing.JDialog implements Runnable {

	/**	The serialVersionUID is required because the base class is serializable. */
	private static final long serialVersionUID = 1L;

	/**	Default dialog box height. */
	private static final int DEFAULT_HEIGHT = 60; // px

	/**	Default dialog box width. */
	private static final int DEFAULT_WIDTH = 200; // px

	/**	Construct a dialog box holding a progress bar, centered in the owner frame which is displayed automatically on
	*	construction.
	*
	*	@param	owner	The containing Frame for the ProgressBar dialog box.
	*	@param	title	The title of the ProgressBar dialog box
	*/
	public ProgressBar(java.awt.Frame owner, String title)
	{
		this(owner, title, true);
	}

	/**	Construct a dialog box holding a progress bar, centered in the owner frame which is displayed automatically if required
	*	on construction.
	*
	*	@param	owner		The containing Frame for the ProgressBar dialog box.
	*	@param	title		The title of the ProgressBar dialog box
	*	@param	fAutoStart	A flag indicating whether the progress bar should be displayed automatically, or later by
	*				calling the {@link #start} function.
	*/
	public ProgressBar(java.awt.Frame owner, String title, boolean fAutoStart)
	{
		super(owner, title, true);
		setLayout(new java.awt.FlowLayout());

		javax.swing.JProgressBar pb = new javax.swing.JProgressBar();
		pb.setIndeterminate(true);
		pb.setSize(DEFAULT_WIDTH-10, DEFAULT_HEIGHT-10);
		add(pb);

		java.awt.Dimension dParent = owner.getSize();
		java.awt.Point p = new java.awt.Point((dParent.width-DEFAULT_WIDTH)/2, (dParent.height-DEFAULT_HEIGHT)/2);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocation(p);

		if (fAutoStart)
			start();
	}

	/**	Start a thread containing this progress bar and make it visible. */
	public void start()
	{
		new Thread(this).start();
		setVisible(true);
	}
}
