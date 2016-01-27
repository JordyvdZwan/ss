package controller;

import java.util.*;
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
		Game game = new Game(this, aiThinkTime, ui);
		gameList.add(game);
		
		connector = new Connector(ui, game, portArg);
	}
	
	/**
	 * Creates a new game for the connector and starts the current one.
	 */
	public void nextGame() {
		(gameList.get(gameList.size() - 1)).start();
		Game game = new Game(this, aiThinkTime, ui);
		connector.newServer(game);
		gameList.add(game);
	}
	
	/**
	 * Is used to handle input received from the ui.
	 * @param command String received from UI.
	 */
	public void handleInput(String command) {
		if (command.equals("start")) {
			nextGame();
		} else if (command.equals("stop")) {
			System.exit(1);
		} else {
			ui.errorOccured("no valid input.");
		}
	}
}
