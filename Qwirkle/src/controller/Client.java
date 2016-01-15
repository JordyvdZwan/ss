package controller;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.*;
import player.*;
import view.*;

public class Client {

	private UI ui;
	private Connection conn;
	
	private Player player;
	private List<Player> opponents = new ArrayList<Player>();
	
	private Board board;
	
	public Client(UI uiArg, Socket sockArg, String userNameArg) {
		ui = uiArg;
		conn = new Connection(this, sockArg);
		
		conn.sendString("HELLO " + userNameArg);
	}
	
	public void processMessage(Connection conn, String msg) {
		System.out.println("[CLIENT]: proccesing Message");
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("WELCOME")) {
			handleWelcome(msg);
		} else if (command.equals("NAMES")) {
			handleNames(msg);
		} else if (command.equals("NEXT")) {
			handleNext(msg);
		} else if (command.equals("NEW")) {
			handleNew(msg);
		} else if (command.equals("TURN")) {
			handleTurn(msg);
		} else if (command.equals("KICK")) {
			handleKick(msg);
		} else if (command.equals("WINNER")) {
			handleWinner(msg);
		} else {
			//TODO throw Exception
		}
		reader.close();
	}
	
	private void error(String msg) {
		
	}
	
	private void fatalError(String msg) {
		System.out.println("[FATAL ERROR]: " + msg);
		System.exit(0);
	}
	
	private void handleWelcome(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
		String playerName = reader.next();
		int playerNumber = Integer.getInteger(reader.next());
		player = new HumanPlayer(playerName, playerNumber);
		reader.close();
	}
	
	private void handleNames(String msg) { //TODO errors catchen
		Scanner reader = new Scanner(msg);
		reader.next();
			while (reader.hasNext()) {
				String playerName = reader.next();
				if (!reader.hasNext()) {fatalError("names command invalid!");}
				int playerNumber = Integer.getInteger(reader.next());
				opponents.add(new NetworkPlayer(playerName, playerNumber));
			}
		reader.close();
	}
	
	private void handleNext(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
			//TODO do something with it.
		reader.close();
	}
	
	private void handleNew(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
			//TODO do something with it.
		reader.close();
	}
	
	private void handleTurn(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
			//TODO do something with it.
		reader.close();
	}
	
	private void handleKick(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
			//TODO do something with it.
		reader.close();
	}
	
	private void handleWinner(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
			//TODO do something with it.
		reader.close();
	}
	
	private void shutdown() {
		
	}
}
