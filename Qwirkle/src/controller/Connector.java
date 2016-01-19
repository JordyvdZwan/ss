package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import player.NetworkPlayer;

public class Connector extends Thread {

	private Game game;
	private int port;
	
	public Connector(Game serverArg, int portArg) {
		game = serverArg;
		port = portArg;
		this.start();
	}
	
	public void run() {
    	ServerSocket serverSocket = null;
    	try {
    		System.out.println("[SERVER]: starting server socket"); //TODO
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
		System.out.println(e.getMessage()); //TODO
	}
}
