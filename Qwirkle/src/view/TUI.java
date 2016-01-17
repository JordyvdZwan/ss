package view;

import java.awt.List;
import java.net.InetAddress;
import java.util.ArrayList;

import model.*;
import player.Player;

public class TUI implements UI {
	private static final int DIM = 182;
	private int thinkTime;
	private Board board;
	private Player player;
	private int Port;

	public TUI() {
		//TODO things not stored in view
		
	}
	
	public String getCommand() {
		// TODO
		return null;
	}
	
	public int getAIThinkTime() {
		return thinkTime;
	}
	
	public void errorOccured(String msg) {
		System.out.println(msg);
	}
	
	public String getChoiceServerClient() {
		// TODO
		return null;
	}
	
	public void displayBoard(Board board) { // TODO
		System.out.print(board.toString());
	} 
	
	public void displayScore() {
		System.out.println(player.getScore());
	}
	
	public List<Move> getMove() {
		// TODO
		return null;
	}
	
	public InetAddress getHost() {
		// TODO
		return null;
	}
	
	public int getPort() {
		return Port;
		
	}
	
	public String getUserName() {
		return player.getName();
	}
	
    public ArrayList<String> handToString(ArrayList<Block> hand) {
    	ArrayList<String> handstring = null;
    	for (int i = 0; i < hand.size(); i++) {
    		handstring.add(hand.get(i).toString());
    	}
    	return handstring;
    }
}
