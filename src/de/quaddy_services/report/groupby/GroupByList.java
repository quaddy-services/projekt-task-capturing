package de.quaddy_services.report.groupby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupByList {
	public static final String NONE = "(none)";
	public static final String DAY = "Day";
	public static final String DEFAULT = DAY;
	private static Map<String, GroupBy> groupby = new HashMap<String, GroupBy>();
	static {
		add(new GroupByNone(NONE));
		add(new GroupByDay(DEFAULT));
		add(new GroupByWeek("Week"));
		add(new GroupByMonth("Month"));
		add(new GroupByYear("Year"));
	}

	public static GroupBy getGroupBy(String anId) {
		return groupby.get(anId);
	}

	private static void add(GroupBy aGroupBy) {
		groupby.put(aGroupBy.getName(), aGroupBy);
	}

	public static List<String> getGroupByNames() {
		ArrayList<String> tempArrayList = new ArrayList<String>(groupby
				.keySet());
		Collections.sort(tempArrayList);
		return tempArrayList;
	}

	public static GroupBy getDefault() {
		return getGroupBy(DEFAULT);
	}
}
