package model;

import org.junit.Before;
import org.junit.Test;

import player.NetworkPlayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
		board.emptyField(3, 4);
		assertEquals(board.getField(3, 4), null);
		assertTrue(board.isEmptyField(3, 4));
	}
	
	@Test
	public void TestScore() { 
		PlayMove move1 = new PlayMove(new Block(Color.ORANGE, Shape.CROSS), 1, 6, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.ORANGE, Shape.DIAMOND), 1, 7, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.RED, Shape.SQUARE), 2, 3, new NetworkPlayer());
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
		PlayMove move = new PlayMove(new Block(Color.PURPLE, Shape.STAR), 42, 69, new NetworkPlayer());
		board.setField(42, 70, new Block(Color.PURPLE, Shape.CIRCLE));
		board.setField(42, 71, new Block(Color.PURPLE, Shape.CLOVER));
		board.setField(42, 68, new Block(Color.PURPLE, Shape.CROSS));
		board.setField(42, 67, new Block(Color.GREEN, Shape.DIAMOND));
		board.setField(42, 66, new Block(Color.PURPLE, Shape.SQUARE));
		board.setField(41, 69, new Block(Color.BLUE, Shape.STAR));
		board.setField(40, 69, new Block(Color.GREEN, Shape.STAR));
		board.setField(43, 69, new Block(Color.ORANGE, Shape.STAR));
		board.setField(44, 69, new Block(Color.RED, Shape.STAR));
		board.setField(39, 69, new Block(Color.YELLOW, Shape.STAR));
		assertEquals(board.moveScore(move), 24);
	}
	
	@Test
	public void TestMultipeScore() {
		ArrayList<PlayMove> multipleMove = new ArrayList<PlayMove>();
		ArrayList<PlayMove> singleMove = new ArrayList<PlayMove>();
		ArrayList<PlayMove> ymove = new ArrayList<PlayMove>();
		PlayMove move = new PlayMove(new Block(Color.PURPLE, Shape.STAR), 92, 96, new NetworkPlayer());
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 92, 95, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CIRCLE), 92 ,92, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 92, 93, new NetworkPlayer());
		PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.CROSS), 92, 94, new NetworkPlayer());
		PlayMove move5 = new PlayMove(new Block(Color.RED, Shape.CROSS), 91, 92, new NetworkPlayer());
		singleMove.add(move1);
		ymove.add(move5);
		ymove.add(move2);
		multipleMove.add(move);
		multipleMove.add(move1);
		multipleMove.add(move2);
		multipleMove.add(move3);
		multipleMove.add(move4);
		assertEquals(5, board.legitMoveScore(multipleMove));
		assertEquals(1, board.legitMoveScore(singleMove));
		assertEquals(2, board.legitMoveScore(ymove));
		board.setField(93, 95, new Block(Color.BLUE, Shape.CROSS));
		board.setField(93, 96, new Block(Color.BLUE, Shape.DIAMOND));
		assertEquals(9, board.legitMoveScore(multipleMove));
		assertEquals(2, board.legitMoveScore(singleMove));
	}
	
	@Test
	public void TestLegally() {
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 34, 65, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CIRCLE), 34 ,66, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 34, 66, new NetworkPlayer());
		PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.DIAMOND), 33, 65, new NetworkPlayer());
		PlayMove move5 = new PlayMove(new Block(Color.PURPLE, Shape.CROSS), 35, 65, new NetworkPlayer());
		PlayMove move6 = new PlayMove(new Block(Color.YELLOW, Shape.SQUARE), 34,64, new NetworkPlayer());
		PlayMove move7 = new PlayMove(new Block(Color.GREEN, Shape.CIRCLE), 34, 64, new NetworkPlayer());
		PlayMove move8 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 34, 66, new NetworkPlayer());
		PlayMove move9 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 34, 64, new NetworkPlayer());
		PlayMove move10 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 33, 65, new NetworkPlayer());
		assertFalse(board.isLegalMove(move1));
		board.setField(34, 65, (new Block(Color.GREEN, Shape.CLOVER)));
		assertFalse(board.isLegalMove(move1));
		assertFalse(board.isLegalMove(move2));
		assertTrue(board.isLegalMove(move3));
		assertFalse(board.isLegalMove(move4));
		assertFalse(board.isLegalMove(move5));
		assertFalse(board.isLegalMove(move6));
		assertTrue(board.isLegalMove(move7));
		assertTrue(board.isLegalMove(move8));
		assertTrue(board.isLegalMove(move9));
		assertTrue(board.isLegalMove(move10));
	}
	
	@Test
	public void TestLegallyMoves() {
		ArrayList<PlayMove> multipleMove = new ArrayList<PlayMove>();
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 34, 65, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CLOVER), 34 ,66, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.RED, Shape.CLOVER), 34, 67, new NetworkPlayer());
		PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 34, 68, new NetworkPlayer());
		PlayMove move5 = new PlayMove(new Block(Color.PURPLE, Shape.CLOVER), 34, 63, new NetworkPlayer());
		PlayMove move6 = new PlayMove(new Block(Color.ORANGE, Shape.CLOVER), 34,64, new NetworkPlayer());
		PlayMove move7 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 35, 68, new NetworkPlayer());
		multipleMove.add(move5);
		multipleMove.add(move1);
		multipleMove.add(move2);
		multipleMove.add(move3);
		multipleMove.add(move7);
		multipleMove.add(move6);
		assertFalse(board.isLegalMoveList(multipleMove));
		board.setField(33, 66, new Block(Color.YELLOW, Shape.DIAMOND));
		assertFalse(board.isLegalMoveList(multipleMove));
		multipleMove.add(move4);
		multipleMove.remove(move5);
		assertFalse(board.isLegalMoveList(multipleMove));
		multipleMove.remove(move7);
		assertTrue(board.isLegalMoveList(multipleMove));
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
		assertFalse(board.isField(66, -420));
		assertFalse(board.isField(666, 5));
	}
	
	@Test
	public void TestStack() {
		board.setField(34, 65, (new Block(Color.GREEN, Shape.CLOVER)));
		assertEquals(board.countStack(), 83);
	}
	
	@Test
	public void TestIsLonely() {
		PlayMove move = new PlayMove( (new Block(Color.GREEN, Shape.CLOVER)), 34, 65, new NetworkPlayer());
		PlayMove move1 = new PlayMove((new Block(Color.GREEN, Shape.CIRCLE)), 0, 0, new NetworkPlayer());
		PlayMove move2 = new PlayMove((new Block(Color.GREEN, Shape.CIRCLE)), 183, 183, new NetworkPlayer());
		assertTrue(board.isLonelyStone(move));
		assertTrue(board.isLonelyStone(move1));
		assertTrue(board.isLonelyStone(move2));
//		board.setField(34, 66, new Block(Color.GREEN, Shape.CLOVER));
		board.setField(33, 65, new Block(Color.PURPLE, Shape.CLOVER));
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
	
	@Test
	public void TestBoard() {
		board.setField(92, 92, new Block(Color.PURPLE, Shape.CIRCLE));
		board.setField(92, 93, new Block(Color.PURPLE, Shape.CLOVER));
		board.setField(92, 94, new Block(Color.PURPLE, Shape.CROSS));
		board.setField(92, 91, new Block(Color.PURPLE, Shape.DIAMOND));
		board.setField(92, 95, new Block(Color.PURPLE, Shape.SQUARE));
		board.setField(91, 95, new Block(Color.BLUE, Shape.SQUARE));
		board.setField(90, 95, new Block(Color.GREEN, Shape.SQUARE));
		board.setField(93, 91, new Block(Color.ORANGE, Shape.DIAMOND));
		board.setField(94, 91, new Block(Color.RED, Shape.DIAMOND));
		board.setField(94, 90, new Block(Color.RED, Shape.STAR));
		assertEquals(board.maxX(), 95);
		assertEquals(board.maxY(), 96);
		assertEquals(board.minX(), 89);
		assertEquals(board.minY(), 89);
		System.out.print(board.toString());
	}
	
	
	@Test
	public void TestBlockToString() {
		Block block1 = new Block(Color.RED, Shape.SQUARE);
		Block block2 = new Block(Color.BLUE, Shape.CIRCLE);
		Block block3 = new Block(Color.GREEN, Shape.CLOVER);
		Block block4 = new Block(Color.ORANGE, Shape.CROSS);
		Block block5 = new Block(Color.PURPLE, Shape.DIAMOND);
		Block block6 = new Block(Color.YELLOW, Shape.STAR);
		assertEquals(block1.toString(), "Rs");
		assertEquals(block2.toString(), "Bo");
		assertEquals(block3.toString(), "Gc");
		assertEquals(block4.toString(), "Ox");
		assertEquals(block5.toString(), "Pd");
		assertEquals(block6.toString(), "Y*");
	}
}
