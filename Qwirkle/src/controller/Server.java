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
	}
	
	public void addConnection(Connection conn) {
		connections.add(conn);
		if (connections.size() == Controller.MAX_PLAYERS) {
			controller.nextGame();
			this.start();
		}
	}
	
	public void run() { //TODO starts the game
		
	}
	
	public void getMessage(Connection conn, String msg) {
		
	}

}
