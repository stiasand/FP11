package server.network;

import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
public class Connection extends Thread implements PropertyChangeListener {
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
        } catch (Exception e) { // TODO: IOException
        	// TODO: On release: remove sysout
            System.err.println(e.getMessage());
            throw new IOException();
        }
        
        running = true;
        while (running) {
        	try {
	        	Client client = new Client(server.accept());
	        	
	        	if (client != null) {
	        		client.addPropertyChangeListener(this);
	        		System.out.println(client);
	        		clients.add(client);
	        		Thread t = new Thread(client);
		        	t.start();
	        	}
        	} catch (Exception e) { // TODO: IOException
        		System.out.println(e.getMessage());
        	}
        	
        	try {
        		Thread.sleep(50);
        	} catch (Exception e) { // TODO: InterruptedException
        		System.out.println(e.getMessage());
        	}
        }
	}
	
	/**
	 * Closes the connection
	 */
	public void close() {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).close();
		}
		
		try {
			server.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Releases all Socket resources on life termination
	 */
	protected void finalize() {
		close();
	}

	/**
	 * Listens for events on the Client sockets
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() != null) {
			if (evt.getPropertyName().equals(Client.CLOSING_PROPERTY)) {
				// Removes the corresponding socket
				if (clients.contains(evt.getSource())) {
					clients.remove(((Client)evt.getSource()));
				}
			}
		}
	}
}