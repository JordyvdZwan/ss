package controller;

import model.*;
import view.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server extends Thread {
	
	private List<Connection> connections;
	private ServerController controller;
	Connector connector;
	
	public Server(ServerController controllerArg) {
		connections = new ArrayList<Connection>();
		controller = controllerArg;
		this.start(); //TODO
	}
	
	public void addConnection(Connection conn) {
		connections.add(conn);
		if (connections.size() == Controller.MAX_PLAYERS) {
			controller.nextGame();
		}
	}
	
	public void run() { //TODO starts the game
		try {
			this.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[SERVER]: Sending Message"); //TODO
		sendMessage(connections.get(0), "WELCOME");
	}
	
	public void processMessage(Connection conn, String msg) {
		int playerNumber = conn.getPlayterNumber();
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("HELLO")) {
			
		} else if (command.equals("MOVE")) {
			
		} else if (command.equals("SWAP")) {
			
		} 
	}
	
	public void sendMessage(Connection conn, String msg) {
		System.out.println("[SERVER]: Sending Message (2)"); //TODO
		conn.sendString(msg);
	}

}
