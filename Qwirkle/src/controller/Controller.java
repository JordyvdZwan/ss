package controller;

import java.io.IOException;
import java.net.*;

import view.*;

public class Controller extends Thread{	
	public static final int MAX_PLAYERS = 4;
	static UI ui;
	public static final int DEFAULT_PORT = 25565;
	public static final int DEFAULT_AITHINKTIME = 2000;
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
		} else if (choice.equals("LOCALCLIENT")) {
			startLocalClient(DEFAULT_PORT);
		} else if (choice.equals("DEFAULTSERVER")) {
			startLocalServer(DEFAULT_PORT);
		} 
	}
	
	@SuppressWarnings("unused")
	public static void startServer() {
		int aiThinkTime = ui.getAIThinkTime();
		int port = ui.getPort();
		ServerController serverController = new ServerController(port, ui, aiThinkTime);
	}
	
	@SuppressWarnings("unused")
	public static void startLocalServer(int port) {
		ServerController serverController = new ServerController(port, ui, DEFAULT_AITHINKTIME);
	}
	
	@SuppressWarnings("unused")
	public static void startLocalClient(int port) {
		try {
			Socket sock;
			InetAddress address;
			
			address = InetAddress.getByName("localhost");
			String userName = ui.getUserName();
			
			sock = new Socket(address, port);
			Client client = new Client(ui, sock, userName);
		} catch (IOException e) {
			ui.errorOccured("Could not start Client.");
		}
	}
	
	@SuppressWarnings("unused")
	public static void startClient() {
		try {
			Socket sock;
			InetAddress address;
			
			address = ui.getHost();
			int port = ui.getPort();
			String userName = ui.getUserName();
			
			sock = new Socket(address, port);
			Client client = new Client(ui, sock, userName);
		} catch (IOException e) {
			ui.errorOccured("Could not start Client.");
		}
	}
	
}

