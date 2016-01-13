package model;

import java.util.ArrayList;

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

}
