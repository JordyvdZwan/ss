package model;

import java.util.*;

import player.*;
import strategy.*;

public class Board {
	private Block[][] blocks;
	private static final int DIM = 183;
	private static final int MID = 92;
	private ComputerPlayer player = new ComputerPlayer("retard", new RetardedStrategy());
	RetardedStrategy retard = new RetardedStrategy();

	public Board() {
		blocks = new Block[DIM][DIM];
	}
	
	public Board(Board b) {
		blocks = new Block[DIM][DIM];
		for (int x = 0; x < b.getBlock().length; x++) {
			for (int y = 0; y < b.getBlock().length; y++) {
				blocks[x][y] = b.getBlock()[x][y];
			}
		}
	}
	
	public Block[][] getBlock() {
		return blocks;
	}
	
	public String hint(List<Block> hand, int stacksize) {
		return retard.getMove(this, player.getHand(), player, stacksize).toString();
	}
	
	public Board(Block[][] blocks) {
		this.blocks = blocks;
	}
	
	public boolean isOnlyX(List<PlayMove> move) {
		boolean onlyX = true;
		int x = move.get(0).x;
		for (PlayMove moves: move) {
			if (moves.x != x) {
				onlyX = false;
				break;
			}
		}
		return onlyX;
	}
	
	public boolean isOnlyY(List<PlayMove> move) {
		boolean onlyY = true;
		int y = move.get(0).y;
		for (PlayMove moves: move) {
			if (moves.y != y) {
				onlyY = false;
				break;
			}
		}
		return onlyY;
	}
	
	// A move is not legal if the block is placed next to a line it does not belong to.
	public boolean isLegalMove(PlayMove move) {
		return isLegalXRow(move) && isLegalYRow(move) 
						&& !isLonelyStone(move) && isEmptyField(move.x, move.y);
	}

	public boolean allConnected(List<PlayMove> moveslist) {
		boolean result = false;
		int lenght = 0;
		int maxX = moveslist.get(0).x;
		int minX = moveslist.get(0).x;
		int maxY = moveslist.get(0).y;
		int minY = moveslist.get(0).y;
		int size = moveslist.size(); 
		if (isOnlyY(moveslist)) {
			for (PlayMove move : moveslist) {
				if (move.x > maxX) {
					maxX = move.x;
				}
				if (move.x < minX) {
					minX = move.x;
				}
			}
			lenght = maxX - minX + 1;
			for (int i = minX; i < maxX; i++) {
				if (!isEmptyField(i, moveslist.get(0).y)) {
					size++;
				}
			}
			if (lenght == size) {
				result = true;
			}
		} else if (isOnlyX(moveslist)) {
			for (PlayMove move : moveslist) {
				if (move.y > maxY) {
					maxY = move.y;
				}
				if (move.y < minY) {
					minY = move.y;
				}
			}
			lenght = maxY - minY + 1;
			for (int i = minY; i < maxY; i++) {
				if (!isEmptyField(moveslist.get(0).x, i)) {
					size++;
				}
			}
			if (lenght == size) {
				result = true;
			}
		}
		return result;
	}

	public boolean isLegalMoveList(List<PlayMove> moveslist) {
		boolean legal = true;
		Board board = deepCopy();
		List<PlayMove> moves = new ArrayList<PlayMove>();
		moves.addAll(moveslist);
		if (moveslist.size() == 0) {
			legal = false;
		} else {
			if (allConnected(moveslist)) {
				if (isOnlyX(moveslist) || isOnlyY(moveslist)) {
					while (moves.size() > 0) {
						boolean legalmove = false;
						for (int i = 0; i < moves.size(); i++) {
							if (board.isLegalMove(moves.get(i))) {
								board.setField(moves.get(i).x, moves.get(i).y, moves.get(i).block);
								legalmove = true;
								moves.remove(i);
							}
						}
						if (!legalmove) {
							legal = false;
							break;
						}
					}
				} else {
					legal = false;
				}
			} else {
				legal = false;
			}
		} 
		return legal;
	}


