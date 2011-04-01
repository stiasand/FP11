package xml;

import java.util.Date;

import model.Appointment;
import model.Employee;
import model.Room;

public abstract class XMLHandler {
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
}
