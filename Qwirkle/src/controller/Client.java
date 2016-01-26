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
	public Connection conn;
	private /*@ spec_public @*/ LocalPlayer player;
	private List<Player> opponents = new ArrayList<Player>();
	private /*@ spec_public @*/ Board board;
	private List<Block> tempHand = new ArrayList<Block>();
	private /*@ spec_public @*/ int stackSize;
	private int numberOfPlayers = opponents.size() + 1;
	
	public Client(UI uiArg, Socket sockArg, LocalPlayer player) {
		board = new Board();
		ui = uiArg;
		this.player = player;
		ui.setClient(this);
		conn = new Connection(this, sockArg);
		this.addObserver(ui);
		conn.sendString("HELLO " + player.getName());
	}
	
	/**
	 * calls the correct method that then handles the message.
	 * when a incorrect command is given, it will give a Fatal Error.
	 * @param connection
	 * @param msg
	 */
	/*@ requires connection != null;
	  @ requires msg != null;
	 */
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

	/**
	 * handles the WELCOME command from the server.
	 * reads the player number gotten from the server and writes it to the player.
	 * when given a wrong number, it will give a fatal error.
	 * @param msg
	 */
	/*@ requires msg != null;
	  @ ensures player.getNumber() == Integer.parseInt(new Scanner(msg).next());
	 */
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

	
	/**
	 * TODO
	 * @param msg
	 */
	/*@ requires msg != null;
	  @ ensures stackSize == 108 - (6 * (opponents.size() + 1)); 
 	  */
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

	/**
	 * TODO.
	 * @param msg
	 */
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
	
	/**
	 * kijkt of alle stenen van de zet wel in de hand van de speler zitten.
	 * @param moves de zet 
	 * @param hand de hand van de speler
	 * @return true als alle stenen in de hand zitten
	 */
	/*@ requires (\forall int i; 0 <= i & i < moves.size(); 
	   									Board.getBlock(moves.get(i)) instanceof Block);
	  @ requires (\forall int i; 0 <= i & i < hand.size();
	  									hand.get(i) instanceof Block);
	  @ ensures (\forall int i; 0 <= i & i < moves.size();
	  			(\forall int j; 0 <= j & j < hand.size();
	  			Board.getBlock(moves.get(i)) == hand.get(j)) ==> \result == true);
	 */
	
	/*@pure*/
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
	// TODO
	private void handleNext(String msg) {
		Scanner reader = new Scanner(msg);
		try {
			if (player == getPlayer(Integer.parseInt(reader.next()))) {
				List<Move> moves = player.determineMove(ui, board, stackSize, opponents, aiThinkTime);
				String move = "";
				if (isInstanceOfPlayMoves(moves)) {
					List<PlayMove> playMoves = toPlayMove(moves);
					System.out.println(board.isLegalMoveList(playMoves));
					System.out.println(blocksInHand(moves, player.getHand()));
					if (board.isLegalMoveList(playMoves) && blocksInHand(moves, player.getHand())) {
						for (PlayMove playMove : playMoves) {
							move = move.concat(" " + playMove.getBlock().toString() + 
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
					if (stackSize >= swapMoves.size() && blocksInHand(moves, player.getHand())) {
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
		} catch (NumberFormatException e) {
			fatalError("invalid Next command from server! (" + msg + ")");
		}
		reader.close();
	}

	// TODO
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
	
	/**
	 * kickt een speler uit de server
	 * @param msg
	 */
	/*@ requires numberOfPlayers > 1;
	  @ ensures stackSize == stackSize + getPlayer(Integer.parseInt(new Scanner(msg).next())).
	  																			getHand().size();
	  @ ensures numberOfPlayers == (\old(numberOfPlayers - 1));
	 */
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
				opponents.remove(getPlayer(player.getNumber()));
			}
		} catch (NumberFormatException e) {
			fatalError("invalid Kick command received from server. (" + msg + ")");
		}
		reader.close();
	}
	
	/**
	 * stuurt een bericht naar alle spelers wie er gewonnen heeft.
	 * @param msg het nummer van de speler
	 */
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
	
	/**
	 * geeft een foutmelding als de connectie met de server verloren gaat.
	 */
	private void handleLossOfConnection() {
		fatalError("Connection with server was lost");
	}

	/**
	 * geeft een foutmelding waardoor verbinding met de server verbreekt.
	 * @param msg het bericht dat wordt meegestuurd
	 */
	private void fatalError(String msg) {
		System.out.println("[FATAL ERROR]: " + msg);
		if (ui.newGame()) {
			Controller.chooseServerClient();
		} else {
			System.exit(0);
		}
	}

	/**
	 * print een foutmelding.
	 * @param msg de foutmelding
	 */
	/*@ requires msg != null;
	 */
	private void error(String msg) {
		System.out.println("ERROR " + msg);
	}
	
	/**
	 * beëindigt een connectie.
	 */
	/*@ ensures conn.active == false;
	 */
	public void stopConnection() {
		conn.stopConnection();
	}

	// TODO
	/**
	 * beëndigt het spel.
	 */
	/*@ ensures numberOfPlayers == 0;
	  @ ensures conn.active == false;
	 */
	private void shutdown() {
		stopConnection();
		System.exit(0);
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
	private List<PlayMove> toPlayMove(List<Move> moves) {
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
	private List<SwapMove> toSwapMove(List<Move> moves) {
		List<SwapMove> result = new ArrayList<SwapMove>();
		for (Move move : moves) {
			result.add((SwapMove) move);
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

	/**
	 * geeft de speler die dit nummer heeft.
	 * @param number het nummer van de speler
	 * @return de speler.
	 */
	/*@ requires number < opponents.size();
	  @ ensures player.getNumber() == number ==> \result == player;
	  @ ensures player.getNumber() != number ==> 
	  	(\forall int i; 0 <= i & i < opponents.size(); opponents.get(i).getNumber() == number
	  															==> \result == opponents.get(i));
	 */
	/*@ pure*/
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
		if (result == null) {
			result = new HumanPlayer("User not found", number);
		}
		return result;
	}

	/**
	 * laat het speelbord zien.
	 * @return het speelbord
	 */
	/*@ ensures \result == board;
	 */
	/*@ pure*/
	public Board getBoard() {
		return board;
	}
	
	/**
	 * geeft de speler.
	 * @return de speler
	 */
	/*@ ensures \result == player;
	 */
	/*@ pure*/
	public Player getPlayer() {
		return player;
	}

	/**
	 * laat de hand van een speler zien.
	 * @return de hand van een speler
	 */
	/*@ ensures \result == player.getHand();
	 */
	/*@ pure*/
	public List<Block> getHand() {
		return player.getHand();
	}
	
	/**
	 * laat zien hoeveel er nog in de pot zit.
	 * @return de grootte van de pot
	 */
	/*@ ensures \result == stackSize;
	 */
	/*@pure*/
	public int getStackSize() {
		return stackSize;
	}
}