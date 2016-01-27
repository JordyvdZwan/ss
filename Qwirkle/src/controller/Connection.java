package controller;

import java.io.*;
import java.net.*;

import player.NetworkPlayer;

public class Connection extends Thread {
	/**
	 * The parent of the Connection, it can be null as client may be the parent.
	 */
	private Game game;
	
	/**
	 * The parent of the Connection, it can be null as game may be the parent.
	 */
	private Client client;
	
	/**
	 * socket used to connect to.
	 */
	private Socket sock;
	
	/**
	 * if true the connection will listen.
	 */
	public boolean active = true;
	
	/**
	 * Player linked to this connection only used in Game.
	 */
	private NetworkPlayer player;
	
	/**
	 * Incoming messages from the other side are written to this.
	 */
	private BufferedReader in;
	
	/**
	 * Outgoing messages are written to this.
	 */
	private BufferedWriter out;
	
	/**
	 * Creates a Connection with a server as parent.
	 * @param serverArg parent of connection i.e. where to pass on the info.
	 * @param sockArg connection to be listening to.
	 * @param playerArg Player that is linked to this connection.
	 */
	public Connection(Game serverArg, Socket sockArg, NetworkPlayer playerArg) {
		player = playerArg;
		game = serverArg;
		sock = sockArg;
		openCommunication();
	}

	/**
	 * Creates a Connection with a client as parent.
	 * @param clientArg parent of the Connection i.e. where to pass on the info.
	 * @param sockArg connection to be listening to.
	 */
	public Connection(Client clientArg, Socket sockArg) {
		client = clientArg;
		sock = sockArg;
		openCommunication();
	}

	/**
	 * Opens the in and out of the class therefore allowing messages to be send and received.
	 */
	public void openCommunication() {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			stopConnection();
		}
		this.start();
	}
	
	/**
	 * Sends a string to the other side.
	 * @param msg String that needs to be send to the other side.
	 */
	public void sendString(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			stopConnection();
		}
	}
	
	/**
	 * Passes received strings to the classes parent.
	 * @param msg received string.
	 */
	public void sendStringToParent(String msg) {
		if (game != null) {
			game.processMessage(this, msg);
		} else {
			client.processMessage(this, msg);
		}
	}
	
	/**
	 * When connection is started, the connection starts listening to the output.
	 * And passes everything to parent.
	 */
	public void run() {
		while (active) {
			try {
				String command;
				while ((command = in.readLine()) != null) {
					sendStringToParent(command);
				}
			} catch (IOException e) {
				if (active) {
					lossOfConnection();
				}
			}
		}
	}
	
	/**
	 * Stops connection and notifies parent.
	 */
	public void lossOfConnection() {
		stopConnection();
		sendStringToParent("LOSSOFCONNECTION");
	}
	
	/**
	 * Stops connection.
	 */
	public void stopConnection() {
		try {
			active = false;
			this.interrupt();
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @return returns player linked to this connection.
	 */
	/*@pure*/
	public NetworkPlayer getPlayer() {
		return player;
	}
	
}
