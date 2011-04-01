package client.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;


import model.Employee;
import model.Model;





public class ContactsPanel extends JPanel implements ListSelectionListener, PropertyChangeListener{

	private Model model = new Model();
	private DefaultComboBoxModel employeesComboBoxModel;

	private JComboBox employeeComboBox;
	private JButton deleteButton;
	private JLabel contactsLabel;
	private JList employeeList;
	private DefaultListSelectionModel listSelectionModel;
	private JScrollPane employeeScrollPane;
	private DefaultListModel employeeListModel;
	private GridBagConstraints c = new GridBagConstraints();

	public ContactsPanel(){
		super(new GridBagLayout());
		//create the model for the comboBox
		System.out.println(model.getEmployees().get(0));
		employeesComboBoxModel = model.getEmployeesAsComboBoxModel();
		//create combobox
		employeeComboBox = new JComboBox(employeesComboBoxModel);
		employeeComboBox.setSelectedIndex(0); 
		employeeComboBox.addActionListener(new ContactsPanelActionListener());

		contactsLabel = new JLabel("Vis Kontakters Avtaler:");
		deleteButton = new JButton("-");
		deleteButton.addActionListener(new MyActionListener());
		//create model to list
		employeeListModel = new DefaultListModel();



		//create List
		employeeList = new JList(employeeListModel);
		listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		employeeList.setSelectionModel(listSelectionModel);
		employeeList.addKeyListener(new MyKeyListener());

		//adding a scroll pane to the JList
		employeeScrollPane = new JScrollPane(employeeList);
		employeeScrollPane.setPreferredSize(new Dimension(350,200));
		employeeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		employeeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		employeeList.setCellRenderer(new ContactsListCellRenderer());
		addComponents();

		SetAsListenerOn(model);

	}

	void addComponents(){
		c.gridy = 0;
		c.gridx = 0;
		c.anchor = GridBagConstraints.WEST;
		add(contactsLabel, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
		add(employeeComboBox, c);
		c.gridx = 2;
		c.gridy = 0;
		add(deleteButton, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		add(employeeScrollPane, c);
	}

	class ContactsPanelActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(employeeComboBox.isFocusOwner()){
				Employee selectedItem = (Employee) employeeComboBox.getSelectedItem();
				employeeListModel.addElement(selectedItem);
				employeesComboBoxModel.removeElement(selectedItem);

			}
		}


	}
	public JComboBox getEmployeeComboBox() {
		return employeeComboBox;
	}
	public JLabel getContactsLabel() {
		return contactsLabel;
	}
	public JScrollPane getEmployeeScrollPane() {
		return employeeScrollPane;
	}

	public void SetAsListenerOn(Model theModel) {
		model = theModel;
		model.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}
	@Override
	public void valueChanged(ListSelectionEvent event) {

		try {
			if(!listSelectionModel.isSelectionEmpty()){
				String selectedItem = (String) employeeListModel.getElementAt(listSelectionModel.getAnchorSelectionIndex());
				System.out.println("Employees name: " + selectedItem);
			}
		} catch (Exception e) {
			System.out.println("error occured" + e);
			e.printStackTrace();
		}

	}

	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(deleteButton.isFocusOwner()){
				try {
					Object selectedItem = employeeListModel.getElementAt(listSelectionModel.getAnchorSelectionIndex());
					employeeListModel.removeElement(selectedItem);
					System.out.println(selectedItem.toString());
					System.out.println("delete button");
					System.out.println("ActionListener, actionPerformed. Delete Button pressed.");
					employeesComboBoxModel.addElement(selectedItem);
				} catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
					System.out.println("Select Person to remove");
				} catch (Exception e2){
					e2.printStackTrace();
				}
			}			
		}
	}

	class MyKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int keyCode = arg0.getKeyCode();
			if(employeeList.isFocusOwner() && !employeeListModel.isEmpty() && employeeList.getSelectedValue() != null){
				if(keyCode == arg0.VK_DELETE || keyCode == arg0.VK_BACK_SPACE){
					int selectedIndex = employeeList.getSelectedIndex();
					Employee selectedElement = (Employee) employeeListModel.getElementAt(selectedIndex);
					employeeListModel.removeElement(selectedElement);
					employeesComboBoxModel.addElement(selectedElement);

				}
			}

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
}