/**
 * 
 */
package de.quaddy_services.report.format;

import java.text.DecimalFormat;

public class DayTimeFormat implements TimeFormat {
	private String name;
	private int hoursPerDay;

	DayTimeFormat(String aName, int aHoursPerDay) {
		// protected
		name = aName;
		hoursPerDay = aHoursPerDay;
		int tempOneHour = 60 * 60 * 1000;
		FACTOR = (hoursPerDay * tempOneHour) / 100;
	}

	private DecimalFormat format = new DecimalFormat("00.00");

	@Override
	public String format(long aTimeInMillis) {
		double tempCalc = aTimeInMillis;
		int tempOneHour = 60 * 60 * 1000;
		tempCalc = tempCalc / (hoursPerDay * tempOneHour);
		String tempFormated = format.format(tempCalc);
		return tempFormated;
	}

	private final int FACTOR;

	@Override
	public long roundToDisplay(long aTimeInMillis) {
		int tempRounded = ((int) Math.round(1.0 * aTimeInMillis / FACTOR)) * FACTOR;
		return tempRounded;
	}

	@Override
	public int maxLength() {
		return 5;
	}

	@Override
	public String getName() {
		return name;
	}

}