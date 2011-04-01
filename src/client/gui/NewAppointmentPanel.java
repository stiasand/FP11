package client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.WindowConstants;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import model.Appointment;
import model.Employee;
import model.Meeting;
import model.Model;
import model.Room;

import sun.tools.tree.ThisExpression;

public class NewAppointmentPanel extends JFrame{

	private JTextField txtDescription = new JTextField(10);
	JPopupMenu popup = new JPopupMenu();

//	private JTextField txtStartDate = new JTextField(10);
//	private JTextField txtStartTime = new JTextField(5);
	private JSpinner spinnerStartTimeDate = new JSpinner();
	private JSpinner spinnerStartTimeHours = new JSpinner();

//	private JTextField txtEndDate = new JTextField(10);
//	private JTextField txtEndTime = new JTextField(5);
	private JSpinner spinnerEndTimeDate = new JSpinner();
	private JSpinner spinnerEndTimeHours = new JSpinner();
	
	//create spinner models
	private SpinnerDateModel modelEndTime = new SpinnerDateModel();
	private SpinnerDateModel modelStartTime = new SpinnerDateModel();
	private SpinnerDateModel modelStartDate = new SpinnerDateModel();
	private SpinnerDateModel modelEndDate = new SpinnerDateModel();

	private JComboBox cmbPlace = new JComboBox();
	private JComboBox cmbParticipants = new JComboBox();

	private JButton btnCancel = new JButton("Avbryt");
	private JButton btnConfirm = new JButton("Opprett");

	DefaultListModel participantsListModel= new DefaultListModel();
	JList lstParticipants = new JList(participantsListModel);
	JScrollPane participantsScrollPane = new JScrollPane(lstParticipants);

	private Model model;
	private DefaultComboBoxModel roomComboBoxModel; 
	private DefaultComboBoxModel participantsComboBoxModel; 

	NewAppointmentPanel(){
		setLayout(new GridBagLayout());

		// create model and get comboBoxModels
		model = new Model();
		roomComboBoxModel = model.getRoomsAsComboBoxModel();
		participantsComboBoxModel = model.getEmployeesAsComboBoxModel();

		//add models to comboBoxes
		cmbPlace.setModel(roomComboBoxModel);
		cmbParticipants.setModel(participantsComboBoxModel);

		//create listeners
		MyActionListener myActionListener = new MyActionListener();
		MyKeyListener myKeyListener = new MyKeyListener();
		//add actionListener to buttons, comboboxes
		btnCancel.addActionListener(myActionListener);
		btnConfirm.addActionListener(myActionListener);
		cmbParticipants.addActionListener(myActionListener);
		cmbPlace.addActionListener(myActionListener);
		//add keyListener to textFields and lists
		lstParticipants.addKeyListener(myKeyListener);

		//set calendar fields
		modelStartDate.setCalendarField(Calendar.DATE);
		modelStartTime.setCalendarField(Calendar.MINUTE);
		modelEndDate.setCalendarField(Calendar.DATE);
		modelEndTime.setCalendarField(Calendar.MINUTE);
		
		//set models and editors to spinners
		spinnerStartTimeHours.setModel(modelStartTime);
		spinnerEndTimeHours.setModel(modelEndTime);
		spinnerStartTimeDate.setModel(modelStartDate);
		spinnerEndTimeDate.setModel(modelEndDate);
		spinnerStartTimeHours.setEditor(new JSpinner.DateEditor(spinnerStartTimeHours, "h:mm a"));
		spinnerEndTimeHours.setEditor(new JSpinner.DateEditor(spinnerEndTimeHours, "h:mm a"));
		spinnerStartTimeDate.setEditor(new JSpinner.DateEditor(spinnerStartTimeDate, "dd/MM/yyyy"));
		spinnerEndTimeDate.setEditor(new JSpinner.DateEditor(spinnerEndTimeDate, "dd/MM/yyyy"));

		//adding a scroll pane to the JList
		participantsScrollPane.setPreferredSize(new Dimension(350,200));
		participantsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		participantsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		

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
		c.gridwidth=4;
		c.gridy=0;
		add(txtDescription, c);
		c.gridy++;

		c.gridwidth=1;

		add(spinnerStartTimeDate, c);
		c.gridx++;
		add(spinnerStartTimeHours, c);
		c.gridx--;

		c.gridy++;
		add(spinnerEndTimeDate, c);
		c.gridx++;
		add(spinnerEndTimeHours, c);
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
		add(participantsScrollPane, c);
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
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(btnCancel.isFocusOwner()){
				//closing the window
				dispose();
			}else if(btnConfirm.isFocusOwner()){
				//added date
				Date addedDate = new Date();
				
				//get startDate
				Date sDate = modelStartDate.getDate();
				Date sTime = modelStartTime.getDate();
				Date sd = new Date(sDate.getTime() + sTime.getTime());
//				System.out.println(sDate);
//				System.out.println(sTime);
//				System.out.println(sd.getTime());
//				System.out.println(sDate.getTime() + sTime.getTime());
				
				//get endDate
				Date eDate = modelEndDate.getDate();
				Date eTime= modelEndTime.getDate();
				Date ed = new Date(eDate.getTime() + eTime.getTime());
				
				//get description
				String description = txtDescription.getText();
				if(participantsListModel.isEmpty()){
					
				}else{
//					Meeting m = new Meeting(-1,null,participantsListModel,addedDate,sd,ed,description,);
					
				}
				//meeting
			}else if(cmbParticipants.isFocusOwner()){
				Employee selectedItem = (Employee) cmbParticipants.getSelectedItem();
				participantsListModel.addElement(selectedItem);
				participantsComboBoxModel.removeElement(selectedItem);
			}else if(cmbPlace.isFocusOwner()){

			}

		}
	}
	class MyKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int keyCode = arg0.getKeyCode();
			if(lstParticipants.isFocusOwner() && !participantsListModel.isEmpty() && lstParticipants.getSelectedValue() != null){
				if(keyCode == arg0.VK_DELETE || keyCode == arg0.VK_BACK_SPACE){
					int selectedIndex = lstParticipants.getSelectedIndex();
					Employee selectedElement = (Employee) participantsListModel.getElementAt(selectedIndex);
					participantsListModel.removeElement(selectedElement);
					participantsComboBoxModel.addElement(selectedElement);

				}
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}
	public static void main(String args[]){
		JFrame frame = new JFrame("Avtale");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		NewAppointmentPanel panel = new NewAppointmentPanel();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}
