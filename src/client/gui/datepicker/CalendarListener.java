package client.gui.datepicker;

import java.util.EventListener;

public interface CalendarListener extends EventListener {
	public void calendarChanged(CalendarEvent event);
}
