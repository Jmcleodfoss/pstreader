package io.github.jmcleodfoss.explorer;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import io.github.jmcleodfoss.pst.BadXBlockLevelException;
import io.github.jmcleodfoss.pst.BadXBlockTypeException;
import io.github.jmcleodfoss.pst.CRCMismatchException;
import io.github.jmcleodfoss.pst.MessageObject;
import io.github.jmcleodfoss.pst.MessageObjectWithBody;
import io.github.jmcleodfoss.pst.PropertyContext;

/**	The Message class exposes some message components. */
@SuppressWarnings("serial")
class Message extends NodeContentsDisplay
{
	/**	The message header. */
	private JTextArea header;

	/**	The scrollpane to hold the message body. */
	private JScrollPane spBodyText;

	/**	The message body as text. */
	private JTextArea bodyText;

	/**	The scrollpane to hold the HTML message body. */
	private JScrollPane spBodyHtml;

	/**	The message body as HTML, if present. */
	private JTextPane bodyHtml;

	/**	Construct a message object.
	*	@param	explorer	The main Explorer application
	*/
	Message(pstExplorer explorer)
	{
		super(explorer);

		header = new JTextArea();

		bodyText = new JTextArea();
		bodyText.setLineWrap(true);

		spBodyText = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spBodyText.getViewport().add(bodyText);

		bodyHtml = new JTextPane();
		bodyHtml.setContentType("text/html");
		bodyHtml.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);

		spBodyHtml = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spBodyHtml.getViewport().add(bodyHtml);
	}

	/**	Clear the data. */
	@Override
	public void reset()
	{
		super.reset();
		header.setText("");
		bodyText.setText("");
		bodyHtml.setText("");
	}

	/**	Update the display to reflect the currently selected node.
	*	@param	message		The new message to display.
	*	@param	messagePC	The message property context.
	*/
//	@SuppressWarnings("PMD.NPathComplexity")
	public void update(final MessageObject message, final PropertyContext messagePC)
	{
		final boolean fMessage = message instanceof io.github.jmcleodfoss.pst.Message;
		final boolean fHasBody = message instanceof MessageObjectWithBody;

		try {
			final String transportHeaders = fMessage ? ((io.github.jmcleodfoss.pst.Message)message).transportHeaders(messagePC) : null;
			if (transportHeaders != null) {
				header.setText(transportHeaders);
				if (indexOfComponent(header) == -1)
					add("Header", header);
			} else {
			remove(header);
			}
		} catch (final	BadXBlockLevelException
			|	BadXBlockTypeException
			|	CRCMismatchException e) {
			remove(header);
		}

		try {
			final String body = fHasBody ? ((MessageObjectWithBody)message).body(messagePC) : null;
			if (body != null) {
				bodyText.setText(body);
				if (indexOfComponent(spBodyText) == -1)
					add("Body (text)", spBodyText);
			} else {
				remove(spBodyText);
			}
		} catch (final	BadXBlockLevelException
			|	BadXBlockTypeException
			|	CRCMismatchException e) {
			remove(spBodyText);
		}

		try {
			final String html = fHasBody ? ((MessageObjectWithBody)message).bodyHtml(messagePC) : null;
			if (html != null) {
				bodyHtml.setText(html);
				if (indexOfComponent(spBodyHtml) == -1)
					add("Body (HTML)", spBodyHtml);
			} else {
				remove(spBodyHtml);
			}
		} catch (final	BadXBlockLevelException
			|	BadXBlockTypeException
			|	CRCMismatchException e) {
			remove(spBodyHtml);
		}
	}
}
