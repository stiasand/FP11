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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}