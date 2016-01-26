package controller;

import model.*;
import model.Stack;
import player.NetworkPlayer;
import player.Player;
import view.UI;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Game extends Thread {
	
	public List<Connection> connections;
	private List<NetworkPlayer> players;
	private Server server;
	
	private int turn;
	private /*@ spec_public @*/ int numberOfPlayers;
	private int aiThinkTime;
	private Board board;
	private /*@ spec_public @*/ Stack stack;
	private CountDownLatch nextMoveAvailable = new CountDownLatch(1);
	
	private boolean moveAvailable = false;
	private Queue<List<Move>> nextMove = new ArrayBlockingQueue<List<Move>>(1);
	private UI ui;
	
	
	//@private invariant board != null;
	
	public Game(Server controllerArg, int aiThinkTimeArg, UI ui) {
		this.ui = ui;
		aiThinkTime = aiThinkTimeArg;
		connections = new ArrayList<Connection>();
		players = new ArrayList<NetworkPlayer>();
		server = controllerArg;
	}
	
	public Game(int aiThinkTimeArg, UI ui) {
		this.ui = ui;
		aiThinkTime = aiThinkTimeArg;
		connections = new ArrayList<Connection>();
		players = new ArrayList<NetworkPlayer>();
	}
	

	/**
	 * Als de thread gestart word, start het spel.
	 * eerst wordt het spel opgestart en vervolgens gespeeld todat het spel over is.
	 * als het spel over is wordy de winnaar gebroadcast.
	 */
	public void run() { 
		createGameEnviroment();
		broadcastNames();
		giveOutStones();
		determineFirstMove();
		while (notGameOver()) {
			nextTurn();
			handleNextMove(waitForNextMove());
		}
		playerWins(detectWinner());
	}
	
	/**
	 * met de stack, het bord en de handen van de spelers, 
	 * kijkt de methode of er nog mogelijke moves zijn.
	 * @return true als de game niet over is, false als de game over is
	 */
	/*@ requires players.size() > 0;
	  @ ensures (\forall int i; 0 <= i & i < players.size(); 
					!board.gameOver(players.get(i).getHand(), stack.size()) ==> \result == true);
	 */
	/*@pure*/
	private boolean notGameOver() {
		int stackSize = stack.size();
		boolean result = false;
		for (Player player : players) {
			if (!board.gameOver(player.getHand(), stackSize)) {
				result = true;
				break;
			}
		}
		return result;
	}
 
	/**
	 * stuurt een boodschap naar een speler.
	 * @param conn naar welke speler de boodschap wordt gestuurd
	 * @param msg de boodschap die wordt verstuurd 
	 */
	/*@ requires conn != null;
	 */
	public void sendMessage(Connection conn, String msg) {
		ui.displayServerMessage("[SERVER]: Sending message to " + 
						conn.getPlayer().getName() + " : \"" + msg + "\"");
		conn.sendString(msg);
	}

	/**
	 * stuurt een boodschap naar alle spelers.
	 * @param msg de boodschap die wordt verstuurd
	 */
	/*@ requires connections.size() > 0;
	 */
	public void broadcastMessage(String msg) {
		for (int i = 0; i < connections.size(); i++) {
			sendMessage(connections.get(i), msg);
		}
	}

	/**
	 * stuurt de laatste zet door als de speler iets neerlegt.
	 * @param playMoves de zet die is gezet
	 * @param nr het nummer van de speler
	 */
	/*@ requires board.isLegalMoveList(playMoves);
	  @ requires nr <= numberOfPlayers;
	 */
	private void broadcastPlayMove(List<PlayMove> playMoves, int nr) {
		String moves = "";
		for (PlayMove move : playMoves) {
			moves = moves.concat(" " + move.getBlock().toString() + " " + move.y + " " + move.x);
		}
		broadcastMessage("TURN " + nr + moves);
	}

	/**
	 * stuurt de laatste zet door als de speler ruilt met de pot.
	 * @param swapMoves de stenen die de speler gaat ruilen
	 * @param nr het nummer van de speler
	 */
	/*@ requires (\forall int i; 0 <= i & i < swapMoves.size(); 
	  							Board.getBlock(swapMoves.get(i)) instanceof Block);
	  @ ensures nr <= numberOfPlayers;
	 */
	private void broadcastSwapMove(List<SwapMove> swapMoves, int nr) {
		broadcastMessage("TURN " + nr + " empty");
	}

	/**
	 * leest een bericht en kijkt wat de opdracht was van de speler.
	 * @param conn de connectie van de speler
	 * @param msg het bericht dat werd meegegeven
	 */
	public void processMessage(Connection conn, String msg) {
		ui.displayServerMessage("[SERVER]: Getting message from " + 
						conn.getPlayer().getName() + " : \"" + msg + "\"");
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("HELLO") && reader.hasNext()) {
			handleHello(conn, reader);
		} else if (command.equals("MOVE")) {
			readMove(conn, reader);
		} else if (command.equals("SWAP")) {
			readSwap(conn, reader);
		} else if (command.equals("LOSSOFCONNECTION")) {
			kickPlayer(conn, conn.getPlayer().getNumber(), 
							conn.getPlayer().getHand(), "Player lost connection");
		} else {
			kickPlayer(conn, conn.getPlayer().getNumber(), 
							conn.getPlayer().getHand(), "Command not recognized (processMessage");
		}
		reader.close();
	}

	/**
	 * begroet een speler, slaat zijn naam op en geeft hem een nummer.
	 * @param conn de connectie van de speler
	 * @param reader
	 */
	/*@ requires reader != null;
	  @ requires conn != null;
	 */
	private void handleHello(Connection conn, Scanner reader) {
		String playerName = reader.next();
		if (isValidName(playerName)) {
			conn.getPlayer().setName(playerName);
			if (connections.size() == Controller.MAX_PLAYERS) {
				server.nextGame();
			}
			sendMessage(conn, "WELCOME " + playerName + " " + conn.getPlayer().getNumber());
		} else {
			kickPlayer(conn, conn.getPlayer().getNumber(), 
							conn.getPlayer().getHand(), 
							"Invalid userName! (processMessage/Welcome)");
		}
	}

	/**
	 * leest de zet van een speler als hij iets ruilt met de pot.
	 * @param conn de connectie met de speler
	 * @param reader
	 */
	/*@ requires reader != null;
	  @ requires conn != null;
	  @ ensures moveAvailable || !reader.hasNext() || conn.getPlayer().getNumber() != turn ==> 
													numberOfPlayers == (\old(numberOfPlayers) - 1);
	 */
	private void readSwap(Connection conn, Scanner reader) {
		if (!moveAvailable && reader.hasNext() && conn.getPlayer().getNumber() == turn) {
			
			
			List<Move> moves = new ArrayList<Move>();
			while (reader.hasNext()) {
				String blockString = reader.next();
				if (Block.isValidBlockString(blockString)) {
					char[] chars = blockString.toCharArray();
					SwapMove move = new SwapMove(new Block(chars[0], 
									chars[1]), conn.getPlayer());
					moves.add(move);
				}
			}
			if (!moves.isEmpty()) {
				nextMove.add(moves);
				moveAvailable = true;
				nextMoveAvailable.countDown();
			} else {
				kickPlayer(conn, conn.getPlayer().getNumber(), 
								conn.getPlayer().getHand(), "Invalid swap command! "
										+ "(processMessage/Swap)");
			}
		} else {
			kickPlayer(conn, conn.getPlayer().getNumber(), 
							conn.getPlayer().getHand(), "Invalid swap command, "
							+ "not your turn or one has already been send ("
							+ "processMessage/Swap)");
		}
	}


	/**
	 * leest de zet die de speler stuurt als de speler iets op het bord speelt.
	 * @param conn de connectie van de speler
	 * @param reader
	 */
	/*@ requires reader != null;
	  @ requires conn != null;
	  @ ensures moveAvailable || !reader.hasNext() || conn.getPlayer().getNumber() != turn ==> 
													numberOfPlayers == (\old(numberOfPlayers) - 1); 
	  
	 */
	private void readMove(Connection conn, Scanner reader) {
		if (!moveAvailable && reader.hasNext() && conn.getPlayer().getNumber() == turn) {
			try {
				boolean cycleDone = false;				
				List<Move> moves = new ArrayList<Move>();
				while (reader.hasNext()) {
					
					
					cycleDone = false;
					String blockString = reader.next();
					char[] chars = blockString.toCharArray();
					if (!(chars.length == 2)) {
						break;
					}
					Block block = new Block(chars[0], chars[1]);
					
					
					if (!reader.hasNext()) {
						break;
					}
					String yString = reader.next();
					if (!yString.matches("^-?\\d+$")) {
						break;
					}
					int y = Integer.parseInt(yString);
					
					
					if (!reader.hasNext()) {
						break;
					}
					String xString = reader.next();
					if (!xString.matches("^-?\\d+$")) {
						break;
					}
					int x = Integer.parseInt(xString); 
					
					
					PlayMove move = new PlayMove(block, x, y, conn.getPlayer());
					moves.add(move);
					cycleDone = true;
					
				}
				if (cycleDone && !moves.isEmpty()) {
					nextMove.add(moves);
					moveAvailable = true;
					nextMoveAvailable.countDown();
				} else {
					kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), 
									"Invalid move command! (processMessage/Move)");
				}
			} catch (NumberFormatException e) {
				kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), 
								"Invalid move command! ((numbers) processMessage/Move)");
			}
		} else {
			kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), 
							"Invalid move command, "
									+ "not your turn or one has already "
									+ "been send! (processMessage/Move)");
		}
	}

	//TODO
	/**
	 * 
	 * @return
	 */
	/*
	 */
	private List<Move> waitForNextMove() {
		List<Move> result;
		while (!moveAvailable) {
			try {
				nextMoveAvailable.await();
			} catch (InterruptedException e) {
				//TODO dont know what to do...
			}
		}
		result = nextMove.remove();
		moveAvailable = false;
		nextMoveAvailable = new CountDownLatch(1);
		return result;
	}

	private boolean blocksInHand(List<Move> moves, List<Block> hand) {
		int counter = 0;
		outer : for (Move move : moves) {
			for (Block block : hand) {
				if (move.getBlock().color == block.color && move.getBlock().shape == block.shape) {
					counter++;
					continue outer;
				}
			}
		}
		return counter == moves.size();
	}
	
	/**
	 * speelt een beurt.
	 * @param moves de zet van de beurt
	 */
	/*@ requires (\forall int i; 0 <= i & i < moves.size(); 
	  											Board.getBlock(moves.get(i)) instanceof Block);
	  @ ensures stack.size() >= moves.size() ==> 
	  										stack.size() == (\old(stack.size()) - moves.size());
	 */
	private void handleNextMove(List<Move> moves) {
		if (isInstanceOfPlaymoves(moves)) {
			List<PlayMove> playMoves = toPlayMove(moves);
			if (board.isLegalMoveList(playMoves) && 
									blocksInHand(moves, playMoves.get(0).getPlayer().getHand())) {
				Player player = playMoves.get(0).getPlayer();
				player.setScore(player.getScore() + board.legitMoveScore(playMoves));
				board.makeMove(playMoves);
				swapStones(player.getConnection(), moves, playMoves.size());
				broadcastPlayMove(playMoves, player.getNumber());
			} else {
				Connection conn = moves.get(0).getPlayer().getConnection();
				kickPlayer(conn, conn.getPlayer().getNumber(), 
								conn.getPlayer().getHand(), "Invalid move command! (HandleNext)");
			}
		} else {
			List<SwapMove> swapMoves = toSwapMove(moves);
			if (stack.isValidSwap(swapMoves) && 
									blocksInHand(moves, swapMoves.get(0).getPlayer().getHand())) {
				swapStones(swapMoves.get(0).getPlayer().getConnection(), moves, swapMoves.size());
				broadcastSwapMove(swapMoves, swapMoves.get(0).getPlayer().getNumber());
			} else {
				Connection conn = moves.get(0).getPlayer().getConnection();
				kickPlayer(conn, conn.getPlayer().getNumber(), 
								conn.getPlayer().getHand(), "Invalid swap command! (HandleNext)");
			}
		}
	}

	/**
	 * kickt een speler uit het spel.
	 * @param conn de connectie van de speler
	 * @param playerNumber het nummer van de speler
	 * @param blocks de stenen die hij nog in zijn hand had
	 * @param reason de reden waarvoor hij gekickt wordt
	 */
	/*@ ensures numberOfPlayers == (\old(numberOfPlayers) - 1);
	  @ ensures blocks != null && stack != null ==> 
	  									stack.size() == (\old(stack.size()) + blocks.size());
	 */
	public void kickPlayer(Connection conn, int playerNumber, List<Block> blocks, String reason) {
		players.remove(conn.getPlayer());
		connections.remove(conn);
		if (blocks != null && stack != null) {
			stack.giveBack(blocks);
		}
		numberOfPlayers--;
		broadcastMessage("KICK " + playerNumber + " " + blocks.size() + " " + reason);
		if (connections.size() == 1) {
			playerWins(connections.get(0).getPlayer().getNumber());
		}
		if (turn == playerNumber) {
			nextTurn();
		}
	}

	/**
	 *  voegt een nieuwe connectie toe.
	 * @param conn de connectie die wordt toegevoegd
	 */
	/*@ ensures numberOfPlayers == (\old(numberOfPlayers) + 1);
	  @ ensures connections.size() == (\old(connections.size()) + 1);
	 */
	public void addConnection(Connection conn) {
		numberOfPlayers++;
		connections.add(conn);
		conn.getPlayer().setConnection(conn);
		for (int i = 0; i < Controller.MAX_PLAYERS; i++) {
			if (getPlayer(i) == conn.getPlayer() || getPlayer(i) == null) {
				connections.get(connections.size() - 1).getPlayer().setNumber(i);
				break;
			}
		}
		players.add(conn.getPlayer());
	}

	/**
	 * maakt een nieuw speelbord en een nieuwe pot aan.
	 */
	/*@ ensures (\forall int i; 0 <= i & i < players.size(); players.get(i).getNumber() == i);
	  @ ensures stack.size() == 108;
	 */
	private void createGameEnviroment() {
		board = new Board();
		stack = new Stack();
		
		for (Player player : players) {
			player.setNumber(players.indexOf(player));
		}
	}
	
	/**
	 * verteld aan iedereen wie er mee doet en welk nummer ze hebben.
	 */
	/*@ requires (\forall int i; 0 <= i & i < players.size(); 
	 												isValidName(players.get(i).getName()));
	 */
	private void broadcastNames() {
		String names = "";
		for (Connection conn: connections) {
			names = names.concat(" " + conn.getPlayer().getName() 
							+ " " + conn.getPlayer().getNumber());
		}
		broadcastMessage("NAMES" + names + " " + aiThinkTime);
	}	
	
	/**
	 * geeft elke speler 6 stenen.
	 */
	/*@ requires players.size() < 0;
	  @ requires stack.size() == 108;
	  @ ensures (\forall int i; 0 <= i & i < players.size(); players.get(i).getHand().size() == 6);
	 */
	private void giveOutStones() {
		for (Player player : players) {
			player.setHand(stack.give(6));
			String stones = "";
			for (Block block : player.getHand()) {
				stones = stones.concat(" " + block.toString());
			}
			sendMessage(player.getConnection(), "NEW" + stones);
		}
	}

	/**
	 * berekent wie de eerste zet mag zetten.
	 */
	private void determineFirstMove() {
		int[] scores = new int[players.size()];
		for (Player player : players) {
			scores[player.getNumber()] = maxScore(player.getHand());
		}
	}

	
	/**
	 * kijkt wat je maximale score is bij het begin van het spel, 
	 * dit gebeurt door te tellen hoeveel je van elke soort hebt.
	 * @param blocks de lijst met stenen die de speler heeft
	 * @return het maximale aantal punten
	 */
	/*@ requires blocks.size() == 6;
	  @ requires (\forall int i; 0 <= i & i < blocks.size(); blocks.get(i) instanceof Block);
	  @ ensures 0 <= \result & \result <= 12;
	 */
	private int maxScore(List<Block> blocks) {
		int score = 0;
		int[] points = new int[12];
		for (Block block : blocks) {
			if (block.color == Color.RED) {
				points[0]++;
			} else if (block.color == Color.ORANGE) {
				points[1]++;
			} else if (block.color == Color.BLUE) {
				points[2]++;
			} else if (block.color == Color.YELLOW) {
				points[3]++;
			} else if (block.color == Color.GREEN) {
				points[4]++;
			} else if (block.color == Color.PURPLE) {
				points[5]++;
			}
			
			if (block.shape == Shape.CIRCLE) {
				points[6]++;
			} else if (block.shape == Shape.DIAMOND) {
				points[7]++;
			} else if (block.shape == Shape.SQUARE) {
				points[8]++;
			} else if (block.shape == Shape.CLOVER) {
				points[9]++;
			} else if (block.shape == Shape.CROSS) {
				points[10]++;
			} else if (block.shape == Shape.STAR) {
				points[11]++;
			}
		}
		for (int i = 0; i < 12; i++) {
			if (points[i] > score) {
				score = points[i];
			}
		}
		return score;
	}

	/**
	 * zegt wie er aan de beurt is. als er maar 1 speler meedoet laat hij die speler winnen.
	 */
	/*@ ensures players.size() == 1 ==> detectWinner() == players.get(0).getNumber();
	  @ ensures players.size() > 1 ==> turn == (\old(turn + 1 % players.size()));
	 */
	private void nextTurn() {
		if (players.size() == 1) {
			playerWins(players.get(0).getNumber());
		} else if (players.size() == 0) {
			endGame();
		} else {
			turn = (turn + 1) % players.size();
			if (getPlayer(turn).getHand().isEmpty() && stack.size() == 0) {
				playerWins(detectWinner());
			}
			if (getPlayer(turn) == null) {
				nextTurn();
			}
			if (!(board.noValidMoves(getPlayer(turn).getHand()) && stack.size() == 0)) {
				broadcastMessage("NEXT " + getPlayer(turn).getNumber());
			} else {
				nextTurn();
			}
		}
	}

	/**
	 * ruilt stenen met de pot.
	 * @param conn de connectie met een speler
	 * @param moves de stenen die geruild gaan worden
	 * @param amount het aantal stenen die worden geruild
	 */
	/*@ requires (\forall int i; 0 <= i & i < moves.size(); 
	 										Board.getBlock(moves.get(i)) instanceof Block);
	 */
	private void swapStones(Connection conn, List<Move> moves, int amount) {
		List<Block> blocks = stack.give(amount);
		if (blocks.isEmpty()) {
			if (isInstanceOfPlaymoves(moves)) {
				for (Move move : moves) {
					conn.getPlayer().removeFromHand(move);
				}
			}
			sendMessage(conn, "NEW empty");
		} else {
			if (isInstanceOfPlaymoves(moves)) {
				conn.getPlayer().swapHand(moves, blocks);
				String blockString = "";
				for (Block block : blocks) {
					blockString = blockString.concat(" " + block.toString());
				}
				sendMessage(conn, "NEW" + blockString);
			} else {
				conn.getPlayer().swapHand(moves, blocks);
				String blockString = "";
				for (Move move : moves) {
					stack.giveBack(move.getBlock());
				}
				for (Block block : blocks) {
					blockString = blockString.concat(" " + block.toString());
				}
				sendMessage(conn, "NEW" + blockString);
			}
		}
	}
	
	/**
	 * laat zien welke speler bij dit nummer hoort.
	 * @param playerNumber het nummer van de speler
	 * @return de speler
	 */
	/*@ requires 0 <= playerNumber & playerNumber < numberOfPlayers;
	  @ ensures (\forall int i; 0 <= i & i < players.size(); 
	  					players.get(i).getNumber() == playerNumber ==> \result == players.get(i));
	  @ ensures \result.getNumber() == playerNumber;
	 */
	/*@pure*/
	private NetworkPlayer getPlayer(int playerNumber) {
		NetworkPlayer player = null;
		for (NetworkPlayer netPlayer : players) {
			if (netPlayer.getNumber() == playerNumber) {
				player = netPlayer;
			}
		}
		return player;
	}
	
	/**
	 * checkt of een naam aan de regels voldoet
	 * @param name de naam 
	 * @return true als hij aan alle regels voldoet
	 */
	/*@ requires name != null;
	  @ ensures name.length() <= 16 ==> (\forall int i; 0 <= i & i < name.length();
	  							Character.isLetterOrDigit(name.charAt(i)) ==> \result == true);
	 */
	/*@pure*/
	private boolean isValidName(String name) {
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

	/**
	 * laat zien wie er gewonnen heeft.
	 * @param win het nummer van de winnende speler.
	 */
	/*@ requires 0 <= win & win < players.size();
	 */
	private void playerWins(int win) {
		if (win >= 0) {
			broadcastWinner(win);
			endGame();
		} else {
			endGame();
		}
	}

	/**
	 * laat zien wie er gewonnen heeft
	 * @param winner de winner
	 */
	/*@ requires 0 <= winner & winner < players.size();
	 */
	private void broadcastWinner(int winner) {
		int win = 0;
		if (winner == 0) {
			win = players.get(0).getNumber();
		} else {
			win = winner;
		}
		broadcastMessage("WINNER " + win);
	}

	/**
	 * sluit alle connecties
	 */
	/*@ ensures connections.size() == 0;
	 */
	private void endGame() {
		for (int i = 0; i < connections.size(); i++) {
			connections.get(i).stopConnection();
		}
		this.interrupt();
	}

	/**
	 * kijkt welke speler heeft gewonnen
	 * @return de speler met de meeste punten.
	 */
	/*@ ensures \result == playerOfScore((\max int i; 0 <= i & i < players.size(); 
	  													players.get(i).getScore())); 
	 */
	/*@pure*/
	private int detectWinner() {
		int result = 0;
		if (getPlayer(turn).getHand().isEmpty()) {
			getPlayer(turn).setScore(getPlayer(turn).getScore() + 6);
		}
		if (players.size() > 0) {
			result = players.get(0).getNumber();
			for (Player player : players) {
				if (player.getScore() > getPlayer(result).getScore()) {
					result = player.getNumber();
				}
			}
		}
		return result;
	}
	
	/**
	 * kijkt welke speler bij deze score hoort
	 * @param score de score
	 * @return de speler
	 */
	/*@ ensures getPlayer(\result).getScore() == score;
	 */
	/*@pure*/
	private int playerOfScore(int score) {
		int result = 0;
		for (Player player : players) {
			if (player.getScore() == score) {
				result = player.getNumber();
			}
		}
		return result;
	}

	/**
	 * laat zien of een lijst met Moves enkel bestaat uit PlayMoves.
	 * @param moves de lijst met moves
	 * @return true als de lijst enkel uit PlayMoves bestaat
	 */
	/*@ requires (\forall int i; 0 <= i & i < moves.size(); 
	 									Board.getBlock(moves.get(i)) instanceof Block);
	  @ ensures (\forall int i; 0 <= i & i < moves.size();
	   									moves.get(i) instanceof PlayMove ==> \result == true);
	 */
	/*@pure*/
	public boolean isInstanceOfPlaymoves(List<Move> moves) {
		boolean result = true;
		for (Move move : moves) {
			if (!(move instanceof PlayMove)) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * zet een lijst met Moves om in een lijst met PlayMoves
	 * @param moves de lijst met moves
	 * @return de lijst met PMoves
	 */
	/*@ requires (\forall int i; 0 <= i & i < moves.size(); 
	 										Board.getBlock(moves.get(i)) instanceof Block);
	  @ ensures (\forall int i; 0 <= i & i < moves.size(); \result.get(i) == moves.get(i));
	 */
	/*@pure*/
	public List<PlayMove> toPlayMove(List<Move> moves) {
		List<PlayMove> result = new ArrayList<PlayMove>();
		for (Move move : moves) {
			result.add((PlayMove) move);
		}
		return result;
	}

	/**
	 * zet een lijst met Moves om in een lijst met SwapMoves
	 * @param moves de lijst met moves
	 * @return de lijst met SwapMoves
	 */
	/*@ requires (\forall int i; 0 <= i & i < moves.size(); 
	 										Board.getBlock(moves.get(i)) instanceof Block);
	  @ ensures (\forall int i; 0 <= i & i < moves.size(); \result.get(i) == moves.get(i));
	 */
	/*@pure*/
	public List<SwapMove> toSwapMove(List<Move> moves) {
		List<SwapMove> result = new ArrayList<SwapMove>();
		for (Move move : moves) {
			result.add((SwapMove) move);
		}
		return result;
	}

	//TODO
	public int getTurn() {
		return turn;
	}
}
