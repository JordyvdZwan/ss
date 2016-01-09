package controller;

import model.*;
import view.*;

public class Game {
	private Board board;
	private Player[] players;
	private int turn;
	public static final int MAXPLAYERS = 8;
	
	
	public Game() {
		players = new Player[8];
		Board board = new Board();
		
		
		start();
	}
	
	public void start() {
		boolean notOver = true;
		while (notOver) {
			
		}
	}
	
	public void turn(Player player) {
		
	}
	
	public int longestRow(Player[] players) {
		return 0;
	}
	
	public boolean playerJoin(Player player) {
		boolean joined = false;
		for (int i = 0; i < MAXPLAYERS; i++) {
			if (players[i] == null) {
				players[i] = player;
				joined = true;
			}
		}
		return joined;
	}
	
	

}
