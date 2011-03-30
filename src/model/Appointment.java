package model;

import java.beans.PropertyChangeSupport;
import java.util.Date;


public class Appointment {
	protected final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

	protected int id;
	protected Employee addedBy;
	protected Date addedDate;
	protected Date startDate, endDate;
	protected String description;
	protected String location;
	protected Room room;
	
	public Appointment(int id, Employee addedBy, Date addedDate,
			Date startDate, Date endDate, String description, String location,
			Room room) {
		this.id = id;
		this.addedBy = addedBy;
		this.addedDate = addedDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.location = location;
		this.room = room;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Employee getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Employee addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
}
