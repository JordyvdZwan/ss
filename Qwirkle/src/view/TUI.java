package view;

import java.util.List;
import java.util.Scanner;

import controller.Client;
import controller.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import model.*;
import player.Player;

public class TUI implements UI {
	private Client client;
	private Server servercontroller;
	
	public void setClient(Client client) {
		this.client = client;
	}

	private Scanner in = new Scanner(System.in);
	
	public TUI() {
		
	}
	
	public void setServerController(Server control) {
		servercontroller = control;
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
	
	public String getCommand() {
		// TODO
		return null;
	}
	
	public void displayHand(List<Block> hand) {
		String handString = "Your hand is:";
		for (Block block : hand) {
			handString = handString.concat(" " + block.toString());
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
		} else {
			System.out.println("Invalid input, please try again.");
			result = getChoiceServerClient();
		}
		return result;
	}
	
	public void displayBoard(Board board) {
		System.out.println(board.toString());
	} 
	
	public void displayScore() {
		System.out.println("Score: " + client.getPlayer().getScore());
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
	
	public List<Move> getMove(Board b) {
		System.out.println("Please enter a move (in a protocol manner [TILE ROW COLLUM]");
		String msg = in.nextLine();	
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		List<Move> result = null;
		if (command.equals("MOVE")) {
			try {
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
					int y = Integer.parseInt(yString);
					
					if (!reader.hasNext()) break;
					String xString = reader.next();
					if (!xString.matches("^-?\\d+$")) break;
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
						SwapMove move = new SwapMove(new Block(chars[0], chars[1]), client.getPlayer());
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
		return result;
	}
	
	private List<Move> invalidMove(Board b) {
		System.out.println("Invalid Move please try again.");
		return getMove(b);
	}
	
	public InetAddress getHost() {
		System.out.println("Please enter a valid host address.");
		InetAddress host = null;
		try {
			host = InetAddress.getByName(in.nextLine());
		} catch (UnknownHostException e) {
			System.out.println("Invalid host name, please try again.");
			host = getHost();
		}
		return host;
	}
	
	public int getPort() {
		System.out.println("Please enter a valid port.");
		int port = 0;
		try {
			port = Integer.parseInt(in.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid host name, please try again.");
			port = getPort();
		}
		return port;
		
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
	
	public String getUserName() {
		System.out.println("Please enter a valid username (a-z, A-Z (max 16 characters))");
		String name = in.nextLine();
		if (!isValidName(name)) {
			System.out.println("Invalid username, please try again.");
			name = getUserName();
		}
		return name;
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
		System.out.println("The winner is: " + player.getName() + "! With a score of: " + player.getScore());
	}

	@Override
	public int getAIThinkTime() { //TODO catch parse
		System.out.println("Please enter a valid AI think time.");
		int port = 0;
		try {
			port = Integer.parseInt(in.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid aiThinkTime, please try again.");
			port = getPort();
		}
		return port;
	}

	@Override
	public void displayFatalError(String msg) {
		System.out.println("[FATAL ERROR]: " + msg);
	}
}
