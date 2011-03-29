package server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	private static final int SLEEP = 50;
	private Socket client;
	private boolean running = false;
	private long tick;
	
	public Client(Socket client) {
		this.client = client;
	}
	
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (Exception e) { // IOException
			System.out.println(e.getMessage());
		}
		
		running = true;
		while (running) {
			try {
				String line = in.readLine();
				if (line != null) {
					tick = System.currentTimeMillis();
					System.out.println("Message: " + line);
				}
			} catch (Exception e) { // IOException
				System.out.println(e.getMessage());
			}
			
			// Waits for message
			try {
        		Thread.sleep(SLEEP);
        	} catch (Exception e) { // InterruptedException
        		System.out.println(e.getMessage());
        	}
		}
	}
	
	/**
	 * @return If Socket is open or not
	 */
	public boolean getRunning() {
		return running;
	}
	
	/**
	 * Runs a pingpong to the client. If no response it closes the connection
	 */
	public void pingpong() {
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (Exception e) { // IOException
			System.out.println(e.getMessage());
		}
		
		boolean waiting = true;
		long pingtick = System.currentTimeMillis();
		out.println("Ping");
		
		while (waiting) {
			try {
				String line = in.readLine();
				if (line != null) {
					tick = System.currentTimeMillis();
					break;
					System.out.println("Message: " + line);
				}
			} catch (Exception e) { // IOException
				System.out.println(e.getMessage());
			}
			
			// Closes connection if no reply was received
			// TODO: NOT FINISHED HERE:
			if (tick)
			
			// Waits for message
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
			client.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}