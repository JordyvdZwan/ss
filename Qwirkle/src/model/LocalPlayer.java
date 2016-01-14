package model;

import java.util.ArrayList;
import java.util.List;

import controller.Connection;

public class LocalPlayer implements Player {
	public String name;
	private int score = 0;
	private Board board = new Board();
	
	public LocalPlayer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}
	
    public int playerScore(ArrayList<PlayMove> move) { 
    	score = score + board.legitMoveScore(move);
    	return score;
    }

	@Override
	public List<Move> determineMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Block> getHand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHand(List<Block> handArg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScore(int scoreArg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String nameArg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNumber(int numberArg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swapHand(List<Move> moves, List<Block> blocks) {
		// TODO Auto-generated method stub
		
	}

}
