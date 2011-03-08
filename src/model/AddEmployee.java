package model;


public class AddEmployee implements Action {
	Employee employee;

	@Override
	public void performServerAction() {
		// Ask database to insert the employee
		// If the insertion was successful, send AddEmployee to every client
	}
	@Override
	public void performClientAction() {
		// Change model
	}
}
