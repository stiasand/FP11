package client.gui.datepicker;

import java.util.Calendar;
import java.util.EventObject;

public class CalendarEvent extends EventObject{
	public Calendar calendar;
	public int field;
	
	public CalendarEvent(Object source, Calendar calendar, int field) {
		super(source);
		this.calendar = calendar;
		this.field = field;
	}
}
