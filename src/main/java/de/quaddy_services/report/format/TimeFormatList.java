package de.quaddy_services.report.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeFormatList {
	public static final String DEFAULT = "Hour";
	public static final MillisTimeFormat MILLIS = new MillisTimeFormat("Millis");
	private static Map<String, TimeFormat> timeFormat = new HashMap<String, TimeFormat>();
	static {
		add(new HourTimeFormat(DEFAULT));
		add(new MinuteTimeFormat("Minute"));
		add(new TimeTimeFormat("Time"));
		add(new DayTimeFormat("Day with 8 hours",8));
		add(new DayTimeFormat("Day with 24 hours",24));
	}

	public static TimeFormat getTimeFormat(String anId) {
		return timeFormat.get(anId);
	}

	private static void add(TimeFormat aHourTimeFormat) {
		timeFormat.put(aHourTimeFormat.getName(), aHourTimeFormat);
	}

	public static List<String> getTimeFormatNames() {
		ArrayList<String> tempArrayList = new ArrayList<String>(timeFormat
				.keySet());
		Collections.sort(tempArrayList);
		return tempArrayList;
	}

	public static TimeFormat getDefault() {
		return getTimeFormat(DEFAULT);
	}

}
