package ss.week5.TicTacToe;

import java.util.*;

public class NaiveStrategy implements Strategy {

	@Override
	public String getName() {
		return "Naive";
	}
	
	@Override
	public int determineMove(Board b, Mark m) {
		Set<Integer> emptyFields = new HashSet<Integer>();
		for (int i = 0; i < b.DIM * b.DIM; i++) {
			if (b.isEmptyField(i)) {
				emptyFields.add(i);
			}
		}
		Double random = Math.random();
		Double index = (random * 4567) % 9;
		int result = index.intValue();
		return result;
	}

}
