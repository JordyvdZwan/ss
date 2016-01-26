package player;

import java.util.List;

import controller.Connection;
import model.Block;
import model.Move;

public interface Player {
	
	public void removeFromHand(Move move);
	/*@pure*/
	public List<Block> getHand();
	public void setHand(List<Block> handArg);
	public void swapHand(List<Move> moves, List<Block> blocks);
	/*@pure*/
	public int getScore();
	public void setScore(int scoreArg);
	public void setName(String nameArg);
	/*@pure*/
	public String getName();
	public void setNumber(int numberArg);
	/*@pure*/
	public int getNumber();
	public Connection getConnection();
	public void setConnection(Connection conn);
}
