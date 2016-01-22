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
	
	private List<Connection> connections;
	private List<NetworkPlayer> players;
	private Server server;
	
	private int turn;
	private int numberOfPlayers;
	private int aiThinkTime;
	private Board board;
	private Stack stack;
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
	

	/**
	 * 
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
	 * 
	 */
	public void sendMessage(Connection conn, String msg) {
		ui.displayServerMessage("[SERVER]: Sending message to " + 
						conn.getPlayer().getName() + " : \"" + msg + "\"");
		conn.sendString(msg);
	}

	/**
	 * 
	 */
	public void broadcastMessage(String msg) {
		for (int i = 0; i < connections.size(); i++) {
			sendMessage(connections.get(i), msg);
		}
	}

	/**
	 * 
	 */
	private void broadcastPlayMove(List<PlayMove> playMoves, int nr) {
		String moves = "";
		for (PlayMove move : playMoves) {
			moves = moves.concat(" " + move.getBlock().toString() + " " + move.y + " " + move.x);
		}
		broadcastMessage("TURN " + nr + moves);
	}

	private void broadcastSwapMove(List<SwapMove> swapMoves, int nr) {
		broadcastMessage("TURN " + nr + " empty");
	}

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

	private void handleNextMove(List<Move> moves) {
		if (isInstanceOfPlaymoves(moves)) {
			List<PlayMove> playMoves = toPlayMove(moves);
			if (board.isLegalMoveList(playMoves)) {
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
			if (stack.isValidSwap(swapMoves)) {
				swapStones(swapMoves.get(0).getPlayer().getConnection(), moves, swapMoves.size());
				broadcastSwapMove(swapMoves, swapMoves.get(0).getPlayer().getNumber());
			} else {
				Connection conn = moves.get(0).getPlayer().getConnection();
				kickPlayer(conn, conn.getPlayer().getNumber(), 
								conn.getPlayer().getHand(), "Invalid swap command! (HandleNext)");
			}
		}
	}

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

	private void createGameEnviroment() {
		board = new Board();
		stack = new Stack();
		
		for (Player player : players) {
			player.setNumber(players.indexOf(player));
		}
	}
	
	private void broadcastNames() {
		String names = "";
		for (Connection conn: connections) {
			names = names.concat(" " + conn.getPlayer().getName() 
							+ " " + conn.getPlayer().getNumber());
		}
		broadcastMessage("NAMES" + names + " " + aiThinkTime);
	}	
	
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

	private void determineFirstMove() {
		int[] scores = new int[players.size()];
		for (Player player : players) {
			scores[player.getNumber()] = maxScore(player.getHand());
		}
	}

	
	/**
	 * 
	 * @param blocks
	 * @return
	 */
	//@ requires blocks.size() == 6;
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

	private void nextTurn() {
		if (players.size() == 1) {
			playerWins(players.get(0).getNumber());
		} else if (players.size() == 0) {
			endGame();
		} else {
			turn = (turn + 1) % players.size();
			if (getPlayer(turn) == null) {
				nextTurn();
			}
			if (!(board.noValidMoves(getPlayer(turn).getHand()) && stack.size() == 0)) {
				broadcastMessage("NEXT " + getPlayer(turn).getNumber());
			} else {
				playerWins(detectWinner());
			}
		}
	}

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
			conn.getPlayer().swapHand(moves, blocks);
			String blockString = "";
			for (Block block : blocks) {
				blockString = blockString.concat(" " + block.toString());
			}
			sendMessage(conn, "NEW" + blockString);
		}
	}
	
	private NetworkPlayer getPlayer(int playerNumber) {
		NetworkPlayer player = null;
		for (NetworkPlayer netPlayer : players) {
			if (netPlayer.getNumber() == playerNumber) {
				player = netPlayer;
			}
		}
		return player;
	}
	
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

	private void playerWins(int win) {
		if (win >= 0) {
			broadcastWinner(win);
			endGame();
		} else {
			endGame();
		}
	}

	private void broadcastWinner(int winner) {
		int win = 0;
		if (winner == 0) {
			win = players.get(0).getNumber();
		} else {
			win = winner;
		}
		broadcastMessage("WINNER " + win);
	}

	private void endGame() {
		for (int i = 0; i < connections.size(); i++) {
			connections.get(i).stopConnection();
		}
		this.interrupt();
	}

	private int detectWinner() {
		int result = -1;
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

	public List<PlayMove> toPlayMove(List<Move> moves) {
		List<PlayMove> result = new ArrayList<PlayMove>();
		for (Move move : moves) {
			result.add((PlayMove) move);
		}
		return result;
	}

	public List<SwapMove> toSwapMove(List<Move> moves) {
		List<SwapMove> result = new ArrayList<SwapMove>();
		for (Move move : moves) {
			result.add((SwapMove) move);
		}
		return result;
	}

}
