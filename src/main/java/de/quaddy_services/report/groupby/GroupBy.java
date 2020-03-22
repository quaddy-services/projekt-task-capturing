/**
 * 
 */
package de.quaddy_services.report.groupby;

import java.util.Comparator;

import de.quaddy_services.ptc.store.Task;

public abstract class GroupBy implements Comparator<Task> {
	private String name;

	GroupBy(String aName) {
		name = aName;
	}

	public abstract String getGroupName(Task t);

	public String getName() {
		return name;
	}
}