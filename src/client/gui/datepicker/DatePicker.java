package client.gui.datepicker;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DatePicker extends JPanel {
	public static final String language = "no";
	protected final Locale locale = new Locale(language);

	protected Calendar selectedCalendar = Calendar.getInstance(locale);
	protected int selectedField = Calendar.DATE;

	protected CalendarGridPanel calendarGridPanel = new CalendarGridPanel();
	protected JPanel navigationPanel = new JPanel(new BorderLayout());
	protected JLabel monthLabel = new JLabel();
	protected JLabel yearLabel = new JLabel();

	public DatePicker() {
		super(new BorderLayout());
		add(calendarGridPanel, BorderLayout.CENTER);
		addNavigation();
	}

	public void setSelected(Calendar calendar, int field) {
		System.out.println(field);
		this.selectedCalendar = calendar;
		this.selectedField = field;
	}

	private void addNavigation() {
		CalendarSwitcher monthSwitcher = new CalendarSwitcher(Calendar.MONTH, locale);
		CalendarSwitcher yearSwitcher = new CalendarSwitcher(Calendar.YEAR, locale);
		monthSwitcher.addPropertyChangeListener("calendar", calendarGridPanel);
		yearSwitcher.addPropertyChangeListener("calendar", calendarGridPanel);
		calendarGridPanel.addPropertyChangeListener("calendar", monthSwitcher);
		calendarGridPanel.addPropertyChangeListener("calendar", yearSwitcher);
		navigationPanel = new JPanel(new BorderLayout());
		navigationPanel.add(monthSwitcher, BorderLayout.WEST);
		navigationPanel.add(yearSwitcher, BorderLayout.EAST);
		add(navigationPanel, BorderLayout.NORTH);
	}
}