package model;
/**
 * het speelbord en de spelregels voor qwirkle.
 *
 * @author Jordy van der Zwan & Reinier Stribos
 */

import java.util.*;

import player.*;
import strategy.*;

public class Board {
	private Block[][] blocks;
	public static final int DIM = 183;
	public static final int MID = 92;
	public ComputerPlayer player = new ComputerPlayer("AI", new RetardedStrategy());
	RetardedStrategy ai = new RetardedStrategy();

	/**
	 * maakt een nieuw, leeg speelbord.
	 */
	 //@ ensures  (\forall int i; 0 <= i & i < DIM * DIM; this.getBlock(i) == null);
	public Board() {
		blocks = new Block[DIM][DIM];
	}
	
//	public Board(Block[][] blocks) {
//		this.blocks = blocks;
//	}

	
    /**
     * maakt een copy van een speelbord.
     * @param b dit is het bord waarvan hij een kopie maakt.
     */
    /*@ ensures (\forall int i; 0 <= i & i < DIM * DIM;
                               getBlock(i) == this.getBlock(i));
      @*/
	public Board(Board b) {
		blocks = new Block[DIM][DIM];
		for (int x = 0; x < b.getBlock().length; x++) {
			for (int y = 0; y < b.getBlock().length; y++) {
				blocks[x][y] = b.getBlock()[x][y];
			}
		}
	}
	/**
	 * laat zien welke steen op een veld ligt.
	 * @return blocks
	 */
	/* @ requires this.isField(x, y);
	 */
	/*@pure*/
	public Block[][] getBlock() {
		return blocks;
	}
	
