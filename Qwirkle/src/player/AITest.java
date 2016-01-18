package player;

import org.junit.Before;
import org.junit.Test;

import model.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class AITest {
	private Block block;
	private Board board = new Board();
	private LocalPlayer player = new LocalPlayer("retard");
	private Stack stack;
	RetardedStrategy retard = new RetardedStrategy(player);
	
	@Before
	public void SetUp() {
		board = new Board();
		stack = new Stack();
		player.setHand(stack.give(6));
		}
	
	@Test
	public void TestRetardedAIFirstMove() {
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.determineMove(board).toString());
	}
	
	@Test
	public void TestRetardedAI() {
		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.determineMove(board).toString());
	}
}
