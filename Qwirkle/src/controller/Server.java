package controller;

import java.util.*;
import model.*;
import view.*;

public class Server {
	Connector connector;
	List<Game> gameList;
	UI ui;
	int aiThinkTime;
	
	public Server(int portArg, UI uiArg, int aiThinkTimeArg) {
		ui = uiArg;
		ui.setServerController(this);
		Thread uiThread = new Thread(ui);
		uiThread.start();
		aiThinkTime = aiThinkTimeArg;
		gameList = new ArrayList<Game>();
		Game game = new Game(this, aiThinkTime);
		gameList.add(game);
		
		connector = new Connector(game, portArg);
	}
	
	public void nextGame() {
		(gameList.get(gameList.size() - 1)).start();
		Game game = new Game(this, aiThinkTime);
		connector.newServer(game);
		gameList.add(game);
	}
	
	public void waitForInput() {
		String command = ui.getCommand();
		handleInput(command);		
	}
	
	public void handleInput(String command) {
		
		if (command.equals("start")) {
			nextGame();
		} else if (command.equals("")) {
			
		}
	}
}
