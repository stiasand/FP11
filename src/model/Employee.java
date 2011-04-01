package model;

import java.beans.PropertyChangeSupport;

public class Employee {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

	private String username, password, name;
	private Calendar calendar;
	
	public Employee(String username) {
		this.username = username;
	}
	
	public Employee(String username, String name) {
		this.username = username;
		this.name = name;
	}
	
	public Employee(String username, String password, String name) {
		this.username = username;
		this.password = password;
		this.name = name;
	}
	
	public boolean verifyPassword(String password){
		return this.password.equals(password);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}

	public String toString() {
		return username;
	}
}
