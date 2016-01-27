package model;

import player.Player;

public interface Move {
	public Player getPlayer();
	public Block getBlock();
	public String toString();
}
