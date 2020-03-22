package de.quaddy_services.ptc.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDelimiterList {
	public static final String DEFAULT = "Space";
	private static Map<String, TaskDelimiter> taskDelimiter = new HashMap<String, TaskDelimiter>();
	static {
		add(new TaskDelimiter(DEFAULT, " "));
		add(new TaskDelimiter("Slash(/)", "/"));
		add(new TaskDelimiter("Backslash(\\)", "\\"));
		add(new TaskDelimiter("Minus(-)", "-"));
		add(new TaskDelimiter("Semicolon(;)", ";"));
		add(new TaskDelimiter("Comma(,)", ","));
		add(new TaskDelimiter("Dot(.)", "."));
		add(new TaskDelimiter("Cross(#)", "#"));
		add(new TaskDelimiter("Plus(+)", "+"));
		add(new TaskDelimiter("Underscore(_)", "_"));
	}

	private static void add(TaskDelimiter aTaskDelimiter) {
		taskDelimiter.put(aTaskDelimiter.getName(), aTaskDelimiter);
	}

	public static TaskDelimiter getTaskDelimiter(String anId) {
		return taskDelimiter.get(anId);
	}

	public static List<String> getTaskDelimiterNames() {
		ArrayList<String> tempArrayList = new ArrayList<String>(taskDelimiter
				.keySet());
		Collections.sort(tempArrayList);
		return tempArrayList;
	}

	public static TaskDelimiter getDefault() {
		return getTaskDelimiter(DEFAULT);
	}
}
