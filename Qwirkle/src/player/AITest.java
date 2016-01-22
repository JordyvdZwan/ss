package player;

import org.junit.Before;
import org.junit.Test;

import model.*;
import strategy.*;


public class AITest {
	private Board board = new Board();
	private ComputerPlayer player = new ComputerPlayer("retard", new RetardedStrategy());
	private ComputerPlayer supremeleader = new ComputerPlayer("alladeen", 
						new SupremeLeaderStrategy());
	private Stack stack;
	RetardedStrategy retard = new RetardedStrategy();
	SupremeLeaderStrategy alladeen = new SupremeLeaderStrategy();
	
	@Before
	public void setUp() {
		board = new Board();
		stack = new Stack();
		player.setHand(stack.give(6));
		supremeleader.setHand(stack.give(6));
	}
	
//	@Test
//	public void TestRetardedAIFirstMove() {
//		System.out.println("retard");
//		System.out.print(board.toString());
//		System.out.println(player.getHand().toString());
//		System.out.println(retard.getMove(board, player.getHand(), 
					//player, stack.size()).toString());
//	}
//	
//	@Test
//	public void TestRetardedAI() {
//		System.out.println("retard");
//		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
//		System.out.print(board.toString());
//		System.out.println(player.getHand().toString());
//		System.out.println(retard.getMove(board, player.getHand(), 
	//				player, stack.size()).toString());
//	}
//	
//	@Test
//	public void TestRetardedAISecondMove() {
//		System.out.println("retard");
//		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
//		board.setField(92, 93, new Block(Color.BLUE, Shape.CIRCLE));
//		System.out.print(board.toString());
//		System.out.println(player.getHand().toString());
//		System.out.println(retard.getMove(board, player.getHand(), 
//					player, stack.size()).toString());
//	}
	
	@Test
	public void testSupremeAIFirstMove() {
		System.out.println("SupremeLeader");
		System.out.print(board.toString());
		System.out.println(supremeleader.getHand().toString());
		System.out.println(alladeen.getMove(board, supremeleader.getHand(), 
						supremeleader, stack.size()).toString());
	}
	
	@Test
	public void testSupremeAI() {
		System.out.println("SupremeLeader");
		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
		System.out.print(board.toString());
		System.out.println(supremeleader.getHand().toString());
		System.out.println(alladeen.getMove(board, supremeleader.getHand(), 
						supremeleader, stack.size()).toString());
	}
	
	@Test
	public void testSupremeAISecondMove() {
		System.out.println("SupremeLeader");
		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
		board.setField(92, 93, new Block(Color.BLUE, Shape.CIRCLE));
		System.out.print(board.toString());
		System.out.println(supremeleader.getHand().toString());
		System.out.println(alladeen.getMove(board, supremeleader.getHand(), 
						supremeleader, stack.size()).toString());
	}
	
}
