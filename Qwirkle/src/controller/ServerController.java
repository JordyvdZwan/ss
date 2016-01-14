package controller;

import java.util.*;
import model.*;
import view.*;

public class ServerController {
	Connector connector;
	List<Server> serverList;
	UI ui;
	int aiThinkTime;
	
	public ServerController(int portArg, UI uiArg, int aiThinkTimeArg) {
		ui = uiArg;
		aiThinkTime = aiThinkTimeArg;
		serverList = new ArrayList<Server>();
		Server server = new Server(this, aiThinkTime);
		serverList.add(server);
		
		connector = new Connector(server, portArg);
		
//		waitForInput();
	}
	
	public void nextGame() {
		(serverList.get(serverList.size() - 1)).start();
		Server server = new Server(this, aiThinkTime);
		connector.newServer(server);
		serverList.add(server);
	}
	
	public void waitForInput() {
		String command = ui.getCommand();
		handleInput(command);		
	}
	
	public void handleInput(String command) {
		if (command.equals("Start Game")) {
			
		} else if (command.equals("")) {
			
		}
	}
}
