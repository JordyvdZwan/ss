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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameTest {
	private Server server;
	private Socket sock;
	private UI ui;
	private Board board;
	private Game game;
	private NetworkPlayer networkplayer = new NetworkPlayer();
	private ComputerPlayer player = new ComputerPlayer("retard", new RetardedStrategy());
	private ComputerPlayer zeventien = new ComputerPlayer("miranda", 
						new MirandaStrategy());
	private Stack stack;
	MirandaStrategy miranda = new MirandaStrategy();
	private Connection conn;	
//	private Client client = new Client(ui, sock, new ComputerPlayer("testABC", new RetardedStrategy()));
	
	@Before
	public void setup() {
		board = new Board();
		stack = new Stack();
		game = new Game(server, 1000, ui);
		RetardedStrategy retard = new RetardedStrategy();

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
		try {
			ServerSocket serverSocket = new ServerSocket(25565);
			Block block = new Block(Color.GREEN, Shape.CLOVER);
			InetAddress address = InetAddress.getByName("localhost");
			Socket sock = new Socket(address, 25565);
			Socket ssock = serverSocket.accept();
			serverSocket.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));	
			game.addConnection(new Connection(game, sock, new NetworkPlayer()));
			game.processMessage(game.connections.get(0), "WELCOME testABC 0");
			game.processMessage(game.connections.get(0), "NAMES testABC 0 thijs 1 5000");
			game.processMessage(game.connections.get(0), "NEW Gc Yc Rc Bc Oc R*");
			Board board = new Board();
			ArrayList<PlayMove> multipleMove = new ArrayList<PlayMove>();
			PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
							34, 65, new NetworkPlayer());
			PlayMove move2 = new PlayMove(new Block(Color.YELLOW, Shape.CLOVER), 
							34, 66, new NetworkPlayer());
			PlayMove move3 = new PlayMove(new Block(Color.RED, Shape.CLOVER), 
							34, 67, new NetworkPlayer());
			PlayMove move4 = new PlayMove(new Block(Color.BLUE, Shape.CLOVER), 
							34, 68, new NetworkPlayer());
			PlayMove move6 = new PlayMove(new Block(Color.ORANGE, Shape.CLOVER), 
							34, 64, new NetworkPlayer());
			multipleMove.add(move4);
			multipleMove.add(move1);
			multipleMove.add(move2);
			multipleMove.add(move3);
			multipleMove.add(move6);
			game.processMessage(game.connections.get(0), 
										"TURN 0 Gc 92 92 Yc 92 93 Rc 92 91 Bc 92 94 Oc 92 95");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
