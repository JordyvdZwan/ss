package controller;

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

import org.junit.Before;
import org.junit.Test;

import model.Block;
import model.Board;
import model.Color;
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
			boolean active = true;
			String command = "";
			while (active) {
				try {
					while ((command = in.readLine()) != null) {
						active = false;
						break;
					}
				} catch (IOException e) {
					System.out.println("oeps..");
				}
			}
			System.out.println(command);
			assertEquals(command, "Hello testABC");
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.stopConnection();
	}

}
