package model;

import java.beans.PropertyChangeSupport;

public class Employee {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

	private String username;
	private String password;
	
	private Appointments appointments;
	
	public boolean verifyPassword(String password){
		return this.password.equals(password);
	}
}