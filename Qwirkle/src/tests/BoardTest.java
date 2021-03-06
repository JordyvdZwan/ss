package tests;

import org.junit.Before;
import org.junit.Test;

import model.Block;
import model.Board;
import model.Color;
import model.PlayMove;
import model.Shape;
import player.NetworkPlayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class BoardTest {
    private Board board;

    /**
     * maakt een nieuw spel aan.
     */
    @Before
	public void setup() {
		board = new Board();
	}
	
    /**
     * test of de methodes werken die stenen op het bord zetten en er af halen.
     */
	@Test
	public void testMove() {
		Block block = new Block(Color.BLUE, Shape.DIAMOND);
		board.setField(3, 4, block);
		assertEquals(board.getField(3, 4), block);
		assertEquals(board.getField(4, 5), null);
		board.emptyField(3, 4);
		assertEquals(board.getField(3, 4), null);
		assertTrue(board.isEmptyField(3, 4));
	}
	
	/**
	 * test of de methode werkt die de score bepaalt als alleen een rij wordt aangelegd.
	 */
	@Test
	public void testScore() { 
		PlayMove move1 = new PlayMove(new Block(Color.ORANGE, Shape.CROSS),
						1, 6, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.ORANGE, Shape.DIAMOND),
						1, 7, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.RED, Shape.SQUARE), 
						2, 3, new NetworkPlayer());
		board.setField(1, 2, new Block(Color.ORANGE, Shape.STAR));
		board.setField(1, 3, new Block(Color.ORANGE, Shape.SQUARE));
		board.setField(1, 4, new Block(Color.ORANGE, Shape.CIRCLE));
		board.setField(1, 5, new Block(Color.ORANGE, Shape.CLOVER));
		assertEquals(board.moveScore(move1), 5);
		board.setField(1, 6, new Block(Color.ORANGE, Shape.CROSS));
		assertEquals(board.moveScore(move2), 12);
		assertEquals(board.moveScore(move3), 2); 
	}
	
	/**
	 * test of de score goed wordt uitgerekent in alle richtingen,
	 * en test of dan de extra punten goed worden gegeven.
	 */
	@Test
	public void testMaxScore() {
		PlayMove move = new PlayMove(new Block(Color.PURPLE, Shape.STAR), 
						42, 69, new NetworkPlayer());
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
	
	/**
	 * test of de score goed wordt berekend als er meerdere stenen tegelijk worden neergelegd.
	 */
	@Test
	public void testMultipeScore() {
		ArrayList<PlayMove> multipleMove = new ArrayList<PlayMove>();
		ArrayList<PlayMove> singleMove = new ArrayList<PlayMove>();
		ArrayList<PlayMove> ymove = new ArrayList<PlayMove>();
		PlayMove move = new PlayMove(new Block(Color.PURPLE, Shape.STAR), 
						92, 96, new NetworkPlayer());
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
						92, 95, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CIRCLE),
						92, 92, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 
						92, 93, new NetworkPlayer());
		PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.CROSS), 
						92, 94, new NetworkPlayer());
		PlayMove move5 = new PlayMove(new Block(Color.RED, Shape.CROSS), 
						91, 92, new NetworkPlayer());
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
	
	/**
	 * test of de methode werkt die kijkt of iemand nog iets kan spelen.
	 */
	@Test
	public void testnoValidMoves() {
		board.setField(91, 91, new Block(Color.BLUE, Shape.CIRCLE));
		Block block1 = new Block(Color.GREEN, Shape.CIRCLE); 
		Block block2 = new Block(Color.YELLOW, Shape.CLOVER); 
		Block block3 = new Block(Color.GREEN, Shape.DIAMOND);
		Block block4 = new Block(Color.ORANGE, Shape.DIAMOND); 
		Block block5 = new Block(Color.PURPLE, Shape.CROSS); 
		Block block6 = new Block(Color.YELLOW, Shape.SQUARE); 
		ArrayList<Block> hand = new ArrayList<Block>();
		hand.add(block2);
		hand.add(block3);
		hand.add(block4);
		hand.add(block5);
		hand.add(block6);
		assertTrue(board.noValidMoves(hand));
		hand.add(block1);
		assertFalse(board.noValidMoves(hand));
	}
	
	/**
	 * deze test kijkt of er een foutmelding wordt gegeven als je 2 rijen met elkaar combineert,
	 * en deze rijen hebben dezelfde steen.
	 */
	@Test
	public void testIllegalConnection() {
		PlayMove move = new PlayMove(new Block(Color.GREEN, Shape.CIRCLE),
							91, 92, new NetworkPlayer());
		board.setField(91, 91, new Block(Color.BLUE, Shape.CIRCLE));
		board.setField(91, 93, new Block(Color.BLUE, Shape.CIRCLE));
		assertFalse(board.isLegalMove(move));
	}
	
	/**
	 * test of er bij een zet goed wordt bekeken of hij volgens de regels is.
	 */
	@Test
	public void testLegally() {
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
						34, 65, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CIRCLE), 
						34, 66, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 
						34, 66, new NetworkPlayer());
		PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.DIAMOND), 
						33, 65, new NetworkPlayer());
		PlayMove move5 = new PlayMove(new Block(Color.PURPLE, Shape.CROSS), 
						35, 65, new NetworkPlayer());
		PlayMove move6 = new PlayMove(new Block(Color.YELLOW, Shape.SQUARE), 
						34, 64, new NetworkPlayer());
		PlayMove move7 = new PlayMove(new Block(Color.GREEN, Shape.CIRCLE),
						34, 64, new NetworkPlayer());
		PlayMove move8 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 
						34, 66, new NetworkPlayer());
		PlayMove move9 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 
						34, 64, new NetworkPlayer());
		PlayMove move10 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 
						33, 65, new NetworkPlayer());
		PlayMove move11 = new PlayMove(new Block(Color.GREEN, Shape.DIAMOND), 
						91, 91, new NetworkPlayer());
		PlayMove move12 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
						35, 65, new NetworkPlayer());
		assertFalse(board.isLegalMove(move1));
		assertTrue(board.isLegalMove(move11));
		board.setField(34, 65, new Block(Color.GREEN, Shape.CLOVER));
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
		assertFalse(board.isLegalMove(move12));
	}
	
	/**
	 * test of er bij een zet met meerde stenen goed wordt bekeken of hij volgens de regels is.
	 */
	@Test
	public void testLegallyMoves() {
		ArrayList<PlayMove> multipleMove = new ArrayList<PlayMove>();
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
						34, 65, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CLOVER), 
						34, 66, new NetworkPlayer());
		PlayMove move3 = new PlayMove(new Block(Color.RED, Shape.CLOVER), 
						34, 67, new NetworkPlayer());
		PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 
						34, 68, new NetworkPlayer());
		PlayMove move5 = new PlayMove(new Block(Color.PURPLE, Shape.CLOVER),
						34, 63, new NetworkPlayer());
		PlayMove move6 = new PlayMove(new Block(Color.ORANGE, Shape.CLOVER), 
						34, 64, new NetworkPlayer());
		PlayMove move7 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 
						35, 68, new NetworkPlayer());
		board.setField(92, 92, new Block(Color.YELLOW, Shape.DIAMOND));
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
	
	/**
	 * test of de methodes nog werken als de stenen niet aan elkaar grenzen.
	 */
	@Test
	public void testLegalMovesNotConected() {
		ArrayList<PlayMove> multipleMove = new ArrayList<PlayMove>();
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
						91, 92, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.GREEN, Shape.STAR), 
						93, 92, new NetworkPlayer());
		board.setField(92, 92, new Block(Color.GREEN, Shape.CIRCLE));
		multipleMove.add(move1);
		multipleMove.add(move2);
		assertTrue(board.isLegalMoveList(multipleMove));
	}
	
	/**
	 * test of de index goed wordt berekent.
	 */
	@Test
	public void testIndex() {
		assertEquals(board.index(0, 1), 1);
		assertEquals(board.index(27, 32), 4973);
		assertEquals(board.index(185, 185), 34040);
	}
	
	/**
	 * test of de methode die kijkt of het veld op het bord ligt goed werkt.
	 */
	@Test
	public void testIsField() {
		assertTrue(board.isField(156, 46));
		assertFalse(board.isField(3, 190));
		assertFalse(board.isField(-42, 30));
		assertFalse(board.isField(666, 420));
		assertFalse(board.isField(66, -420));
		assertFalse(board.isField(666, 5));
	}
	
	/**
	 * test of de methode goed werkt die kijkt of de steen wordt omringd door andere stenen.
	 */
	@Test
	public void testIsLonely() {
		PlayMove move = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
						34, 65, new NetworkPlayer());
		PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CIRCLE), 
						0, 0, new NetworkPlayer());
		PlayMove move2 = new PlayMove(new Block(Color.GREEN, Shape.CIRCLE), 
						182, 182, new NetworkPlayer());
		assertTrue(board.isLonelyStone(move));
		assertTrue(board.isLonelyStone(move1));
		assertTrue(board.isLonelyStone(move2));
//		board.setField(34, 66, new Block(Color.GREEN, Shape.CLOVER));
		board.setField(33, 65, new Block(Color.PURPLE, Shape.CLOVER));
		assertFalse(board.isLonelyStone(move));
	}
	
//	@Test
//	public void TestNoValidMoves() {
//		board.setField(92, 92, new Block(Color.BLUE, Shape.DIAMOND));
//		assertTrue(board.noValidMoves(hand));
//	}
//	
	/**
	 * test of de methodes werken die kijken of een rij/kolom helemaal leeg is.
	 */
	@Test
	public void testEmptyRow() {
		assertTrue(board.emptyXRow(50));
		assertTrue(board.emptyYRow(46));
		board.setField(50, 46, new Block(Color.PURPLE, Shape.SQUARE));
		assertFalse(board.emptyXRow(50));
		assertFalse(board.emptyYRow(46));
	}
	
	/**
	 * deze test is om te kijken of de toStrings het goed doen.
	 */
	@Test
	public void testBoard() {
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
		System.out.println(board.toColorString());

	}
	
	/**
	 * deze test is om te kijken of de toString van een Block werkt.
	 */
	@Test
	public void testBlockToString() {
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
