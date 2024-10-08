/**
 * This class contains the GUI for the Client.
 */
package chat_client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ClientWindow extends JFrame implements ActionListener{

	private Color foreground;
	private Color background;
	private Color buttonColor;
	private Color userColor;
	private Color friendsColor;
	private Color timestampColor;

	private Font font;

	private ClientTextField sendMsgField;
	private ClientTextArea chatTextArea;
	private StartUpDialog startUpDialog;

	private SendButton sendButton;

	private Client client;

	public ClientWindow() {

		foreground =  Color.white;
		background = new Color(50, 50, 50);
		buttonColor = new Color(51, 102, 255);
		timestampColor = new Color(193, 192, 190);
		userColor = Color.cyan;
		friendsColor = Color.red;

		font = new Font("Arial", Font.PLAIN, 18);

		this.setFont(font);

		JLabel chatRoom = new JLabel();
		chatRoom.setForeground(foreground);
		chatRoom.setText("Welcome to the chat room!");

		sendMsgField = new ClientTextField(foreground, background, font, 700, 40);
		chatTextArea = new ClientTextArea(foreground, background, font, 765, 500);

		// ChatTextArea will be scrollable if the chat exceeds the height.
		JScrollPane scrollChat = new JScrollPane(chatTextArea);
		scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		sendButton = new SendButton("Send", foreground, buttonColor);
		sendButton.addActionListener(this);

		this.setTitle("Chat Client");
		this.setSize(1000, 700);
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.getContentPane().setBackground(background);

		this.add(scrollChat);
		this.add(sendMsgField);
		this.add(sendButton);

		startUpDialog = new StartUpDialog(this, foreground, background, font);
		startUpDialog.setVisible(true);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				client.closeClient();
				System.exit(0);
			}

		});

	}

	/**
	 * Starts a client with the specified username (called from StartUpDialog).
	 * @param username : username of the client.
	 */
	public void startClient(String username) {
		client = new Client(username, this);
		client.listenForMsg();
		client.setUserName(username);
	}

	/**
	 * Receives the message and the username and appends it to the chatTextArea
	 * @param received : message from the ClientHandler. Contains the username of the sender and the message
	 */
	public void receiveMsg(String received) {
		SwingUtilities.invokeLater(() -> {

			int colonIndex = received.indexOf(':');
			String username = received.substring(0, colonIndex);
			String msg = received.substring(colonIndex+1);
			chatTextArea.appendMsg(msg, username, foreground, friendsColor, timestampColor);
		});
	}

	/**
	 * Sends the message that the user has typed to the ClientHandler when pressing the send button.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton) {
			String msg = sendMsgField.getText();
			String username = client.getUsername();

			chatTextArea.appendMsg(msg, username, foreground, userColor, timestampColor);

			sendMsgField.setText("");
			client.sendMsg(msg);

		}

	}

	public static void main(String[] args) {
		new ClientWindow();

	}
}
