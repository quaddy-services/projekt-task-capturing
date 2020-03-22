package de.quaddy_services.ptc.store;

import java.util.Date;

public class Task {
	private String name;
	private Date start;
	private Date stop;
	public Task(String aName,Date aStart,Date aStop) {
		setName(aName);
		start = aStart;
		stop = aStop;
	}
	public Task() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String aName) {
		if (aName == null || aName.length() == 0) {
			// Avoid ParseExceptions
			name = " ";
		} else {
			name = aName;
		}
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date aStart) {
		start = aStart;
	}
	public Date getStop() {
		return stop;
	}
	public void setStop(Date aStop) {
		stop = aStop;
	}
	@Override
	public String toString() {
		return super.toString()+"\r"+name+" "+start+" "+stop;
	}
	/**
	 * 
	 * @return Millis the duration of the task
	 */
	public long getMillis() {
		return getStop().getTime()-getStart().getTime();
	}
	public void setStart(long aStartTime) {
		setStart(new Date(aStartTime));
	}
	public void setStop(long aStopTime) {
		setStop(new Date(aStopTime));
	}
}
