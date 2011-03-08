package model;

import java.beans.PropertyChangeSupport;
import java.util.Date;


public class Appointment {
	protected final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

	protected Employee addedBy;
	protected Date addedDate;
	protected Date startDate, endDate;
	protected String description;
	protected String location;
	protected Room room;
	
}
