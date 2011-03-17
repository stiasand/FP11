package model;


public class AddEmployee implements Action {
	Employee employee;

	@Override
	public void performClientAction() {
		// Change model
	}
	public void performServerAction(){
		//try to change database
		//if successful
		//	change model
		//	send action to clients
	}
	
	public String toXML(){
		return "";
	}
	public AddEmployee fromXML(String xml){
		return this;
	}
}


