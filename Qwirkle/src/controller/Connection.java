package controller;

import java.io.*;
import java.net.*;

import player.NetworkPlayer;

public class Connection extends Thread {
	private Server server;
	private Client client;
	private Socket sock;
	private boolean active = true;
	
	private NetworkPlayer player;
	
	
	private BufferedReader in;
	private BufferedWriter out;
	
	public Connection(Server serverArg, Socket sockArg, NetworkPlayer playerArg) {
		player = playerArg;
		server = serverArg;
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
			lossOfConnection();
		}
		this.start();
	}
	
	public void sendString(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			lossOfConnection();
		}
	}
	
	public void sendStringToParent(String msg) {
		if (server != null) {
			server.processMessage(this, msg);
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
				lossOfConnection();
			}
		}
	}
	
	public void lossOfConnection() {
		try {
			//TODO 
			System.out.println("ERROR loss of connection");
			active = false;
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		//TODO need to notify parent (need to kick from game)
	}
	
	public NetworkPlayer getPlayer() {
		return player;
	}
	
}
