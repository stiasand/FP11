package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class DatePicker extends JPanel {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(
			this);

	public static final String previousSymbol = "◂";
	public static final String nextSymbol = "▸";
	public static final String language = "no";

	protected final Locale locale = new Locale(language);

	protected final Calendar selected = Calendar.getInstance(locale);
	protected final Calendar from = Calendar.getInstance(locale);
	protected final Calendar to = Calendar.getInstance(locale);

	protected JPanel calendarPanel = new JPanel(new GridLayout(7, 8));
	protected JPanel navigationPanel = new JPanel(new BorderLayout());
	protected DatePickerButton[][] buttonGrid = new DatePickerButton[7][8];
	protected JLabel monthLabel = new JLabel();
	protected JLabel yearLabel = new JLabel();

	public DatePicker() {
		super(new BorderLayout());
		addCalendar();
		addNavigation();
		updateCalendar();
	}

	private void addSwitcher(final int field, JLabel label, String constraints) {
		JPanel panel = new JPanel();
		JButton prevButton = new JButton(previousSymbol);
		JButton nextButton = new JButton(nextSymbol);

		label.setHorizontalAlignment(JLabel.CENTER);

		prevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected.add(field, -1);
				updateCalendar();
			}
		});
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected.add(field, 1);
				updateCalendar();
			}
		});

		panel.add(prevButton);
		panel.add(label);
		panel.add(nextButton);

		navigationPanel.add(panel, constraints);
	}

	private void updateLabels() {
		monthLabel.setText(selected.getDisplayName(Calendar.MONTH,
				Calendar.LONG, locale));
		yearLabel.setText("" + selected.get(Calendar.YEAR));
	}

	private void addNavigation() {
		addSwitcher(Calendar.MONTH, monthLabel, BorderLayout.WEST);
		addSwitcher(Calendar.YEAR, yearLabel, BorderLayout.EAST);
		updateLabels();

		add(navigationPanel, BorderLayout.NORTH);
	}

	private void addCalendar() {
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 8; j++) {
				DatePickerButton b;
				if (i==0 && j==0){
					b = new DatePickerButton();
				} else if (j==0){
					b = new DatePickerWeekButton();
				} else if (i==0){
					b = new DatePickerDayButton();
				} else {
					b = new DatePickerDateButton();
				}
				group.add(b);
				calendarPanel.add(b);
				buttonGrid[i][j] = b;
			}
		}
		add(calendarPanel, BorderLayout.CENTER);
	}

	private void updateCalendar() {
		Calendar calendar = (Calendar) selected.clone();

		updateLabels();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayOfWeek);
		for (int i = 1; i < 7; i++) {
			buttonGrid[i][0].setCalendar((Calendar)calendar.clone(), month, locale);
			for (int j = 1; j < 8; j++) {
				if (i==1){
					buttonGrid[0][j].setCalendar((Calendar)calendar.clone(), month, locale);
				}
				buttonGrid[i][j].setCalendar((Calendar)calendar.clone(), month, locale);
				calendar.add(Calendar.DATE, 1);
			}
		}
		calendar.set(Calendar.MONTH, month);
	}
}

class DatePickerButton extends JToggleButton {
	protected Calendar calendar;

	public void setCalendar(Calendar calendar, int month, Locale locale){
		
	}
}

class DatePickerDayButton extends DatePickerButton {
	
	@Override
	public void setCalendar(Calendar calendar, int month, Locale locale) {
		this.calendar = calendar;
		setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale));
	}
	
}

class DatePickerDateButton extends DatePickerButton {
	public DatePickerDateButton(){
		super();
		setBorderPainted(false);
	}
	
	@Override
	public void setCalendar(Calendar calendar, int month, Locale locale) {
		this.calendar = calendar;
		setText("" + calendar.get(Calendar.DATE));
		if (calendar.get(Calendar.MONTH) == month) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
}

class DatePickerWeekButton extends DatePickerButton {
	
	@Override
	public void setCalendar(Calendar calendar, int month, Locale locale) {
		this.calendar = calendar;
		setText("" + calendar.get(Calendar.WEEK_OF_YEAR));
	}
}