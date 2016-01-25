package strategy;

import java.util.List;

import model.*;
import player.Player;

public interface Strategy {
	public List<Move> getMove(Board b, Player player, int stackSize, int thinkTime);
}
