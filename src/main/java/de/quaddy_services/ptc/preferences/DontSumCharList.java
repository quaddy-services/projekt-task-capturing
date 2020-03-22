package de.quaddy_services.ptc.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DontSumCharList {
	public static final String DEFAULT = "Minus(-)";
	private static Map<String, DontSumChar> dontSumChar = new HashMap<String, DontSumChar>();
	static {
		// Same as TaskDelimiter
		for (String tempTaskDelimiter : TaskDelimiterList
				.getTaskDelimiterNames()) {
			add(new DontSumChar(tempTaskDelimiter, TaskDelimiterList
					.getTaskDelimiter(tempTaskDelimiter).getDelimiter()));
		}
	}

	private static void add(DontSumChar aDontSumChar) {
		dontSumChar.put(aDontSumChar.getName(), aDontSumChar);
	}

	public static DontSumChar getDontSumChar(String anId) {
		return dontSumChar.get(anId);
	}

	public static List<String> getDontSumCharNames() {
		ArrayList<String> tempArrayList = new ArrayList<String>(dontSumChar
				.keySet());
		Collections.sort(tempArrayList);
		return tempArrayList;
	}

	public static DontSumChar getDefault() {
		return getDontSumChar(DEFAULT);
	}
}
