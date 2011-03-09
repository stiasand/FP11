package client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {
	private JTextField usernameComponent, passwordComponent;
	private JButton submitComponent;
	private JLabel usernameLabel, passwordLabel;
	
	public LoginPanel(){
		setLayout(new GridBagLayout());
		
		usernameComponent = new JTextField();
		usernameComponent.setColumns(10);
		passwordComponent = new JTextField();
		passwordComponent.setColumns(10);
		submitComponent = new JButton("Log in");
		usernameLabel = new JLabel("User name:");
		passwordLabel = new JLabel("Password:");

		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		c1.gridy = 0;
		c1.gridx = 0;
		c1.anchor = GridBagConstraints.WEST;
		c2.gridy = 0;
		c2.gridx = 1;
		c2.anchor = GridBagConstraints.EAST;
		
		add(usernameLabel,c1);	
		add(usernameComponent,c2);
		
		c1.gridy = 1;
		c2.gridy = 1;
		add(passwordLabel,c1);
		add(passwordComponent,c2);
		
		c2.gridy = 2;
		add(submitComponent,c2);
	}
}
