package controller;

import java.net.*;

import model.*;
import view.*;

public class Controller extends Thread{	
	public static final int MAX_PLAYERS = 4;
	static UI ui;
	
	public static void main(String[] args) {
		ui = new TUI();
//		String choice = ui.getChoiceServerClient();
//		if (choice.equals("SERVER")) {
//			startServer();
//		} else if (choice.equals("CLIENT")) {
//			startClient();
//		}
		Controller control = new Controller();
		System.out.println("starting server");
		control.startServer(2727);
		System.out.println("starting client");
		control.startClient();
	}
	
	public static void startServer() {
		int port = ui.getPort();
		ServerController serverController = new ServerController(port, ui);
	}
	
	public static void startServer(int port) {
		ServerController serverController = new ServerController(port, ui);
	}
	
	public static void startClient() {
		try {
			Socket sock;
			InetAddress address;
			address = InetAddress.getByName("192.168.2.10");
			int port = 2727;
			sock = new Socket(address, port);
			System.out.println("creating client");
			Client client = new Client(ui, sock);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}

