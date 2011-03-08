package client.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;



public class Person {

	public static final String NAME_PROPERTY ="Name";
	public static final String HEIGHT_PROPERTY ="Height";
	public static final String DATEOFBIRTH_PROPERTY ="Age";
	public static final String GENDER_PROPERTY ="Gender";
	public static final String EMAIL_PROPERTY ="email";
	private PropertyChangeSupport pcs;
	
	private String name;
	private String dateOfBirth;
	private Gender gender;
	private String email;
	private int height;
	
	public Person(String name){
		pcs = new PropertyChangeSupport(this);
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		pcs.firePropertyChange(NAME_PROPERTY, oldValue, name);
		
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		String oldValue = this.dateOfBirth;
		this.dateOfBirth = dateOfBirth;
		pcs.firePropertyChange(DATEOFBIRTH_PROPERTY, oldValue, dateOfBirth);
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		String oldValue = this.email;
		this.email = email;
		pcs.firePropertyChange(EMAIL_PROPERTY, oldValue, email);
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
		pcs.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		pcs.removePropertyChangeListener(listener);
	}
	@Override
	public String toString(){
		if (name == null || name.equals("")){
			return"This is a person with no name";
		}else
			return name;
	}
}
