package view;

import model.*;
import controller.*;

public interface UI {
	public void displayBoard(Board board);
	public void displayScore();
	
	public Move getMove();
	public String getHost();
	public int getPort();
	public String getUserName();
	
	
	
}
