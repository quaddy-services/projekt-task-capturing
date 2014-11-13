package de.quaddy_services.report;

import java.util.HashMap;
import java.util.Map;


/**
 * Sum Tasks with one subtask will be ignored in report because they have
 * the same time-sum.
 * 
 * @author Stefan Cordes
 */
class TimeAndSubtasks {
	String taskName;
	String partName;
	long time;
	Map<String, TimeAndSubtasks> subtasks = new HashMap<String, TimeAndSubtasks>();
	@Override
	public String toString() {
		return super.toString()+"\r"+taskName+" ("+partName+")"+subtasks.size();
	}
}