package model;

public class Block {
	public Color color;
	public Shape shape;	
	
	public static final String RED = (char)27+"[01;41;37mR";
	public static final String BLUE = (char)27+"[01;44;37mB";
	public static final String PURPLE = (char)27+"[01;45;37mP";
	public static final String ORANGE = (char)27+"[01;46;37mO"; //CYAN
	public static final String GREEN = (char)27+"[01;42;37mG";
	public static final String YELLOW = (char)27+"[01;43;37mY";
	public static final String BLACK = (char)27+"[01;47;00m";
	
	public Block(Color color, Shape shape) {
		this.color = color;
		this.shape = shape;
	}
	
	 //prints out a block
    public String toColorString() {
    	String color = "";
    	String shape = "";
    	if (this.color.equals(Color.BLUE)) {
    		color = BLUE;
    	}
    	if (this.color.equals(Color.ORANGE)) {
    		color = ORANGE;
    	}
    	if (this.color.equals(Color.PURPLE)) {
    		color = PURPLE;
    	}
    	if (this.color.equals(Color.RED)) {
    		color = RED;
    	}
    	if (this.color.equals(Color.YELLOW)) {
    		color = YELLOW;
    	}
    	if (this.color.equals(Color.GREEN)) {
    		color = GREEN;
    	}
    	if (this.shape.equals(Shape.CIRCLE)) {
    		shape = "o";
    	}
    	if (this.shape.equals(Shape.STAR)) {
    		shape = "*";
    	}
    	if (this.shape.equals(Shape.DIAMOND)) {
    		shape = "d";
    	}
    	if (this.shape.equals(Shape.SQUARE)) {
    		shape = "s";
    	}
    	if (this.shape.equals(Shape.CROSS)) {
    		shape = "x";
    	}
    	if (this.shape.equals(Shape.CLOVER)) {
    		shape = "c";
    	}
    	return color + shape + BLACK;
    }
	
	 //prints out a block
    public String toString() {
    	char color = 'E';
    	char shape = 'E';
    	if (this.color.equals(Color.BLUE)) {
    		color = 'B';
    	}
    	if (this.color.equals(Color.ORANGE)) {
    		color = 'O';
    	}
    	if (this.color.equals(Color.PURPLE)) {
    		color = 'P';
    	}
    	if (this.color.equals(Color.RED)) {
    		color = 'R';
    	}
    	if (this.color.equals(Color.YELLOW)) {
    		color = 'Y';
    	}
    	if (this.color.equals(Color.GREEN)) {
    		color = 'G';
    	}
    	if (this.shape.equals(Shape.CIRCLE)) {
    		shape = 'o';
    	}
    	if (this.shape.equals(Shape.STAR)) {
    		shape = '*';
    	}
    	if (this.shape.equals(Shape.DIAMOND)) {
    		shape = 'd';
    	}
    	if (this.shape.equals(Shape.SQUARE)) {
    		shape = 's';
    	}
    	if (this.shape.equals(Shape.CROSS)) {
    		shape = 'x';
    	}
    	if (this.shape.equals(Shape.CLOVER)) {
    		shape = 'c';
    	}
    	return color + "" + shape;
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
	
}
