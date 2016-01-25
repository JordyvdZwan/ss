package player;

import java.util.List;

import model.Block;
import model.Board;
import model.Move;
import view.UI;

public interface LocalPlayer extends Player {
	public List<Move> determineMove(UI ui, Board board,	int stackSize, List<Player> opponents, int thinkTime);
}
