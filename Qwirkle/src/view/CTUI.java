package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;

import controller.Client;
import controller.Server;
import model.Block;
import model.Board;
import model.Move;
import model.PlayMove;
import model.SwapMove;
import player.Player;
//NOTE: only works when console has ANSI escape 

public class CTUI implements UI {
	private Client client;
	private Server servercontroller;
	private Scanner in = new Scanner(System.in);
	private boolean localGame;
	
	/**
	 * returns a list of moves, if a valid input has been given.
	 */
	public List<Move> getMove(Board b) {
		System.out.println("Please enter a move (in a protocol manner [TILE ROW COLLUM]");
		List<Move> result = null;
		if (in.hasNextLine()) {
			String msg = in.nextLine();	
			Scanner reader = new Scanner(msg);
			String command = reader.next();
			if (command.equals("MOVE")) {
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
						
						PlayMove move = new PlayMove(block, x, y, client.getPlayer());
						moves.add(move);
						cycleDone = true;
					}
					if (cycleDone && !moves.isEmpty()) {
						result = moves;
					} else {
						result = invalidMove(b);
					}
				} catch (NumberFormatException e) {
					result = invalidMove(b);
				}
			} else if (command.equals("SWAP")) {
				List<Move> moves = new ArrayList<Move>();
				while (reader.hasNext()) {
					String blockString = reader.next();
					if (Block.isValidBlockString(blockString)) {
						char[] chars = blockString.toCharArray();
						SwapMove move = new SwapMove(new Block(chars[0],
										chars[1]), client.getPlayer());
						moves.add(move);
					}
				}
				if (!moves.isEmpty()) {
					result = moves;
				} else {
					result = invalidMove(b);
				}
			} else {
				result = invalidMove(b);
			}
			reader.close();
		} else {
			result = invalidMove(b);
		}
		return result;
	}

	public String getHost() {
		System.out.println("Please enter a valid host address.");
		return in.nextLine();
	}

	public String getPort() {
		System.out.println("Please enter a valid port.");
		return in.nextLine();
	}

	public String getUserName() {
		System.out.println("Please enter a valid username (a-z, A-Z (max 16 characters))");
		return in.nextLine();
	}

	@Override
	public String getAIThinkTime() { 
		System.out.println("Please enter a valid AI think time.");
		return in.nextLine();
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setServerController(Server control) {
		servercontroller = control;
	}
	
	public CTUI(boolean local) {
		this.localGame = local;
	}
	
	public void run() {
		boolean running = true;
		while (running) {
			String txt = in.nextLine();
			if (txt.equals("start")) {
				servercontroller.handleInput(txt);
			}
		}
	}
	
	public void displayHand(List<Block> hand) {
		String handString = "Your hand is:";
		for (Block block : hand) {
			handString = handString.concat(" " + block.toColorString());
		}
		System.out.println(handString);
	}
	
	public void errorOccured(String msg) {
		System.out.println("ERROR: " + msg);
	}
	
	public String getChoiceServerClient() {
		System.out.println("Do you want to start a server of client? press corresponding number.");
		System.out.println("1. start Server          // 2. start Client");
		System.out.println("3. start Default Server // 4. start local Client");
		System.out.println("5. start local AI      // 6. start AI");
		String input = in.nextLine();
		String result = "";
		if (input.equals("1")) {
			result = "SERVER";
		} else if (input.equals("2")) {
			result = "CLIENT";
		} else if (input.equals("3")) {
			result = "DEFAULTSERVER";
		} else if (input.equals("4")) {
			result = "LOCALCLIENT";
		} else if (input.equals("5")) {
			result = "LOCALAI";
		} else if (input.equals("6")) {
			result = "AI";
		} else {
			System.out.println("Invalid input, please try again.");
			result = getChoiceServerClient();
		}
		return result;
	}
	
	public void displayBoard(Board board) {
		System.out.println(board.toColorString());
	} 
	
	public void displayScore(Player player, List<Player> opponents) {
		System.out.println("Scores:");
		Map<Player, Integer> scores = new HashMap<Player, Integer>();
		scores.put(player, player.getScore());
		for (Player person : opponents) {
			scores.put(person, person.getScore());
		}
		int counter = 1;
		while (scores.size() > 0) {
			Player person = highestPlayer(scores);
			System.out.println(counter + ". " + person.getName() +
							" (" + person.getNumber() + "): " + person.getScore());
			scores.remove(person);
			counter++;
		}
	}
	
	private Player highestPlayer(Map<Player, Integer> scores) {
		Player result = null;
		Integer score = 0;
		for (Player i : scores.keySet()) {
			if (i.getScore() >= score) {
				result = i;
				score = i.getScore();
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
	
	
	private List<Move> invalidMove(Board b) {
		System.out.println("Invalid Move please try again.");
		return getMove(b);
	}
	
	public void displayServerMessage(String msg) {
		if (!localGame) {
			System.out.println(msg);
		}
	}
	
	@Override
	public boolean newGame() {
		System.out.println("Do you want to start a new game? (y/n)");
		String input = in.nextLine();
		boolean result = false;
		if (input.equals("y")) {
			result = true;
		} else if (input.equals("n")) {
			result = false;
		} else {
			System.out.println("Invalid input, please try again.");
			result = newGame();
		}
		return result;
	}

	@Override
	public void displayKick(Player player, String reason) {
		System.out.println("Player " + player.getName() + " has been kicked! Reason: " + reason);
	}

	@Override
	public void displayWinner(Player player) {
		System.out.println("The winner is: " + 
						player.getName() + "! With a score of: " + player.getScore());
	}


	@Override
	public void displayFatalError(String msg) {
		System.out.println("[FATAL ERROR]: " + msg);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals("BOARD")) {
			displayBoard(client.getBoard());
		}		
	}

	@Override
	public String getCommand() {
		return in.nextLine();
	}
}
