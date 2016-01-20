package strategy;

import java.util.ArrayList;
import java.util.List;

import model.*;
import player.Player;

public class SupremeLeaderStrategy implements Strategy {

	@Override
	public List<Move> getMove(Board board, List<Block> hand, Player player) {
		List<PlayMove> playmove = new ArrayList<PlayMove>();
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		List<Move> result = new ArrayList<Move>();
		playmove = supremeLeaderStrategyPlay(board, hand, player);
		if (playmove.size() == 0) {
			swapmove = supremeLeaderStrategySwap(hand, player);
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
	
	public List<PlayMove> supremeLeaderStrategyPlay(Board board, List<Block> hand, Player player) {
		Board moveboard = board.deepCopy();
		List<Block> movehand = new ArrayList<Block>();
		movehand.addAll(hand);
		List<List<PlayMove>> allmoves = new ArrayList<>();
		List<PlayMove> moves = new ArrayList<PlayMove>();
		PlayMove move = null;
		if (movehand.size() > 0 ) {
			if (moveboard.isEmptyField(92, 92)) {
				move = new PlayMove(movehand.get(0), 92, 92, player);
				moves.add(move);
				moveboard.setField(92, 92, movehand.get(0));
				movehand.remove(0);
			}
			for (Block block : movehand) {
				for (int i = moveboard.minX(); i <= moveboard.maxX(); i++) {
					for (int j = moveboard.minY(); j <= moveboard.maxY(); j++) {
						move = new PlayMove(block, i, j, player);
						if (moveboard.isLegalMove(move)) {
							moves.add(move);
							moveboard.setField(i, j, block);
							if (!board.isLegalMoveList(moves)) {
								moves.remove(move);
							} else {
								moveboard.setField(i, j, block);
							}
						}
					}
				}
			}
			allmoves.add(moves);
			while (moves.size() > 0) {
				moves = new ArrayList<PlayMove>();
				for (List<PlayMove> all : allmoves) {
					for (Block block : movehand) {
						for (int i = all.get(0).x; i <= moveboard.maxX(); i++) {
							for (int j = all.get(0).y; j <= moveboard.maxY(); j++) {
								move = new PlayMove(block, i, j, player);
								if (moveboard.isLegalMove(move)) {
									moves.add(move);
									moveboard.setField(i, j, block);
									if (!board.isLegalMoveList(moves)) {
										moves.remove(move);
									} else {
										moveboard.setField(i, j, block);
									}
								}
							}
						}
					}
					allmoves.add(moves);
				}
			}
		}
		Board testboard = board.deepCopy();
		for (int i = 0; i < allmoves.size() - 1; i++) {
			if (testboard.legitMoveScore(allmoves.get(i)) > testboard.legitMoveScore(allmoves.get(i - 1))) {
				allmoves.remove(i - 1);
			} else {
				allmoves.remove(i);
			}
		}
		moves = allmoves.get(0);
		return moves;
	}
	
	public List<SwapMove>  supremeLeaderStrategySwap (List<Block> hand, Player player) {
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
