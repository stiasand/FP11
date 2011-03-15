package client.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class ContactsListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer{
	final static ImageIcon narutoIcon = new ImageIcon("/Users/john_edvard/Documents/Workshop/workspace/FP11/src/client/gui/naruto_icon.png");
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // the list and the cell have the focus
	{
		
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		String tempValue = (String)value; //TODO change to correct object. It is currently just a random list for testing purpose.
		setText("..."+tempValue);
		setIcon(narutoIcon);
		
		return label;


	}


}