	/**
	 * geeft de steen die op de gegeven plek ligt.
	 * @param index de index van de plek
	 * @return Block
	 */
	/*@ requires 0 <= index & index <= DIM * DIM;
	   	ensures \result == this.getBlock()[index / DIM][index % DIM];
	   	*/
	/*@pure*/
	public Block getBlock(int index) {
		Block result = null;
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				if (index == y + (DIM * x)) {
					result = blocks[x][y];
				}
			}
		}
		return result;
	}
	
	/**
	 * laat zien welke steen wordt neergelegd.
	 * @param move
	 * 			de zet die gespeeld wordt
	 * @return de steen die neergelegd wordt
	 */
	/*@ requires getBlock(move) instanceof Block;
	  @ ensures \result instanceof Block;
	  @ ensures \result == getBlock(move);
	 */
	/*@pure*/
	public static Block getBlock(Move move) {
		return move.getBlock();
	}

	/**
	 * geeft de x-coördinaat van een zet.
	 * @param move
	 * 				de zet
	 * @return de x-coördinaat
	 */
	/*@ requires isLegalMove(move);
	  @ ensures \result <= DIM; 
	  @ ensures \result >= 0;
	 */
	/*@pure*/
	public int getX(PlayMove move) {
		return move.x;
	}

	/**
	 * geeft de y-coördinaat van een zet.
	 * @param move
	 * 				de zet
	 * @return de y-coördinaat
	 */
	/*@ requires isLegalMove(move);
	  @ ensures \result <= DIM; 
	  @ ensures \result >= 0;
	 */
	/*@pure*/
	public int getY(PlayMove move) {
		return move.y;
	}
	
	/**
	 * checkt of een zet maar uit 1 rij bestaat.
	 * @param move
	 * 				de zet die gedaan wordt
	 * @return true als alle x-coördinaten gelijk zijn
	 */
	/*@ requires (\forall int x; 0 <= x & x < move.size(); 
					    	getX(move.get(x)) < DIM);
	  @ ensures (\forall int i; 0 <= i & i < move.size(); getX(move.get(0)) == getX(move.get(i)) ==> \result == true);
	 */
	/*@pure*/
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

	/**
	 * checkt of een zet maar uit 1 kolom bestaat.
	 * @param move
	 * 				de steen die neer gelegd wordt
	 * @return true als alle y-coördinaten gelijk zijn
	 */
	/*@ requires (\forall int y; 0 <= y & y < move.size(); 
					    	getX(move.get(y)) < DIM);
	  @ ensures (\forall int i; 0 <= i & i < move.size(); getY(move.get(0)) == getY(move.get(i)) ==> \result == true);
	 */
	/*@pure*/
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

	/**
	 * kijkt of een steen neergelegd mag worden.
	 * @param move
	 * 				de steen die neer gelegd wordt
	 * @return true als de zet aan alle regels voldoet
	 */
	/*@ requires 0 <= getX(move) & getX(move) < DIM;
	  @ requires 0 <= getY(move) & getY(move) < DIM;
	  @ requires getBlock(move) instanceof Block;
	  @ ensures \result == isLegalXRow(move) && isLegalYRow(move) 
						   && !isLonelyStone(move) && isEmptyField(getX(move), getY(move));
	 */
	/*@pure*/
	public boolean isLegalMove(PlayMove move) {
		return isLegalXRow(move) && isLegalYRow(move) 
						&& !isLonelyStone(move) && isEmptyField(move.x, move.y);
	}

	/**
	 * kijkt of alle stenen van een zet verbonden zijn met elkaar.
	 * @param moveslist
	 * 					de zet die gedaan wordt.
	 * @return true als alle stenen verbonden zijn
	 */
	/*@ requires (\forall int i; 0 <= i & i <= moveslist.size();
	  											getX(moveslist.get(i)) <= DIM
	  												& getX(moveslist.get(i)) >= 0);
	  @ requires (\forall int i; 0 <= i & i <= moveslist.size();
	  											getY(moveslist.get(i)) <= DIM
	  												& getY(moveslist.get(i)) >= 0);
	  @ requires (\forall int i; 0 <= i & i <= moveslist.size());
	 */
	/*@pure*/
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

	/**
	 * kijkt of een lijst met zetten volgens de spelregels zijn.
	 * @param moveslist
	 * 				de lijst met zetten
	 * @return true als er volgens de spelregels wordt gespeeld
	 */
	/*@ requires (\forall int i; 0 <= i & i <= moveslist.size();
	  											getX(moveslist.get(i)) <= DIM
	  												& getX(moveslist.get(i)) >= 0);
	  @ requires (\forall int i; 0 <= i & i <= moveslist.size();
	  											getY(moveslist.get(i)) <= DIM
	  												& getY(moveslist.get(i)) >= 0);
	  @ requires (\forall int i; 0 <= i & i <= moveslist.size();
	  											getBlock(moveslist.get(i)) instanceof Block);
	  @ ensures moveslist.size() > 0 ==> allConnected(moveslist) ==> isOnlyX(moveslist) || isOnlyY(moveslist) ==>
	  						(\forall int i; 0 <= i & i < moveslist.size(); this.isLegalMove(moveslist.get(i)) ==> \result == true);
	 */
	/*@pure*/
	public boolean isLegalMoveList(List<PlayMove> moveslist) {
		boolean legal = true;
		Board board = deepCopy();
		List<PlayMove> moves = new ArrayList<PlayMove>();
		moves.addAll(moveslist);
		if (moveslist.size() > 0) {
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
		} else {
			legal = false;
		}
		return legal;
	}


	/**
	 * kijkt of een zet in deze rij gezet mag worden.
	 * @param move
	 * 			de zet die gespeeld wordt
	 * @return true als er volgens de regels gespeeld wordt
	 */
	/*@ requires getBlock(move) instanceof Block;
	  @ requires 0 <= getX(move) & getX(move) <= DIM;
	  @ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ ensures ((\forall int i; (\forall int j; 0 <= j & j < i; 
		this.getField(getX(move) + j, getY(move)) != null && getX(move) + j < DIM);
		(Block.getColor(getField(getX(move) + i, getY(move))) != Block.getColor(getBlock(move))) &&
		!(Block.getShape(getField(getX(move) + i, getY(move))) != Block.getShape(getBlock(move))) ||
		!(Block.getColor(getField(getX(move) + i, getY(move))) != Block.getColor(getBlock(move))) &&
		(Block.getShape(getField(getX(move) + i, getY(move))) != Block.getShape(getBlock(move))) ||
		i == getX(move))) && ((\forall int i; (\forall int j; 0 <= j & j < i; 
		this.getField(getX(move) - j, getY(move)) != null && getX(move) - j > 0);
		(Block.getColor(getField(getX(move) - i, getY(move))) != Block.getColor(getBlock(move))) &&
		!(Block.getShape(getField(getX(move) - i, getY(move))) != Block.getShape(getBlock(move))) ||
		!(Block.getColor(getField(getX(move) - i, getY(move))) != Block.getColor(getBlock(move))) &&
		(Block.getShape(getField(getX(move) - i, getY(move))) != Block.getShape(getBlock(move))) ||
		i == getX(move))) ==> \result == true; 
	 */
	/*@pure*/
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
			if (colorResult && shapeResult) {
				result = false;
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
			if (colorResult && shapeResult) {
				result = false;
			}
			counter++;
		}
		return result;		
	}

	/**
	 * kijkt of een in deze kolom geplaatst mag worden.
	 * @param move
	 * 			de zet die gespeeld wordt
	 * @return true als er volgens de regels gespeeld wordt
	 */
	/*@ requires getBlock(move) instanceof Block;
	  @ requires 0 <= getX(move) & getX(move) <= DIM;
	  @ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ ensures ((\forall int i; (\forall int j; 0 <= j & j < i; 
		this.getField(getX(move), getY(move) + j) != null && getY(move) + j < DIM);
		(Block.getColor(getField(getX(move), getY(move) + i)) != Block.getColor(getBlock(move))) &&
		!(Block.getShape(getField(getX(move), getY(move) + i)) != Block.getShape(getBlock(move))) ||
		!(Block.getColor(getField(getX(move), getY(move) + i)) != Block.getColor(getBlock(move))) &&
		(Block.getShape(getField(getX(move), getY(move) + i)) != Block.getShape(getBlock(move))) ||
		i == getY(move))) && ((\forall int i; (\forall int j; 0 <= j & j < i; 
		this.getField(getX(move), getY(move) - j) != null && getY(move) - j > 0);
		(Block.getColor(getField(getX(move), getY(move) - i)) != Block.getColor(getBlock(move))) &&
		!(Block.getShape(getField(getX(move), getY(move) - i)) != Block.getShape(getBlock(move))) ||
		!(Block.getColor(getField(getX(move), getY(move) - i)) != Block.getColor(getBlock(move))) &&
		(Block.getShape(getField(getX(move), getY(move) - i)) != Block.getShape(getBlock(move))) ||
		i == getY(move))) ==> \result == true; 
	 */
	/*@pure*/
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
			if (colorResult && shapeResult) {
				result = false;
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
			if (colorResult && shapeResult) {
				result = false;
			}
			counter++;
		}
		return result;		
	}

	/**
	 * kijkt of een steen die op het bord wordt gezet aangrenzende stenen heeft.
	 * @param move
	 * 			de zet die gespeeld gaat worden
	 * @return true als de steen geen aangrenzende stenen heeft
	 */
	/*@ requires getBlock(move) instanceof Block;
	  @ requires 0 <= getX(move) & getX(move) <= DIM;
	  @ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ ensures this.getBlock()[getX(move) + 1][getY(move)] != null || 
	 			this.getBlock()[getX(move) - 1][getY(move)] != null || 
	 			this.getBlock()[getX(move)][getY(move) + 1] != null || 
	 			this.getBlock()[getX(move)][getY(move) - 1] != null ==> \result == false;
	 */
	/*@pure*/
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

	/**
     * maakt een copy van een speelbord.
     */
    /*@ ensures (\forall int i; 0 <= i & i < DIM * DIM;
                               getBlock(i) == this.getBlock(i));
      @*/
	public Board deepCopy(Board b) {
		Board board = new Board(b);
		return board;
	}

	/**
     * maakt een copy van een speelbord.
     */
    /*@ ensures (\forall int i; 0 <= i & i < DIM * DIM;
                               getBlock(i) == this.getBlock(i));
      @*/
	public Board deepCopy() { 
		Board board = new Board(this);
		return board;
	}

	/**
	 * zet een steen op het speelbord.
	 * @param x
	 * 			de x-coördinaat van de zet
	 * @param y
	 * 			de y-coördinaat van de zet
	 * @param block
	 * 			de steen die gezet wordt
	 */
	/*@ requires isField(x, y);
	  @ requires block instanceof Block;
	  @ ensures getField(x, y) == block;
	 */
	public void setField(int x, int y, Block block) {
		blocks[x][y] = block;
	}

	/**
	 * haalt een steen van het bord af.
	 * @param x
	 * 			de x-coördinaat
	 * @param y
	 * 			de y-coördinaat
	 */
	/*@ requires isField(x, y);
	  @ ensures getField(x, y) == null;
	 */
	public void emptyField(int x, int y) {
		blocks[x][y] = null;
	}

	/**
	 * laat zien welke steen op een punt ligt.
	 * @param x
	 * 			de x-coördinaat
	 * @param y
	 * 			de y-coördinaat
	 */
	/*@ requires isField(x, y);
	  @ ensures \result instanceof Block || \result == null;
	 */
	/*@pure*/
	public Block getField(int x, int y) {
	    return blocks[x][y];
	}

	/**
	 * laat zien of een veld leeg is.
	 * @param x
	 * 			de x-coördinaat
	 * @param y
	 * 			de y-coördinaat
	 * @return true als het veld leeg is
	 */
	/*@ requires isField(x, y);
	  @ ensures \result == (getField(x, y) == null);
	 */
	/*@pure*/
	public Boolean isEmptyField(int x, int y) {
		return getField(x, y) == null;
	}

	/**
	 * laat zien of de pot leeg is.
	 * @param stackSize
	 * 				de grote van de pot
	 * @return true als de pot leeg is
	 */
	/*@ ensures \result == (stackSize == 0); 
	 */
	/*@pure*/
	public boolean emptyStack(int stackSize) {
		return stackSize == 0;
	}

	/**
	 * berekent de score van 1 steen.
	 * @param move
	 * 			de steen en waar hij wordt neergelegd
	 * @return de score 
	 */
	/*@ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ requires getBlock(move) instanceof Block;
	  @ ensures \result > 0;
	  @ ensures xScore(move) + yScore(move) > 0 ==> \result == xScore(move) + yScore(move);
	  @ ensures xScore(move) + yScore(move) == 0 ==> \result == 1;
	 */
	/*@pure*/
	public int moveScore(PlayMove move) { 
		int score = 0;
		score = xScore(move) + yScore(move);
		if (score == 0) {
			score++;
		}
		return score;
	
	}

	/**
	 * de score van alleen de rij.
	 * @param move
	 * 			de steen die gelegd wordt en waar hij gelegd wordt
	 * @return de score
	 */
	/*@ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ requires getBlock(move) instanceof Block;
	  @ requires isLegalMove(move);
	  @ ensures 0 <= \result & \result <= 12;
	  //TODO 
	/*@pure*/
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
	
	/**
	 * de score van alleen de kolom.
	 * @param move
	 * 			de steen die gelegd wordt en waar hij gelegd wordt
	 * @return de score
	 */
	/*@ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ requires 0 <= getY(move) & getY(move) <= DIM;
	  @ requires getBlock(move) instanceof Block;
	  @ requires isLegalMove(move);
	  @ ensures 0 <= \result & \result <= 12;
	  // TODO
	 */
	/*@pure*/
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

	/**
	 * de score van meerdere stenen tegelijk.
	 * @param move
	 * 			de zet die gespeeld wordt.
	 * @return de score.
	 */
	/*@ requires (\forall int i; 0 <= i & i < move.size();
	  											getX(move.get(i)) <= DIM
	  												& getX(move.get(i)) >= 0);
	  @ requires (\forall int i; 0 <= i & i < move.size();
	  											getY(move.get(i)) <= DIM
	  												& getY(move.get(i)) >= 0);
	  @ requires (\forall int i; 0 <= i & i < move.size();
	  											getBlock(move.get(i)) instanceof Block);
	  @ requires isLegalMoveList(move);
	  @ ensures \result > 0;
	  @ ensures move.size() > 1 ==> this.isOnlyX(move) ==> (\forall int k; 0 <= k & k < move.size() - 1;
	  											\result == moveScore(move.get(move.size() - 1)) + xScore(move.get(k)));
	  @ ensures move.size() > 1 ==> this.isOnlyY(move) ==> (\forall int k; 0 <= k & k < move.size() - 1;
	  											\result == moveScore(move.get(move.size() - 1)) + yScore(move.get(k)));
	  @ ensures move.size() == 1 ==> \result == moveScore(move.get(0));
	 */
	/*@pure*/
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

    /**
     * berekent de index van een veld.
     * @return de index die bij een veld hoort
     */
    //@ requires 0 <= x & x < DIM;
    //@ requires 0 <= y & y < DIM;
    /*@pure*/
	public int index(int x, int y) {
		return (x * DIM) + y;
	}

	/**
     * laat zien of het veld op het speelbord ligt.
     *
     * @return true als het veld op het bord ligt
     */
    //@ ensures \result == (0 <= x && x < DIM && 0 <= y && y < DIM);
    /*@pure*/
	public boolean isField(int x, int y) {
		return 0 < x && x < DIM && 0 < y && y < DIM;
		
	}

	/**
	 * geeft true als een speler niks meer kan.
	 * @param hand
	 * 			alle stenen die in een spelers hand zijn
	 * @param stackSize
	 * 			de grote van de pot
	 * @return true als een speler niks meer kan
	 */
	/*@ ensures \result == this.emptyStack(stackSize) && this.noValidMoves(hand);
	 */
	/*@pure*/
	public boolean gameOver(List<Block> hand, int stackSize) { 
		if (emptyStack(stackSize) && noValidMoves(hand)) {
			return true;
		} else {
			return false;
		}
	}	

	/**
	 * geeft true als een speler niks meer kan.
	 * @param hand
	 * 			alle stenen die in de hand van een speler zijn.
	 * @return true als een speler niks meer kan
	 */
	/*@ requires (\forall int i; 0 <= i & i < hand.size();
	  											hand.get(i) instanceof Block);
	  @ ensures (\forall int i; 0 <= i & i < hand.size();
	  				\forall int j; 0 <= j & j < DIM;
	  					\forall int k; 0 <= k & k < DIM;
	  						this.isLegalMove(new PlayMove(hand.get(i), j, k, new NetworkPlayer())) == true ==> \result == true);
	 */
	/*@pure*/
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

	/**
	 * geeft true als een rij helemaal leeg is.
	 * @param x
	 * 			geeft aan welke rij je bekijkt.
	 * @return true als een rij helemaal leeg is.
	 */
	/*@ requires 0 <= x & x <= DIM;
	  @ ensures (\forall int y; 0 <= y & y < DIM; 
	   					this.getBlock()[x][y] == null ==> \result == true);
	 */
	/*@pure*/
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

	/**
	 * geeft true als een kolom helemaal leeg is.
	 * @param y
	 * 			geeft aan welke rij je bekijkt.
	 * @return true als een rij helemaal leeg is.
	 */
	/*@ requires 0 <= y & y <= DIM;
	  @ ensures (\forall int x; 0 <= x & x < DIM; 
	   					this.getBlock()[x][y] == null ==> \result == true);
	 */
	/*@pure*/
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

	/**
	 * maakt een zet op het speelbord.
	 * @param moves
	 * 			de zet die gespeeld wordt.
	 */
	/*@ requires (\forall int i; 0 <= i & i <= moves.size();
													getX(moves.get(i)) <= DIM
														& getX(moves.get(i)) >= 0);
		@ requires (\forall int i; 0 <= i & i <= moves.size();
													getY(moves.get(i)) <= DIM
														& getY(moves.get(i)) >= 0);
		@ requires (\forall int i; 0 <= i & i <= moves.size();
													getBlock(moves.get(i)) instanceof Block);
		@ requires isLegalMoveList(moves);
		@ ensures (\forall int i; 0 <= i & i <= moves.size();
												getField(getX(moves.get(i)), getY(moves.get(i)))
													== getBlock(moves.get(i)));
	 */
	public void makeMove(List<PlayMove> moves) { 
		for (PlayMove move : moves) {
			setField(move.x, move.y, move.block);
		}
	} 	
	
	/**
	 * geeft de speler een hint.
	 * @param stacksize
	 * 			de grote van de pot
	 * @return een hint
	 */
	/*@ requires player.getHand().size() > 0;
	 */
	public String hint(int stacksize) {
		return ai.getMove(this, player, stacksize, 50000).toString();
	}

	/**
	 * print het huidige bord uit.
	 * @return de huidige speel situatie
	 */
	public String toString() {
		int maxX = maxX();
		int minX = minX();
		int maxY = maxY();
		int minY = minY();
		String colum = "";
		String index = " \\x ";
		String swag = "y \\ ";
		for (int i = minX; i <= maxX; i++) {
			index = index + String.format("%03d", i) + "|";
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

	/**
	 * print het huidige bord uit in kleur.
	 * @return de huidige speel situatie in kleur
	 */
	public String toColorString() {
		int maxX = maxX();
		int minX = minX();
		int maxY = maxY();
		int minY = minY();
		String colum = "";
		String index = " \\x ";
		String swag = "y \\ ";
		for (int i = minX; i <= maxX; i++) {
			index = index + String.format("%03d", i) + "|";
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
			colum = colum + String.format("%03d", i) + "|" + row + "\n";
		}
		return index + "\n" + swag + "\n" + colum;
    }
    
	/*@ ensures \result >= MID + 1;
      @ ensures \result <= MID + MID;
      @*/	
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
    	
    /*@ ensures \result >= 0;
      @ ensures \result <= MID - 1;
      @*/
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

    /*@ ensures \result >= MID + 1;
      @ ensures \result <= MID + MID;
      @*/	
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

    /*@ ensures \result >= 0;
      @ ensures \result <= MID - 1;
      @*/
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
