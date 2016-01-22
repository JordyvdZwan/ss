package view;

import model.*;
import player.Player;

import java.net.InetAddress;
import java.util.List;
import java.util.Observer;

import controller.*;

public interface UI extends Runnable, Observer {
	public void displayBoard(Board board);
	public void displayScore(Player player, List<Player> opponents);
	public void displayFatalError(String msg);
	public void displayHand(List<Block> hand);
	public void displayKick(Player player, String reason);
	public void displayWinner(Player player);
	public void displayServerMessage(String msg);
	
	public String getUserName();
	public String getChoiceServerClient();
	public List<Move> getMove(Board b);
	public String getHost();	
	public String getPort();
	public String getAIThinkTime();	
	public String getCommand();
	
	public void run();
	public void errorOccured(String msg);
	public boolean newGame();
	
	public void setServerController(Server control);
	public void setClient(Client client);
}
