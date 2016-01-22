package controller;

import java.net.*;
import java.util.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.*;
import player.*;
import view.*;

public class Client extends Observable {

	private int aiThinkTime;
	private UI ui;
	private Connection conn;
	private Player player;
	private List<Player> opponents = new ArrayList<Player>();
	private Board board;
	private List<Block> tempHand = new ArrayList<Block>();
	private int stackSize;
	
	public Client(UI uiArg, Socket sockArg, Player player) {
		board = new Board();
		ui = uiArg;
		this.player = player;
		ui.setClient(this);
		conn = new Connection(this, sockArg);
		this.addObserver(ui);
		conn.sendString("HELLO " + player.getName());
	}
	
	public void processMessage(Connection connection, String msg) {
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
			reader.next();
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
				if (!(playerNumber == player.getNumber())) {
					opponents.add(new NetworkPlayer(playerName, playerNumber));
				}
			}
			stackSize = 108 - (6 * (opponents.size() + 1));
		} catch (NumberFormatException e) {
			fatalError("invalid Names command was given by server! (" + msg + ")");
		}
		ui.displayBoard(board);
		reader.close();
	}

	private void handleNew(String msg) {
		Scanner reader = new Scanner(msg);
		while (reader.hasNext()) {
			String block = reader.next();
			if (!block.equals("empty")) {
				char[] chars = block.toCharArray();
				synchronized (player) {
					player.getHand().add(new Block(chars[0], chars[1]));
				}
			} else {
				for (Block handBlock : tempHand) {
					player.getHand().add(handBlock);
				}
			}
		}
		tempHand = new ArrayList<Block>();
		reader.close();
	}
	
	private void handleNext(String msg) {
		List<Move> moves = player.determineMove(ui, board, player.getHand(), stackSize, opponents);
		String move = "";
		if (isInstanceOfPlayMoves(moves)) {
			List<PlayMove> playMoves = toPlayMove(moves);
			if (board.isLegalMoveList(playMoves)) {
				for (PlayMove playMove : playMoves) {
					move = move.concat(" " + playMove.getBlock().toString() + //TODO fuck dit
										" " + playMove.y + " " + playMove.x);
					player.removeFromHand(playMove);
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
					player.removeFromHand(swapMove);
				}
				conn.sendString("SWAP" + move);	
			} else {
				error("no enough blocks in stack: " + stackSize);
				handleNext(msg);
			}
		}
	}

	private void handleTurn(String msg) {
		Scanner reader = new Scanner(msg);
		try {
			Player speler = getPlayer(Integer.parseInt(reader.next()));
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
				moves.add(new PlayMove(block, x, y, speler));
			}
			if (!word.equals("empty")) {
				speler.setScore(speler.getScore() + board.legitMoveScore(moves));
				stackSize -= moves.size();
				board.makeMove(moves);
				setChanged();
				notifyObservers("BOARD");
			}
		} catch (NumberFormatException e) {
			fatalError("invalid Turn command from server! (" + msg + ")");
		}
		reader.close();
	}
	
	private void handleKick(String msg) {
		Scanner reader = new Scanner(msg);
		try {
			Player speler = getPlayer(Integer.parseInt(reader.next()));
			int tilesBack = Integer.parseInt(reader.next());
			String reason = reader.nextLine();
			if (speler == this.player) {
				ui.displayKick(speler, reason);
				fatalError("You got kicked from the server! " + reason);
			} else {
				ui.displayKick(speler, reason);
				stackSize += tilesBack;
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
				Controller.chooseServerClient();
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
			Controller.chooseServerClient();
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
			for (Player speler : opponents) {
				if (speler.getNumber() == number) {
					result = speler;
				}
			}
		}
		return result;
	}

	public Board getBoard() {
		return board;
	}
	
	public Player getPlayer() {
		return player;
	}

	public List<Block> getHand() {
		return player.getHand();
	}
}
