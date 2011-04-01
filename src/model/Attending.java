package model;

public class Attending {
	protected Employee employee;
	protected int status;
	
	// TODO: Any way to make this into an enum that returns ints?
	public static final int STATUS_NOTRECIEVED = 0;
	public static final int STATUS_RECIEVED = 1;
	public static final int STATUS_NOTACCEPTED = 2;
	public static final int STATUS_ACCEPTED = 3;
	
	public Attending(Employee employee) {
		this(employee, Attending.STATUS_NOTRECIEVED);
	}
	
	public Attending(Employee employee, int status) {
		this.employee = employee;
		this.status = status;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
	public int getStatus() {
		return status;
	}
}