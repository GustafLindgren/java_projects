/**
 * The ClientTextField class is the text box where the user enters their messages.
 */
package chat_client;

import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

@SuppressWarnings("serial")
public class ClientTextField extends JTextField{
	
	
	public ClientTextField(Color foreground, Color background, Font font, int width, int height) {
		
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(background);
		this.setForeground(foreground);
		this.setCaretColor(foreground);
		this.setFont(font);

		
	}
}
