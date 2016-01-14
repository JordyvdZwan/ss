package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.NetworkPlayer;

public class Connector extends Thread {

	Server server;
	int port;
	
	public Connector(Server serverArg, int portArg) {
		server = serverArg;
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
    			Connection connection = new Connection(server, sock, new NetworkPlayer());
				server.addConnection(connection);
    		}
    	} catch (IOException e) {
    		lossOfConnection(e);
    	}
	}
	
	public void newServer(Server serverArg) {
		server = serverArg;
	}
	
	public void lossOfConnection(Exception e) {
		System.out.println(e.getMessage()); //TODO
	}
}
