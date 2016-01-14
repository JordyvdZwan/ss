package model;

public class Block {
	public Color color;
	public Shape shape;	
	
	public Block(Color color, Shape shape) {
		this.color = color;
		this.shape = shape;
	}
	
	public static boolean isValidBlockString(String blockString) {
		boolean result = true;
		char[] chars = blockString.toCharArray();
		if (chars.length == 2) {
			if (!(chars[0] == 'R' || 
					chars[0] == 'O' ||
						chars[0] == 'B' ||
							chars[0] == 'Y' ||
								chars[0] == 'G' ||
									chars[0] == 'P')) {
				result = false;
			} else {
				if (!(chars[1] == 'o' || 
						chars[1] == 'd' ||
							chars[1] == 's' ||
								chars[1] == 'c' ||
									chars[1] == 'x' ||
										chars[1] == '*')) {
					result = false;
				}
			}
		} else {
			result = false;
		}
		return result;
	}
	
	public Block(char color, char shape) {
		if (color == 'R') {
			this.color = Color.RED;
		} else if (color == 'O') {
			this.color = Color.ORANGE;
		} else if (color == 'B') {
			this.color = Color.BLUE;
		} else if (color == 'Y') {
			this.color = Color.YELLOW;
		} else if (color == 'G') {
			this.color = Color.GREEN;
		} else if (color == 'P') {
			this.color = Color.PURPLE;
		}
		
		if (shape == 'o') {
			this.shape = Shape.CIRCLE;
		} else if (shape == 'd') {
			this.shape = Shape.DIAMOND;
		} else if (shape == 's') {
			this.shape = Shape.SQUARE;
		} else if (shape == 'c') {
			this.shape = Shape.CLOVER;
		} else if (shape == 'x') {
			this.shape = Shape.CROSS;
		} else if (shape == '*') {
			this.shape = Shape.STAR;
		}
	}
	
	public String toString() {
		String result = "";
		if (color == Color.RED) {
			result.concat("R");
		} else if (color == Color.ORANGE) {
			result.concat("O");
		} else if (color == Color.BLUE) {
			result.concat("B");
		} else if (color == Color.YELLOW) {
			result.concat("Y");
		} else if (color == Color.GREEN) {
			result.concat("G");
		} else if (color == Color.PURPLE) {
			result.concat("P");
		}
		
		if (shape == Shape.CIRCLE) {
			result.concat("o");
		} else if (shape == Shape.DIAMOND) {
			result.concat("d");
		} else if (shape == Shape.SQUARE) {
			result.concat("s");
		} else if (shape == Shape.CLOVER) {
			result.concat("c");
		} else if (shape == Shape.CROSS) {
			result.concat("x");
		} else if (shape == Shape.STAR) {
			result.concat("*");
		}
		return result;
	}
	
}
