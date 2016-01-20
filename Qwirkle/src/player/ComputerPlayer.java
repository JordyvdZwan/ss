package player;

import java.util.ArrayList;
import java.util.List;

import controller.Connection;
import model.Block;
import model.Board;
import model.Move;
import strategy.Strategy;
import view.UI;

public class ComputerPlayer implements Player {

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
	 * 
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
	 * 
	 */
	@Override
	public List<Move> determineMove(UI ui, Board board, List<Block> hand) {
		ui.displayBoard(board);
		ui.displayScore(score);
		ui.displayHand(hand);
		return strategy.getMove(board, hand, this);
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
