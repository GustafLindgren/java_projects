/**
 * The StartUpDialog class is a JDialog that opens on startup of the client where the user must
 * enter a username.
 */
package chat_client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class StartUpDialog extends JDialog{

	/**
	 * 
	 * @param clientWindow : the client main frame.
	 * @param foreground : the foreground color.
	 * @param background : the background color.
	 * @param font : the specified font.
	 */
	public StartUpDialog(ClientWindow clientWindow, Color foreground, Color background, Font font) {
		setTitle("Choose username");
		setModal(true); // so user can't interact with main window
		JLabel messageLabel = new JLabel("Welcome to the application!");
		SendButton okButton = new SendButton("Set username", foreground, background);
		ClientTextField usernameField = new ClientTextField(foreground, background, font, 500, 40);

		setLayout(new BorderLayout());
		add(messageLabel, BorderLayout.CENTER);
		add(usernameField, BorderLayout.CENTER);
		add(okButton, BorderLayout.SOUTH);

		okButton.addActionListener(e -> {
			clientWindow.startClient(usernameField.getText());
			dispose(); // Close the dialog
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				System.exit(0);
			}

		});

		pack(); // Adjust the dialog size to fit its components
		setLocationRelativeTo(clientWindow); // Center the dialog relative to the mainFrame
	}

}
