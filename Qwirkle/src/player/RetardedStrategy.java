package player;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class RetardedStrategy {
	private Player player;
	private Board board;
	
	public RetardedStrategy(Board board, Player player) {
		this.board = board;
		this.player = player;
	}
	
	public List<Move> determineMove() {
		List<Move> move = new ArrayList<Move>();
		if (retardedStrategyPlay().size() == 0) {
			move.addAll(retardedStrategySwap());
		} else {
			move.addAll(retardedStrategyPlay());
		}
		return move;
	}
	
	public List<PlayMove> retardedStrategyPlay() {
		Board moveboard = board.deepCopy();
		List<Block> hand = player.getHand();
		List<PlayMove> moves = new ArrayList<PlayMove>();
		PlayMove move = null;
		if (moveboard.isEmptyField(92, 92)) {
			move = new PlayMove(hand.get(0), 92, 92, player);
			moves.add(move);
			moveboard.setField(92, 92, hand.get(0));
			hand.remove(0);
		}
		for (Block block : hand) {
			for (int i = moveboard.minX(); i <= moveboard.maxX(); i++) {
				for (int j = moveboard.minY(); j <= moveboard.maxY(); j++) {
					move = new PlayMove(block, i, j, player);
					if (moveboard.isLegalMove(move)) {
						moves.add(move);
						if (!moveboard.isLegalMoveList(moves)) {
							moves.remove(move);
						} else if (moveboard.isLegalMoveList(moves)) {
							moveboard.setField(i, j, block);
							hand.remove(block);
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
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		double j = Math.random() * 6;
		for(int i = 0; i < j; i++) {
			move = new SwapMove(hand.get(i), player);
			swapmove.add(move);
			
		}
		return swapmove;
	}

}
