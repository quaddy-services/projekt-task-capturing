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

	@Override
	public String format(long aTimeInMillis) {
		return "" + aTimeInMillis;
	}

	@Override
	public int maxLength() {
		return LENGTH;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long roundToDisplay(long aTimeInMillis) {
		return aTimeInMillis;
	}

}