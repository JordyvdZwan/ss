package model;

import player.Player;

public class PlayMove implements Move {
	public Block block;
	public int x;
	public int y;
	private Player player;
	
	public PlayMove(Block block, int x, int y, Player player) {
		this.x = x;
		this.y = y;
		this.block = block;
		this.setPlayer(player);
	}

	public Block getBlock() {
		return block;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String toString() {
		return block.toString() + " " + x + " " + y;
	}
}
