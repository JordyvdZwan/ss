package player;
import java.util.List;

import model.*;

public class RetardedStrategy {
	private Board board;
	private Player player;
	
	public String retardedStrategy() {
		String result = null;
		List<PlayMove> playmove = null;
		List<SwapMove> swapmove = null;
		if (retardedStrategyPlay().size() == 0) {
			swapmove = retardedStrategySwap();
			result = "SWAP" + swapmove.toString();
		} else {
			playmove = retardedStrategyPlay();
			result = "MOVE" + playmove.toString();
		}
		return result;
	}
	
	public List<PlayMove> retardedStrategyPlay() {
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
	
	public List<SwapMove> retardedStrategySwap() {
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
