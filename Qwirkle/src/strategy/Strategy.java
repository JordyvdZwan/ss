package strategy;

import java.util.List;

import model.*;

public interface Strategy {
	public List<Move> getMove(Board b);
}
