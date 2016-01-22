package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import player.NetworkPlayer;
import view.UI;

public class Connector extends Thread {

	private Game game;
	private int port;
	private UI ui;
	
	public Connector(UI uiArg, Game serverArg, int portArg) {
		game = serverArg;
		port = portArg;
		ui = uiArg;
		this.start();
	}
	
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
    		lossOfConnection(e);
    	}
	}
	
	public void newServer(Game gameArg) {
		game = gameArg;
	}
	
	public void lossOfConnection(Exception e) {
		ui.errorOccured("could not bind to port");
		Controller.chooseServerClient();
	}
}
