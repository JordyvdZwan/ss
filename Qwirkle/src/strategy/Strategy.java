package strategy;

import java.util.List;

import model.*;
import player.Player;

public interface Strategy {
	public List<Move> getMove(Board b, List<Block> hand, Player player, int stackSize);
}
