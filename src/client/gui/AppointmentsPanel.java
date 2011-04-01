package client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Appointment;
import model.Model;

public class AppointmentsPanel extends JPanel implements PropertyChangeListener{

	private DefaultComboBoxModel appointmentsComboBoxModel;


	private JComboBox appointmentComboBox;
	private JLabel appointmentLabel;
	private JButton appointmentButton;
	private GridBagConstraints c = new GridBagConstraints();
	private Model model = new Model();

	public AppointmentsPanel(){
		super(new GridBagLayout());

		appointmentsComboBoxModel = model.getAppointmentsAsComboBoxModel();
		appointmentComboBox = new JComboBox(appointmentsComboBoxModel);

		appointmentButton = new JButton("Ny");
		appointmentLabel = new JLabel("Avtaler: ");
		appointmentButton.addActionListener(new MyActionListener());

		addComponents();

		SetAsListenerOn(model);

	}

	private void addComponents() {
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		add(appointmentLabel, c);
		c.gridy = 1;
		c.gridx = 0;
		add(appointmentComboBox, c);
		c.gridy = 1;
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
		add(appointmentButton, c);
	}
	public void SetAsListenerOn(Model theModel) {
		model = theModel;
		model.addPropertyChangeListener(this);
	}
	public void addAppointment(Appointment appointment){
		model.addAppointment(appointment);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if(evt.getPropertyName().equalsIgnoreCase(Model.ADD_APPOINTMENT_PROPERTY)){
			appointmentsComboBoxModel.addElement(evt.getNewValue());

		}else if(evt.getPropertyName().equals(Model.REMOVE_APPOINTMENT_PROPERTY)){
			appointmentsComboBoxModel.removeElement(evt.getNewValue());
		}
	}

	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(appointmentButton.isFocusOwner()){
				System.out.println("spawn new window creating an appointment");
				NewAppointmentPanel nap = new NewAppointmentPanel();
				nap.setVisible(true);
				nap.setSize(new Dimension(300,400));
			}
		}

	}


}
