package model;

import java.util.*;

import player.NetworkPlayer;

public class Board {
	private List<Block> hand;
	private Block[][] blocks;
	private final static int DIM = 183;
	private final static int MID = 92;
	
	public static final int boardSize = (93*2)+1;
	
	public Board() {
		blocks = new Block[boardSize][boardSize];
		hand = new ArrayList<Block>();
	}
	
	public Board(Block[][] blocks) {
		this.blocks = blocks;
	}
	
	public boolean isOnlyX(List<PlayMove> move) {
		boolean onlyX = false;
		if (move.size() == 1) {
			onlyX = true;
		} else if ()
		return onlyX;
	}
	
	// A move is not legal if the block is placed next to a line it does not belong to.
	public boolean isLegalMove(PlayMove move) {
		return isLegalXRow(move) && isLegalYRow(move) && !isLonelyStone(move) && isEmptyField(move.x, move.y);
	}
	
	public boolean isLegalMoveList(List<PlayMove> moveslist) {
		Board board = deepCopy(this);
		List<PlayMove> moves = moveslist;
		boolean legal = true;
		while(moves.size() > 0) {
			int j = 0;
			for(int i = 0; i < moves.size(); i++) {
				if(isLegalMove(moves.get(i))) {
					board.setField(moves.get(i).x, moves.get(i).y, moves.get(i).block);
					j = 1;
					moves.remove(i);
					}
				}
			if(j == 0) {
				legal = false;
				break;
			}
		}
		return legal;
	}
	
	
	//Checks for each block in the x row if the color of shape of the blocks match the moveblock !!!!!! not final
	public boolean isLegalXRow(PlayMove move) {
		boolean result = true;
		int counter = 1;
		boolean shapeResult = true;
		boolean colorResult = true;
		while (move.x + counter < DIM && blocks[move.x + counter][move.y] != null) {
			if (blocks[move.x + counter][move.y].color != move.block.color) {
				colorResult = false;
			}
			if (blocks[move.x + counter][move.y].shape != move.block.shape) {
				shapeResult = false;
			}
			if (!colorResult && !shapeResult) {
				result = false;
				break;
			}
			counter++;
		}
		counter = 1;
		while (move.x - counter > 0 && result && blocks[move.x - counter][move.y] != null) {
			if (blocks[move.x - counter][move.y].color != move.block.color) {
				colorResult = false;
			}
			if (blocks[move.x - counter][move.y].shape != move.block.shape) {
				shapeResult = false;
			}
			if (!colorResult && !shapeResult) {
				result = false;
				break;
			}
			counter++;
		}
		return result;		
	}
	
	//Checks for each block in the x row if the color of shape of the blocks match the moveblock !!!!!! not final
	public boolean isLegalYRow(PlayMove move) {
		boolean result = true;
		int counter = 1;
		boolean shapeResult = true;
		boolean colorResult = true;
		while (move.y + counter < DIM && blocks[move.x][move.y + counter] != null) {
			if (blocks[move.x][move.y + counter].color != move.block.color) {
				colorResult = false;
			}
			if (blocks[move.x][move.y + counter].shape != move.block.shape) {
				shapeResult = false;
			}
			if (!colorResult && !shapeResult) {
				result = false;
				break;
			}
			counter++;
		}
		counter = 1;
		while (move.y - counter > 0 && result && blocks[move.x][move.y - counter] != null) {
			if (blocks[move.x][move.y - counter].color != move.block.color) {
				colorResult = false;
			}
			if (blocks[move.x][move.y - counter].shape != move.block.shape) {
				shapeResult = false;
			}
			if (!colorResult && !shapeResult) {
				result = false;
				break;
			}
			counter++;
		}
		return result;		
	}
	
	//checks if stone has no other stone surrounding him
	public boolean isLonelyStone(PlayMove move) {
		boolean islonely = true;
		if(move.y + 1 < DIM && blocks[move.x][move.y + 1] != null) {
			islonely = false;
		}
		if(move.y - 1 > 0 && blocks[move.x][move.y - 1] != null) {
			islonely = false;
		}
		if(move.x + 1 < DIM && blocks[move.x + 1][move.y] != null) {
			islonely = false;
		}
		if(move.x - 1 > 0 && blocks[move.x - 1][move.y] != null) {
			islonely = false;
		}
		return islonely;
	}
	
	public Board deepCopy(Board b) {
		return new Board(b.blocks);
	}
	
	public Board deepCopy() {
		return new Board(blocks);
	}
	
	//puts a stone on the board
	public void setField(int x, int y, Block block) {
		blocks[x][y] = block;
	}
	
	public void emptyField(int x, int y) {
		blocks[x][y] = null;
	}
	
	//shows the stone on the board
    public Block getField(int x, int y) {
        return blocks[x][y];
    }
    
    //says if a field is empty
    public Boolean isEmptyField(int x, int y) {
    	return getField(x, y) == null;
    }
    
    //gives the number of stones left in the stack
    public int countStack() { //TODO fix it numberofplayers moet variabel worden tijdens kick moet worden geüpdate.
    	int NumberOfStones = 0;
    	int NumberOfPlayers = 4;
    	int stack = 108;
    	for(int i = 0; i <= DIM; i++) {
    		for(int j = 0; j <= DIM; j++) {
    			if(!isEmptyField(i,j)) {
    				NumberOfStones++;
    			}
    		}
    		
    	}
    	stack = stack - NumberOfStones - (6 * NumberOfPlayers);
    	if (stack < 0) {
    		stack = 0;
    	}
    	return stack;
    }
    
