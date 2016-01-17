package player;
import java.util.List;

import model.*;

public class StupidStrategy {
	private Board board;
	private Player player;
	
	public int stupidStrategy() {
		int score = 0;
		List<PlayMove> playmove = null;
		List<SwapMove> swapmove = null;
		if (stupidStrategyPlay().size() == 0) {
			swapmove = stupidStrategySwap();
			board.makeSwap(swapmove);
		} else {
			playmove = stupidStrategyPlay();
//			board.makeMove(playmove);
			score = board.makeMove(playmove);
		}
		return score;
	}
	
	public List<PlayMove> stupidStrategyPlay() {
		Board moveboard = board.deepCopy();
		List<Block> hand = player.getHand();
		List<PlayMove> moves = null;
		PlayMove move = null;
		for (Block block : hand) {
			for (int i = moveboard.minX(); i <= moveboard.maxX(); i++) {
				for (int j = moveboard.minY(); j <= moveboard.maxY(); j++) {
					move = new PlayMove(block, i, j, player);
					if (moveboard.isLegalMove(move)) {
						moves.add(move);
						hand.remove(block);
						if (!moveboard.isLegalMoveList(moves)) {
							moves.remove(move);
							hand.add(block);
						} else if (moveboard.isLegalMoveList(moves)) {
							moveboard.setField(i, j, block);
						}
					}
					
				}
			}
		}
		return moves;
	}
	
	public List<SwapMove> stupidStrategySwap() {
		List<Block> hand = player.getHand();
		SwapMove move = null;
		List<SwapMove> swapmove = null;
		double j = Math.random() * 6;
		for(int i = 0; i < j; i++) {
			move = new SwapMove(hand.get(1), player);
			swapmove.add(move);
			
		}
		return swapmove;
	}

}
