package model;

public class PlayMove implements Move {
	public Block block;
	public int x;
	public int y;
	
	public PlayMove(Block block, int x, int y) {
		this.x = x;
		this.y = y;
		this.block = block;
	}

}
