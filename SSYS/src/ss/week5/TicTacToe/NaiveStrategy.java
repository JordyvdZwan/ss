package ss.week5.TicTacToe;

import java.util.*;

public class NaiveStrategy implements Strategy {

	@Override
	public String getName() {
		return "Naive";
	}
	
	@Override
	public int determineMove(Board b, Mark m) {
		int result;
		List<Integer> emptyFields = new ArrayList<Integer>();
		for (int i = 0; i < b.DIM * b.DIM; i++) {
			if (b.isEmptyField(i)) {
				emptyFields.add(i);
			}
		}
		return emptyFields.get((int)(Math.random() * emptyFields.size()));
	}

}