	//Checks for each block in the x row if the color of shape of the blocks match the moveblock
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
			if (blocks[move.x + counter][move.y].shape == move.block.shape
								&& blocks[move.x + counter][move.y].color == move.block.color) {
				result = false;
				break;
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
			if (blocks[move.x - counter][move.y].shape == move.block.shape 
								&& blocks[move.x - counter][move.y].color == move.block.color) {
				result = false;
				break;
			}
			if (!colorResult && !shapeResult) {
				result = false;
				break;
			}
			counter++;
		}
		return result;		
	}
	
	//Checks for each block in the x row if the color of shape of the blocks match the moveblock
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
			if (blocks[move.x][move.y + counter].shape == move.block.shape 
								&& blocks[move.x][move.y + counter].color == move.block.color) {
				result = false;
				break;
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
			if (blocks[move.x][move.y - counter].shape == move.block.shape 
								&& blocks[move.x][move.y - counter].color == move.block.color) {
				result = false;
				break;
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
		if (move.x == MID && move.y == MID) {
			islonely = false;
		}
		if (move.y + 1 < DIM && blocks[move.x][move.y + 1] != null) {
			islonely = false;
		}
		if (move.y - 1 > 0 && blocks[move.x][move.y - 1] != null) {
			islonely = false;
		}
		if (move.x + 1 < DIM && blocks[move.x + 1][move.y] != null) {
			islonely = false;
		}
		if (move.x - 1 > 0 && blocks[move.x - 1][move.y] != null) {
			islonely = false;
		}
		return islonely;
	}
	
	public Board deepCopy(Board b) {
		Board board = new Board(b);
		return board;
	}
	
	public Board deepCopy() { 
		Board board = new Board(this);
		return board;
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
		boolean result = false;
		if (getField(x, y) == null) {
			result = true;
		}
		return result;
	}
	
	//indicates when the stack is empty
	public boolean emptyStack(int stackSize) { //TODO
		if (stackSize == 0) {
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
	public boolean gameOver(List<Block> hand, int stackSize) { 
		if (emptyStack(stackSize) && noValidMoves(hand)) {
			return true;
		} else {
			return false;
		}
	}	
	
	//checks if there are no more valid moves
	public boolean noValidMoves(List<Block> hand) { 
		boolean illegal = true;
		if (hand.size() == 0) {
			illegal = true;
		} else {
			for (int i = 0; i < hand.size(); i++) {
				for (int j = 0; j < DIM; j++) {
					for (int k = 0; k < DIM; k++) {
						PlayMove move = new PlayMove(hand.get(i), j, k, new NetworkPlayer());
						if (isLegalMove(move)) {
							illegal = false;
							break;
						}
					}
				}
			}
		}
		return illegal;
	}
	
	//checks if Xrow is empty
	public boolean emptyXRow(int x) {
		boolean empty = true;
		for (int i = 0; i < DIM; i++) {
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
		for (int i = 0; i < DIM; i++) {
			if (!isEmptyField(i, y)) { 
				empty = false;
				break;
			}
		}
		return empty;
	}
	
	public void makeMove(List<PlayMove> moves) { 
		for (PlayMove move : moves) {
			setField(move.x, move.y, move.block);
		}
	} 
	
	//prints out the board
	public String toString() {
		int maxX = maxX();
		int minX = minX();
		int maxY = maxY();
		int minY = minY();
		String colum = "";
		String index = " \\ x";
		String swag = "y \\ ";
		for (int i = minX; i <= maxX; i++) {
			index = index + " " + i + "|";
		}
		for (int i = minX; i <= maxX; i++) {
			swag = swag + "___" + "|";
		}
		for (int i = minY; i <= maxY; i++) {
	    	String row = "";
			for (int j = minX; j <= maxX; j++) {
				if (getField(j, i) != null) {
					row = row + " " + getField(j, i).toString() + "|";
				} else {
					row = row + "  " + "_" + "|";
				}
			}
			colum = colum + String.format("%03d", i) + "|" + row + "\n";
		}
		return index + "\n" + swag + "\n" + colum;
	}
	
	public String toColorString() {
		int maxX = maxX();
		int minX = minX();
		int maxY = maxY();
		int minY = minY();
		String colum = "";
		String index = "\\x ";
		String swag = "y\\ ";
		for (int i = minX; i <= maxX; i++) {
			index = index + " " + i + "|";
		}
		for (int i = minX; i <= maxX; i++) {
			swag = swag + "___" + "|";
		}
		for (int i = minY; i <= maxY; i++) {
	    	String row = "";
			for (int j = minX; j <= maxX; j++) {
				if (getField(j, i) != null) {
					row = row + " " + getField(j, i).toColorString() + "|";
				} else {
					row = row + "  " + "_" + "|";
				}
			}
			colum = colum + i + "|" + row + "\n";
		}
		return index + "\n" + swag + "\n" + colum;
    }
    
    public int maxX() {
    	int maxX = 0;
    	for (int i = 1; i < MID; i++) {
    		if (emptyXRow(MID + i)) {
    			maxX = MID + i;
    			break;
    		}
    	}
    	return maxX;
    }
    	
    public int minX() {
        int minX = 0;
        for (int i = 1; i < MID; i++) {
        	if (emptyXRow(MID - i)) {
        		minX = MID - i;
        		break;
        	}
        }
        return minX;
    }

    	
    public int maxY() {
    	int maxY = 0;
    	for (int i = 1; i < MID; i++) {
    		if (emptyYRow(MID + i)) {
    			maxY = MID + i;
    			break;
    		}
    	}
    	return maxY;
    }

    public int minY() {
    	int minY = 0;
    	for (int i = 1; i < MID; i++) {
    		if (emptyYRow(MID - i)) {
    			minY = MID - i;
    			break;
    		}
    	}
    	return minY;
    }

}
