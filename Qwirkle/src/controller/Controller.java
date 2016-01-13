package controller;

import java.io.IOException;
import java.net.*;

import view.*;

public class Controller extends Thread{	
	public static final int MAX_PLAYERS = 4;
	static UI ui;
	
	public static void main(String[] args) {
		ui = new TUI();
		chooseServerClient();
	}
	
	public static void chooseServerClient() {
		String choice = ui.getChoiceServerClient();
		if (choice.equals("SERVER")) {
			startServer();
		} else if (choice.equals("CLIENT")) {
			startClient();
		}
	}
	
	@SuppressWarnings("unused")
	public static void startServer() {
		int port = ui.getPort();
		ServerController serverController = new ServerController(port, ui);
	}
	
	@SuppressWarnings("unused")
	public static void startClient() {
		try {
			Socket sock;
			InetAddress address;
			
			address = ui.getHost();
			int port = ui.getPort();
			
			sock = new Socket(address, port);
			Client client = new Client(ui, sock);
		} catch (IOException e) {
			ui.errorOccured("Could not start Client.");
		}
	}
	
}

