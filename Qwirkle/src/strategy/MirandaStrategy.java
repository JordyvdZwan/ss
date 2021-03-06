package strategy;

import java.util.ArrayList;
import java.util.List;

import model.Block;
import model.Board;
import model.Move;
import model.PlayMove;
import model.SwapMove;
import player.Player;

public class MirandaStrategy implements Strategy {
	/**
	 * het aantal extra plaatsen die rondom een put worden bekeken.
	 */
	public static final int EDGE = 1;
	
	/**
	 * deze methode krijgt al een zet mee, 
	 * hij kijkt dan rondom die zet of daar nog meer stenen bij kunnen.
	 * @param b het bord
	 * @param hand de hand
	 * @param player de speler
	 * @param stackSize de grootte van de pot
	 * @param moves een lijst met verschillende zetten.
	 * @param move een zet
	 * @param end de tijd die de AI maximaal mag denken
	 */
	private List<List<PlayMove>> getMove(Board b, List<Block> hand, 
									Player player, int stackSize, List<List<PlayMove>> moves, 
																			List<PlayMove> move) {
		List<List<PlayMove>> allPossibleMoves = new ArrayList<List<PlayMove>>();
		allPossibleMoves.addAll(moves);
		for (Block block : hand) {
			for (int x = b.minX() - EDGE; x < b.maxX() + EDGE; x++) {
				for (int y = b.minY() - EDGE; y < b.maxY() + EDGE; y++) {
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
						allPossibleMoves = getMove(b, localHand, player, stackSize, 
																	allPossibleMoves, movelist);
					}
				}
			}
		}
		return allPossibleMoves;
	}
	
	/**
	 * berekent de zet die de meeste punten oplevert.
	 * dit gebeurt door eerst een lijst te maken met alle mogelijke zetten.
	 * hier wordt dan de zet uitgehaald met die de meeste punten geeft.
	 * @param b het bord
	 * @param player de speler
	 * @param stackSize de grootte van de pot
	 * @param thinkTime de maximale tijd dat de AI mag denken.
	 * @return de zet die de meeste punten oplevert
	 */
	@Override
	public List<Move> getMove(Board b, Player player, int stackSize, int thinkTime) {
		List<List<PlayMove>> allPossibleMoves = new ArrayList<List<PlayMove>>();
		List<Block> hand = player.getHand();
		for (Block block : hand) {
			for (int x = b.minX(); x < b.maxX(); x++) {
				for (int y = b.minY(); y < b.maxY(); y++) {
					PlayMove move = new PlayMove(block, x, y, player);
					if (b.isLegalMove(move)) {
						List<Block> localHand = new ArrayList<Block>();
						localHand.addAll(hand);
						localHand.remove(block);
						List<PlayMove> movelist = new ArrayList<PlayMove>();
						movelist.add(move);
						allPossibleMoves.add(movelist);
						allPossibleMoves = getMove(b, localHand, player, stackSize, 
																		allPossibleMoves, movelist);
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
	
	/**
	 * kijkt hoe groot de pot is,
	 * als de pot kleiner is dan 6 dan ruilt hij het aantal stenen dat nog in de pot zit.
	 * anders ruilt hij een random aantal tussen 1 en 6.
	 * @param hand de hand
	 * @param player de speler
	 * @param board het bord
	 * @param stackSize de grootte van de pot
	 * @return de stenen die worden geruild.
	 */
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
