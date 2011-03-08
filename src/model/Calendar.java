package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;

public class Calendar {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
	
	protected ArrayList<Appointment> appointments;
	protected HashSet<Employee> viewers = new HashSet<Employee>();
}
