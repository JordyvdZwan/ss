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
	public void TestMaxScore() {
		PlayMove move = new PlayMove(new Block(Color.PURPLE, Shape.STAR), 42, 69);
		board.setField(42, 70, new Block(Color.PURPLE, Shape.CIRCLE));
		board.setField(42, 71, new Block(Color.PURPLE, Shape.CLOVER));
		board.setField(42, 68, new Block(Color.PURPLE, Shape.CROSS));
		board.setField(42, 67, new Block(Color.PURPLE, Shape.DIAMOND));
		board.setField(42, 66, new Block(Color.PURPLE, Shape.SQUARE));
		board.setField(41, 69, new Block(Color.BLUE, Shape.STAR));
		board.setField(40, 69, new Block(Color.GREEN, Shape.STAR));
		board.setField(43, 69, new Block(Color.ORANGE, Shape.STAR));
		board.setField(44, 69, new Block(Color.RED, Shape.STAR));
		board.setField(39, 69, new Block(Color.YELLOW, Shape.STAR));
		assertEquals(board.moveScore(move), 24);
	}
	
	@Test
	public void TestLegally() {
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 34, 65);
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CIRCLE), 34 ,66);
		PlayMove move3 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 34, 66);
		PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.DIAMOND), 33, 65);
		assertFalse(board.isLegalMove(move1));
		board.setField(34, 65, (new Block(Color.GREEN, Shape.CLOVER)));
		assertFalse(board.isLegalMove(move2));
		assertTrue(board.isLegalMove(move3));
		assertFalse(board.isLegalMove(move4));
	}
	
	@Test
	public void TestIndex() {
		assertEquals(board.index(0, 1), 1);
		assertEquals(board.index(27, 32), 4973);
		assertEquals(board.index(185, 185), 34040);
	}
	
	@Test
	public void TestIsField() {
		assertTrue(board.isField(156, 46));
		assertFalse(board.isField(3, 190));
		assertFalse(board.isField(-42, 30));
		assertFalse(board.isField(666, 420));
	}
	
	@Test
	public void TestStack() {
		board.setField(34, 65, (new Block(Color.GREEN, Shape.CLOVER)));
		assertEquals(board.countStack(), 83);
	}
	
	@Test
	public void TestIsLonely() {
		PlayMove move = new PlayMove( (new Block(Color.GREEN, Shape.CLOVER)), 34, 65);
		assertTrue(board.isLonelyStone(move));
		board.setField(34, 66, new Block(Color.GREEN, Shape.CLOVER));
		assertFalse(board.isLonelyStone(move));
	}
	
	@Test
	public void TestNoValidMoves() {
		board.addToHand(new Block(Color.GREEN, Shape.CLOVER));
		assertTrue(board.noValidMoves());
	}
	
	@Test
	public void TestEmptyRow() {
		assertTrue(board.emptyXRow(42));
		assertTrue(board.emptyYRow(90));
		board.setField(50, 46, new Block(Color.PURPLE, Shape.SQUARE));
		assertFalse(board.emptyXRow(50));
		assertFalse(board.emptyYRow(46));
	}
}
