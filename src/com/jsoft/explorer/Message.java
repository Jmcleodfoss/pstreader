package com.jsoft.explorer;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import com.jsoft.pst.MessageObject;
import com.jsoft.pst.MessageObjectWithBody;
import com.jsoft.pst.PropertyContext;
import com.jsoft.pst.StickyNote;

/**	The Message class exposes some message components. */
@SuppressWarnings("serial")
class Message extends NodeContentsDisplay {

	/**	The message header. */
	JTextArea header;

	/**	The scrollpane to hold the message body. */
	JScrollPane spBodyText;

	/**	The message body as text. */
	JTextArea bodyText;

	/**	The scrollpane to hold the HTML message body. */
	JScrollPane spBodyHtml;

	/**	The message body as HTML, if present. */
	JTextPane bodyHtml;

	/**	Construct a message object. */
	Message()
	{
		super();

		header = new JTextArea();

		bodyText = new JTextArea();
		bodyText.setLineWrap(true);

		spBodyText = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spBodyText.getViewport().add(bodyText);

		bodyHtml = new javax.swing.JTextPane();
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
	*
	*	@param	message		The new message to display.
	*	@param	messagePC	The message property context.
	*/
	public void update(final MessageObject message, final PropertyContext messagePC)
	{
		final boolean fMessage = message instanceof com.jsoft.pst.Message;
		final boolean fHasBody = message instanceof MessageObjectWithBody;

		final String transportHeaders = fMessage ? ((com.jsoft.pst.Message)message).transportHeaders(messagePC) : null;
		if (transportHeaders != null) {
			header.setText(transportHeaders);
			if (indexOfComponent(header) == -1)
				add("Header", header);
		} else {
			remove(header);
		}

		final String body = fHasBody ? ((MessageObjectWithBody)message).body(messagePC) : null;
		if (body != null) {
			bodyText.setText(body);
			if (indexOfComponent(spBodyText) == -1)
				add("Body (text)", spBodyText);
		} else {
			remove(spBodyText);
		}

		final String html = fHasBody ? ((MessageObjectWithBody)message).bodyHtml(messagePC) : null;
		if (html != null) {
			bodyHtml.setText(html);
			if (indexOfComponent(spBodyHtml) == -1)
				add("Body (HTML)", spBodyHtml);
		} else {
			remove(spBodyHtml);
		}
	}
}
