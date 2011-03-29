package server.network;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * FROM http://www.oracle.com/technetwork/java/socket-140484.html
 */
public class Connection extends Thread {
	public static final int PORT = 123;
	private ServerSocket server = null;
	private boolean running = false;
	private List<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args) {
		try {
			Connection con = new Connection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Connection() throws IOException {
		listen();
		start();
	}
	
	public void listen() throws IOException {
		try {
            server = new ServerSocket(PORT);
        } catch (Exception e) { // IOException
        	// TODO: On release: remove sysout
            System.err.println(e.getMessage());
            throw new IOException();
        }
        
        running = true;
        while (running) {
        	try {
	        	Client client = new Client(server.accept());
	        	if (client != null) {
	        		System.out.println(client);
	        		clients.add(client);
	        	}
	        	Thread t = new Thread(client);
	        	t.start();
        	} catch (Exception e) { // IOException
        		System.out.println(e.getMessage());
        	}
        	
        	try {
        		Thread.sleep(50);
        	} catch (Exception e) { // InterruptedException
        		System.out.println(e.getMessage());
        	}
        }
	}
	
	/**
	 * Releases all Socket resources on program termination
	 */
	protected void finalize() {
		try {
			server.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}