package controller;

import java.util.*;

public class ServerController {
	Connector connector;
	List<Server> serverList;
	
	public ServerController(int port) {
		serverList = new ArrayList<Server>();
		Server server = new Server(this);
		serverList.add(server);
		
		connector = new Connector(server, port);
	}
	
	public void nextGame() {
		connector.newServer(new Server(this));
	}
}
