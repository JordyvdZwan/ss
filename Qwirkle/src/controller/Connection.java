package controller;

import java.io.*;
import java.net.*;

public class Connection extends Thread {
	private Server server;
	private Client client;
	private Socket sock;
	private boolean active = true;
	
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
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			lossOfConnection();
		}
	}
	
	public void sendStringToParent(String msg) {
		if (server != null) {
			server.getMessage(this, msg);
		} else {
			client.getMessage(this, msg);
		}
	}
	
	public void run() {
		while (active) {
			try {
				String command = in.readLine();
				if (command != null) {
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
}
