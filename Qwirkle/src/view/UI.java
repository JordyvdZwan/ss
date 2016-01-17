package view;

import model.*;
import player.Player;

import java.net.InetAddress;
import java.util.List;

import controller.*;

public interface UI {
	public void displayBoard(Board board);
	public void displayScore();
	
	public List<Move> getMove();
	public InetAddress getHost();
	public int getPort();
	public String getUserName();
	public String getChoiceServerClient();
	public String getCommand();
	public int getAIThinkTime();
	public void errorOccured(String msg);
	public void displayKick(Player player, String reason);
	public boolean newGame();
	public void displayWinner(Player player);
	public void setClient(Client client);
}
