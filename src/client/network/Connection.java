package client.network;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JOptionPane;

import server.network.Client;

public class Connection extends Thread {
	public static final int SLEEP = 50;
	private String host = "";
	private int port = 0;
	private Socket socket;
	private boolean running = false;
	public static final String[] VALID_EVENTS = {"Event", "AddAppointment", "EditAppointment", "RemoveAppointment", "RecieveAppointsments",
		"RecieveEmployeeList", "RecieveRoom", "RecieveAvailiableRooms"};
	
	private PropertyChangeSupport pcs;
	
	public Connection() {
		this("127.0.0.1", 123);
	}

	public Connection(String host, int port) {
		this.host = host;
		this.port = port;
		pcs = new PropertyChangeSupport(this);
		start();
		open();
	}
	
	public void listen() {
		BufferedReader in = null;
		running = true;
		
		while(running) {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String line = in.readLine();
				if (line != null) {
					String message = "";
					// Leser flere linjer til meldingen er avsluttet med en tom linje
					while (line != null && !(line != null && line.equals(""))) {
						message += line;
						line = in.readLine();
					}
					// Behandler meldingen
					recieveMessage(message);
				}
				
			} catch (Exception e) {
				System.out.println(e.getMessage()); // TODO: Remove on release
				// TODO: Handle error
			} finally {
				try {
					in.close();
				} catch (Exception e) {}
			}
			
			try {
        		Thread.sleep(50);
        	} catch (Exception e) { // TODO: InterruptedException
        		System.out.println(e.getMessage());
        	}
		}
	}
	
	private void recieveMessage(String message) {
		// TODO: Handle recieveMessage
		System.out.println("Message recieved: " + message);
		
		String event = getEventType(message);
		if (event != null && Arrays.asList(VALID_EVENTS).contains(event)) {
			if (event.equals("Event")) {
				// TODO: Read values into vars from XML in 'message'
				String eventType = "";
				String eventMessage = "";
				
				JOptionPane.showMessageDialog(null, "Error: " + eventMessage, eventType, JOptionPane.ERROR_MESSAGE);				
			} else if (event.equals("AddAppointment")) {
				
			} else if (event.equals("EditAppointment")) {
				
			} else if (event.equals("RemoveAppointment")) {
				
			} else if (event.equals("RecieveAppointsments")) {
				
			} else if (event.equals("RecieveEmployeeList")) {
				
			} else if (event.equals("RecieveRoom")) {
				
			} else if (event.equals("RecieveAvailiableRooms")) {
				
			} else {
				// TODO: Remove on release
				System.out.println("Recieved not valid event");
			}
		} else {
			// TODO: Remove on release
			System.out.println("Recieved not valid event");
		}
	}
	
	/**
	 * @param message Message (XML)
	 * @return Event name in the message
	 */
	private String getEventType(String message) {
		// TODO: Get event type from XML in message
		return "";
	}
	
	/**
	 * Sends a message to the server
	 * @param message XML-object of event
	 */
	public boolean sendMessage(String message) {
		boolean sent = false;
		
		if (socket.isConnected() && message != null && message != "") {
			PrintWriter out = null;
			
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(message+"\n");
				sent = true;
			} catch (Exception e) {
				// TODO: Handle error
				System.out.println(e.getMessage()); // TODO: Remove on release
			} finally {
				try {
					out.close();
				} catch (Exception e) {}
			}
		}
		
		return sent;
	}
	
	/**
	 * Executes an event
	 * @param event Event name
	 * @param value Value of the event
	 */
	private void executeEvent(String event, Object value) {
		if (pcs.getPropertyChangeListeners().length > 0) {
			pcs.firePropertyChange(event, null, value);
		}
	}

	public void open() {
		try {
			socket = new Socket(host, port);
		} catch (Exception e) {
			System.out.println(e.getMessage()); // TODO: Remove on release
		}
	}
	
	/**
	 * Closes the connection
	 */
	public void close() {
		if (socket.isConnected()) {
			sendMessage("close");
			try {
				socket.close();
			} catch (Exception e) {
				System.out.println(e.getMessage()); // TODO: Remove on release
			}
		}
	}
	
	/**
	 * Adds listener to events related to this connection
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public boolean isRunning() {
		return running && socket.isConnected();
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	/**
	 * Releases all Socket resources
	 */
	protected void finalize() {
		close();
	}
}