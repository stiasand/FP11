package xml;

import java.util.Date;

import model.Appointment;
import model.Attending;
import model.Employee;
import model.Meeting;
import model.Room;

public abstract class XMLHandler {
	/**
	 * @param node What node
	 * @param xml XML data
	 * @return Returns how many nodes of the given node the XML contains 
	 */
	public static int nodeSize(String xml, String node) {
		// TODO: Implement
		return 0;
	}
	
	public static String getXmlNode(String xml, int index) {
		// TODO: Implement
		return "";
	}
	
	/**
	 * Creates an Appointment object from the given XML
	 * @param xml Appointment-data
	 * @return Appointment object with values from the XML
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static Appointment createAppointment(String xml) throws Exception {
		// TODO: Read XML from "xml" into these vars
		String id = "";
		String addedDate = "";
		String startDate = "";
		String endDate = "";
		String description = "";
		String location = "";
		Appointment appointment;
		
		try {
			Employee employee = createEmployee(xml);
			Room room = createRoom(xml);
			
			appointment = new Appointment(Integer.parseInt(id), employee, new Date(addedDate), 
					new Date(startDate), new Date(endDate), description, location, room);
		} catch (Exception e) {
			throw new Exception("XML-Error");
		}
		return appointment;
	}
	
	/**
	 * Creates a Room object from the given XML
	 * @param xml Room-data
	 * @return Room object with values from the XML
	 * @throws Exception
	 */
	public static Room createRoom(String xml) throws Exception {
		// TODO: Read XML from "xml" into these vars
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
	
	/**
	 * Creates a Employee object from the given XML
	 * @param xml Employee-data
	 * @return Employee object with values from the XML
	 * @throws Exception
	 */
	public static Employee createEmployee(String xml) throws Exception {
		// TODO: Read XML from "xml" into these vars
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
	 * Creates a Meeting object from the given XML
	 * @param xml Meeting-data
	 * @return Meeting object with values from the XML
	 * @throws Exception
	 */
	public static Meeting createMeeting(String xml) throws Exception {
		// TODO: Implement
		return null;	
	}
	
	/**
	 * Creates a Attending object from the given XML
	 * @param xml Attending-data
	 * @return Attending object with values from the XML
	 * @throws Exception
	 */
	public static Attending createAttending(String xml) throws Exception {
		// TODO: Implement
		return null;
	}
	
	/**
	 * Creates a finished XML with and event and subnodes
	 * @param event Event name
	 * @param xml XML
	 * @return Finished XML
	 */
	public static String createXml(String event, String xml) {
		// TODO: Implement
		return "";
	}
	
	/**
	 * Creates XML out of a Meeting Object
	 * @param meeting Meeting object
	 * @return XML
	 */
	public static String createXml(Meeting meeting) {
		return "";
	}
		
	/**
	 * Creates XML out of an Employee Object
	 * @param employee Employee object
	 * @return XML
	 */
	public static String createXml(Employee employee) {
		// TODO: Implement
		return "";
	}
	
	/**
	 * Creates XML out of a Room Object
	 * @param room Room object
	 * @return XML
	 */
	public static String createXml(Room room) {
		// TODO: Implement
		return "";
	}
	
	/**
	 * Creates XML out of an Appointment Object
	 * @param appointment Appointment object
	 * @return XML
	 */
	public static String createXml(Appointment appointment) {
		// TODO: Implement
		return "";
	}
} 
