package view;

import model.*;
import player.Player;

import java.net.InetAddress;
import java.util.List;

import controller.*;

public interface UI extends Runnable{
	public void displayBoard(Board board);
	public void displayScore(int score);
	public void displayFatalError(String msg);
	public void displayHand(List<Block> hand);
	public void displayKick(Player player, String reason);
	public void displayWinner(Player player);
	
	public String getUserName();
	public String getChoiceServerClient();
	public String getCommand();	
	public List<Move> getMove(Board b);
	public InetAddress getHost();	
	public int getPort();
	public int getAIThinkTime();	
	
	public void run();
	public void errorOccured(String msg);
	public boolean newGame();
	
	public void setServerController(Server control);
	public void setClient(Client client);
}
