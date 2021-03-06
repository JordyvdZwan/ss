package player;

import java.util.ArrayList;
import java.util.List;

import controller.Connection;
import model.Block;
import model.Board;
import model.Move;
import strategy.Strategy;
import view.UI;

public class ComputerPlayer implements LocalPlayer {

	private int score;
	private int number;
	private String name;
	private Connection connection;
	private List<Block> hand = new ArrayList<Block>();
	private Strategy strategy;
	
	public ComputerPlayer(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public ComputerPlayer(String name, Strategy strategy) {
		this.name = name;
		this.strategy = strategy;
	}
	
	/**
	 * haalt eerst de stenen uit de hand van de speler
	 * en geeft hem vervolgens stenen terug.
	 * @param moves de stenen die geruilt worden,
	 * @param blocks de stenen die de speler terug krijgt
	 */
	public void swapHand(List<Move> moves, List<Block> blocks) {
		outer : for (Move move : moves) {
			for (Block block : getHand()) {
				if (block.color == move.getBlock().color && block.shape == move.getBlock().shape) {
					getHand().remove(block);
					continue outer;
				}
			}
		}
		for (Block block : blocks) {
			getHand().add(block);
		}
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
	 * bedenkt een zet.
	 * @param ui de UI
	 * @param board het bord
	 * @param stackSize de grootte van het bord
	 * @param opponents de tegenstanders
	 * @param thinkTime de maximale denktijd
	 * @return  een zet.
	 */
	@Override
	public List<Move> determineMove(UI ui, Board board, int stackSize, 
													List<Player> opponents, int thinkTime) {
		ui.displayBoard(board);
		ui.displayScore(this, opponents);
		ui.displayHand(hand);
		return strategy.getMove(board, this, stackSize, thinkTime);
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
