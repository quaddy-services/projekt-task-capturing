/**
 * 
 */
package de.quaddy_services.report.format;


public class MillisTimeFormat implements TimeFormat {
	public static final int LENGTH = 20;
	private String name;

	MillisTimeFormat(String aName) {
		// protected
		name = aName;
	}

	public String format(long aTimeInMillis) {
		return "" + aTimeInMillis;
	}

	public int maxLength() {
		return LENGTH;
	}

	public String getName() {
		return name;
	}

	public long roundToDisplay(long aTimeInMillis) {
		return aTimeInMillis;
	}

}