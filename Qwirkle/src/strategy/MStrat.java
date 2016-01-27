package strategy;

import java.util.ArrayList;
import java.util.List;

import model.Block;
import model.Board;
import model.Move;
import model.PlayMove;
import model.SwapMove;
import player.Player;

public class MStrat implements Strategy {


	public static final int EDGE = 4;
	List<List<PlayMove>> allPossibleMoves;
	
	private void getMove(Board b, List<Block> hand, 
									Player player, int stackSize, List<List<PlayMove>> moves, 
																	List<PlayMove> move, long end) {
		for (Block block : hand) {
			for (int x = move.get(0).x - EDGE; 
									x < move.get(0).x + EDGE && 
									System.currentTimeMillis() < end; x++) {
				for (int y = move.get(0).y - EDGE; 
									y < move.get(0).y + EDGE && 
									System.currentTimeMillis() < end; y++) {
					PlayMove move2 = new PlayMove(block, x, y, player);
					List<PlayMove> movelist2 = new ArrayList<PlayMove>();
					movelist2.addAll(move);
					movelist2.add(move2);
					if (b.isLegalMoveList(movelist2)) {
						List<Block> localHand = new ArrayList<Block>();
						localHand.addAll(hand);
						localHand.remove(block);
						List<PlayMove> movelist = new ArrayList<PlayMove>();
						movelist.addAll(movelist2);
						allPossibleMoves.add(movelist);
						getMove(b, localHand, player, stackSize, allPossibleMoves, movelist, end);
					}
				}
			}
		}
	}
	
	
	
	@Override
	public List<Move> getMove(Board b, Player player, int stackSize, int thinkTime) {
		allPossibleMoves = new ArrayList<List<PlayMove>>();
		List<Block> hand = player.getHand();
		long start = System.currentTimeMillis();
		long end = start + thinkTime;
		for (Block block : hand) {
			for (int x = b.minX(); x < b.maxX() && System.currentTimeMillis() < end; x++) {
				for (int y = b.minY(); y < b.maxY() && System.currentTimeMillis() < end; y++) {
					PlayMove move = new PlayMove(block, x, y, player);
					if (b.isLegalMove(move)) {
						List<Block> localHand = new ArrayList<Block>();
						localHand.addAll(hand);
						localHand.remove(block);
						List<PlayMove> movelist = new ArrayList<PlayMove>();
						movelist.add(move);
						allPossibleMoves.add(movelist);
						getMove(b, localHand, player, stackSize, allPossibleMoves, movelist, end);
					}
				}
			}
		}
		List<Move> result = new ArrayList<Move>();
		List<PlayMove> playresult = new ArrayList<PlayMove>();
		List<SwapMove> swapresult = new ArrayList<SwapMove>();
		if (!allPossibleMoves.isEmpty()) {
			playresult = allPossibleMoves.get(0);
			for (List<PlayMove> move : allPossibleMoves) {
				if (b.legitMoveScore(move) > b.legitMoveScore(playresult)) {
					playresult = move;
				}
			}
			for (PlayMove move : playresult) {
				result.add(move);
			}
		} else {
			swapresult = retardedStrategySwap(hand, player, b, stackSize);
			for (SwapMove move : swapresult) {
				result.add(move);
			}
		}
		return result;
	}
	
	public List<SwapMove> retardedStrategySwap(List<Block> hand, 
					Player player, Board board, int stackSize) {
		List<Block> swaphand = new ArrayList<Block>();
		swaphand.addAll(hand);
		SwapMove move = null;
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		if (stackSize > 6) {
			double j = Math.random() * 6;
			for (int i = 0; i < j; i++) {
				move = new SwapMove(swaphand.get(i), player);
				swapmove.add(move);
			} 
		} else {
			int j = stackSize;
			for (int i = 0; i < j; i++) {
				move = new SwapMove(swaphand.get(i), player);
				swapmove.add(move);
			}
		}
		return swapmove;
	}
}
