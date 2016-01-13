package view;

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
	
	public String getHost() {
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
    			row = row + blockToString(board.getField(i,j));
    			}
    		}
    		colum = i + row + "/n";
    	}
    	return index + colum;
    }
    
    public ArrayList<String> handToString(ArrayList<Block> hand) {
    	ArrayList<String> handstring = null;
    	for (int i = 0; i < hand.size(); i++) {
    		handstring.add(blockToString(hand.get(i)));
    	}
    	return handstring;
    }
    
    //prints out a block
    public static String blockToString(Block block) {
    	char color = 'E';
    	char shape = 'E';
    	if (block.color.equals(Color.BLUE)) {
    		color = 'B';
    	}
    	if (block.color.equals(Color.ORANGE)) {
    		color = 'O';
    	}
    	if (block.color.equals(Color.PURPLE)) {
    		color = 'P';
    	}
    	if (block.color.equals(Color.RED)) {
    		color = 'R';
    	}
    	if (block.color.equals(Color.YELLOW)) {
    		color = 'Y';
    	}
    	if (block.color.equals(Color.GREEN)) {
    		color = 'G';
    	}
    	if (block.shape.equals(Shape.CIRCLE)) {
    		shape = 'o';
    	}
    	if (block.shape.equals(Shape.STAR)) {
    		shape = '*';
    	}
    	if (block.shape.equals(Shape.DIAMOND)) {
    		shape = 'd';
    	}
    	if (block.shape.equals(Shape.SQUARE)) {
    		shape = 's';
    	}
    	if (block.shape.equals(Shape.CROSS)) {
    		shape = 'x';
    	}
    	if (block.shape.equals(Shape.CLOVER)) {
    		shape = 'c';
    	}
    	return color + "" + shape;
    }
	

}
