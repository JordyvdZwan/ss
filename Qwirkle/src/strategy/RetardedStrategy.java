package strategy;
import java.util.ArrayList;
import java.util.List;

import model.*;
import player.Player;
import controller.*;

public class RetardedStrategy implements Strategy {
	
	public RetardedStrategy() {
	}
	
	public List<Move> getMove(Board board, List<Block> hand, Player player) {
		List<PlayMove> playmove = new ArrayList<PlayMove>();
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		List<Move> result = new ArrayList<Move>();
		playmove = retardedStrategyPlay(board, hand, player);
		if (playmove.size() == 0) {
			swapmove = retardedStrategySwap(hand, player, board);
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
//						if (moveboard.isLegalMove(move)) {
//							moves.add(move);
//							if (!moveboard.isLegalMoveList(moves)) {
//								moves.remove(move);
//							} else {
//								moveboard.setField(i, j, block);
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
	
	public List<SwapMove>  retardedStrategySwap (List<Block> hand, Player player, Board board) {
		List<Block> swaphand = new ArrayList<Block>();
		swaphand.addAll(hand);
		SwapMove move = null;
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		if (//TODO > 6) {
			double j = Math.random() * 6;
			for(int i = 0; i < j; i++) {
				move = new SwapMove(swaphand.get(i), player);
				swapmove.add(move);
			} 
		} else {
			int j = board.countStack();
			for(int i = 0; i < j; i++) {
				move = new SwapMove(swaphand.get(i), player);
				swapmove.add(move);
			}
		}
		return swapmove;
	}

}
