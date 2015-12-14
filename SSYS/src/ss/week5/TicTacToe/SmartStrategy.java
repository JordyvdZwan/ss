package ss.week5.TicTacToe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmartStrategy implements Strategy {

	@Override
	public String getName() {
		return "Smart";
	}

	@Override
	public int determineMove(Board b, Mark m) {
		int result = b.DIM * b.DIM;
		
		if (b.isEmptyField(4)) {
			result = 4;
		} else {
			Mark oppositeMark = null;
			if (m == Mark.OO) {
				oppositeMark = Mark.XX;
			} else {
				oppositeMark = Mark.OO;
			}
			
			for (int i = 0; i < b.DIM * b.DIM; i++) {
				Board test = b.deepCopy();
				test.setField(i, oppositeMark);
				if (test.isWinner(oppositeMark) && b.isEmptyField(i)) {
					result = i;
					break;
				}
			}
			for (int i = 0; i < b.DIM * b.DIM; i++) {
				Board test = b.deepCopy();
				test.setField(i, m);
				if (test.isWinner(m) && b.isEmptyField(i)) {
					result = i;
					break;
				}
			}
		}
		if (result == b.DIM * b.DIM) {
			List<Integer> emptyFields = new ArrayList<Integer>();
			for (int i = 0; i < b.DIM * b.DIM; i++) {
				if (b.isEmptyField(i)) {
					emptyFields.add(i);
				}
			}
			Double random = Math.random();
			Double index = (random * emptyFields.size());
			result = emptyFields.get(index.intValue());
		}
		return result;
	}

}
