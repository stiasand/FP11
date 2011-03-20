package client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class NewAppointmentPanel extends JPanel{
	
	private JTextField txtDescription = new JTextField(10);
	
	private JTextField txtStartDate = new JTextField(10);
	private JTextField txtStartTime = new JTextField(5);
	
	private JTextField txtEndDate = new JTextField(10);
	private JTextField txtEndTime = new JTextField(5);
	
	private JComboBox cmbPlace = new JComboBox();
	private JComboBox cmbParticipants = new JComboBox();
	
	private JButton btnCancel = new JButton("Avbryt");
	private JButton btnConfirm = new JButton("Opprett");
	
	NewAppointmentPanel(){
		setLayout(new GridBagLayout());
		
		DefaultListModel dummylistmodel= new DefaultListModel();
		dummylistmodel.addElement("Jens");
		dummylistmodel.addElement("Kari");
		JList lstParticipants = new JList(dummylistmodel);
		
		
		GridBagConstraints c = new GridBagConstraints();
		
		//place labels
		c.weightx=0;
		c.weighty=0;
		c.ipady=8;
		c.ipadx=8;
		c.anchor=GridBagConstraints.WEST;
		c.insets= new Insets(0, 5, 0, 0);
		c.gridx=0;
		c.gridy=0;
		add(new JLabel("Beskrivelse"), c);
		c.gridy=1;
		add(new JLabel("Start"), c);
		c.gridy=2;
		add(new JLabel("Slutt"), c);
		c.gridy=3;
		add(new JLabel("Sted"), c);
		c.gridy=4;
		add(new JLabel("Deltakere"), c);
		c.insets= new Insets(1, 1, 1, 1);
		
		c.weightx=1;
		//place textfields and combo boxes
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor=GridBagConstraints.EAST;
		c.gridx=1;
		c.gridwidth=2;
		c.gridy=0;
		add(txtDescription, c);
		c.gridy++;
		
		c.gridwidth=1;
		
		add(txtStartDate, c);
		c.gridx++;
		add(txtStartTime, c);
		c.gridx--;
		
		c.gridy++;
		add(txtEndDate, c);
		c.gridx++;
		add(txtEndTime, c);
		c.gridx--;
		
		
		//combo boxes
		c.gridwidth=2;
		
		c.ipady=0;
		c.gridy++;
		add(cmbPlace, c);
		
		c.gridy++;
		add(cmbParticipants, c);
		c.ipady=8;
		
		//Participants list
		c.weighty=1;
		c.gridy++;
		c.gridx=0;
		c.ipady=80;
		c.insets=new Insets(5, 5, 0, 5);
		//new Insets(top, left, bottom, right)
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.fill=GridBagConstraints.BOTH;
		add(lstParticipants, c);
		c.ipady=0;
		c.insets=new Insets(0, 1, 1, 1);
		
		c.weighty=0;
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.gridy++;
		JPanel p = new JPanel();
		p.add(btnCancel);
		p.add(btnConfirm);
		add(p,c);
		
		
		
		
	}
	
	public static void main(String args[]){
		JFrame frame = new JFrame("Avtale");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		NewAppointmentPanel panel = new NewAppointmentPanel();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
