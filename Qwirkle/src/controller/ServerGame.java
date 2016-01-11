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
	
	public static void main(String[] args) {
		ServerGame server = new ServerGame();
		server.start();
	}
	
	public void start() {
//		int port = ui.getPort();
		StartListening(25565);
		
		Scanner in = new Scanner(System.in);
		in.next();
		
		StopListening();
	}
		
	public void StartListening(int port) {
    	Connector connector = new Connector(this, port);
    	connector.start();
	}
	
	public void StopListening() {
		listening = false;
		System.out.println("Server has stopped to listen.");
	}
	
	boolean listening = true;
	
	public void addConnection(Connection conn) {
		if (listening) {
			connection.add(conn);
		} else {
			conn.suspend();
		}
	}
	
	
}
