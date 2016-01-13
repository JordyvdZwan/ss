package controller;

import java.net.*;
import java.util.Scanner;

import model.*;
import view.*;

public class Client {

	private UI ui;
	private Connection conn;
	
	public Client(UI uiArg, Socket sockArg) {
		System.out.println("[CLIENT]: creating connection");
		ui = uiArg;
		conn = new Connection(this, sockArg);
		System.out.println("[CLIENT]: connection created");
		
		conn.sendString("HELLO");
	}
	
	public void processMessage(Connection conn, String msg) {
		System.out.println("[CLIENT]: proccesing Message");
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("WELCOME")) {
			System.out.println("WELCOME");
		} else if (command.equals("NAMES")) {
			System.out.println("NAMES");
		} else if (command.equals("NEXT")) {
			System.out.println("NEXT");
		} else if (command.equals("NEW")) {
			System.out.println("NEW");
		} else if (command.equals("TURN")) {
			System.out.println("TURN");
		} else if (command.equals("KICK")) {
			System.out.println("KICK");
		} else if (command.equals("WINNER")) {
			System.out.println("WINNER");
		} else {
			System.out.println("wrong argument");
		}
	}
}
