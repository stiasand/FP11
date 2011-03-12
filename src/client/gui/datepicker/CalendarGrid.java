package client.gui.datepicker;

import java.util.Calendar;
import java.util.Locale;

/*
 * The days of a month, laid out on a grid.
 */
public class CalendarGrid {
	/*
	 * The row takes values from 0 to 5, indicating the week within the month. 
	 * The column takes values from 0 to 6, indicating the day of the week.
	 * Note that the values in row 0 may be from the previous month,
	 * while the values in row 4 and 5 may be from the next month.
	 */
	private final Calendar[][] grid = new Calendar[6][7];

	/* Determines which month and year to display in the grid.
	 * Also determines the first day of the week.
	 */
	private Calendar selected;
	
	public CalendarGrid(Locale locale){
		selected = Calendar.getInstance(locale);
		updateGrid(selected);
	}
	
	public Calendar getSelected() {
		return selected;
	}
	
	public void setSelected(Calendar calendar) {
		if (needsUpdate(calendar)) updateGrid(calendar);
		this.selected = calendar;
	}

	private void updateGrid(Calendar calendar){
		calendar = (Calendar) calendar.clone();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayOfWeek);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				grid[i][j] = (Calendar) calendar.clone();
				calendar.add(Calendar.DATE, 1);
			}
		}		
	}
	
	private boolean needsUpdate(Calendar calendar){
		return calendar.get(Calendar.MONTH) != this.selected.get(Calendar.MONTH)
				|| calendar.get(Calendar.YEAR) != this.selected
						.get(Calendar.YEAR);
	}

	public Calendar getValue(int row, int column){
		return grid[row][column];
	}
}