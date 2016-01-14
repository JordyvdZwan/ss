package controller;

import java.net.*;
import java.util.Scanner;

import model.*;
import view.*;

public class Client {

	private UI ui;
	private Connection conn;
	
	public Client(UI uiArg, Socket sockArg, String userNameArg) {
		ui = uiArg;
		conn = new Connection(this, sockArg);
		
		conn.sendString("HELLO");
	}
	
	public void processMessage(Connection conn, String msg) {
		System.out.println("[CLIENT]: proccesing Message");
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("WELCOME")) {
			System.out.println("WELCOME"); //TODO
		} else if (command.equals("NAMES")) {
			System.out.println("NAMES"); //TODO
		} else if (command.equals("NEXT")) {
			System.out.println("NEXT"); //TODO
		} else if (command.equals("NEW")) {
			System.out.println("NEW"); //TODO
		} else if (command.equals("TURN")) {
			System.out.println("TURN"); //TODO
		} else if (command.equals("KICK")) {
			System.out.println("KICK"); //TODO
		} else if (command.equals("WINNER")) {
			System.out.println("WINNER"); //TODO
		} else {
			System.out.println("wrong argument");
		}
	}
}
