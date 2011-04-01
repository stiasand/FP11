package model;

import java.util.Date;
import java.util.List;

public class Meeting extends Appointment {
	protected List<Attending> attending;
	
	public Meeting(int id, Employee addedBy, Employees employees, Date addedDate, Date startDate,
			Date endDate, String description, String location, Room room) {
		super(id, addedBy, addedDate, startDate, endDate, description, location, room);
	}
	
	public void cancel(){
		
	}
	public void accept(Employee employee){
		
	}
	public void discard(Employee employee){
		
	}
}