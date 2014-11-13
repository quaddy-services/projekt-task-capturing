/**
 * 
 */
package de.quaddy_services.report.groupby;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.quaddy_services.ptc.store.Task;

public class GroupByDay extends GroupBy {
	GroupByDay(String aName) {
		// protected
		super(aName);
	}

	private Calendar tempCal1 = Calendar.getInstance();
	private Calendar tempCal2 = Calendar.getInstance();

	public int compare(Task t1, Task t2) {
		tempCal1.setTime(t1.getStart());
		tempCal2.setTime(t2.getStart());
		if (tempCal1.get(Calendar.DAY_OF_YEAR) == tempCal2
				.get(Calendar.DAY_OF_YEAR)
				&& tempCal1.get(Calendar.YEAR) == tempCal2.get(Calendar.YEAR)) {
			return 0;
		}
		if (t1.getStart().getTime() < t2.getStart().getTime()) {
			return -1;
		}
		return 1;
	}

	private DateFormat DATE_FORMAT = DateFormat
			.getDateInstance(DateFormat.SHORT);

	private DateFormat DAY_FORMAT = new SimpleDateFormat("EEE");

	private DateFormat WEEK_FORMAT = new SimpleDateFormat("ww");

	@Override
	public String getGroupName(Task t) {
		return DATE_FORMAT.format(t.getStart()) + " ("
				+ DAY_FORMAT.format(t.getStart()) + "/W"
				+ WEEK_FORMAT.format(t.getStart()) + ")";
	}
}