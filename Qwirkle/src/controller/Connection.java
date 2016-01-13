package controller;

import java.io.*;
import java.net.*;

public class Connection extends Thread {
	private Server server;
	private Client client;
	private Socket sock;
	private boolean active = true;
	
	private int playerNumber;
	
	
	private BufferedReader in;
	private BufferedWriter out;
	
	public Connection(Server serverArg, Socket sockArg) {
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
			System.out.println("[SERVER]: Sending Message (3)"); //TODO
			out.write(msg + "\n");
			out.flush();
			System.out.println("[SERVER]: Sending Message (3)"); //TODO
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
				System.out.println("Listening"); //TODO
				String command;
				while ((command = in.readLine()) != null) {
					System.out.println("Heard"); //TODO
					sendStringToParent(command);
				}
			} catch (IOException e) {
				lossOfConnection();
			}
		}
	}
	
	public void lossOfConnection() {
		//TODO implement
	}
	
	public void setPlayerNumber(int playerNumberArg) {
		playerNumber = playerNumberArg;
	}
	
	public int getPlayterNumber() {
		return playerNumber;
	}
}
