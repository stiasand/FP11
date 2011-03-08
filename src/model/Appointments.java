package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Appointments extends ArrayList<Appointment> {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

}
