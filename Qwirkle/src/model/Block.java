package model;

public class Block {
	public Color color;
	public Shape shape;	
	
	public static final String RED = (char) 27 + "[01;41;37mR";
	public static final String BLUE = (char) 27 + "[01;44;37mB";
	public static final String PURPLE = (char) 27 + "[01;45;37mP";
	public static final String ORANGE = (char) 27 + "[01;46;37mO"; //CYAN
	public static final String GREEN = (char) 27 + "[01;42;37mG";
	public static final String YELLOW = (char) 27 + "[01;43;37mY";
	public static final String BLACK = (char) 27 + "[01;47;00m";
	
	public Block(Color color, Shape shape) {
		this.color = color;
		this.shape = shape;
	}
	
	/*@pure*/
	public Color getColor() {
		return this.color;
	}
	
	/*@pure*/
	public Shape getShape() {
		return this.shape;
	}
	
	/**
	 * @return the String representation of a block as described in the Group Protocol,
	 * With ANSI color codes.
	 */
	 //prints out a block
    public String toColorString() {
    	String localcolor = "";
    	String localshape = "";
    	if (this.color.equals(Color.BLUE)) {
    		localcolor = BLUE;
    	}
    	if (this.color.equals(Color.ORANGE)) {
    		localcolor = ORANGE;
    	}
    	if (this.color.equals(Color.PURPLE)) {
    		localcolor = PURPLE;
    	}
    	if (this.color.equals(Color.RED)) {
    		localcolor = RED;
    	}
    	if (this.color.equals(Color.YELLOW)) {
    		localcolor = YELLOW;
    	}
    	if (this.color.equals(Color.GREEN)) {
    		localcolor = GREEN;
    	}
    	if (this.shape.equals(Shape.CIRCLE)) {
    		localshape = "o";
    	}
    	if (this.shape.equals(Shape.STAR)) {
    		localshape = "*";
    	}
    	if (this.shape.equals(Shape.DIAMOND)) {
    		localshape = "d";
    	}
    	if (this.shape.equals(Shape.SQUARE)) {
    		localshape = "s";
    	}
    	if (this.shape.equals(Shape.CROSS)) {
    		localshape = "x";
    	}
    	if (this.shape.equals(Shape.CLOVER)) {
    		localshape = "c";
    	}
    	return localcolor + localshape + BLACK;
    }
	
    /**
     * @return the String representation of a block as described in the Group Protocol.
     */
    public String toString() {
    	char localcolor = 'E';
    	char localshape = 'E';
    	if (this.color.equals(Color.BLUE)) {
    		localcolor = 'B';
    	}
    	if (this.color.equals(Color.ORANGE)) {
    		localcolor = 'O';
    	}
    	if (this.color.equals(Color.PURPLE)) {
    		localcolor = 'P';
    	}
    	if (this.color.equals(Color.RED)) {
    		localcolor = 'R';
    	}
    	if (this.color.equals(Color.YELLOW)) {
    		localcolor = 'Y';
    	}
    	if (this.color.equals(Color.GREEN)) {
    		localcolor = 'G';
    	}
    	if (this.shape.equals(Shape.CIRCLE)) {
    		localshape = 'o';
    	}
    	if (this.shape.equals(Shape.STAR)) {
    		localshape = '*';
    	}
    	if (this.shape.equals(Shape.DIAMOND)) {
    		localshape = 'd';
    	}
    	if (this.shape.equals(Shape.SQUARE)) {
    		localshape = 's';
    	}
    	if (this.shape.equals(Shape.CROSS)) {
    		localshape = 'x';
    	}
    	if (this.shape.equals(Shape.CLOVER)) {
    		localshape = 'c';
    	}
    	return localcolor + "" + localshape;
    }

    /**
     * Checks if the given String is a valid blockSting.
     * @param blockString String to be tested.
     * @return true if it is a valid blockString.
     */
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
	
	/**
	 * Creates a Block given two chars.
	 * @param color Color char.
	 * @param shape Shape char.
	 */
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
