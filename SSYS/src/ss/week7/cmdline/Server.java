
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
    	try {
    		int port = Integer.parseInt(args[1]);
    		ServerSocket serverSocket = new ServerSocket(port);
    		while (true) {
    			Socket sock = serverSocket.accept();
    			Peer peer = new Peer("Name", sock);
    		}
    	} catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    
    	
    	
    	
    	
    	
    	
    }

} // end of class Server