    //indicates when the stack is empty
    public boolean emptyStack() {
    	if(countStack() == 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    //calculates the score of 1 move
    public int moveScore(PlayMove move) { 
    	int score = 0;
    	score = xScore(move) + yScore(move);
    	if (score == 0) {
    		score++;
    	}
    	return score;

    }
    
    public int xScore(PlayMove move) {
    	int scorex = 0;
    	int counter = 1;
		while (blocks[move.x + counter][move.y] != null) {
			scorex++;
			counter++;
		}
		counter = 1;
		while (blocks[move.x - counter][move.y] != null) {
			scorex++;
			counter++;
		}
		if (scorex == 5) {
			scorex = scorex + 6;
		}
		if (scorex > 0) {
			scorex++;
		}
		return scorex;
    }
    
    public int yScore(PlayMove move) {
    	int counter = 1;
    	int scorey = 0;
		while (blocks[move.x][move.y + counter] != null) {
			scorey++;
			counter++;
		}
		counter = 1;
		while (blocks[move.x][move.y - counter] != null) {
			scorey++;
			counter++;
		}
		if (scorey == 5) {
			scorey = scorey + 6;
		}
		if (scorey > 0) {
			scorey++;
		}
		return scorey;
    }
    
    public int legitMoveScore(List<PlayMove> move) {
    	int result = 0;
    	if (move.size() > 1) {
    		if (move.get(0).x == move.get(1).x) {
    			for (int i = 0; i < move.size() - 1; i++) {
    				setField(move.get(i).x, move.get(i).y, move.get(i).block);
    			}
    			result = moveScore(move.get(move.size() - 1));
    			for (int k = 0; k < move.size() - 1; k++) {
    				result = result + xScore(move.get(k));
    			}
    		}
    		if (move.get(0).y == move.get(1).y) {
    			for (int i = 0; i < move.size() - 1; i++) {
    				setField(move.get(i).x, move.get(i).y, move.get(i).block);
    			}
    			result = moveScore(move.get(move.size() - 1));
    			for (int k = 0; k < move.size() - 1; k++) {
    				result = result + yScore(move.get(k));
    			}
    		}
    		for (int j = 0; j < move.size() - 1; j++) {
    			emptyField(move.get(j).x, move.get(j).y);
    		}	
    	} else {
    		result = moveScore(move.get(0));
    	}
    	return result;

    } 
	
    public int index(int x, int y) {
    	return (x * DIM) + y;
    }
    
    public boolean isField(int x, int y) {
    	return 0 < x && x < DIM && 0 < y && y < DIM;
    	
    }
    
    //is true if the game is over
    public boolean gameOver() { 
    	if (emptyStack() && noValidMoves()) {
    		return true;
    	} else {
    		return false;
    	}
    }	
    
    //checks if there are no more valid moves
    public boolean noValidMoves() { 
    	boolean illegal = true;
    	for(int i = 0; i < hand.size(); i++) {
    		for(int j = 0; j < DIM; j++) {
    			for(int k = 0; k < DIM; k++) {
    				PlayMove move = new PlayMove(hand.get(i), j, k, new NetworkPlayer()); //TODO ?
    				if (isLegalMove(move)) {
    					illegal = false;
    					break;
    				}
    			}
    		}
    	}
    	return illegal;
    }
    
    public void addToHand(Block block) {
    	hand.add(block);
    }
    
    public void removeFromHand(Block block) {
    	hand.remove(block);
    }
    
    //checks if Xrow is empty
    public boolean emptyXRow(int x) {
    	boolean empty = true;
    	for(int i = 0; i < DIM; i++) {
    		if (!isEmptyField(x, i)) {
    			empty = false;
    			break;
    		}
    	}
    	return empty;
    }
    
    //checks if Yrow is empty
    public boolean emptyYRow(int y) {
    	boolean empty = true;
    	for(int i = 0; i < DIM; i++) {
    		if (!isEmptyField(i, y)) { 
    			empty = false;
    			break;
    		}
    	}
    	return empty;
    }
    
    //sets the move on the board, gives the player points and returns the point for this particular move 
    public void makeMove(List<PlayMove> move) { 
    	for (int i = 0; i < move.size(); i ++) {
    		if (isLegalMove(move.get(i))) {
    			legitMoveScore(move);
    			setField(move.get(i).x, move.get(i).y, move.get(i).block);
    			removeFromHand(move.get(i).block);
    		}
    	} 
    }
    
    public void makeSwap(List<PlayMove> move) {
    	for (int i = 0; i < move.size(); i ++) {
    		removeFromHand(move.get(i).block);
    	}
    }
    
    //prints out the board
    public String toString(Board board) {
    	String row = "";
    	String colum = "";
    	String index = "";
    	for (int i = 0; i < DIM; i++) {
    		index = index + " " + i;
    	}
    	for (int i = 0; i < DIM; i++) {
    		for (int j = 0; j < DIM; j++) {
    			if(board.getField(i,j) != null) {
    				row = row + (board.getField(i,j).toString());
    			} else {
        			row = row + "_";
    			} 
    		}
    		colum = i + " " + row + "/n";
    	}
    	return index + colum;
    }
    
}
