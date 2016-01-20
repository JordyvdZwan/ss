package controller;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.*;
import player.*;
import view.*;

public class Client {

	private int aiThinkTime;
	private UI ui;
	private Connection conn;
	private Player player;
	private List<Player> opponents = new ArrayList<Player>();
	private Board board;
	private List<Block> hand = new ArrayList<Block>();
	private List<Block> tempHand = new ArrayList<Block>();
	private int stackSize;
	
	public Client(UI uiArg, Socket sockArg, Player player) {//TODO maak er localplayer van
		board = new Board();
		ui = uiArg;
		this.player = player;
		ui.setClient(this);
		conn = new Connection(this, sockArg);
		
		conn.sendString("HELLO " + player.getName());
	}
	
	public void processMessage(Connection conn, String msg) {
		try {
			Scanner reader = new Scanner(msg);
			String command = reader.next();
			String message = reader.nextLine();
			reader.close();
		
			if (command.equals("WELCOME")) {
				handleWelcome(message);
			} else if (command.equals("NAMES")) { 
				handleNames(message);
			} else if (command.equals("NEXT")) {
				handleNext(message);
			} else if (command.equals("NEW")) {
				handleNew(message);
			} else if (command.equals("TURN")) {
				handleTurn(message);
			} else if (command.equals("KICK")) {
				handleKick(message);
			} else if (command.equals("WINNER")) {
				handleWinner(message);
			} else if (command.equals("LOSSOFCONNECTION")) {
				handleLossOfConnection();
			} else {
				fatalError("invalid command received from connection!");
			}
		} catch (NoSuchElementException e) {
			fatalError("invalid command received from connection!");
		}
	}

	private void handleWelcome(String msg) {
		Scanner reader = new Scanner(msg);
		try {
			String playerName = reader.next();
			int playerNumber = Integer.parseInt(reader.next());
			player.setNumber(playerNumber);
		} catch (NumberFormatException e) {
			fatalError("invalid Welcome command given by server (" + msg + ")");
		}
		reader.close();
	}

	private void handleNames(String msg) { 
		Scanner reader = new Scanner(msg);
		try {
			while (reader.hasNext()) {
				String playerName = reader.next();
				if (!reader.hasNext()) {
					aiThinkTime = Integer.parseInt(playerName); 
					break;
				}
				int playerNumber = Integer.parseInt(reader.next()); 
				opponents.add(new NetworkPlayer(playerName, playerNumber));
			}
			stackSize = 108 - (6 * (opponents.size() + 1));
		} catch (NumberFormatException e) {
			fatalError("invalid Names command was given by server! (" + msg + ")");
		}
		reader.close();
	}

	private void handleNew(String msg) {
		Scanner reader = new Scanner(msg);
		while (reader.hasNext()) {
			String block = reader.next();
			if (!block.equals("empty")) {
				char[] chars = block.toCharArray();
				hand.add(new Block(chars[0], chars[1]));
			} else {
				for (Block handBlock : tempHand) {
					hand.add(handBlock);
				}
			}
		}
		tempHand = new ArrayList<Block>();
		reader.close();
	}
	
	private void removeBlockFromHand(Block block) {
		for (Block handBlock : hand) {
			if (block.color == handBlock.color && block.shape == handBlock.shape) {
				hand.remove(handBlock);
			}
		}
	}

	private void handleNext(String msg) {
		List<Move> moves = player.determineMove(ui, board, hand);
		String move = "";
		if (isInstanceOfPlayMoves(moves)) {
			List<PlayMove> playMoves = toPlayMove(moves);
			if (board.isLegalMoveList(playMoves)) {
				for (PlayMove playMove : playMoves) {
					move = move.concat(" " + playMove.getBlock().toString() + " " + playMove.y + " " + playMove.x);
					tempHand.add(playMove.getBlock());
					removeBlockFromHand(playMove.getBlock());
				}
				conn.sendString("MOVE" + move);
			} else {
				error("invalid move command");
				handleNext(msg);
			}
		} else {
			List<SwapMove> swapMoves = toSwapMove(moves);
			if (stackSize >= swapMoves.size()) {
				tempHand = new ArrayList<Block>();
				for (SwapMove swapMove : swapMoves) {
					move = move.concat(" " + swapMove.getBlock().toString());
					tempHand.add(swapMove.getBlock());
					removeBlockFromHand(swapMove.getBlock());
				}
				conn.sendString("SWAP" + move);	
			} else {
				error("invalid swap command");
				handleNext(msg);
			}
		}
	}

	private void handleTurn(String msg) {
		Scanner reader = new Scanner(msg);
		try {
			Player player = getPlayer(Integer.parseInt(reader.next()));
			List<PlayMove> moves = new ArrayList<PlayMove>();
			String word = "";
			while (reader.hasNext()) {
				word = reader.next();
				if (word.equals("empty")) {
					break;
				}
				char[] chars = word.toCharArray();
				Block block = new Block(chars[0], chars[1]);
				int y = Integer.parseInt(reader.next());
				int x = Integer.parseInt(reader.next());
				moves.add(new PlayMove(block, x, y, player));
			}
			if (!word.equals("empty")) {
				player.setScore(player.getScore() + board.legitMoveScore(moves));
				board.makeMove(moves);
			}
		} catch (NumberFormatException e) {
			fatalError("invalid Turn command from server! (" + msg + ")");
		}
		reader.close();
	}
	
	private void handleKick(String msg) {
		Scanner reader = new Scanner(msg);
		try {
			Player player = getPlayer(Integer.parseInt(reader.next()));
			int tilesBack = Integer.parseInt(reader.next());
			String reason = reader.nextLine();
			if (player == this.player) {
				ui.displayKick(player, reason);
				fatalError("You got kicked from the server! " + reason);
			} else {
				ui.displayKick(player, reason);
				stackSize =+ tilesBack;
			}
		} catch (NumberFormatException e) {
			fatalError("invalid Kick command received from server. (" + msg + ")");
		}
		reader.close();
	}
	
	private void handleWinner(String msg) {
		Scanner reader = new Scanner(msg);
		try {
			int winner = Integer.parseInt(reader.next());
			ui.displayWinner(getPlayer(winner));
			if (ui.newGame()) {
				Controller.startClient();
			} else {
				shutdown();
			}
		} catch (NumberFormatException e) {
			fatalError("invalid winner command received from server. (" + msg + ")");
		}
		
		reader.close();
	}
	
	private void handleLossOfConnection() {
		fatalError("Connection with server was lost");
	}

	private void fatalError(String msg) {
		System.out.println("[FATAL ERROR]: " + msg);
		if (ui.newGame()) {
			Controller.startClient();
		} else {
			System.exit(0);
		}
	}

	private void error(String msg) {
		System.out.println("ERROR " + msg);
	}

	private void shutdown() {
		conn.stopConnection();
		System.exit(0);
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

	private boolean isInstanceOfPlayMoves(List<Move> moves) {
		boolean result = true;
		for (Move move : moves) {
			if (!(move instanceof PlayMove)) {
				result = false;
				break;
			}
		}
		return result;
	}

	private Player getPlayer(int number) {
		Player result = null;
		if (player.getNumber() == number) {
			result = player;
		} else {
			for (Player player : opponents) {
				if (player.getNumber() == number) {
					result = player;
				}
			}
		}
		return result;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Block> getHand() {
		return hand;
	}
}
