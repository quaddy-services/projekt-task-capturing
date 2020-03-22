package de.quaddy_services.ptc.preferences;

public class TaskDelimiter {
	private String name;
	private String c;
	public TaskDelimiter(String aName,String aChar) {
		name=aName;
		c=aChar;
	}
	public String getDelimiter() {
		return c;
	}
	public String getName() {
		return name;
	}
}
