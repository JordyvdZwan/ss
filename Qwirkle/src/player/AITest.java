package player;

import org.junit.Before;
import org.junit.Test;

import model.*;


public class AITest {
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
		System.out.println(retard.determineMove(board, player.getHand()).toString());
	}
	
//	@Test
//	public void TestRetardedAI() {
//		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
//		System.out.print(board.toString());
//		System.out.println(player.getHand().toString());
//		System.out.println(retard.determineMove(board, player.getHand()).toString());
//	}
//	
//	@Test
//	public void TestRetardedAISecondMove() {
//		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
//		board.setField(92, 93, new Block(Color.BLUE, Shape.CIRCLE));
//		System.out.print(board.toString());
//		System.out.println(player.getHand().toString());
//		System.out.println(retard.determineMove(board, player.getHand()).toString());
//	}
}
