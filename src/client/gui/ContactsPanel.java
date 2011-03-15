package client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;





public class ContactsPanel extends JPanel implements ListSelectionListener{
	
	//TODO use the models list of employees instead of this tempList.
	private String[] tempListModel = {"Nils","Petter","Jens","Beate","Tiril", "SupaJaaahn"};
	private JComboBox employeeComboBox;
	private JLabel contactsLabel;
	private JList employeeList;
	private DefaultListSelectionModel listSelectionModel;
	private JScrollPane employeeScrollPane;
	private DefaultListModel employeeListModel;
	private GridBagConstraints c;
	
	public ContactsPanel(){
		c = new GridBagConstraints();
	
		employeeComboBox = new JComboBox(tempListModel);
		employeeComboBox.setSelectedIndex(4); 
		employeeComboBox.addActionListener(new ContactsPanelActionListener());
		
		contactsLabel = new JLabel("Vis Kontakters Avtaler:");

		employeeListModel = new DefaultListModel();
		employeeListModel.addElement(tempListModel[2]); 
		employeeListModel.addElement(tempListModel[5]); 
		
		employeeList = new JList(employeeListModel);
		listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		employeeList.setSelectionModel(listSelectionModel);
		
		//adding a scroll pane to the JList
		employeeScrollPane = new JScrollPane(employeeList);
		employeeScrollPane.setPreferredSize(new Dimension(200,100));
		employeeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		employeeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
		employeeList.setCellRenderer(new ContactsListCellRenderer());

		
	}
	
	class ContactsPanelActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(employeeComboBox.isFocusOwner()){
				String selectedItem = (String) employeeComboBox.getSelectedItem();
				employeeListModel.addElement(selectedItem);
				//TODO remove the selected item from the employeeComoBox's listModel
				
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
	
	
	

}
