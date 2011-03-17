package client.gui.datepicker;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class CalendarGridPanel extends JPanel implements ActionListener, PropertyChangeListener {
	private final PropertyChangeSupport propertyChange = new PropertyChangeSupport(
			this);

	private static final Locale locale = new Locale("no");
	private CalendarGrid calendarGrid;
	private GridButton[][] buttonGrid = new GridButton[7][8];

	public CalendarGridPanel() {
		super(new GridLayout(7, 8));
		calendarGrid = new CalendarGrid(locale);
		addButtonGrid();
		setSelected(Calendar.getInstance());
	}

	private void addButtonGrid() {
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 8; j++) {
				GridButton b = new GridButton(i, j);
				b.addActionListener(this);
				group.add(b);
				add(b);
				buttonGrid[i][j] = b;
			}
		}
	}

	private void updateCalendar() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 8; j++) {
				GridButton b = buttonGrid[i][j];
				if (i == 0 && j == 0) {

				} else if (i == 0) {
					Calendar calendar = calendarGrid.getValue(i, j - 1);
					b.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK,
							Calendar.SHORT, locale));
				} else if (j == 0) {
					Calendar calendar = calendarGrid.getValue(i - 1, j);
					b.setText("" + calendar.get(Calendar.WEEK_OF_YEAR));
				} else {
					Calendar calendar = calendarGrid.getValue(i - 1, j - 1);
					b.setText("" + calendar.get(Calendar.DATE));
				}
			}
		}
	}

	private void setSelected(Calendar calendar) {
		Calendar oldCalendar = (Calendar) calendarGrid.getSelected();
		if (oldCalendar.equals(calendar)) return;
		calendarGrid.setSelected(calendar);
		updateCalendar();
		propertyChange.firePropertyChange("calendar", oldCalendar,
				calendarGrid.getSelected());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GridButton b = (GridButton) e.getSource();
		int row = b.getRow();
		int column = b.getColumn();
		if (row == 0 && column == 0) {

		} else if (row == 0) {

		} else if (column == 0) {

		} else {
			Calendar calendar = calendarGrid.getValue(b.getRow() - 1, b
					.getColumn() - 1);
			setSelected(calendar);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		setSelected((Calendar) evt.getNewValue());
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

class GridButton extends JToggleButton {
	protected int row, column;

	public GridButton(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		if (row != 0 && column != 0) {
			setBorderPainted(false);
		}
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
}