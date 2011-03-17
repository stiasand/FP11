package client.gui.datepicker;

import java.util.Calendar;

public class CalendarSelection {
	public Calendar calendar;
	public int field;
	
	public CalendarSelection(Calendar calendar, int field){
		this.calendar = calendar;
		this.field = field;
	}
}
