/**
 * 
 */
package de.quaddy_services.report.format;

public interface TimeFormat {
	public String format(long aTimeInMillis);

	public int maxLength();
	
	public String getName();
	
	public long roundToDisplay(long aTimeInMillis);
}