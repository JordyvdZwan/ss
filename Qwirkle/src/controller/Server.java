package controller;

import model.*;
import model.Stack;
import view.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


public class Server extends Thread {
	
	
	
	private List<Connection> connections;
	private List<NetworkPlayer> players;
	private ServerController controller;
	Connector connector;
	
	private int turn;
	private boolean notOver = true;
	private int numberOfPlayers;
	private int aiThinkTime;
	private int winner;
	
	private boolean nextMoveAvailable = false;
	private Queue<List<Move>> nextMove = new ArrayBlockingQueue<List<Move>>(1);
	
	public Server(ServerController controllerArg, int aiThinkTimeArg) {
		aiThinkTime = aiThinkTimeArg;
		connections = new ArrayList<Connection>();
		players = new ArrayList<NetworkPlayer>();
		controller = controllerArg;
	}
	
	public void run() { 
		createGameEnviroment();
		broadcastNames();
		giveOutStones();
		determineFirstMove();
		while (notOver) {
			nextTurn();
			handleNextMove(waitForNextMove());
		}
		playerWins(turn);
	}
	
	Board board;
	Stack stack;
	
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
			names.concat(" " + conn.getPlayer().getName() + " " + conn.getPlayer().getNumber());
		}
		broadcastMessage("NAMES" + names + " " + aiThinkTime);
	}	
	
	private void cleanUpGame() {
		// TOD Auto-generated method stub
	}

	private void stopServer() {
		this.interrupt();
	}

	private void broadcastWinner() {
		int win = 0;
		if (winner == 0) {
			win = players.get(0).getNumber();
		} else {
			win = winner;
		}
		broadcastMessage("WINNER " + win);
	}

	private void handleNextMove(List<Move> moves) {
		board.is
		
		
		
		
		
		
		
		
		
	}

	private List<Move> waitForNextMove() {
		while (!nextMoveAvailable) {
			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				//TOD
			}
		}
		return nextMove.poll();
	}

	private void nextTurn() { //TODO if 1 player is left he wins, if next player is same player he wins
		turn++;
		if (getPlayer(turn) == null) {
			nextTurn();
		}
		sendMessage(getPlayer(turn).getConnection(), "TURN " + getPlayer(turn).getNumber());
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
	
	private void determineFirstMove() {
		int[] scores = new int[players.size()];
		for (Player player : players) {
			scores[player.getNumber()] = maxScore(player.getHand());
		}
		
	}

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
	
	private void giveOutStones() {
		for (Player player : players) {
			player.setHand(stack.give(6));
			String stones = "";
			for (Block block : player.getHand()) {
				stones.concat(" " + block.toString());
			}
			sendMessage(player.getConnection(), "NEW" + stones);
		}
	}

	private void playerWins(int win) {
		winner = win;
		broadcastWinner();
		cleanUpGame();
		stopServer();
	}

	public void addConnection(Connection conn) {
		connections.add(conn);
		players.add(conn.getPlayer());
		numberOfPlayers++;
		conn.getPlayer().setConnection(conn);
		connections.get(connections.size() - 1).getPlayer().setNumber(connections.size() - 1);
		if (connections.size() == Controller.MAX_PLAYERS) {
			controller.nextGame();
		}
	}
	
	private boolean isValidName(String name) {
		boolean result = true;
		if (name.length() > 16) {
			result = false;
		}
		for (char c : name.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				result = false;
			}
		}
		return result;
	}
	
	public void kickPlayer(Connection conn, int playerNumber, List<Block> blocks, String reason) {
		connections.remove(conn);
		players.remove(conn.getPlayer());
		stack.giveBack(blocks);
		numberOfPlayers--;
		broadcastMessage("KICK " + playerNumber + " " + blocks.size() + " " + reason);
		if (numberOfPlayers == 1) {
			playerWins(connections.get(0));
		}
	}
	
	public void broadcastMessage(String msg) {
		for (Connection conn: connections) {
			sendMessage(conn, msg);
		}
	}
	
	
	public void processMessage(Connection conn, String msg) {
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("HELLO") && reader.hasNext()) {
			String playerName = reader.next();
			if (isValidName(playerName)) {
				conn.getPlayer().setName(playerName);
				sendMessage(conn, "WELCOME " + playerName + " " + conn.getPlayer().getNumber());
			} else {
				kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), "Invalid userName");
			}
		} else if (command.equals("MOVE")) {
			if (!nextMoveAvailable && reader.hasNext() && conn.getPlayer().getNumber() == turn) {
				nextMoveAvailable = true;
				boolean cycleDone = false;				
				List<Move> moves = new ArrayList<Move>();
				while (reader.hasNext()) {
					cycleDone = false;
					String blockString = reader.next();
					char[] chars = blockString.toCharArray();
					if (!(chars.length == 2)) break;
					Block block = new Block(chars[0], chars[1]);
					
					if (!reader.hasNext()) break;
					String yString = reader.next();
					if (!yString.matches("^-?\\d+$")) break;
					int y = Integer.getInteger(yString);
					
					if (!reader.hasNext()) break;
					String xString = reader.next();
					if (!xString.matches("^-?\\d+$")) break;
					int x = Integer.getInteger(xString);
					
					PlayMove move = new PlayMove(block, x, y, conn.getPlayer());
					moves.add(move);
					cycleDone = true;
				}
				if (cycleDone && !moves.isEmpty()) {
					nextMove.add(moves);
				} else {
					kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), "Invalid move command!");
				}
			} else {
				kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), "Invalid move command, not your turn or one has already been send!");
			}
		} else if (command.equals("SWAP")) {
			if (!nextMoveAvailable && reader.hasNext() && conn.getPlayer().getNumber() == turn) {
				nextMoveAvailable = true;
				
				List<Move> moves = new ArrayList<Move>();
				while (reader.hasNext()) {
					String blockString = reader.next();
					if (Block.isValidBlockString(blockString)) {
						char[] chars = blockString.toCharArray();
						SwapMove move = new SwapMove(new Block(chars[0], chars[1]), conn.getPlayer());
						moves.add(move);
					}
				}
				if (!moves.isEmpty()) {
					nextMove.add(moves);
				} else {
					kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), "Invalid swap command!");
				}
			} else {
				kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), "Invalid swap command, not your turn or one has already been send");
			}
		} else {
			kickPlayer(conn, conn.getPlayer().getNumber(), conn.getPlayer().getHand(), "Command not recognized");
		}
	}
	
	public void sendMessage(Connection conn, String msg) {
		System.out.println("[SERVER]: Sending message: \"" + msg + "\"");
		conn.sendString(msg);
	}

}
