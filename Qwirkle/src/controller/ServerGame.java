package controller;

import model.*;
import view.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class ServerGame {
	private List<Connection> connection;
	private UI ui;
	
	public ServerGame() {
		connection = new ArrayList<Connection>();
		
		start();
	}
	
	public void start() {
		int port = ui.getPort();
		StartListening(port);
	}
	
	
	
	
	
	
	public void StartListening(int port) {
    	ServerSocket serverSocket = null;
    	try {
    		serverSocket = new ServerSocket(port);
    		while (true) {
    			Socket sock = serverSocket.accept();
    			Connection cHandler = new Connection(this, sock);
				cHandler.start();
				addHandler(cHandler);
				cHandler.announce();
    		}
    	} catch (IOException e) {
    		System.out.println(e.getMessage());
    	} finally {
    		try {
    			serverSocket.close();
    		} catch (IOException e) {
    			System.out.println(e.getMessage());
    		}
    	}
	}
	
	public void StopListening () {
		
	}
	

}
