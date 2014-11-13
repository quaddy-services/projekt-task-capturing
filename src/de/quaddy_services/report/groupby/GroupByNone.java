package de.quaddy_services.report.groupby;

import de.quaddy_services.ptc.store.Task;

public class GroupByNone extends GroupBy {
	GroupByNone(String aName) {
		//protected
		super(aName);
	}

	@Override
	public String getGroupName(Task aT) {
		return "Total";
	}

	public int compare(Task aO1, Task aO2) {
		return 0;
	}

}
