package controller;

import org.junit.Before;
import org.junit.Test;

import model.*;
import player.*;
import strategy.*;
import view.UI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameTest {
	private Server server;
	private Socket sock;
	private Connection conn;
	private UI ui;
	private Board board;
	private Game game;
	private NetworkPlayer networkplayer = new NetworkPlayer();
	private ComputerPlayer player = new ComputerPlayer("retard", new RetardedStrategy());
	private ComputerPlayer XVII = new ComputerPlayer("miranda", 
						new MirandaStrategy());
	private Stack stack;
	RetardedStrategy retard = new RetardedStrategy();
	MirandaStrategy miranda = new MirandaStrategy();
	
	@Before
	public void setup() {
		board = new Board();
		stack = new Stack();
		game = new Game(server, 1000, ui);
		conn = new Connection(game, sock, networkplayer);
	}
	
	@Test
	public void testInstanceOfPlayMoves() {
		List<Move> moves = new ArrayList<Move>();
		Move move1 = new PlayMove(new Block(Color.ORANGE, Shape.CROSS),
						91, 6, new NetworkPlayer());
		Move move2 = new PlayMove(new Block(Color.ORANGE, Shape.DIAMOND),
						1, 7, new NetworkPlayer());
		Move move3 = new PlayMove(new Block(Color.RED, Shape.SQUARE), 
						2, 3, new NetworkPlayer());
		moves.add(move1);
		moves.add(move2);
		moves.add(move3);
		assertTrue(game.isInstanceOfPlaymoves(moves));
	}
	
	@Test
	public void testRun() {
		game.run();
	}

}
