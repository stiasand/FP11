package model;

import java.beans.PropertyChangeSupport;

public class Room {
	protected final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
	
	protected String name; 
	protected int size;
	protected String description;

	public Room(String name, int size, String description) {
		this.name = name;
		this.size = size;
		this.description = description;
	}
}