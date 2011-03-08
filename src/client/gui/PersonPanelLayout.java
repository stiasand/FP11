package client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class PersonPanelLayout extends JPanel{

	private PersonPanel myPanel;
	private Person model;
	
	public PersonPanelLayout(){
		GridBagConstraints c;
		model = new Person("John");
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		myPanel = new PersonPanel();
		add(myPanel);
		
		model.setName("John");
		
		positionOfComponents(c);

	}
	private void positionOfComponents(GridBagConstraints c) {
		
		c.anchor = c.WEST;
		c.gridx = 0;
		c.gridy = 0;
		add(myPanel.getNameLabel(), c);
		
		c.gridx = 1;
		c.gridy = 0;
		add(myPanel.getNameTextField(), c);


    	c.gridx = 0;
    	c.gridy = 1;
    	add(myPanel.getBirthdayLabel(), c);

    	c.gridx = 1;
    	c.gridy = 1;
    	add(myPanel.getBirthdayTextField(), c);

    	c.gridx = 0;
    	c.gridy = 2;
    	add(myPanel.getEmailLabel(), c);
    	
    	c.gridx = 1;
    	c.gridy = 2;
    	add(myPanel.getEmailTextField(), c);
    	
    	c.gridx = 0;
    	c.gridy = 3;
    	add(myPanel.getGenderLabel(), c);
    	
    	c.gridx = 1;
    	c.gridy = 3;
    	add(myPanel.getGenderComboBox(), c);
    	
    	
    	c.gridx = 0;
    	c.gridy = 4;
    	add(myPanel.getHeightLabel(), c);

    	c.gridx = 1;
    	c.gridy = 4;
    	add(myPanel.getHeightSlider(), c);


	}
	public static void main(String[] args) {
        JFrame frame = new JFrame("GridBag example");
        frame.getContentPane().add(new PersonPanelLayout());
        frame.pack();
        frame.setVisible(true);  
	}

}
