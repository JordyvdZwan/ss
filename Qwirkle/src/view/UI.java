package view;

import model.*;

import java.net.InetAddress;

import controller.*;

public interface UI {
	public void displayBoard(Board board);
	public void displayScore();
	
	public Move getMove();
	public InetAddress getHost();
	public int getPort();
	public String getUserName();
	public String getChoiceServerClient();
	public String getCommand();
	public int getAIThinkTime();
	public void errorOccured(String msg);
	
	
}
