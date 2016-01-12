package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector extends Thread {

	Server server;
	int port;
	
	public Connector(Server serverArg, int portArg) {
		server = serverArg;
		port = portArg;
	}
	
	public void run() {
    	ServerSocket serverSocket = null;
    	try {
    		serverSocket = new ServerSocket(port);
    		while (true) {
    			Socket sock = serverSocket.accept();
    			Connection connection = new Connection(server, sock);
    			connection.start();
				server.addConnection(connection);
    		}
    	} catch (IOException e) {
    		//TODO catch IOException
    	}
	}
	
	public void nextGame() {
		server = new Server();
	}
}
