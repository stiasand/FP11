package model;

import java.beans.PropertyChangeSupport;

public class Employee {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

	private String username, password;
	
	public Employee(String username) {
		this.username = username;
	}

	private Calendar calendar;
	
	public boolean verifyPassword(String password){
		return this.password.equals(password);
	}
	
	public String toString() {
		return username;
	}
}
