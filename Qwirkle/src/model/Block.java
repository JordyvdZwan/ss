package model;

public class Block {
	public Color color;
	public Shape shape;	
	
	public Block(Color color, Shape shape) {
		this.color = color;
		this.shape = shape;
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

	
}
