package client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	private GridBagConstraints c = new GridBagConstraints();
	private int colorCounter = 1;
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // the list and the cell have the focus
	{
		System.out.println("inside Renderer");
//		JPanel panel = new JPanel();
		JLabel defaultComp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		setIcon(narutoIcon);
		if (isSelected) defaultComp.setBackground(Color.GRAY);
		else defaultComp.setBackground(null);
//		if( colorCounter % 2 == 0){
//			defaultComp.setBackground(Color.LIGHT_GRAY);
//			colorCounter = 1;
//		}else colorCounter = 2;
//		panel.add(new JLabel(value.toString()), c);
//		panel.add(new JButton("jada"), c);
//		if (isSelected) panel.setBackground(Color.GRAY);
//		else panel.setBackground(null);
//		String tempValue = value.toString(); //TODO change to correct object. It is currently just a random list for testing purpose.
		
		return defaultComp;


	}


}
