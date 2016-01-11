package model;

import java.util.*;

public class Board {
	private List<Block> hand;
	private Block[][] blocks;
	private List<Block> stack;
	private final static int DIM = 183;
	
	public static final int boardSize = (93*2)+1;
	
	public Board() {
		blocks = new Block[boardSize][boardSize];
		stack = new ArrayList<Block>();
		hand = new ArrayList<Block>();
	}
	
	public Board(Block[][] blocks, List<Block> stack) {
		this.blocks = blocks;
		this.stack = stack;
	}
	
	// A move is not legal if the block is placed next to a line it does not belong to.
	public boolean isLegalMove(PlayMove move) {
		return isLegalXRow(move) && isLegalYRow(move);
	}
	
	
	//Checks for each block in the x row if the color of shape of the blocks match the moveblock !!!!!! not final
	public boolean isLegalXRow(PlayMove move) {
		boolean result = true;
		int counter = 1;
		boolean shapeResult = true;
		boolean colorResult = true;
		while (blocks[move.x + counter][move.y] != null) {
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
		while (result && blocks[move.x - counter][move.y] != null) {
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
		while (blocks[move.x][move.y + counter] != null) {
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
		while (result && blocks[move.x][move.y - counter] != null) {
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
	
	public Board deepCopy(Board b) {
		return new Board(b.blocks, b.stack);
	}
	
	public Board deepCopy() {
		return new Board(blocks, stack);
	}
	
	public void setField(int x, int y, Block block) {
		blocks[x][y] = block;
	}
	
    public Block getField(int x, int y) {
        return blocks[x][y];
    }
    
    public Boolean isEmptyField(int x, int y) {
    	return getField(x, y) == null;
    }
    
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
    
    public boolean emptyStack() {
    	if(countStack() <= 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public int moveScore(PlayMove move) { 
		int counter = 1;
		int scorex = 0;
		int scorey = 0;
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
		counter = 1;
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
		if (scorex > 0 && scorey > 0) {
			scorex++;
		}
    	return scorex + scorey + 1;
    }
	
    public int index(int x, int y) {
    	return (x * DIM) + y;
    }
    
    public boolean isField(int x, int y) {
    	return 0 < x && x < DIM && 0 < y && y < DIM;
    	
    }
    
    public boolean gameOver() { 
    	if (emptyStack() && noValidMoves()) {
    		return true;
    	} else {
    		return false;
    	}
    }	
    
    public boolean noValidMoves() { 
    	boolean illegal = true;
    	for(int i = 0; i < hand.size(); i++) {
    		for(int j = 0; j < DIM; j++) {
    			for(int k = 0; k < DIM; k++) {
    				PlayMove move = new PlayMove(hand.get(i), j, k);
    				if (isLegalMove(move)) {
    					illegal = false;
    					break;
    				}
    			}
    		}
    	}
    	return illegal;
    }
}
