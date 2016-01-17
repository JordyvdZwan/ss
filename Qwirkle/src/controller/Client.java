package controller;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
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
	
	public Client(UI uiArg, Socket sockArg, String userNameArg) {
		board = new Board();
		ui = uiArg;
		ui.setClient(this);
		conn = new Connection(this, sockArg);
		
		conn.sendString("HELLO " + userNameArg);
	}
	
	public List<Block> getHand() {
		return hand;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void processMessage(Connection conn, String msg) {
		System.out.println("[CLIENT]: proccesing Message");
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		if (command.equals("WELCOME")) {
			handleWelcome(msg);
		} else if (command.equals("NAMES")) {
			handleNames(msg);
		} else if (command.equals("NEXT")) {
			handleNext(msg);
		} else if (command.equals("NEW")) {
			handleNew(msg);
		} else if (command.equals("TURN")) {
			handleTurn(msg);
		} else if (command.equals("KICK")) {
			handleKick(msg);
		} else if (command.equals("WINNER")) {
			handleWinner(msg);
		} else {
			//TODO throw Exception
		}
		reader.close();
	}
	
	private void error(String msg) {
		
	}
	
	private void fatalError(String msg) {
		System.out.println("[FATAL ERROR]: " + msg);
		System.exit(0);
	}
	
	private void handleWelcome(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
		String playerName = reader.next();
		int playerNumber = Integer.parseInt(reader.next()); //TODO catch error
		player = new HumanPlayer(playerName, playerNumber);
		reader.close();
	}
	
	private void handleNames(String msg) { //TODO errors catchen
		Scanner reader = new Scanner(msg);
		reader.next();
			while (reader.hasNext()) {
				String playerName = reader.next();
				if (!reader.hasNext()) {
					aiThinkTime = Integer.parseInt(playerName); //TODO catch
					break;
				}
				int playerNumber = Integer.parseInt(reader.next()); //TODO catch
				opponents.add(new NetworkPlayer(playerName, playerNumber));
			}
			stackSize = 108 - (6 * (opponents.size() + 1));
		reader.close();
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
	
	private void handleNext(String msg) {
		ui.displayBoard(board);
		List<Move> moves = ui.getMove();
		String move = "";
		if (isInstanceOfPlayMoves(moves)) {
			List<PlayMove> playMoves = toPlayMove(moves);
			for (PlayMove playMove : playMoves) {
				move = move.concat(" " + playMove.getBlock().toString() + " " + playMove.y + " " + playMove.x);
			}
			conn.sendString("MOVE" + move);			
		} else {
			List<SwapMove> swapMoves = toSwapMove(moves);
			tempHand = new ArrayList<Block>();
			for (SwapMove swapMove : swapMoves) {
				move = move.concat(" " + swapMove.getBlock().toString());
				tempHand.add(swapMove.getBlock());
				hand.remove(swapMove.getBlock());
			}
			conn.sendString("SWAP" + move);		
		}
	}
	
	private void handleNew(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
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
		reader.close();
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
	
	private void handleTurn(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
		Player player = getPlayer(Integer.parseInt(reader.next())); //TODO catchen
		List<PlayMove> moves = new ArrayList<PlayMove>();
		String word = "";
		while (reader.hasNext()) {
			word = reader.next();
			if (word.equals("empty")) {
				break;
			}
			char[] chars = word.toCharArray();
			Block block = new Block(chars[0], chars[1]);
			int y = Integer.parseInt(reader.next()); //TODO catchen
			int x = Integer.parseInt(reader.next()); //TODO catchen
			moves.add(new PlayMove(block, x, y, player));
		}
		if (!word.equals("empty")) {
			board.makeMove(moves);
			player.setScore(player.getScore() + board.legitMoveScore(moves));
		}
		
		reader.close();
	}
	
	private void handleKick(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
		Player player = getPlayer(Integer.parseInt(reader.next())); //TODO catchen
		int tilesBack = Integer.parseInt(reader.next()); //TODO catchen
		String reason = reader.nextLine();
		if (player == this.player) {
			ui.displayKick(player, reason);
			shutdown();
		} else {
			ui.displayKick(player, reason);
			stackSize =+ tilesBack;
		}
		reader.close();
	}
	
	private void handleWinner(String msg) {
		Scanner reader = new Scanner(msg);
		reader.next();
		int winner = Integer.parseInt(reader.next()); //TODO catchen
		ui.displayWinner(getPlayer(winner));
		if (ui.newGame()) {
			Controller.startClient();
		} else {
			shutdown();
		}
		reader.close();
	}
	
	private void shutdown() {
		conn.lossOfConnection();
		System.exit(0);
	}
}
