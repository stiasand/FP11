package client.gui.datepicker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CalendarSwitcher extends JPanel implements PropertyChangeListener {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(
			this);
	
	public static final String previousSymbol = "<";
	public static final String nextSymbol = ">";

	private JLabel label;
	private Locale locale;
	private Calendar calendar;
	private int field;
	
	public CalendarSwitcher(int field, Locale locale) {
		super(new BorderLayout());
		this.field = field;
		this.locale = locale;
		addButtons();
		addLabel();
		
		setCalendar(Calendar.getInstance(locale));
	}

	public void setCalendar(Calendar calendar){
		System.out.println(calendar.get(Calendar.DATE));
		if (calendar.equals(this.calendar)) return;
		propertyChange.firePropertyChange("calendar", this.calendar,
				calendar);
		this.calendar = calendar;
		setLabelText();
	}
	
	public void setLabelText(){
		String text = "";
		text = calendar.getDisplayName(field, Calendar.LONG, locale);
		if (text == null) text = ""+calendar.get(field);
		label.setText(text);
	}
	
	private void addButtons() {
		JButton prevButton = new JButton(new AbstractAction(previousSymbol) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = (Calendar) calendar.clone();
				cal.add(field, -1);
				setCalendar(cal);
			}
		});
		JButton nextButton = new JButton(new AbstractAction(nextSymbol) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = (Calendar) calendar.clone();
				cal.add(field, 1);
				setCalendar(cal);
			}
		});

		add(prevButton, BorderLayout.WEST);
		add(nextButton, BorderLayout.EAST);
	}
	
	private void addLabel(){
		label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);
		add(label, BorderLayout.CENTER);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		setCalendar((Calendar) evt.getNewValue());
	}
	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		propertyChange.addPropertyChangeListener(propertyName, propertyChangeListener);
	}
	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		propertyChange.removePropertyChangeListener(propertyName, propertyChangeListener);
	}
}
