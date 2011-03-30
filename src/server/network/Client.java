package server.network;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import model.Appointment;
import model.Employee;
import model.Room;

import server.database.Database;

public class Client implements Runnable {
	public static final String CLOSING_PROPERTY = "Closing";
	public static final String MESSAGE_OK = "OK!";
	public static final String MESSAGE_FAIL = "FAIL!";
	public static final String MESSAGE_NOTRECOGNIZED = "NOT RECOGNIZED!";
	public static final String[] VALID_EVENTS = {"AddAppointment", "EditAppointment", "RemoveAppointment", 
		"GetAppointsments", "GetEmployeeList", "GetRoom", "GetAvailiableRooms", "Login"};
	public static final int SLEEP = 50;
	private Socket client;
	private boolean running = false;
	private long tick;
	private String auth = null;
	
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
		
		if (message.toLowerCase().equals("close")) {
			close();
		}
		else {
			String event = getEvent(message);
			if (Arrays.asList(VALID_EVENTS).contains(event)) {
				return executeEvent(event, message);
			} else {
				// TODO: Remove this else.
				return "Reply to: " + message;
			}
		}
		
		// TODO: Return XMl-object for NOTRECOGNIZED
		return Client.MESSAGE_NOTRECOGNIZED;
	}
	
	/**
	 * @param message Message (XML)
	 * @return Event name in the message
	 */
	private String getEvent(String message) {
		return "";
	}
	
	/**
	 * Executes the given event with the given values from message
	 * @param event Event to execute
	 * @param message Message with values for the event
	 * @return Reply message to Client
	 */
	private String executeEvent(String event, String message) {
		if (event != null) {
			if (event.equals("Login")) {
				// TODO: Read XML from "message" into these vars
				String username = "";
				String password = "";
				
				String sql = "SELECT * FROM Employees WHERE username = ? AND password = ?";
				String[] params = {username, password};
				List<HashMap<String, String>> res = Database.retrieve(sql, params);
				if (res.size() > 0) {
					// User was authorized via login
					auth = username;
					// TODO: Return XML-object that was created by this event
					return "";
				}
			} else if (event.equals("AddAppointment")) {
				// TODO: Read XML from "message" into these vars
				String addedBy = ""; // Employees-> username
				String startDate = "";
				String endDate = "";
				String description = "";
				String location = "";
				String roomName = ""; // Rooms-> name
				
				// TODO: Possible bug: will this set addedDate to now() or not?
				String sql = "INSERT INTO Appointments (employee, startDate, endDate, description, location) VALUES (?, ?, ?, ?, ?)";
				String[] params = {addedBy, startDate, endDate, description, location};
				int id = Database.modify(sql, params, Database.ReturnType.ID);
				int rows = 0;
				
				if (id != 0) {
					sql = "INSERT INTO Meetings (id, room) VALUES (?, ?)";
					String[] paramsMeeting = {String.valueOf(id), roomName};
					rows = Database.modify(sql, paramsMeeting, Database.ReturnType.ROWS);
				}
				
				if (id != 0 && rows != 0) {
					// TODO: Return XML-object that was created by this event
					return "";
				} else {
					// TODO: Return XML-object for FAILURE
					return "";
				}
			} else if (event.equals("EditAppointment")) {
				// TODO: Read XML from "message" into these vars
				String id = "";
				String addedBy = ""; // Employees-> username
				String addedDate = "";
				String startDate = "";
				String endDate = "";
				String description = "";
				String location = "";
				String room_Name = ""; // Rooms-> name
				String room_Size = "";
				String room_Description = "";
				
				try {
					Employee employee = new Employee(addedBy);
					Room room = new Room(room_Name, Integer.parseInt(room_Size), room_Description);
					Appointment appointment = new Appointment(Integer.parseInt(id), employee, new Date(addedDate), 
							new Date(startDate), new Date(endDate), description, location, room);
					
					String sqlAppointments = "UPDATE Appointments SET startDate = ?, endDate = ?, description = ?, location = ? WHERE id = ?";
					String[] paramsAppointments = {appointment.getStartDate().toString(), appointment.getEndDate().toString(), 
							appointment.getDescription(), appointment.getLocation(), String.valueOf(appointment.getId())};
					int rowsAppointments = Database.modify(sqlAppointments, paramsAppointments, Database.ReturnType.ROWS);
					
					String sqlMeetings = "UPDATE Meetings SET room = ? WHERE id = ?";
					String[] paramsMeetings = {roomName, id};
					int rowsMeetings = Database.modify(sqlMeetings, paramsMeetings, Database.ReturnType.ROWS);
					
					if (rowsAppointments != 0 || rowsMeetings != 0) {
						// TODO: Return XML-object that was created by this event
						return "";
					} else {
						throw new Exception("Error: Possible XML-failure");
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage()); // TODO Remove on release
					// Return ERROR-object (XML-Error)
					return null;
				}
			} else if (event.equals("RemoveAppointment")) {
				// TODO: Read XML from "message" into these vars
				String id = "";
				
				String sqlAppointments = "DELETE FROM Appointments WHERE id = ?";
				String[] paramsAppointments = {id};
				int rowsAppointments = Database.modify(sqlAppointments, paramsAppointments, Database.ReturnType.ROWS);
				
				String sqlMeetings = "DELETE FROM Meetings WHERE id = ?";
				String[] paramsMeetings = {id};
				int rowsMeetings = Database.modify(sqlMeetings, paramsMeetings, Database.ReturnType.ROWS);
				
				if (rowsAppointments != 0 && rowsMeetings != 0) {
					// TODO: Return XML-object that was created by this event
					return "";
				} else {
					// TODO: Return XML-object for FAILURE
					return "";
				}
			} else if (event.equals("GetAppointsments")) {
				// TODO: Read XML from "message" into these vars
				String employee_username = "";
				
				String sql = "SELECT * FROM Appointments WHERE employee = ?";
				String[] params = {employee_username};
				
				List<HashMap<String, String>> res = Database.retrieve(sql, params);
				for (HashMap<String, String> hm : res) {
					// TODO: Create Appointment objects
					//hm.get("name"));
				}
				
				if (res.size() > 0) {
					// TODO: Return one XML-object with all Appointment XML-objects that were made 
				} else {
					// TODO: Return XML-object for FAILURE
				}
			}
		}
		
		return null;
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