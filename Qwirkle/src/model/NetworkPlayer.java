package model;

import java.util.ArrayList;
import java.util.List;

import controller.Connection;

public class NetworkPlayer implements Player{
	private int score;
	private int number;
	private String name;
	private Connection connection;
	private List<Block> hand;
	
	public NetworkPlayer() {
		hand = new ArrayList<Block>();
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
