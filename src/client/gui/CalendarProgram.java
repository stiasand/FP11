package client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import client.gui.datepicker.DatePicker;

public class CalendarProgram implements PropertyChangeListener {
	private static CalendarPanel calendarPanel;
	private static LoginPanel loginPanel;
	private static DatePicker datePicker;
	private static JFrame calendarFrame;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		setupWindow();
	}

	private static void setupComponents(){
		GridBagConstraints c = new GridBagConstraints();
		
		datePicker = new DatePicker();
		datePicker.setPreferredSize(new Dimension(300,200));
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		calendarFrame.getContentPane().add(datePicker,c);
		
		calendarPanel = new CalendarPanel();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		calendarFrame.getContentPane().add(calendarPanel,c);
	}
	
	private static void setupWindow() {
		String windowTitle = Config.frameTitle;
		calendarFrame = new JFrame(windowTitle);
		calendarFrame.getContentPane().setLayout(new GridBagLayout());
		calendarFrame.setSize(Config.frameWidth, Config.frameHeight);
		calendarFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setupComponents();
		calendarFrame.setVisible(true);
	}

	public static String readResourceAsString(String resourceName) {
		BufferedReader f = new BufferedReader(new InputStreamReader(
				CalendarProgram.class.getResourceAsStream(resourceName)));
		StringBuilder s = new StringBuilder();
		try {
			while (f.ready()) {
				s.append(f.readLine());
			}
			return s.toString();
		} catch (IOException e) {
			return "";
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}
}
