/**
 * The Client class contains the logic for communicating with the server
 */

package chat_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private String username;
	private ClientWindow window;

	/**
	 * Constructor for the Client. 
	 * @param username : username for the client.
	 * @param window : the main GUI window for the client.
	 */
	public Client(String username, ClientWindow window) {
		try {
			this.username = username;
			this.window = window;
			socket = new Socket("localhost", 1234);
			// A printwrite would be better (as is used in the ClientHandler class).
			// The BufferedWriter is only used for learning purposes.
			this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e){
			e.printStackTrace();
		}

	}

	public String getUsername() {return username;}

	/**
	 * Sends the username to the ClientHandler instance. 
	 * @param username : username of the client
	 */
	public void setUserName(String username) {
		try {
			out.write(username);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Sends the message to the ClientHandler instance.
	 * @param msg : message to be sent.
	 */
	public void sendMsg(String msg) {
		try {
			out.write(username + ": " + msg);
			out.newLine();
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Listens for messages from its corresponding ClientHandler instance.
	 * Exception is thrown when a user exits the program via the GUI (closeClient is called)
	 */
	public void listenForMsg() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				String incomingMsg;
				try {
					while ((incomingMsg = in.readLine()) != null) {
						window.receiveMsg(incomingMsg);
					}
				} catch (IOException e){
					System.out.println("Client exited");
				}
			}

		}).start();
	}
	
	/**
	 * Close the client end socket.
	 */
	public void closeClient() {

		try {
			if (socket != null) {
				socket.close();	
			}
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
