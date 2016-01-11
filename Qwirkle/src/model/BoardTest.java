package model;

import org.junit.Before;
import org.junit.Test;
import model.Board;
import model.Block;
import model.PlayMove;
import model.Move;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {
    private Board board;

	@Before
	public void Setup() {
		board = new Board();
	}
	
	@Test
	public void TestMove() {
		Block block = new Block(Color.BLUE, Shape.DIAMOND);
		board.setField(3,4,block);
		assertEquals(board.getField(3, 4), block);
		assertEquals(board.getField(4, 5), null);
	}
	
	@Test
	public void TestScore() { 
		PlayMove move1 = new PlayMove(new Block(Color.ORANGE, Shape.CROSS), 1, 6);
		PlayMove move2 = new PlayMove(new Block(Color.ORANGE, Shape.DIAMOND), 1, 7);
		PlayMove move3 = new PlayMove(new Block(Color.RED, Shape.SQUARE), 2, 3);
		board.setField(1, 2, new Block(Color.ORANGE, Shape.STAR));
		board.setField(1, 3, new Block(Color.ORANGE, Shape.SQUARE));
		board.setField(1, 4, new Block(Color.ORANGE, Shape.CIRCLE));
		board.setField(1, 5, new Block(Color.ORANGE, Shape.CLOVER));
		assertEquals(board.moveScore(move1), 5);
		board.setField(1, 6, new Block(Color.ORANGE, Shape.CROSS));
		assertEquals(board.moveScore(move2), 12);
		assertEquals(board.moveScore(move3), 2); 
	}
	
	@Test
	public void TestLegally() {
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 34, 65);
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CIRCLE), 34 ,66);
		assertTrue(board.isLegalMove(move1));
		board.setField(34, 65, (new Block(Color.GREEN, Shape.CLOVER)));
		assertFalse(board.isLegalMove(move2));
	}
	
	@Test
	public void TestIndex() {
		assertEquals(board.index(0, 1), 1);
		assertEquals(board.index(27, 32), 5027);
		assertEquals(board.index(185, 185), 34410);
	}
	
	@Test
	public void TestIsField() {
		assertTrue(board.isField(156, 46));
		assertFalse(board.isField(3, 190));
		assertFalse(board.isField(-42, 30));
		assertFalse(board.isField(666, 420));
	}
}
