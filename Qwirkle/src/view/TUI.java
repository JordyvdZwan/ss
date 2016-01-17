package view;

import java.util.List;
import java.util.Scanner;

import controller.Client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import model.*;
import player.Player;

public class TUI implements UI {
	private static final int DIM = 182;
	private Player player;
	private int Port;
	private Client client;
	
	public void setClient(Client client) {
		this.client = client;
	}

	private Scanner in = new Scanner(System.in);
	
	public TUI() {
		
	}
	
	public String getCommand() {
		// TODO
		return null;
	}
	
	public void displayHand() {
		List<Block> hand = client.getHand();
		String handString = "Your hand is:";
		for (Block block : hand) {
			handString.concat(" " + block.toString());
		}
		System.out.println(handString);
	}
	
	public void errorOccured(String msg) {
		System.out.println("ERROR: " + msg);
	}
	
	public String getChoiceServerClient() {
		System.out.println("Do you want to start a server of client? (Server/Client)");
		String input = in.nextLine();
		String result = "";
		if (input.equals("Server")) {
			result = "SERVER";
		} else if (input.equals("Client")) {
			result = "CLIENT";
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
	
	public List<Move> getMove() {
		String msg = in.nextLine();	
		Scanner reader = new Scanner(msg);
		String command = reader.next();
		List<Move> result = null;
		if (command.equals("MOVE")) {
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
					
					PlayMove move = new PlayMove(block, x, y, client.getPlayer());
					moves.add(move);
					cycleDone = true;
				}
				if (cycleDone && !moves.isEmpty()) {
					result = moves;
				} else {
					result = invalidMove();
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
					result = invalidMove();
				}
		} else {
			result = invalidMove();
		}
		return result;
	}
	
	private List<Move> invalidMove() {
		System.out.println("Invalid Move please try again.");
		return getMove();
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
	public int getAIThinkTime() { //TODO 
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
}
