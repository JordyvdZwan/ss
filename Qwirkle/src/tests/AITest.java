package tests;

import org.junit.Before;
import org.junit.Test;

import model.*;
import player.ComputerPlayer;
import strategy.*;


public class AITest {
	private Board board = new Board();
	private ComputerPlayer player = new ComputerPlayer("retard", new RetardedStrategy());
	private Stack stack;
	RetardedStrategy retard = new RetardedStrategy();
	
	@Before
	public void setUp() {
		board = new Board();
		stack = new Stack();
		player.setHand(stack.give(6));
	}
	
	@Test
	public void testRetardedAIFirstMove() {
		System.out.println("retard");
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.getMove(board, 
					 player, stack.size(), 50000).toString());
	}
	
	@Test
	public void testRetardedAI() {
		System.out.println("retard");
		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.getMove(board, 
					 player, stack.size(), 50000).toString());
	}
	
	@Test
	public void testRetardedAISecondMove() {
		System.out.println("retard");
		board.setField(92, 92, new Block(Color.BLUE, Shape.CLOVER));
		board.setField(92, 93, new Block(Color.BLUE, Shape.CIRCLE));
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.getMove(board, 
					 player, stack.size(), 50000).toString());
	}
	

}
