package client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JPanel;

class CalendarPanel extends JPanel {
	public static final Locale locale = new Locale("no");
	public static final int millisWidth = 1000 * 60 * 60 * 24; // day
	public static final int millisHeight = 1000 * 60 * 15; // quarter
	public static final int verticalGridLineAfterEvery = 8; // 2 hours per gridline

	private Calendar showFrom;
	private Calendar showTo;

	private int gridWidth;
	private int gridHeight;

	private ArrayList<AppointmentPanel> panels = new ArrayList<AppointmentPanel>();

	public CalendarPanel() {
		super(null);

		showWeek();
		AppointmentPanel panel = new AppointmentPanel();
		panel.setBackground(Color.red);

		panel.from = Calendar.getInstance();
		panel.from.set(2011, 2, 10, 12, 0, 0);
		panel.to = Calendar.getInstance();
		panel.to.set(2011, 2, 10, 16, 15, 0);

		panels.add(panel);
		
		add(panel);
	}

	public void placePanel(AppointmentPanel panel) {
		int[] fromPosition = getPosition(panel.from);
		int[] toPosition = getPosition(panel.to);

		panel.setSize(getWidth() / gridWidth, toPosition[1] - fromPosition[1]);
		panel.setLocation(fromPosition[0], fromPosition[1]);
	}

	public void showWeek() {
		showWeek(Calendar.getInstance(locale));
	}

	private void moveToStartOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	private void moveToEndOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}

	public void showWeek(Calendar calendar) {
		Calendar showFrom = (Calendar) calendar.clone();
		Calendar showTo = (Calendar) calendar.clone();

		int diff = showFrom.getFirstDayOfWeek()
				- showFrom.get(Calendar.DAY_OF_WEEK);
		showFrom.add(Calendar.DATE, diff);
		showTo.add(Calendar.DATE, 7 + diff);

		moveToStartOfDay(showFrom);
		moveToEndOfDay(showTo);
		setShowRange(showFrom, showTo);
	}

	public int[] getGridIndex(Calendar calendar) {
		int[] index = new int[2];
		int dt = (int) (calendar.getTimeInMillis() - showFrom.getTimeInMillis());
		index[0] = dt / millisWidth;
		index[1] = (dt - index[0] * millisWidth) / millisHeight;
		return index;
	}

	public int[] getPosition(Calendar calendar) {
		int[] position = new int[2];
		int[] index = getGridIndex(calendar);
		position[0] = (index[0] * getWidth()) / gridWidth;
		position[1] = (index[1] * getHeight()) / gridHeight;
		return position;
	}

	public void setShowRange(Calendar showFrom, Calendar showTo) {
		this.showFrom = showFrom;
		this.showTo = showTo;
		int[] index = getGridIndex(showTo);
		gridWidth = index[0];
		gridHeight = index[1];
	}

	private void paintGrid(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		for (int i = 0; i <= gridWidth; i++) {
			int x = i * w / gridWidth;
			g.drawLine(x, 0, x, h);
		}
		for (int i = 0; i <= gridHeight / verticalGridLineAfterEvery; i++) {
			int y = i * h * verticalGridLineAfterEvery / gridHeight;
			g.drawLine(0, y, w, y);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintGrid(g);
		for (AppointmentPanel panel : panels){
				placePanel(panel);
		}
	}
}

class AppointmentPanel extends JPanel{
	public Calendar from, to;
}