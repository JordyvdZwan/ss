package view;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;

import controller.Client;
import controller.Server;

import java.util.ArrayList;
import java.util.HashMap;

import model.*;
import player.Player;

public class TUI implements UI {
	private Client client = null;
	private Server servercontroller;
	private Scanner in = new Scanner(System.in);
	private boolean localGame;
	
	/**
	 * returns a list of moves, if a valid input has been given.
	 */
	public List<Move> getMove(Board b) {
		System.out.println("Please enter a move (in a protocol manner "
						+ "MOVE [TILE ROW COLLUM] / SWAP [TILE])");
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
			} else if (command.equals("HINT")) {
				System.out.println(client.getBoard().
											hint(client.getStackSize(), client.getPlayer()));
				result = getMove(b);
			} else {
				result = invalidMove(b);
			}
			reader.close();
		} else {
			result = invalidMove(b);
		}
		return result;
	}

	/**
	 * asks on which host address you would like to play.
	 * it reads the next line and uses that as address.
	 */
	public String getHost() {
		System.out.println("Please enter a valid host address.");
		return in.nextLine();
	}

	/**
	 * asks on which port you would like to play.
	 * it reads the next line and uses that as port.
	 */
	public String getPort() {
		System.out.println("Please enter a valid port.");
		return in.nextLine();
	}

	/**
	 * asks what your name is.
	 */
	public String getUserName() {
		System.out.println("Please enter a valid username (a-z, A-Z (max 16 characters))");
		return in.nextLine();
	}

	/**
	 * asks how long the AI can think.
	 */
	@Override
	public String getAIThinkTime() { 
		System.out.println("Please enter a valid AI think time.");
		return in.nextLine();
	}

	/**
	 * sets a client.
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * sets a servercontoller.
	 */
	public void setServerController(Server control) {
		servercontroller = control;
	}
	
	/**
	 * TODO.
	 * @param local
	 */
	public TUI(boolean local) {
		this.localGame = local;
	}
	
	/**
	 * wacht op start/stop input van de server.
	 */
	public void run() {
		boolean running = true;
		while (running) {
			String txt = in.nextLine();
			if (txt.equals("start")) {
				servercontroller.handleInput(txt);
			} else if (txt.equals("stop")) {
				servercontroller.handleInput(txt);
			}
		}
	}
	
	/**
	 * print de hand van een speler.
	 * @param de hand van de speler
	 */
	public void displayHand(List<Block> hand) {
		String handString = "Your hand is:";
		for (Block block : hand) {
			handString = handString.concat(" " + block.toString());
		}
		System.out.println(handString);
	}
	
	/**
	 * print een foutmelding.
	 * @param msg de foutmelding
	 */
	public void errorOccured(String msg) {
		System.out.println("ERROR: " + msg);
	}
	
	/**
	 * dit is het beginmenu waar je kan kiezen wat je wil doen.
	 */
	public String getChoiceServerClient() {
		System.out.println("Qwirkle version 1.0 by Reinier Stribos and Jordy van der Zwan");
		System.out.println("Do you want to start a server of client? press corresponding number.");
		System.out.println("1. start Server                      // 2. start Client");
		System.out.println("3. start Default Server             // 4. start local Client");
		System.out.println("5. start local Multi Thread AI     // 6. start Multi Thread AI");
		System.out.println("7. start local Miranda AI         // 8. start Miranda AI");
		System.out.println("9. start local Retarded AI       // 10. start Retarded AI");
		System.out.println("NOTE: the Miranda and Retarded "
											+ "AI do not respect the ThinkTime of the AI");
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
			result = "LOCALAIMU";
		} else if (input.equals("6")) {
			result = "AIMU";
		} else if (input.equals("7")) {
			result = "LOCALAIMI";
		} else if (input.equals("8")) {
			result = "AIMI";
		} else if (input.equals("9")) {
			result = "LOCALAIR";
		} else if (input.equals("10")) {
			result = "AIR";
		} else if (input.equals("1000000")) {
			result = "ATTACK";
		} else {
			System.out.println("Invalid input, please try again.");
			result = getChoiceServerClient();
		}
		return result;
	}
	
	/**
	 * print het speelbord uit.
	 * @param board het speelbord
	 */
	public void displayBoard(Board board) {
		System.out.println(board.toString());
	} 
	
	/**
	 * print de score (op volgorde van hoeveelheid punten die iedereen heeft).
	 * @param player de speler zelf
	 * @param opponents de lijst met tegenstanders
	 */
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
	
	/**
	 * kijk wie er de hoogste score heeft.
	 * @param scores een map met daarin iedere speler gelinkt aan zijn puntenaantal
	 * @return de speler met de hoogste score
	 */
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
	
	/**
	 * zet een lijst met moves om in een lijst met playmoves.
	 * @param moves de lijst met moves
	 * @return de lijst met playmoves
	 */
	public List<PlayMove> toPlayMove(List<Move> moves) {
		List<PlayMove> result = new ArrayList<PlayMove>();
		for (Move move : moves) {
			result.add((PlayMove) move);
		}
		return result;
	}
	
	/**
	 * zet een lijst met moves om in een lijst met swapmoves.
	 * @param moves de lijst met moves
	 * @return de lijst met swapmoves
	 */
	public List<SwapMove> toSwapMove(List<Move> moves) {
		List<SwapMove> result = new ArrayList<SwapMove>();
		for (Move move : moves) {
			result.add((SwapMove) move);
		}
		return result;
	}
	
	/**
	 * geeft een foutmelding als je een foutieve zet doorgeeft.
	 * @param b het speelbord
	 * @return de foutieve zet
	 */
	private List<Move> invalidMove(Board b) {
		System.out.println("Invalid Move please try again.");
		return getMove(b);
	}
	
	/**
	 * print een foutmelding van de server.
	 * @param msg de foutmelding
	 */
	public void displayServerMessage(String msg) {
		if (!localGame) {
			System.out.println(msg);
		}
	}
	
	/**
	 * vraagt of je een nieuwe game wil starten.
	 * @return true als je nog een game wil starten.
	 */
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

	/**
	 * print uit dat iemand gekickt is en waarom.
	 * @param player de gekickte speler
	 * @param reason de reden waarom hij gekickt is
	 */
	@Override
	public void displayKick(Player player, String reason) {
		System.out.println("Player " + player.getName() + " has been kicked! Reason: " + reason);
	}

	/**
	 * laat zien wie er gewonnen heeft.
	 * @param de winnaar
	 */
	@Override
	public void displayWinner(Player player) {
		System.out.println("The winner is: " + 
						player.getName() + "! With a score of: " + player.getScore());
	}


	/**
	 * print een foutmelding wanneer die ervoor zorgt dat de connectie wordt verbroken.
	 * @param msg de foutmelding
	 */
	@Override
	public void displayFatalError(String msg) {
		System.out.println("[FATAL ERROR]: " + msg);
	}

	/**
	 * print elke keer het nieuwe boord en de nieuwe scores uit.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals("BOARD")) {
			System.out.println("\n==========================================="
							+ "============================================================\n");
			displayBoard(client.getBoard());
			displayScore(client.getPlayer(), client.getOpponents());
		}		
	}

	/**
	 * leest in wat de speler wil doen.
	 */
	@Override
	public String getCommand() {
		return in.nextLine();
	}

	/**
	 * print uit hoe groot de pot nog is.
	 */
	@Override
	public void displayStackSize(int stackSize) {
		System.out.println("StackSize: " + stackSize);
	}
}
