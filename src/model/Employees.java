package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

public class Employees extends ArrayList<Employee>{
	protected final PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
	protected HashMap<String, Employee> employeeMap = new HashMap<String, Employee>();
	
	public Employee logOn(String username, String password){
		if (employeeMap.containsKey(username)){
			Employee employee = employeeMap.get(username);
			if (employee.verifyPassword(password)){
				return employee;
			}
		}
		return null;
	}
}
