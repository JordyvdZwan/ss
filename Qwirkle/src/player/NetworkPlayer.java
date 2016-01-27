package player;

import java.util.ArrayList;
import java.util.List;

import controller.Connection;
import model.Block;
import model.Move;

public class NetworkPlayer implements Player {
	private int score;
	private int number;
	private String name;
	private Connection connection;
	private List<Block> hand;
	
	public NetworkPlayer() {
		hand = new ArrayList<Block>();
	}
	
	public NetworkPlayer(String name, int number) {
		this.number = number;
		this.name = name;
		hand = new ArrayList<Block>();
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
