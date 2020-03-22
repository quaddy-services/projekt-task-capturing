package de.quaddy_services.ptc;

import java.util.List;
import java.util.StringTokenizer;

public class FixedTasksTokenizer {

	private List<String> fixedTaskNames;
	private String taskName;
	private String delimiter;
	private boolean firstToken = true;
	private StringTokenizer nextTokens;

	public FixedTasksTokenizer(List<String> aFixedTaskNames, String aTaskName,
			String aDelimiter) {
		fixedTaskNames = aFixedTaskNames;
		taskName = aTaskName;
		delimiter = aDelimiter;
	}

	public boolean hasMoreTokens() {
		if (firstToken) {
			return taskName.length() > 0;
		}
		return nextTokens.hasMoreTokens();
	}

	public String nextToken() {
		if (firstToken) {
			firstToken = false;
			if (fixedTaskNames != null && fixedTaskNames.size() > 0) {
				// First token is the fixed one
				for (String tempFixed : fixedTaskNames) {
					if (taskName.startsWith(tempFixed)) {
						String tempRemainingText = taskName.substring(tempFixed
								.length());
						nextTokens = new StringTokenizer(tempRemainingText,
								delimiter);
						return tempFixed;
					}
				}
			}
			nextTokens = new StringTokenizer(taskName, delimiter);
		}
		return nextTokens.nextToken();
	}

}
