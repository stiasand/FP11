package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/*
 * Lager en klasse for testing.
 * Det er ikke n¿dvendigvis slik vi vil gj¿re det, men her er et fors¿k.
 */
public class Model{
	public static final String EMPLOYEE_PROPERTY  = "employee change";
	public static final String ADD_APPOINTMENT_PROPERTY  = "appointment added";
	public static final String REMOVE_APPOINTMENT_PROPERTY  = "appointment removed";
	
	
//	private Employee employee;
//	private Appointment appointment;
	private List<Appointment> appointments = new ArrayList<Appointment>();
	private Employees employees = new Employees();
	private PropertyChangeSupport pcs;
	private List<Room> rooms = new ArrayList<Room>();
	
	public Model(){
		employees.add(new Employee("Dough","kim"));
		employees.add(new Employee("Jin","kim"));
		employees.add(new Employee("Solo","kim"));
		
		appointments.add(new Appointment(0,null, new Date(3452), new Date(100), new Date(3452345), 
				"F¿rste testm¿te", "P15", new Room("roomName",14,"roomDescription")));
		System.out.println(employees.get(0));
		System.out.println(employees.get(1));
		System.out.println(employees.get(2));
		appointments.add(new Meeting(0,null,employees, null, null, null, null, null, null));
		
		rooms.add(new Room("P15",14,"Gløs en plass"));
		pcs = new PropertyChangeSupport(this);
		
		
	}
	public void addAppointment(Appointment appointment){
		String oldValue = "oldValue";
		Appointment newValue = appointment;
		appointments.add(appointment);
		pcs.firePropertyChange(ADD_APPOINTMENT_PROPERTY, oldValue, newValue);
	}

	public Employee[] getEmployeesAsArray() {
		Employee[] e = new Employee[employees.size()];
		for (int i = 0; i < e.length; i++) {
			e[i] = employees.get(i);
		}
		return e;
	}

	public List<Employee> getEmployees(){
		return employees;
	}

	public DefaultComboBoxModel getEmployeesAsComboBoxModel() {
		DefaultComboBoxModel m = new DefaultComboBoxModel();
		for (Employee e : employees) {
			m.addElement(e);
		}
		return m;
	}
	public DefaultComboBoxModel getAppointmentsAsComboBoxModel(){
		DefaultComboBoxModel m = new DefaultComboBoxModel();
		for (Appointment a : appointments) {
			m.addElement(a);
		}
		return m;
	}
	public DefaultComboBoxModel getRoomsAsComboBoxModel() {
		DefaultComboBoxModel m = new DefaultComboBoxModel();
		for (Room r : rooms) {
			m.addElement(r);
		}
		return m;
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
		pcs.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		pcs.removePropertyChangeListener(listener);
	}

}
