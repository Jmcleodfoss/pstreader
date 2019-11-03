package com.jsoft.wholovesyou;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.jsoft.pst.Appointment;
import com.jsoft.pst.Contact;
import com.jsoft.pst.Folder;
import com.jsoft.pst.IPF;
import com.jsoft.pst.JournalEntry;
import com.jsoft.pst.MessageObject;
import com.jsoft.pst.NotHeapNodeException;
import com.jsoft.pst.NotPSTFileException;
import com.jsoft.pst.PST;
import com.jsoft.pst.StickyNote;
import com.jsoft.pst.TableContext;
import com.jsoft.pst.Task;
import com.jsoft.pst.UnknownClientSignatureException;
import com.jsoft.pst.UnparseablePropertyContextException;
import com.jsoft.pst.UnparseableTableContextException;

/**	The ContactFormBean shares the data from the contact upload form with the contact server. */
@ManagedBean(name = "pstBean")
@SessionScoped
public class PSTBean {

	/**	The maximum number of attempts to provide a password permitted. */
	private static final int MAX_PASSWORD_ATTEMPTS = 3;

	/**	Text resources used by this bean. */
	private final ResourceBundle rb;

	/**	The uploaded PST file. */
	private UploadedFile uploadedFile;

	/**	The password for the PST file. */
	private String password;

	/**	The PST file. */
	private PST pst;

	/**	The number of attempts to provide a password. */
	private int numPasswordAttempts;

	/**	Create a bean for communication between the form and the servlet. */
	public PSTBean()
	{
		rb = ResourceBundle.getBundle("com.jsoft.pstExtractor.text-resources");
	}

	/**	Get the required information from a PST file and handle any exceptions encountered during processing.
	*
	*	@param	clientDestination	The client component in which to write any error messages.
	*
	*	@return	A String indicating the next view
	*/
	private String doProcessPST(String clientDestination)
	{
		try {
			processPST();
			return "Results";
		} catch (IOException e) {
			return "ProcessingProblem";
		} catch (NotHeapNodeException e) {
			return "CorruptPST";
		} catch (UnknownClientSignatureException e) {
			return "CorruptPST";
		} catch (UnparseablePropertyContextException e) {
			return "CorruptPST";
		} catch (UnparseableTableContextException e) {
			return "CorruptPST";
		}
	}

	/**	Retrieve the password.
	*
	*	@return	The password for the PST file, if any.
	*/
	public String getPassword()
	{
		return password;
	}

	/**	Retrieve the uploaded file.
	*
	*	@return	The uploaded PST file
	*/
	public UploadedFile getUploadedFile()
	{
		return uploadedFile;
	}

	/**	Determine whether any results are available.
	*
	*	@return	true if a PST file has been processed and is available, false otherwise.
	*/
	public boolean isResultAvailable()
	{
		return pst != null;
	}

	/**	Set the password.
	*
	*	@param	password	The password for the PST file, if any.
	*/
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**	Get the required information from a PST file
	*/
	private void processPST()
	throws
		NotHeapNodeException,
		UnknownClientSignatureException,
		UnparseablePropertyContextException,
		UnparseableTableContextException,
		java.io.IOException
	{
		Folder rootFolder = pst.getFolder(pst.nodeBTree.find(pst.messageStore.rootMailboxEntry.nid));
		TableContext rootHierarchyTable = new TableContext(rootFolder.nodeHierarchyTable, pst.blockBTree, pst);

		for (Iterator<Folder> folderIterator = rootFolder.subfolderIterator(); folderIterator.hasNext(); ) {
			final Folder f = folderIterator.next();

			if (!IPF.isMessage(f))
				continue;

			// Find e-mails with only one recipient, and count the number with the correspondent.
		}
	}

	/**	Set the uplaoded PST file.
	*
	*	@param	uploadedFile	The uploaded PST file.
	*/
	public void setUploadedFile(UploadedFile uploadedFile)
	{
		this.uploadedFile = uploadedFile;
	}

	/**	Process submission from Incorrect Password form.
	*
	*	@return	A String indicating the next view.
	*/
	public String submitPassword()
	{
		if (pst == null) {
			// Note that this is not an expected workflow.
			return submitPST();
		}

		if ((pst.hasPassword() && password.length() == 0) || (!pst.hasPassword() && password.length() > 0))
			return "ResubmitPassword";

		return doProcessPST("uploadPasswordForm");
	}

	/**	Process submission from main PST extraction form.
	*
	*	@return	A String indicating the next view.
	*/
	public String submitPST()
	{
		try {
			InputStream is = uploadedFile.getInputStream();

			if (is instanceof FileInputStream) {
				try {
					pst = null;
					pst = new PST((FileInputStream)is, true);
					if ((pst.hasPassword() && password.length() == 0) || (!pst.hasPassword() && password.length() > 0))
						return "ResubmitPassword";
					return doProcessPST(clientDestination);
				} catch (IOException e) {
					// IO Exception creating or reading PST file
					return "ProcessingProblem";
				} catch (NotHeapNodeException e) {
					return "CorruptPST";
				} catch (NotPSTFileException e) {
					return "NotPST";
				} catch (UnknownClientSignatureException e) {
					return "CorruptPST";
				} catch (UnparseablePropertyContextException e) {
					return "CorruptPST";
				} catch (UnparseableTableContextException e) {
					return "CorruptPST";
				}
			}
		} catch (IOException e) {
			// IO Exception retrieving input stream.
			return "ProcessingProblem";
		}

		// This is an unexpected condition. It can only occur if we raised an uncaught exception.
		return "ProcessingProblem";
	}
}