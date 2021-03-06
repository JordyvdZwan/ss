package strategy;

import java.util.ArrayList;
import java.util.List;
import model.*;
import player.Player;

public class RetardedStrategy implements Strategy {
	
	public RetardedStrategy() {
	}
	
	/**
	 * deze methode bepaald wat de AI zijn zet wordt.
	 * hij kijkt eerst of hij iets kan zetten,
	 * als dat niet kan ruilt hij met de pot.
	 * @param board de status van het spel.
	 * @param player de AI.
	 * @param stackSize de grootte van de pot.
	 * @param thinkTime de maximale tijd dat de AI erover mag doen.
	 * @return de beurt.
	 */
	public List<Move> getMove(Board board, Player player, int stackSize, int thinkTime) {
		List<Block> hand = player.getHand();
		List<PlayMove> playmove = new ArrayList<PlayMove>();
		List<SwapMove> swapmove = new ArrayList<SwapMove>();
		List<Move> result = new ArrayList<Move>();
		playmove = retardedStrategyPlay(board, hand, player);
		if (playmove.size() == 0) {
			swapmove = retardedStrategySwap(hand, player, board, stackSize);
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
	
	/**
	 * deze methode kijkt of hij iets op het bord kan zetten, geeft het eerste terug wat hij vindt.
	 * eerst kijt hij of het midden leeg is, 
	 * als dit zo is zet hij daar de eerste steen uit zijn hand op.
	 * daarna gaat hij voor elke steen in zijn hand het hele bord af,
	 * wanneer hij een zet tegenkomt die volgens de spelregels is zet de AI hem in een lijst.
	 * als de lijst vervolgens niet meer volgens de regels is wordt de zet er weer uitgehaald.
	 * 
	 * @param board het speelbord
	 * @param hand de hand
	 * @param player de speler
	 * @return een zet.
	 */
	public List<PlayMove> retardedStrategyPlay(Board board, List<Block> hand, Player player) {
		Board moveboard = board.deepCopy();
		List<Block> movehand = new ArrayList<Block>();
		movehand.addAll(hand);
		List<PlayMove> moves = new ArrayList<PlayMove>();
		PlayMove move = null;
		if (movehand.size() > 0) {
			if (moveboard.isEmptyField(Board.MID, Board.MID)) {
				move = new PlayMove(movehand.get(0), Board.MID, Board.MID, player);
				moves.add(move);
				moveboard.setField(Board.MID, Board.MID, movehand.get(0));
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
