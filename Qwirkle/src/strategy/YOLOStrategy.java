package strategy;

import java.util.ArrayList;
import java.util.List;

import model.Block;
import model.Board;
import model.Move;
import player.Player;

public class YOLOStrategy implements Strategy {
	
	

	public YOLOStrategy() {
		
	}

	@Override
	public List<Move> getMove(Board b, List<Block> hand, Player player, int stackSize) {
		Board localBoard = new Board(b);
		for (int x = b.minX(); x < b.maxX(); x++) {
			for (int y = b.minY(); y < b.maxY(); y++) {
				
				
				
			}
		}
		
		
		
		
		List<Move> result = new ArrayList<Move>();
		
		return result;
	}

}
