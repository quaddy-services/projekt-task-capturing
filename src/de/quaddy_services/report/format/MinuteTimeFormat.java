/**
 * 
 */
package de.quaddy_services.report.format;

import java.text.DecimalFormat;

public class MinuteTimeFormat implements TimeFormat {
	private String name;
	MinuteTimeFormat(String aName) {
		// protected
		name = aName;
	}
	private DecimalFormat format = new DecimalFormat("00");

	public String format(long aTimeInMillis) {
		long tempSec = (aTimeInMillis / 1000) % 60;
		long tempMin = aTimeInMillis / (60 * 1000);
		return format.format(tempMin) + ":" + format.format(tempSec);
	}

	public int maxLength() {
		return 6;
	}

	public String getName() {
		return name;
	}

	public long roundToDisplay(long aTimeInMillis) {
		// Round to minutes
		return Math.round(aTimeInMillis/(60*1000))*(60*1000);
	}

}