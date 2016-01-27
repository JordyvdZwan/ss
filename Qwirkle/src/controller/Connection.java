package controller;

import java.io.*;
import java.net.*;

import player.NetworkPlayer;

public class Connection extends Thread {
	private Game game;
	private Client client;
	private Socket sock;
	public boolean active = true;
	
	private NetworkPlayer player;
	
	
	private BufferedReader in;
	private BufferedWriter out;
	
	public Connection(Game serverArg, Socket sockArg, NetworkPlayer playerArg) {
		player = playerArg;
		game = serverArg;
		sock = sockArg;
		openCommunication();
	}

	public Connection(Client clientArg, Socket sockArg) {
		client = clientArg;
		sock = sockArg;
		openCommunication();
	}

	public void openCommunication() {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			stopConnection();
		}
		this.start();
	}
	
	public void sendString(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			stopConnection();
		}
	}
	
	public void sendStringToParent(String msg) {
		if (game != null) {
			game.processMessage(this, msg);
		} else {
			client.processMessage(this, msg);
		}
	}
	
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
	
	public void lossOfConnection() {
		stopConnection();
		sendStringToParent("LOSSOFCONNECTION");
	}
	
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
	
	/*@pure*/
	public NetworkPlayer getPlayer() {
		return player;
	}
	
}
