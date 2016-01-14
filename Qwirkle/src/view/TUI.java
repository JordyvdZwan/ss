package view;

import java.net.InetAddress;
import java.util.ArrayList;

import model.*;

public class TUI implements UI {
	private static final int DIM = 182;

	public TUI() {
		// TODO Auto-generated constructor stub
	}
	
	public String getCommand() {
		// TODO
		return null;
	}
	
	public int getAIThinkTime() {
		// TODO
		return 0;
	}
	
	public void errorOccured(String msg) {
		// TODO
	}
	
	public String getChoiceServerClient() {
		// TODO
		return null;
	}
	
	public void displayBoard(Board board) {
		// TODO
	} 
	
	public void displayScore() {
		// TODO
	}
	
	public Move getMove() {
		// TODO
		return null;
	}
	
	public InetAddress getHost() {
		// TODO
		return null;
	}
	
	public int getPort() {
		// TODO
		return 0;
		
	}
	
	public String getUserName() {
		// TODO
		return null;
	}
	
    //prints out the board
    public String toString(Board board) {
    	// TODO
    	String row = "";
    	String colum = "";
    	String index = "";
    	for (int i = 0; i < DIM; i++) {
    		index = index + i;
    	}
    	for (int i = 0; i < DIM; i++) {
    		for (int j = 0; j < DIM; j++) {
    		if(board.getField(i,j) != null) {
    			row = row + (board.getField(i,j).toString());
    			}
    		}
    		colum = i + row + "/n";
    	}
    	return index + colum;
    }
    
    public ArrayList<String> handToString(ArrayList<Block> hand) {
    	ArrayList<String> handstring = null;
    	for (int i = 0; i < hand.size(); i++) {
    		handstring.add(hand.get(i).toString());
    	}
    	return handstring;
    }
    
   	

}
