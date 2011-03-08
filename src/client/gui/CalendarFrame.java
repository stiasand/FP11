package client.gui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class CalendarFrame {
	private static CalendarPanel calendarPanel;
	private static JFrame calendarFrame;

	public static void main(String[] args) {
		setupWindow();
	}

	private static void setupWindow() {
		String windowTitle = Config.frameTitle;
		calendarFrame = new JFrame(windowTitle);
		calendarPanel = new CalendarPanel();
		calendarFrame.add(calendarPanel);
		calendarFrame.setSize(Config.frameWidth, Config.frameHeight);
		calendarFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		calendarFrame.setVisible(true);
	}

	public static String readResourceAsString(String resourceName) {
		BufferedReader f = new BufferedReader(new InputStreamReader(
				CalendarFrame.class.getResourceAsStream(resourceName)));
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
}
