
package ss.week7.cmdline;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Server. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server {
    private static final String USAGE
        = "usage: " + Server.class.getName() + " <name> <port>";

    /** Starts a Server-application. */
    public static void main(String[] args) {
    	if (args.length != 2) {
    		System.out.println(USAGE);
    		System.exit(0);
    	}
    	
    	ServerSocket serverSocket = null;
    	try {
    		int port = Integer.parseInt(args[1]);
    		serverSocket = new ServerSocket(port);
    		Peer peer;
    		while (true) {
    			Socket sock = serverSocket.accept();
    			(new ClientHandler(args[0], sock)).start();
    		}
    	} catch (IOException e) {
    		System.out.println(e.getMessage());
    	} finally {
    		try {
    			serverSocket.close();
    		} catch (IOException e) {
    			System.out.println(e.getMessage());
    		}
    	}
    }
    
    
    private static class ClientHandler extends Thread {
		private String name;
		private Socket sock;
		
		public ClientHandler(String name, Socket sock) {
			this.name = name;
			this.sock = sock;
		}
		
		public void run() {
			try {
				Peer server = new Peer(name, sock);
				Thread streamInputHandler = new Thread(server);
	            streamInputHandler.start();
	            server.handleTerminalInput();
	            server.shutDown();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
		}
		
	}

} // end of class Server
//package ss.week7.cmdline;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
///**
// * Server.
// * 
// * @author Theo Ruys
// * @version 2005.02.21
// */
//public class Server {
//	private static final String USAGE = "usage: " + Server.class.getName() + " <name> <port>";
//
//	/** Starts a Server-application. */
//	public static void main(String[] args) {
//		if (args.length != 2) {
//			System.out.println(USAGE);
//			System.exit(0);
//		}
//		
//		String name = args[0];
//		int port = 0;
//		ServerSocket serverSock;
//		Socket sock = null;
//		
//		// parse args[1] - the port
//        try {
//            port = Integer.parseInt(args[1]);
//        } catch (NumberFormatException e) {
//            System.out.println(USAGE);
//            System.out.println("ERROR: port " + args[1]
//            		           + " is not an integer");
//            System.exit(0);
//        }
//        
//		// create server socket
//		try {
//			serverSock = new ServerSocket(port);
//			while (true){
//				sock = serverSock.accept();
//				(new ClientHandler(name, sock)).start();
//				
//			}
//		} catch (IOException e) {
//			System.out.println(e);
//		}
//	}
//	
//	private static class ClientHandler extends Thread{
//		private String name;
//		private Socket sock;
//		
//		public ClientHandler(String name, Socket sock) {
//			this.name = name;
//			this.sock = sock;
//		}
//		
//		public void run(){
//			try {
//				Peer server = new Peer(name, sock);
//				Thread streamInputHandler = new Thread(server);
//	            streamInputHandler.start();
//	            server.handleTerminalInput();
//	            server.shutDown();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//		}
//		
//	}
//
//} // end of class Server