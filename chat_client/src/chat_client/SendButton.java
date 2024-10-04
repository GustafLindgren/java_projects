/**
 * The SendButton class contains the styling for the "send" button.
 */
package chat_client;

import java.awt.Color;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SendButton extends JButton{
	
	
	public SendButton(String title, Color foreground, Color background) {

		this.setText(title);
		this.setForeground(foreground);
		this.setBackground(background);
		
	}
}
