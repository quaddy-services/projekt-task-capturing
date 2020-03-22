/**
 * 
 */
package de.quaddy_services.report.format;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeTimeFormat implements TimeFormat {
	private String name;

	TimeTimeFormat(String aName) {
		// protected
		name = aName;
	}

	private Format format = new SimpleDateFormat("HH:mm");

	@Override
	public String format(long aTimeInMillis) {
		Calendar tempCal = Calendar.getInstance();
		tempCal.clear();
		tempCal.add(Calendar.MILLISECOND, (int) aTimeInMillis);
		String tempFormated = format.format(tempCal.getTime());
		return tempFormated;
	}

	@Override
	public int maxLength() {
		return 5;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long roundToDisplay(long aTimeInMillis) {
		// Round to minutes
		return Math.round(aTimeInMillis / (60 * 1000)) * (60 * 1000);
	}

}