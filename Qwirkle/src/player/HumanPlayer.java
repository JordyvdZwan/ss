package player;

import java.util.ArrayList;
import java.util.List;

import controller.Connection;
import model.Block;
import model.Board;
import model.Move;
import view.UI;

public class HumanPlayer implements LocalPlayer {

	private int score;
	private int number;
	private String name;
	private Connection connection;
	private List<Block> hand = new ArrayList<Block>();
	
	public HumanPlayer(String name) {
		this.name = name;
	}
	
	/**
	 * bedenkt een zet.
	 * @param ui de UI
	 * @param board het bord
	 * @param stackSize de grootte van het bord
	 * @param opponents de tegenstanders
	 * @param thinkTime de maximale denktijd
	 * @return  een zet.
	 */
	@Override
	public List<Move> determineMove(UI ui, Board board,	int stackSize, 
													List<Player> opponents, int thinkTime) {
		ui.displayStackSize(stackSize);
		ui.displayHand(hand);
		return ui.getMove(board);
	}
	
	public HumanPlayer(String name, int number) {
		this.number = number;
		this.name = name;
	}
	
	/**
	 * haalt stenen uit je hand.
	 * @param move de stenen die weg moeten
	 */
	public void removeFromHand(Move move) {
		Block deleteBlock = null;
		for (Block block : hand) {
			if (block.color == move.getBlock().color && block.shape == move.getBlock().shape) {
				deleteBlock = block;
			}
		}
		hand.remove(deleteBlock);
	}
	
	/**
	 * haalt eerst de stenen uit de hand van de speler
	 * en geeft hem vervolgens stenen terug.
	 * @param moves de stenen die geruilt worden,
	 * @param blocks de stenen die de speler terug krijgt
	 */
	public void swapHand(List<Move> moves, List<Block> blocks) {
		outer : for (Move move : moves) {
			for (Block block : hand) {
				if (block.color == move.getBlock().color && block.shape == move.getBlock().shape) {
					hand.remove(block);
					continue outer;
				}
			}
		}
		for (Block block : blocks) {
			hand.add(block);
		}
	}
	
	@Override
	public int getScore() {
		return score;
	}
	
	@Override
	public void setScore(int scoreArg) {
		score = scoreArg;
	}
	
	@Override
	public List<Block> getHand() {
		return hand;
	}

	@Override
	public void setHand(List<Block> handArg) {
		hand = handArg;
	}	
	
	@Override
	public void setName(String nameArg) {
		name = nameArg;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setNumber(int numberArg) {
		number = numberArg;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void setConnection(Connection conn) {
		connection = conn;		
	}





}
