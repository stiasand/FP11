package server.network;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	public static final String CLOSING_PROPERTY = "Closing";
	public static final String MESSAGE_OK = "OK!";
	public static final String MESSAGE_FAIL = "FAIL!";
	public static final int SLEEP = 50;
	private Socket client;
	private boolean running = false;
	private long tick;
	private String auth = "";
	
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private PropertyChangeSupport pcs;
	
	public Client(Socket client) {
		this.client = client;
		pcs = new PropertyChangeSupport(this);
	}
	
	/**
	 * Closes the connection
	 */
	public void close() {
		try {
			running = false;
			client.close();
			in.close();
			out.close();
			
			if (pcs.getPropertyChangeListeners().length > 0) {
				pcs.firePropertyChange(Client.CLOSING_PROPERTY, true, false);
			}
		} catch (Exception e) { // IOException
			System.out.println(e.getMessage());
		}
	}
	
	public void run() {
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
					String message = "";
					// Leser flere linjer til meldingen er avsluttet med en tom linje
					while (line != null && !(line != null && line.equals(""))) {
						message += line;
						line = in.readLine();
					}
					System.out.println("Message is : " + message);
					String reply = message(message);
					System.out.println("Reply is " + reply);
					if (reply != null) {
						out.println(reply);
					}
				}
			} catch (Exception e) { // IOException
				System.out.println(e.getMessage());
			}
			
			// Waits a bit
			try {
        		Thread.sleep(SLEEP);
        	} catch (Exception e) { // InterruptedException
        		System.out.println(e.getMessage());
        	}
		}
	}
	
	/**
	 * Gets a message and returns a reply
	 * @param message Message from client
	 * @return Reply to message
	 */
	private String message(String message) {
		tick = System.currentTimeMillis();
		
		if (message.equals("close")) {
			close();
		} else if (isEvent("AddAppointment", message)) {
			boolean executed = executeEvent(message);
			if (executed) {
				return Client.MESSAGE_OK;
			} else {
				return Client.MESSAGE_FAIL;
			}
		} else if (isEvent("EditAppointment", message)) {
			boolean executed = executeEvent(message);
			if (executed) {
				return Client.MESSAGE_OK;
			} else {
				return Client.MESSAGE_FAIL;
			}
		} else if (isEvent("RemoveAppointment", message)) {
			boolean executed = executeEvent(message);
			if (executed) {
				return Client.MESSAGE_OK;
			} else {
				return Client.MESSAGE_FAIL;
			}
		} else if (isEvent("GetAppointsments", message)) {
			return getEvent(message);
		} else {
			return "Reply to: " + message;
		}
		
		return null;
	}
	
	/**
	 * Checks if the given message is 
	 * @param event
	 * @param message
	 * @return
	 */
	private boolean isEvent(String event, String message) {
		return false;
	}
	
	private boolean executeEvent(String message) {
		return false;
	}
	
	private String getEvent(String message) {
		return "";
	}
	
	/**
	 * @return If Socket is open or not
	 */
	public boolean getRunning() {
		return running && client.isConnected();
	}
	
	/**
	 * @return Tick when last message was received
	 */
	public long getTick() {
		return tick;
	}
	
	/**
	 * Runs a pingpong to the client. If no response it closes the connection
	 */
	public void pingpong() {
		/*
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
		*/
	}
	
	/**
	 * Throws events related to this Client
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	/**
	 * Releases all Socket resources
	 */
	protected void finalize() {
		close();
	}
}