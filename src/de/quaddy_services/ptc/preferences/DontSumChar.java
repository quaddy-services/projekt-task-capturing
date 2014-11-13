package de.quaddy_services.ptc.preferences;

public class DontSumChar {
	private String name;
	private String c;
	public DontSumChar(String aName,String aChar) {
		name=aName;
		c=aChar;
	}
	public String getChar() {
		return c;
	}
	public String getName() {
		return name;
	}
}
