package controller;

import java.io.IOException;
import java.net.*;

import player.ComputerPlayer;
import player.HumanPlayer;
import strategy.MirandaStrategy;
import strategy.MultiThreadStrategy;
import strategy.RetardedStrategy;
import view.*;

public class Controller extends Thread {	
	public static final int MAX_PLAYERS = 4;
	static UI ui = new CTUI(false);
	public static final int DEFAULT_PORT = 25565;
	public static final int DEFAULTAITHINKTIME = 2000;
	
	
	public static void main(String[] args) {
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
		} else if (choice.equals("LOCALAI")) {
			startLocalAI(DEFAULT_PORT);
		} else if (choice.equals("AI")) {
			startAI();
		} else {
			ui.errorOccured("wrong input");
			chooseServerClient();
		}
	}
	
	@SuppressWarnings("unused")
	public static void startServer() {
		
		int aiThinkTime = getAiThinkTime();
		int port = getPort();
		Server server = new Server(port, ui, aiThinkTime);
	}
	
	@SuppressWarnings("unused")
	public static void startLocalServer(int port) {
		Server server = new Server(port, ui, DEFAULTAITHINKTIME);
	}
	
	@SuppressWarnings("unused")
	public static void startLocalAI(int port) {
		try {
			Socket sock;
			InetAddress address;
			
			address = InetAddress.getByName("localhost");
			
			sock = new Socket(address, port);
			Client client = new Client(ui, sock, new ComputerPlayer("AI", new MultiThreadStrategy()));
		} catch (IOException e) {
			ui.errorOccured("Could not start Client.");
			chooseServerClient();
		}
	}
	
	@SuppressWarnings("unused")
	public static void startAI() {
		try {
			Socket sock;
			InetAddress address;
			
			address = getHost();
			int port = getPort();
			String userName = getUserName();
			
			sock = new Socket(address, port);
			Client client = new Client(ui, sock, new ComputerPlayer(new MultiThreadStrategy()));
		} catch (IOException e) {
			ui.errorOccured("Could not start Client.");
			chooseServerClient();
		}
	}
	
	@SuppressWarnings("unused")
	public static void startLocalClient(int port) {
		try {
			Socket sock;
			InetAddress address;
			
			address = InetAddress.getByName("localhost");
			String userName = getUserName();
			
			sock = new Socket(address, port);
			Client client = new Client(ui, sock, new HumanPlayer(userName));
		} catch (IOException e) {
			ui.errorOccured("Could not start Client. Please try again");
			chooseServerClient();
		}
	}
	
	@SuppressWarnings("unused")
	public static void startClient() {
		try {
			Socket sock;
			InetAddress address;
			
			address = getHost();
			int port = getPort();
			String userName = getUserName();
			
			sock = new Socket(address, port);
			Client client = new Client(ui, sock, new HumanPlayer(userName));
		} catch (IOException e) {
			ui.errorOccured("Could not start Client.");
			chooseServerClient();
		}
	}
	
	private static InetAddress getHost() {
		InetAddress host = null;
		try {
			host = InetAddress.getByName(ui.getHost());
		} catch (UnknownHostException e) {
			ui.errorOccured("Invalid host name, please try again.");
			host = getHost();
		}
		return host;
	}
	
	private static int getPort() {
		int port = 0;
		try {
			port = Integer.parseInt(ui.getPort());
		} catch (NumberFormatException e) {
			ui.errorOccured("Invalid host name, please try again.");
			port = getPort();
		}
		return port;
	}
	
	private static String getUserName() {
		String name = ui.getUserName();
		if (!isValidName(name)) {
			ui.errorOccured("Invalid username, please try again.");
			name = getUserName();
		}
		return name;
	}
	
	private static int getAiThinkTime() {
		int time = 0;
		try {
			time = Integer.parseInt(ui.getAIThinkTime());
		} catch (NumberFormatException e) {
			ui.errorOccured("Invalid aiThinkTime, please try again.");
			time = getAiThinkTime();
		}
		return time;
	}
	
	private static boolean isValidName(String name) {
		boolean result = true;
		if (name.length() > 16) {
			result = false;
		}
		char[] chars = name.toCharArray();
		for (char c : chars) {
			if (!Character.isLetterOrDigit(c)) {
				result = false;
			}
		}
		return result;
	}
}

