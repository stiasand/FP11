package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DatePicker extends JPanel {
	public static final String previousSymbol = "◂";
	public static final String nextSymbol = "▸";
	public static final String language = "no";
	
	protected final Locale locale = new Locale(language);
	protected final Calendar calendar = Calendar.getInstance(locale);
	protected JPanel calendarPanel = new JPanel(new GridLayout(7, 8));
	protected JPanel navigationPanel = new JPanel(new BorderLayout());
	protected JButton[][] buttonGrid = new JButton[7][8];
	protected JLabel monthLabel = new JLabel();
	protected JLabel yearLabel = new JLabel();
	
	public DatePicker() {
		super(new BorderLayout());
		addCalendar();
		addNavigation();
		updateCalendar();
		setMonthDays();
	}
	
	private void addSwitcher(final int field, JLabel label, String constraints) {
		JPanel panel = new JPanel();
		JButton prevButton = new JButton(previousSymbol);
		JButton nextButton = new JButton(nextSymbol);

		label.setHorizontalAlignment(JLabel.CENTER);
		
		prevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calendar.add(field, -1);
				updateCalendar();
			}
		});
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calendar.add(field, 1);
				updateCalendar();
			}
		});
		
		panel.add(prevButton);
		panel.add(label);
		panel.add(nextButton);

		navigationPanel.add(panel, constraints);
	}
	
	private void updateLabels(){
		monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale));
		yearLabel.setText(""+calendar.get(Calendar.YEAR));
	}
	
	private void addNavigation(){
		addSwitcher(Calendar.MONTH, monthLabel, BorderLayout.WEST);
		addSwitcher(Calendar.YEAR, yearLabel, BorderLayout.EAST);
		updateLabels();
		
		add(navigationPanel, BorderLayout.NORTH);
	}
	
	private void addCalendar() {
		for (int i = 0; i < 7; i++){
			for(int j=0; j<8; j++){
				JButton b = new JButton();
				if (i>0 && j>0){
					b.setBorderPainted(false);
				}
				calendarPanel.add(b);
				buttonGrid[i][j] = b;
			}
		}
		add(calendarPanel, BorderLayout.CENTER);
	}

	private void setMonthDays(){
		Calendar calendar = (Calendar) this.calendar.clone();
		
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		for (int i=1; i<8; i++){
			JButton b = buttonGrid[0][i];
			String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
			b.setText(day);
			calendar.add(Calendar.DATE, 1);
		}
	}
	
	private void updateCalendar(){
		Calendar calendar = (Calendar) this.calendar.clone();

		updateLabels();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek()-dayOfWeek);
		for (int i=1; i<7; i++){
			JButton b = buttonGrid[i][0];
			b.setText(""+calendar.get(Calendar.WEEK_OF_YEAR));
			for (int j=1; j<8; j++){
				b = buttonGrid[i][j];
				b.setText(""+calendar.get(Calendar.DATE));
				if (calendar.get(Calendar.MONTH) == month){
					b.setEnabled(true);
				} else{
					b.setEnabled(false);
				}
				calendar.add(Calendar.DATE, 1);
			}
		}
		calendar.set(Calendar.MONTH, month);
	}
}