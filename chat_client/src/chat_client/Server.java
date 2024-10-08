/**
 * The server class.
 */

package chat_client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Server {

	// serverSocket listens for incoming connections from clients
	private ServerSocket serverSocket;
	private static int port = 1234;

	/**
	 * @param serverSocket : serverSocket object with a specified port
	 */
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * Starts the server.
	 * accept() blocks until a connection is accepted.
	 * When the server receives a connection request, accept() is called and a socket object is returned.
	 * The client communicates with the server through this socket.
	 */
	public void startServer() {

		try {

			while (!serverSocket.isClosed()) {

				Socket socket = serverSocket.accept();
				System.out.println("A new client has connected!");

				ClientHandler clientHandler = new ClientHandler(socket);
				
				// Run on multiple threads to ensure concurrent client handling
				Thread thread = new Thread(clientHandler);
				thread.start();

			}
		}

		catch (IOException e) {

			closeServerSocket();
		}
	}

	public void closeServerSocket() {

		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException{

		// listens on port 1234
		ServerSocket serverSocket = new ServerSocket(port);
		Server server = new Server(serverSocket);
		server.startServer();

	}


	/**
	 * ClientHandler class
	 * The purpose of this class is to handle communication with connected clients.
	 * Reads messages by its connected client and broadcasts the messages to all the other clients.
	 * Implements runnable so instances will be executed on different threads.
	 */
	private class ClientHandler implements Runnable{

		// ArrayList of all ClientHandlers
		// Purpose: loop through and send message to each client
		public static List<ClientHandler> clientHandlers= new ArrayList<>();

		// Socket that is passed from the server class

		private Socket socket;
		// read data that sent from the client
		private BufferedReader in;

		//Purpose: send message to client
		private PrintWriter out;
		private String username;

		/**
		 * Will be initiated with the socket object from server class.
		 * @param socket : socket object from the server class
		 */
		public ClientHandler(Socket socket) {

			try {
				this.socket = socket;
				// printwriter is used instead of BufferedWriter since it has automatic flushing (true) and
				// formatting methods such as println. It's better for network communication
				this.out = new PrintWriter(socket.getOutputStream(), true);
				// Read data from socket
				this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// Blocks until the username is sent from the client
				this.username = in.readLine();
				clientHandlers.add(this);

				broadCastMsg("SERVER: " + username + " has entered the chat!");
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		/**
		 * Broadcasts the message to the other clients (except the client that sent the message)
		 * @param msg : message to be sent to other clients
		 */
		public void broadCastMsg(String msg) {
			for (ClientHandler clientHandler : clientHandlers) {

				if (!clientHandler.username.equals(username)) {
					clientHandler.out.println(msg);
				}  
			}
		}

		/**
		 * Listens for messages from the clienthandler's corresponding client and then broadcasts the message
		 * to the other clients.
		 * Runs on a separate thread because of the blocking in the while loop.
		 */
		@Override
		public void run() {
			try {
				String inputLine;
				// reads a line from the input stream, will continue as long as there are more lines to read
				// i.e. as long as the client is connected. (Blocks while waiting for the user to type messages)
				while ((inputLine = in.readLine()) != null) {
					broadCastMsg(inputLine);
				}
			} catch (IOException e) {
				System.out.println("An error occurred: " + e.getMessage());
			} finally {
				broadCastMsg("SERVER: " + username + " has left the chat!");
				try {
					in.close();
					out.close();
					socket.close();
					clientHandlers.remove(this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
