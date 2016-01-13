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
	
	private int turn;
	private boolean notOver = true;
	private int numberOfPlayers;
	
	public Server(ServerController controllerArg) {
		connections = new ArrayList<Connection>();
		controller = controllerArg;
	}
	
	public void run() { 
		createGameEnviroment();
		broadcastNames();
		giveOutStones();
		determineFirstMove();
		while (notOver) {
			giveNextMove();
			waitForNextMove();
			handleNextMove();
			broadcastMove();
		}
		broadcastWinner();
		cleanUpGame();
		stopServer();
	}
	
	Board board;
	Stack stack;
	
	private void createGameEnviroment() {
		numberOfPlayers = connections.size();
		for (int i = 0; i < connections.size(); i++) {
			connections.get(i).setPlayerNumber(i);
		}
		
	}
	
	private void cleanUpGame() {
		// TODO Auto-generated method stub
		
	}

	private void stopServer() {
		// TODO Auto-generated method stub
		
	}

	private void broadcastWinner() {
		// TODO Auto-generated method stub
		
	}

	private void broadcastMove() {
		// TODO Auto-generated method stub
		
	}

	private void handleNextMove() {
		// TODO Auto-generated method stub
		
	}

	private void waitForNextMove() {
		// TODO Auto-generated method stub
		
	}

	private void giveNextMove() {
		// TODO Auto-generated method stub
		
	}

	private void determineFirstMove() {
		// TODO Auto-generated method stub
		
	}

	private void giveOutStones() {
		// TODO Auto-generated method stub
		
	}



	private void broadcastNames() {
		// TODO Auto-generated method stub
		
	}

	public void addConnection(Connection conn) {
		connections.add(conn);
		if (connections.size() == Controller.MAX_PLAYERS) {
			controller.nextGame();
		}
	}
	
	public void processMessage(Connection conn, String msg) {
		int playerNumber = conn.getPlayterNumber();
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("HELLO")) {
			sendMessage(conn, "WELCOME");
		} else if (command.equals("MOVE")) {
			System.out.println("MOVE"); //TODO
		} else if (command.equals("SWAP")) {
			System.out.println("SWAP"); //TODO
		} 
	}
	
	public void sendMessage(Connection conn, String msg) {
		System.out.println("[SERVER]: Sending message: \"" + msg + "\"");
		conn.sendString(msg);
	}

}
