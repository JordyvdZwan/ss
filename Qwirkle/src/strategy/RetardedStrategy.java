package strategy;
import java.util.ArrayList;
import java.util.List;

import model.*;
import player.Player;

public class RetardedStrategy implements Strategy {
	
	public RetardedStrategy() {
	}
	
//	public List<Move> determineMove() {
//		List<Move> move = new ArrayList<Move>();
//		if (retardedStrategyPlay().size() == 0) {
//			move.addAll(retardedStrategySwap());
//		} else {
//			move.addAll(retardedStrategyPlay());
//		}
//		return move;
//	}
	
	public List<Move> getMove(Board board, List<Block> hand, Player player) {
		List<PlayMove> playmove = new ArrayList<PlayMove>();
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		List<Move> result = new ArrayList<Move>();
		playmove = retardedStrategyPlay(board, hand, player);
		if (playmove.size() == 0) {
			swapmove = retardedStrategySwap(hand, player);
			for (SwapMove move : swapmove) {
				result.add(move);
			}
		} else {
			for (PlayMove move : playmove) {
				result.add(move);
			}
		}
		return result;	
	}
	
	public List<PlayMove> retardedStrategyPlay(Board board, List<Block> hand, Player player) {
		Board moveboard = board.deepCopy();
		List<Block> movehand = new ArrayList<Block>();
		movehand.addAll(hand);
		List<PlayMove> moves = new ArrayList<PlayMove>();
		PlayMove move = null;
		if (movehand.size() > 0 ) {
			if (moveboard.isEmptyField(92, 92)) {
				move = new PlayMove(movehand.get(0), 92, 92, player);
				moves.add(move);
				moveboard.setField(92, 92, movehand.get(0));
				movehand.remove(0);
			}
//			for (Block block : movehand) {
//				for (int i = moveboard.minX(); i <= moveboard.maxX(); i++) {
//					for (int j = moveboard.minY(); j <= moveboard.maxY(); j++) {
//						move = new PlayMove(block, i, j, player);
//						System.out.println(move.toString());
//						if (moveboard.isLegalMove(move)) {
//							moves.add(move);
////							System.out.println(moves.toString());
//							boolean valid = moveboard.isLegalMoveList(moves);
//							if (!valid) {
//								moves.remove(move);
//								System.out.println(moves.toString());
//							} else if (valid) {
//								moveboard.setField(i, j, block);
//								movehand.remove(block);
//								System.out.println(moves.toString());
////								break;
//							}
//						}
//					}
//				}
//			}
			for (int k = 0; k < movehand.size(); k++) {
				for (int i = moveboard.minX(); i <= moveboard.maxX(); i++) {
					for (int j = moveboard.minY(); j <= moveboard.maxY(); j++) {
						move = new PlayMove(movehand.get(k), i, j, player);
						if (moveboard.isLegalMove(move)) {
							moves.add(move);
							if (!board.isLegalMoveList(moves)) {
								moves.remove(move);
							} else {
								moveboard.setField(i, j, movehand.get(k));
							}
						}
					}
				}
			}
		}
		return moves;
	}
	
	public List<SwapMove> retardedStrategySwap(List<Block> hand, Player player) {
		List<Block> swaphand = new ArrayList<Block>();
		swaphand.addAll(hand);
		SwapMove move = null;
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		double j = Math.random() * 6;
		for(int i = 0; i < j; i++) {
			move = new SwapMove(swaphand.get(i), player);
			swapmove.add(move);
			
		}
		return swapmove;
	}

}
