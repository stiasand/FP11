package model;

import java.util.Date;
import java.util.List;

public class Meeting extends Appointment {
	protected List<Attending> attending;
	protected Room room;
	
	public Meeting(int id, Employee addedBy, Employees employees, Date addedDate, Date startDate,
			Date endDate, String description, String location, Room room) {
		super(id, addedBy, addedDate, startDate, endDate, description, room.name);
		this.room = room;
	}
	
	@Override
	public void setLocation(String location) {
		super.location = room.name;
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public List<Attending> getAttending() {
		return attending;
	}

	public void setAttending(List<Attending> attending) {
		this.attending = attending;
	}

	public void cancel(){
		
	}
	public void accept(Employee employee){
		
	}
	public void discard(Employee employee){
		
	}
}