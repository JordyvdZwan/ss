package player;

import java.util.List;

import controller.Connection;
import model.Block;
import model.Board;
import model.Move;
import view.UI;

public interface Player {
	
	public void removeFromHand(Move move);
	
	public List<Move> determineMove(UI ui, Board board, List<Block> hand, int stackSize);
	public List<Block> getHand();
	public void setHand(List<Block> handArg);
	public void swapHand(List<Move> moves, List<Block> blocks);
	
	public int getScore();
	public void setScore(int scoreArg);
	
	public void setName(String nameArg);
	public String getName();
	public void setNumber(int numberArg);
	public int getNumber();
	public Connection getConnection();
	public void setConnection(Connection conn);
}
