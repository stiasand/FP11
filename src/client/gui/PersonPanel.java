package client.gui;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;




public class PersonPanel extends JPanel implements PropertyChangeListener{

	private JTextField nameTextField;
	private JTextField emailTextField;
	private JTextField dateOfBirthTextField;
	private JComboBox genderComboBox;
	private JSlider heightSlider;
	private JLabel nameLabel;
	private JLabel emailLabel;
	private JLabel genderLabel;
	private JLabel birthdayLabel; 
	private JLabel heightLabel;

	
	private Person model = new Person("John"); 

	public PersonPanel(){
		genderComboBox = new JComboBox(Gender.values());
		heightSlider = new JSlider(120,220);
		heightSlider.setPreferredSize(new Dimension(250, 50));
		heightSlider.setMajorTickSpacing(10);
		heightSlider.setMinorTickSpacing(5);
		heightSlider.setPaintTicks(true);
		heightSlider.setPaintLabels(true);
		heightSlider.setValue(170);

		nameTextField = new JTextField(20); 
		emailTextField = new JTextField(20); 
		dateOfBirthTextField = new JTextField(20); 
		

		nameLabel = new JLabel("Name");
		emailLabel = new JLabel("E-Mail");
		genderLabel = new JLabel("Gender");
		birthdayLabel = new JLabel("Birthday");
		heightLabel = new JLabel("Height");

		nameTextField.addKeyListener(new PersonPanelKeyListener());
		emailTextField.addKeyListener(new PersonPanelKeyListener());
		dateOfBirthTextField.addKeyListener(new PersonPanelKeyListener());
		genderComboBox.addActionListener(new PersonPanelActionListener());
		heightSlider.addChangeListener(new PersonPanelChangeListener());
		

		
		
		SetAsListenerOn(model);
//		setDefaultModel();
	}



	private void setDefaultModel() {
		model.setHeight(170);
		model.setDateOfBirth("ddmmyyyy");
		model.setName("Ola Nordmann");
		model.setEmail("ola@nordmann.no");
		model.setGender(Gender.male);
	}



	public void setModel(Person model){
		this.model = model;
		displayModelInfo(model);
		
	}
	private void displayModelInfo(Person model2) {
		nameTextField.setText(model.getName());
		emailTextField.setText(model.getEmail());
		dateOfBirthTextField.setText(model.getDateOfBirth());
		heightSlider.setValue(model.getHeight());
		genderComboBox.setSelectedItem(model.getGender());
	}



	public Person getModel(){
		return model;
	}

	public JTextField getNameTextField() {
		return nameTextField;
	}

	public void setNameTextField(JTextField nameTextField) {
		this.nameTextField = nameTextField;
	}

	public JTextField getEmailTextField() {
		return emailTextField;
	}

	public void setEmailTextField(JTextField emailTextField) {
		this.emailTextField = emailTextField;
	}

	public JTextField getBirthdayTextField() {
		return dateOfBirthTextField;
	}

	public void setBirthdayTextField(JTextField birthdayTextField) {
		this.dateOfBirthTextField = birthdayTextField;
	}

	public JComboBox getGenderComboBox() {
		return genderComboBox;
	}

	public void setGenderComboBox(JComboBox genderComboBox) {
		this.genderComboBox = genderComboBox;
	}

	public JSlider getHeightSlider() {
		return heightSlider;
	}

	public void setHeightSlider(JSlider heightSlider) {
		this.heightSlider = heightSlider;
	}

	public JLabel getNameLabel() {
		return nameLabel;
	}

	public void setNameLabel(JLabel nameLabel) {
		this.nameLabel = nameLabel;
	}

	public JLabel getEmailLabel() {
		return emailLabel;
	}

	public void setEmailLabel(JLabel emailLabel) {
		this.emailLabel = emailLabel;
	}

	public JLabel getGenderLabel() {
		return genderLabel;
	}

	public void setGenderLabel(JLabel genderLabel) {
		this.genderLabel = genderLabel;
	}

	public JLabel getBirthdayLabel() {
		return birthdayLabel;
	}

	public void setBirthdayLabel(JLabel birthdayLabel) {
		this.birthdayLabel = birthdayLabel;
	}

	public JLabel getHeightLabel() {
		return heightLabel;
	}

	public void setHeightLabel(JLabel heightLabel) {
		this.heightLabel = heightLabel;
	}

	class PersonPanelActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(genderComboBox.isFocusOwner()){
				model.setGender((Gender)genderComboBox.getSelectedItem());
			}
		}


	}
	
	class PersonPanelChangeListener implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent arg0) {
			model.setHeight(heightSlider.getValue());
			System.out.println(heightSlider.getValue());
		}

	}
	class PersonPanelKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCodeEnter = e.VK_ENTER; 
				if(e.getKeyCode() == keyCodeEnter){
					if(nameTextField.isFocusOwner()){
						model.setName(nameTextField.getText());
					System.out.println("Hei");
					}else if(emailTextField.isFocusOwner()){
						model.setEmail(emailTextField.getText());
					}else if(dateOfBirthTextField.isFocusOwner()){
						model.setDateOfBirth(dateOfBirthTextField.getText());
					}
				}
		}

		private void printInfoAboutModel() {
		}

		private void updatePerson() {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}
	public void SetAsListenerOn(Person theModel) {
		model = theModel;
		model.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == Person.NAME_PROPERTY) {
			emailTextField.setText(model.getName());
		}else if (evt.getPropertyName() == Person.DATEOFBIRTH_PROPERTY){
			nameTextField.setText(model.getDateOfBirth());
		}else if(evt.getPropertyName() == Person.EMAIL_PROPERTY){
			dateOfBirthTextField.setText((String)evt.getNewValue());
		}else if(evt.getPropertyName() == Person.HEIGHT_PROPERTY){
			heightSlider.setValue(model.getHeight());
		}else if(evt.getSource() == Person.GENDER_PROPERTY){
			genderComboBox.setSelectedItem(model.getGender());
		}
	}
}

