package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import player.NetworkPlayer;
import view.UI;

public class Connector extends Thread {

	/**
	 * Game variable to which incoming requests are linked.
	 */
	private Game game;
	
	/**
	 * Port that will be listened to.
	 */
	private int port;
	
	/**
	 * UI that is only used to display the starting of the server socket.
	 */
	private UI ui;
	
	/**
	 * Creates a Connector.
	 * @param uiArg ui to which to output.
	 * @param serverArg server, needed to continue to next game.
	 * @param portArg port to which the server socket will be linked.
	 */
	public Connector(UI uiArg, Game serverArg, int portArg) {
		game = serverArg;
		port = portArg;
		ui = uiArg;
		this.start();
	}
	
	/**
	 * Creates a serversocket to earlier determined port and listens for incoming requests.
	 */
	public void run() {
    	ServerSocket serverSocket = null;
    	try {
    		ui.displayServerMessage("[SERVER]: starting server socket");
    		serverSocket = new ServerSocket(port);
    		while (true) {
    			Socket sock = serverSocket.accept();
    			Connection connection = new Connection(game, sock, new NetworkPlayer());
				game.addConnection(connection);
    		}
    	} catch (IOException e) {
    		lossOfConnection();
    	}
	}
	
	/**
	 * Used to divert the incoming requests to the new game.
	 * @param gameArg game to which the incoming request will be diverted.
	 */
	public void newServer(Game gameArg) {
		game = gameArg;
	}
	
	/**
	 * stops current try of connecting, and starts a new controller.
	 */
	public void lossOfConnection() {
		ui.errorOccured("port already in use.");
		Controller.chooseServerClient();
	}
}
