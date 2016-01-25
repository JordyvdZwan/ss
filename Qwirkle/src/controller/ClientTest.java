package controller;

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

import org.junit.Before;
import org.junit.Test;

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
import controller.Game;

public class ClientTest {
	Client client;
	Connection serverConnection;
	UI ui = new TUI(true);
	
	@Before
	public void setup() {

	}
	
	@Test
	public void testNames() {
		try {
			ServerSocket serverSocket = new ServerSocket(25565);
			InetAddress address = InetAddress.getByName("localhost");
			Socket sock = new Socket(address, 25565);
			Socket ssock = serverSocket.accept();
			serverSocket.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));			
			client = new Client(ui, sock, new ComputerPlayer("testABC", new RetardedStrategy()));
			client.processMessage(client.conn, "WELCOME testABC 0");
			client.processMessage(client.conn, "NAMES testABC 0 poep 1 thijs 2 5000");
			client.processMessage(client.conn, "NEW Gc Yc Rc Bc Oc R*");
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
			client.processMessage(client.conn, "TURN 2 Gc 34 65 Yc 34 66 Rc 34 67 Bc 34 68 Oc 34 64");
			System.out.println(board.toString());
			assertTrue(board.getBlock() == client.getBoard().getBlock());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
