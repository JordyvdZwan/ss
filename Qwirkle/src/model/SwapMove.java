package model;

import player.Player;

public class SwapMove implements Move {
	private Block block;
	private Player player;

	public SwapMove(Block block, Player player) {
		this.setBlock(block);
		this.setPlayer(player);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public String toString() {
		return " SWAP " + block.toString();
		
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	
}
