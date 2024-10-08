/**
 * The ClientTextArea class is the text box where the chat messages are displayed.
 */
package chat_client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;



@SuppressWarnings("serial")
public class ClientTextArea extends JTextPane{


	/**
	 * 
	 * @param foreground : default foreground color (e.g. for text).
	 * @param background : background color.
	 * @param font : default font.
	 * @param width : width of the ClientTextArea.
	 * @param height : height of the ClientTextArea.
	 */
	public ClientTextArea(Color foreground, Color background, Font font, int width, int height) {

		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(background);
		this.setForeground(foreground);
		this.setCaretColor(foreground);
		this.setEditable(false);
		this.setBorder(BorderFactory.createLineBorder(foreground, 1));
		this.setFont(font);
	}
	
	/**
	 * Appends messages to the ClientTextArea with the format "Timestamp Username: Message"
	 * @param msg : message that is to be displayed on the ClientTextArea.
	 * @param username : username of the sender of the message.
	 * @param foreground : color of the message text.
	 * @param userColor : color for the username.
	 * @param timestampColor : color of the timestamp.
	 */
	public void appendMsg(String msg, String username, Color foreground, Color userColor, Color timestampColor) {

		Document doc = getDocument();
		SimpleAttributeSet usernameAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(usernameAttr, userColor);

		SimpleAttributeSet messageAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(messageAttr, foreground);

		SimpleAttributeSet timestampAttr = new SimpleAttributeSet();
		StyleConstants.setForeground(timestampAttr, timestampColor);

		String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

		try {
			doc.insertString(doc.getLength(), timestamp, timestampAttr);
			doc.insertString(doc.getLength(), " ", null);
			doc.insertString(doc.getLength(), username, usernameAttr);
			doc.insertString(doc.getLength(), ": ", null); // Insert a colon with default attributes
			doc.insertString(doc.getLength(), msg, messageAttr);
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException e2) {
			e2.printStackTrace();
		}
	}
}