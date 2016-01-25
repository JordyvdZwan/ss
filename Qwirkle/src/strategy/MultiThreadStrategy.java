package strategy;

import java.util.ArrayList;
import java.util.List;

import model.Block;
import model.Board;
import model.Move;
import model.PlayMove;
import model.SwapMove;
import player.Player;

public class MultiThreadStrategy extends Thread implements Strategy {
	public static final int EDGE = 1;
	MultiThreadStrategy setter;
	
	
	Board b;
	Player player;
	int stackSize;
	List<Block> hand;
	List<PlayMove> moveList = new ArrayList<PlayMove>();
	
	
	int thinktime = 5000;
	
	public List<PlayMove> playResult = new ArrayList<PlayMove>();
	
	
	public MultiThreadStrategy() {
		setter = this;
		
	}
	
	public MultiThreadStrategy(MultiThreadStrategy strat, int thinktime, List<Block> hand , Player player, int stackSize, Board b, List<PlayMove> movelist) {
		setter = strat;
		this.hand = hand;
		this.player = player;
		this.thinktime = thinktime;
		this.stackSize = stackSize;
		this.b = new Board(b);
		this.moveList = movelist;
}

	public void setResult(List<PlayMove> newRes) {
		synchronized (setter.playResult) {
			if (b.isLegalMoveList(newRes)) {
				if (setter.playResult.isEmpty()) {
					setter.playResult = newRes;
				} else {
					if (b.legitMoveScore(newRes) > b.legitMoveScore(setter.playResult)) {
						setter.playResult = newRes;
					}
				}
			}
		}
	}
	
	public void run() {
		for (Block block : hand) {
			for (int x = b.minX(); x < b.maxX(); x++) {
				for (int y = b.minY(); y < b.maxY(); y++) {
					PlayMove move = new PlayMove(block, x, y, player);
					List<PlayMove> moveList2 = new ArrayList<PlayMove>();
					moveList2.addAll(moveList);
					moveList2.add(move);
					if (!moveList2.isEmpty() && b.isLegalMoveList(moveList2)) {
						List<Block> localHand = new ArrayList<Block>();
						localHand.addAll(hand);
						localHand.remove(block);
						setResult(moveList2);
						MultiThreadStrategy strategy = new MultiThreadStrategy(setter, thinktime, localHand, player, stackSize, b, moveList2);
						strategy.start();
					}
				}
			}
		}
	}
	
	@Override
	public List<Move> getMove(Board b, Player player, int stackSize, int thinkTime) {
		List<PlayMove> moveList = new ArrayList<PlayMove>();
		this.b = new Board(b);
		this.player = player;
		this.stackSize = stackSize;
		hand = player.getHand();
		MultiThreadStrategy strategy = new MultiThreadStrategy(setter, thinktime, hand, player, stackSize, b, moveList);
		strategy.start();
		try {
			this.sleep(thinktime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Move> result = new ArrayList<Move>();
		List<SwapMove> swapresult = new ArrayList<SwapMove>();
		if (b.isLegalMoveList(playResult)) {
			for (PlayMove move : playResult) {
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
