package client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;


public class ContactsListCellRenderer extends DefaultListCellRenderer {
	final static ImageIcon narutoIcon = new ImageIcon("/Users/john_edvard/Documents/Workshop/workspace/FP11/src/client/gui/naruto_icon.png");
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // the list and the cell have the focus
	{
		JPanel panel = new JPanel();
		JLabel defaultComp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		panel.add(new JLabel((String) value));
		panel.add(new JButton("jada"));
		if (isSelected) panel.setBackground(defaultComp.getBackground());
		else panel.setBackground(null);
		String tempValue = (String)value; //TODO change to correct object. It is currently just a random list for testing purpose.
		//setText("..."+tempValue);
		//setIcon(narutoIcon);
		
		return panel;


	}


}
