package tests;

import static org.junit.Assert.assertEquals;
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
import java.util.Scanner;

import org.junit.Test;

import controller.Client;
import controller.Connection;
import model.Block;
import model.Board;
import model.Color;
import model.PlayMove;
import model.Shape;
import player.NetworkPlayer;
import view.TUI;
import view.UI;
import player.*;
import strategy.*;

public class ClientTest {
	Client client;
	Connection serverConnection;
	/**
	 * de TUI.
	 */
	UI ui = new TUI(true);

	/**
	 * simuleert een server, doet dan alsof hij berichten stuurt naar de client.
	 * we testen hier of deze berichten goed zijn overgekomen.
	 * dit doen we door te kijken of de veranderingen zijn gemaakt.
	 */
	@Test
	public void testClient() {
		try {
			ServerSocket serverSocket = new ServerSocket(25565);
			Block block = new Block(Color.GREEN, Shape.CLOVER);
			InetAddress address = InetAddress.getByName("localhost");
			Socket sock = new Socket(address, 25565);
			Socket ssock = serverSocket.accept();
			serverSocket.close();
			client = new Client(ui, sock, new ComputerPlayer("testABC", new RetardedStrategy()));
			client.processMessage(client.conn, "WELCOME testABC 0");
			client.processMessage(client.conn, "NAMES testABC 0 thijs 1 5000");
			client.processMessage(client.conn, "NEW Gc Gc Gc Gc Gc Gc");
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
			board.makeMove(multipleMove);
			client.processMessage(client.conn, 
										"TURN 0 Gc 92 92 Yc 92 93 Rc 92 91 Bc 92 94 Oc 92 95");
			assertEquals(block.getColor(), client.getBoard().getField(92, 92).getColor());
			assertEquals(block.getShape(), client.getBoard().getField(92, 92).getShape());
			assertEquals("testABC", client.getPlayer().getName());
			assertEquals(5, client.getPlayer().getScore());
			client.processMessage(client.conn, "TURN 1 empty");
			ssock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * hier doen we precies hetzelfde als bij de andere test maar dan met andere commando's.
	 */
	@Test
	public void testNext() {
		Scanner reader = new Scanner(System.in);
		try {
			ServerSocket serverSocket = new ServerSocket(25565);
			InetAddress address = InetAddress.getByName("localhost");
			Socket sock = new Socket(address, 25565);
			Socket ssock = serverSocket.accept();
			serverSocket.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
																sock.getOutputStream()));			
			client = new Client(ui, sock, new ComputerPlayer("testABC", new RetardedStrategy()));
			client.processMessage(client.conn, "WELCOME testABC 0");
			client.processMessage(client.conn, "NAMES testABC 0 thijs 1 5000");
			client.processMessage(client.conn, "NEW Gc Gc Gc Gc Gc Gc");
			ArrayList<PlayMove> multipleMove = new ArrayList<PlayMove>();
			PlayMove move1 = new PlayMove(new Block(Color.GREEN, Shape.CLOVER), 
							91, 91, new NetworkPlayer());
			multipleMove.add(move1);
			client.processMessage(client.conn, "NEXT 0");
			assertTrue(client.getPlayer().getHand().size() == 5);
			client.processMessage(client.conn, "NEXT 0");
			assertTrue(client.getPlayer().getHand().size() >= 0);
			assertTrue(client.getPlayer().getHand().size() < 5);
			ssock.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader.close();
	}

}
