package tests;

import org.junit.Before;
import org.junit.Test;

import controller.Connection;
import controller.Game;
import controller.Server;
import model.*;
import player.*;
import strategy.*;
import view.TUI;
import view.UI;

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

public class GameTest extends Thread {
	private UI ui = new TUI(true);
	private Game game;
	private Server server = new Server(25566, ui, 5000);
	MirandaStrategy miranda = new MirandaStrategy();
	
	@Before
	public void setup() {
		game = new Game(server, 1000, ui);

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
			BufferedWriter out = new BufferedWriter(
												new OutputStreamWriter(sock.getOutputStream()));	
			game.addConnection(new Connection(game, sock, new NetworkPlayer()));
			System.out.println(game.connections.size());
			game.processMessage(game.connections.get(0), "HELLO poep");
			System.out.println(game.connections.size());
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.addConnection(new Connection(game, sock, new NetworkPlayer()));
			System.out.println(game.connections.size());
			game.processMessage(game.connections.get(0), "HELLO hallo");
			System.out.println(game.connections.size());
			game.start();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(game.connections.size());
			block = game.connections.get(game.getTurn()).getPlayer().getHand().get(0);
			System.out.println(game.connections.size());
			String test = block.toString();
			System.out.println(game.connections.size());
			game.processMessage(game.connections.get(1), "MOVE " + test + " 91 91");
			System.out.println(game.connections.size());
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
			System.out.println(game.connections.size());
			server.handleInput("stop");
			out.close();
			in.close();
			ssock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
