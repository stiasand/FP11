package server.network;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
	private Employee auth = null;
	
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
						out.println(reply + "\n");
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
		String event = getEvent(message);
		
		if (message.toLowerCase().equals("close")) {
			close();
			return null;
		} else if (Arrays.asList(VALID_EVENTS).contains(event)) {
			return executeEvent(event, message);
		} else {
			// TODO: Return XMl-object for NOTRECOGNIZED
			return Client.MESSAGE_NOTRECOGNIZED;
		}
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
	@SuppressWarnings("deprecation")
	private String executeEvent(String event, String message) {
		if (event != null) {
			if (event.equals("Login")) {
				try {
					Employee employee = createEmployee(message);
					
					String sql = "SELECT * FROM Employees WHERE username = ? AND password = ?";
					String[] params = {employee.getUsername(), employee.getPassword()};
					List<HashMap<String, String>> res = Database.retrieve(sql, params);
					if (res.size() > 0) {
						// User was authorized via login
						auth = employee;
						// TODO: Return XML-object that was created by this event
						return "";
					} else {
						throw new Exception("Error: Nothing updated");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage()); // TODO: Remove on release
					// Return ERROR-object (XML-Error)
					return null;
				}
			} else if (auth == null) {
				// Client not logged in
				// TODO: Return NOTLOGGEDIN XMl-Object
				return null;
			} else if (event.equals("AddAppointment")) {
				try {
					Appointment appointment = createAppointment(message);
					Room room = createRoom(message);
					
					// TODO: Possible bug: will this set addedDate to now() or not?
					String sql = "INSERT INTO Appointments (employee, addedDate, startDate, endDate, description, location) VALUES (?, ?, ?, ?, ?)";
					String[] params = {auth.getUsername(), appointment.getAddedDate().toString(), appointment.getStartDate().toString(), 
							appointment.getEndDate().toString(), appointment.getDescription(), appointment.getLocation()};
					int id = Database.modify(sql, params, Database.ReturnType.ID);
					int rows = 0;
					
					if (id != 0) {
						sql = "INSERT INTO Meetings (id, room) VALUES (?, ?)";
						String[] paramsMeeting = {String.valueOf(id), room.getName()};
						rows = Database.modify(sql, paramsMeeting, Database.ReturnType.ROWS);
					}
					
					if (id != 0 && rows != 0) {
						// TODO: Return XML-object that was created by this event
						return "";
					} else {
						throw new Exception("Error: Nothing updated");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage()); // TODO Remove on release
					// Return ERROR-object (XML-Error)
					return null;
				}
			} else if (event.equals("EditAppointment")) {
				try {
					// TODO: Currently any employee can edit any appointment (does not have to be owner)
					Appointment appointment = createAppointment(message);
					Room room = createRoom(message);
					
					String sqlAppointments = "UPDATE Appointments SET employee = ?, startDate = ?, endDate = ?, description = ?, location = ? WHERE id = ?";
					String[] paramsAppointments = {appointment.getAddedBy().getUsername(), appointment.getStartDate().toString(), 
							appointment.getEndDate().toString(), appointment.getDescription(), appointment.getLocation(), String.valueOf(appointment.getId())};
					int rowsAppointments = Database.modify(sqlAppointments, paramsAppointments, Database.ReturnType.ROWS);
					
					String sqlMeetings = "UPDATE Meetings SET room = ? WHERE id = ?";
					String[] paramsMeetings = {room.getName(), String.valueOf(appointment.getId())};
					int rowsMeetings = Database.modify(sqlMeetings, paramsMeetings, Database.ReturnType.ROWS);
					
					if (rowsAppointments != 0 || rowsMeetings != 0) {
						// TODO: Return XML-object that was created by this event
						return "";
					} else {
						throw new Exception("Error: Nothing updated");
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage()); // TODO Remove on release
					// Return ERROR-object (XML-Error)
					return null;
				}
			} else if (event.equals("RemoveAppointment")) {
				// TODO: Read XML from "message" into these vars
				try {
					// TODO: Currently anyone can delete any appointment
					Appointment appointment = createAppointment(message);
	
					String sqlAppointments = "DELETE FROM Appointments WHERE id = ?";
					String[] paramsAppointments = {String.valueOf(appointment.getId())};
					int rowsAppointments = Database.modify(sqlAppointments, paramsAppointments, Database.ReturnType.ROWS);
					
					String sqlMeetings = "DELETE FROM Meetings WHERE id = ?";
					String[] paramsMeetings = {String.valueOf(appointment.getId())};
					int rowsMeetings = Database.modify(sqlMeetings, paramsMeetings, Database.ReturnType.ROWS);
					
					if (rowsAppointments != 0 && rowsMeetings != 0) {
						// TODO: Return XML-object that was created by this event
						return "";
					} else {
						throw new Exception("Error: Nothing updated");
					}
				} catch (Exception e ) {
					System.out.println(e.getMessage()); // TODO Remove on release
					// Return ERROR-object (XML-Error)
					return null;
				}
			} else if (event.equals("GetAppointsments")) {
				try {
					Employee employee = createEmployee(message);

					String sql = "SELECT * FROM Appointments WHERE employee = ?";
					String[] params = {employee.getUsername()};
					
					List<Appointment> appointments = new ArrayList<Appointment>();
					List<HashMap<String, String>> res = Database.retrieve(sql, params);
					for (HashMap<String, String> hm : res) {
						int id = Integer.parseInt(hm.get("id"));
						Employee addedBy = new Employee(hm.get("employee"));
						Date addedDate = new Date(hm.get("addedDate"));
						Date startDate = new Date(hm.get("startDate"));
						Date endDate = new Date(hm.get("endDate"));
						String description = hm.get("description");
						String location = hm.get("location");
						
						String sqlRoom = "SELECT * FROM rooms WHERE name = (SELECT room FROM Meetings WHERE id = ?)";
						String[] paramsRoom = {String.valueOf(id)};
						List<HashMap<String, String>> resRoom = Database.retrieve(sqlRoom, paramsRoom);
						Room room;
						
						if (resRoom.size() > 0) {
							String room_name = resRoom.get(1).get("name");
							int room_size = Integer.parseInt(resRoom.get(1).get("size"));
							String room_description = resRoom.get(1).get("description");
							room = new Room(room_name, room_size, room_description);
						} else {
							throw new Exception("Error: No such room");
						}
						
						Appointment appointment = new Appointment(id, addedBy, addedDate, startDate, endDate, description, location, room);
						appointments.add(appointment);
					}
					
					if (res.size() > 0 && appointments.size() > 0) {
						// TODO: Return one XML-object with all Appointment XML-objects that were made 
						return null;
					} else {
						throw new Exception("Error: Nothing found");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage()); // TODO Remove on release
					// Return ERROR-object (XML-Error)
					return null;
				}
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public Appointment createAppointment(String message) throws Exception {
		// TODO: Read XML from "message" into these vars
		String id = "";
		String addedDate = "";
		String startDate = "";
		String endDate = "";
		String description = "";
		String location = "";
		Appointment appointment;
		
		try {
			Employee employee = createEmployee(message);
			Room room = createRoom(message);
			
			appointment = new Appointment(Integer.parseInt(id), employee, new Date(addedDate), 
					new Date(startDate), new Date(endDate), description, location, room);
		} catch (Exception e) {
			throw new Exception("XML-Error");
		}
		return appointment;
	}
	
	public Room createRoom(String message) throws Exception {
		// TODO: Read XML from "message" into these vars
		String room_Name = ""; // Rooms-> name
		String room_Size = "";
		String room_Description = "";
		Room room;
		
		try {
			room = new Room(room_Name, Integer.parseInt(room_Size), room_Description);
		} catch (Exception e) {
			throw new Exception("XML-Error");
		}
		
		return room;
	}
	
	public Employee createEmployee(String message) throws Exception {
		// TODO: Read XML from "message" into these vars
		String username = "";
		String password = "";
		String name = "";
		
		Employee employee;
		
		try {
			employee = new Employee(username, password, name);
		} catch (Exception e) {
			throw new Exception("XML-Error");
		}
		
		return employee;
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