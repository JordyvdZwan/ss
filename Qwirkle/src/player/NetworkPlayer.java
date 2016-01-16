package player;

import java.util.ArrayList;
import java.util.List;

import controller.Connection;
import model.Block;
import model.Move;

public class NetworkPlayer implements Player{
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
	}
	
	public void swapHand(List<Move> moves, List<Block> blocks) {
		for (Move move : moves) {
			hand.remove(move.getBlock());
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

	@Override
	public List<Move> determineMove() {
		return null;
	}


}
