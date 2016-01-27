package tests;

import org.junit.Before;
import org.junit.Test;

import model.*;
import player.ComputerPlayer;
import strategy.*;


public class AITest {
	/**
	 * het speelboard.
	 */
	private Board board = new Board();
	/**
	 * de speler.
	 */
	private ComputerPlayer player = new ComputerPlayer("retard", new RetardedStrategy());
	/**
	 * de pot.
	 */
	private Stack stack;
	/**
	 * de AI die we testen.
	 */
	RetardedStrategy retard = new RetardedStrategy();
	
	/**
	 * maakt een nieuw bord aan.
	 * maakt een niuwe pot.
	 * geeft de AI 6 stenen.
	 */
	@Before
	public void setUp() {
		board = new Board();
		stack = new Stack();
		player.setHand(stack.give(6));
	}
	
	/**
	 * test wat er gebeurt als de AI als eerste mag spelen.
	 */
	@Test
	public void testRetardedAIFirstMove() {
		System.out.println("retard");
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.getMove(board, 
					 player, stack.size(), 50000).toString());
	}
	
	/**
	 * test wat er gebeurt als er 1 steen op het bord ligt.
	 */
	@Test
	public void testRetardedAI() {
		System.out.println("retard");
		board.setField(91, 91, new Block(Color.BLUE, Shape.CLOVER));
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.getMove(board, 
					 player, stack.size(), 50000).toString());
	}
	
	/**
	 * test wat er gebeurt als er al 2 stenen op het bord liggen.
	 */
	@Test
	public void testRetardedAISecondMove() {
		System.out.println("retard");
		board.setField(91, 91, new Block(Color.BLUE, Shape.CLOVER));
		board.setField(92, 91, new Block(Color.BLUE, Shape.CIRCLE));
		System.out.print(board.toString());
		System.out.println(player.getHand().toString());
		System.out.println(retard.getMove(board, 
					 player, stack.size(), 50000).toString());
	}
	

}
