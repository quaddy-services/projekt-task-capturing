package de.quaddy_services.report;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;

import de.quaddy_services.ptc.DisplayHelper;
import de.quaddy_services.ptc.preferences.DontSumChar;
import de.quaddy_services.ptc.preferences.TaskDelimiter;
import de.quaddy_services.ptc.store.Task;
import de.quaddy_services.ptc.store.TaskHistory;
import de.quaddy_services.report.format.ReportType;
import de.quaddy_services.report.format.ReportTypeList;
import de.quaddy_services.report.format.TimeFormat;
import de.quaddy_services.report.format.TimeFormatList;
import de.quaddy_services.report.groupby.GroupBy;

public class TaskReport {
	public static final String GROUP_INDICATOR = "--- ";
	private TaskHistory taskHistory;
	private JFrame frame;
	private TaskDelimiter taskDelimiter;
	private DontSumChar dontSumChar;
	private List<String> fixedTaskNames;
	private boolean prettyFormat = true;
	private boolean ignoreDontSumTasks = false;
	private ReportType reportType;

	public TaskReport(TaskHistory aTaksHistory, JFrame aFrame, TaskDelimiter aTaskDelimiter, DontSumChar aDontSumChar, List<String> aFixedTaskNames) {
		taskHistory = aTaksHistory;
		frame = aFrame;
		taskDelimiter = aTaskDelimiter;
		dontSumChar = aDontSumChar;
		fixedTaskNames = aFixedTaskNames;
	}

	String CR = System.lineSeparator();
	private boolean scrollToBottom;

	public void showReport(long aFrom, long aTo, GroupBy[] aGroupBy, TimeFormat aTimeFormat, List<Action> anActions) throws IOException {
		StringBuilder tempReport = new StringBuilder();
		for (int i = 0; i < aGroupBy.length; i++) {
			GroupBy tempGroupBy = aGroupBy[i];
			createReport(tempReport, aFrom, aTo, tempGroupBy, aTimeFormat);
			tempReport.append(CR);
			tempReport.append("-------------");
			tempReport.append(CR);
		}

		displayText(tempReport.toString(), anActions);
	}

	protected void displayText(String tempReport, List<Action> anActions) {
		boolean tempOld = frame.isAlwaysOnTop();
		frame.setAlwaysOnTop(false);
		try {
			DisplayHelper tempDisplayHelper = new DisplayHelper();
			tempDisplayHelper.displayText(frame, "Report", tempReport, false, anActions, isScrollToBottom());
		} finally {
			frame.setAlwaysOnTop(tempOld);
		}
	}

	/**
	 *
	 */
	private boolean isScrollToBottom() {
		return scrollToBottom;
	}

	public void createReport(StringBuilder aReport, long aFrom, long aTo, GroupBy aGroupBy, TimeFormat aTimeFormat) throws IOException {
		aReport.append(formatDateTime(aFrom, aTo));
		aReport.append(" Format: ");
		aReport.append(aTimeFormat.getName());
		aReport.append(CR);
		List<Task> tempTasks = new ArrayList<Task>();
		for (Iterator<Task> i = taskHistory.getTaskIterator(); i.hasNext();) {
			Task tempTask = i.next();
			if (tempTask != null) {
				if (aFrom <= tempTask.getStart().getTime() && tempTask.getStop().getTime() <= aTo) {
					tempTasks.add(tempTask);
				}
			}
		}
		ReportType tempReportType = getReportType();
		if (tempReportType == null || ReportTypeList.DEFAULT.equals(tempReportType)) {
			Collections.sort(tempTasks, aGroupBy);
			String tempOldGroupName = "";
			List<Task> tempGroupTasks = new ArrayList<Task>();
			for (Iterator<Task> i = tempTasks.iterator(); i.hasNext();) {
				Task tempTask = i.next();
				if (isIgnoreDontSumTasks()) {
					if (tempTask.getName().startsWith(dontSumChar.getChar())) {
						continue;
					}
				}
				String tempGroupName = aGroupBy.getGroupName(tempTask);
				if (!tempGroupName.equals(tempOldGroupName)) {
					// New Group
					formatGroup(aReport, tempGroupTasks, aGroupBy, aTimeFormat);
					tempGroupTasks.clear();
					tempOldGroupName = tempGroupName;
				}
				tempGroupTasks.add(tempTask);
			}
			formatGroup(aReport, tempGroupTasks, aGroupBy, aTimeFormat);
		} else if (tempReportType.equals(ReportTypeList.WORKING_TIMES)) {
			formatWorkingTimes(aReport, tempTasks);
		} else {
			throw new IllegalArgumentException("Invalid report: " + tempReportType);
		}
	}

	/**
	 *
	 */
	String formatDateTime(long aFrom, long aTo) {
		DateFormat tempDateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		return tempDateTimeInstance.format(aFrom) + " - " + tempDateTimeInstance.format(aTo);
	}

	/**
	 *
	 */
	private void formatWorkingTimes(StringBuilder aReport, List<Task> aTasks) {

		Collections.sort(aTasks, (aO1, aO2) -> aO1.getStart().compareTo(aO2.getStart()));

		DateFormat tempDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat tempTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

		String tempCurrentDay = null;
		// Stop time may be taken from next tasks with different name.
		String tempCurrentStopTime = null;
		StringBuilder tempCurrentDayLine = new StringBuilder();

		boolean tempAddNextStartTime = false;
		boolean tempFirstStartTime = true;
		for (Task tempTask : aTasks) {
			String tempDay = tempDateFormat.format(tempTask.getStart());
			String tempStartTime = tempTimeFormat.format(tempTask.getStart());
			String tempTaskName = tempTask.getName();
			if (tempTaskName.startsWith(dontSumChar.getChar())) {
				// user made a break
				if (tempCurrentStopTime != null) {
					tempCurrentDayLine.append(" - " + tempCurrentStopTime);
					tempCurrentStopTime = null;
				}
				tempAddNextStartTime = true;
			} else {
				if (tempCurrentDay == null || !tempCurrentDay.equals(tempDay)) {
					if (tempCurrentDayLine.length() > 0) {
						if (tempCurrentStopTime != null) {
							tempCurrentDayLine.append(" - " + tempCurrentStopTime);
						}
						aReport.append(tempCurrentDayLine);
						aReport.append(CR);
						tempCurrentDayLine.setLength(0);
					}
					tempCurrentDay = tempDay;
					tempCurrentDayLine.append(tempDay + ":");
					tempAddNextStartTime = true;
					tempFirstStartTime = true;
				}
				if (tempAddNextStartTime) {
					if (tempFirstStartTime) {
						tempFirstStartTime = false;
					} else {
						// Append a delimiter for the breaks
						tempCurrentDayLine.append("  / ");
					}
					tempCurrentDayLine.append(" " + tempStartTime);
					tempAddNextStartTime = false;
				}
				tempCurrentStopTime = tempTimeFormat.format(tempTask.getStop());
			}
		}
		if (tempCurrentDayLine.length() > 0) {
			if (tempCurrentStopTime != null) {
				tempCurrentDayLine.append(" - " + tempCurrentStopTime);
			}
			aReport.append(tempCurrentDayLine);
			aReport.append(CR);
		}
	}

	private void formatGroup(StringBuilder aReport, List<Task> aGroupTasks, GroupBy aGroupBy, TimeFormat aTimeFormat) {
		if (aGroupTasks.size() == 0) {
			return;
		}
		Task tempFirstTask = aGroupTasks.get(0);
		if (aTimeFormat == TimeFormatList.MILLIS) {
			aReport.append(GROUP_INDICATOR + tempFirstTask.getStart().getTime());
		} else {
			aReport.append(GROUP_INDICATOR + aGroupBy.getGroupName(tempFirstTask));
		}
		aReport.append(CR);
		// Tree aufbauen
		// Sum the times of the Tasks
		TimeAndSubtasks tempTaskTimes = new TimeAndSubtasks();
		for (Task tempTask : aGroupTasks) {
			String tempTaskName = tempTask.getName();
			addTaskTime(tempTaskName, tempTask, tempTaskTimes, aTimeFormat);
		}
		for (TimeAndSubtasks tempTask : tempTaskTimes.subtasks.values()) {
			if (tempTask.taskName.startsWith(dontSumChar.getChar())) {
				// Decrease the sum by tasks marked with "-"
				tempTaskTimes.time -= tempTask.time;
			}
		}
		if (isPrettyFormat()) {
			tempTaskTimes = removeTasksWithOneSubTask(tempTaskTimes);
		}
		String tempIntend = "";
		printTasks(aReport, tempTaskTimes, tempIntend, aTimeFormat);
	}

	private TimeAndSubtasks removeTasksWithOneSubTask(TimeAndSubtasks aTaskTimes) {
		if (aTaskTimes.subtasks.size() == 1) {
			if (aTaskTimes.taskName != null && aTaskTimes.taskName.startsWith(dontSumChar.getChar())) {
				// Keep the "-" Task
				return aTaskTimes;
			}
			TimeAndSubtasks tempNext = aTaskTimes.subtasks.values().iterator().next();
			if (aTaskTimes.time > tempNext.time) {
				// The "Sum-Task" contains values itself keep the entry
				return aTaskTimes;
			}
			if (aTaskTimes.partName != null) {
				tempNext.partName = aTaskTimes.partName + taskDelimiter.getDelimiter() + tempNext.partName;
			}
			return removeTasksWithOneSubTask(tempNext);
		}
		List<TimeAndSubtasks> tempSubTasks = new ArrayList<TimeAndSubtasks>(aTaskTimes.subtasks.values());
		aTaskTimes.subtasks.clear();
		for (TimeAndSubtasks tempTask : tempSubTasks) {
			TimeAndSubtasks tempChanged = removeTasksWithOneSubTask(tempTask);
			// As level may be mixed (Task1 Part1 / Task2 Part1)
			// use taskName as new key.
			aTaskTimes.subtasks.put(tempChanged.taskName, tempChanged);
		}
		return aTaskTimes;
	}

	private void printTasks(StringBuilder aReport, TimeAndSubtasks aTaskTimes, String aIntend, TimeFormat aTimeFormat) {
		List<String> tempPrintTasks = new ArrayList<String>(aTaskTimes.subtasks.keySet());
		Collections.sort(tempPrintTasks);
		for (String tempTaskName : tempPrintTasks) {
			TimeAndSubtasks tempSubTask = aTaskTimes.subtasks.get(tempTaskName);
			String tempNewIntend = aIntend + " ";
			for (int i = 0; i < aTimeFormat.maxLength(); i++) {
				tempNewIntend += " ";
			}
			printTasks(aReport, tempSubTask, tempNewIntend, aTimeFormat);
		}
		aReport.append(aIntend);
		aReport.append(aTimeFormat.format(aTaskTimes.time));
		aReport.append(" ");
		String tempTaskInfo;
		if (aTaskTimes.subtasks.size() == 0) {
			tempTaskInfo = aTaskTimes.taskName;
		} else {
			tempTaskInfo = aTaskTimes.partName;
		}
		if (tempTaskInfo == null) {
			tempTaskInfo = "Sum";
		}
		aReport.append(tempTaskInfo);
		aReport.append(CR);
	}

	private void addTaskTime(String aTaskName, Task aTask, TimeAndSubtasks tempTimeAndSubtasks, TimeFormat aTimeFormat) {
		long tempTaskTime = aTask.getStop().getTime() - aTask.getStart().getTime();
		tempTimeAndSubtasks.time += aTimeFormat.roundToDisplay(tempTaskTime);
		String tempTaskName = aTaskName;
		while (tempTaskName.startsWith(taskDelimiter.getDelimiter())) {
			tempTaskName = tempTaskName.substring(1);
		}
		if (tempTaskName.length() > 0) {
			int tempNextSplitPos = -1;
			if (aTaskName.equals(aTask.getName())) {
				// First iteration, check for fixedTasks
				tempNextSplitPos = getFixedTaskEndPos(tempTaskName);
			}
			if (tempNextSplitPos == -1 && isPrettyFormat()) {
				// fixed task not found
				// use normal splitting
				tempNextSplitPos = tempTaskName.indexOf(taskDelimiter.getDelimiter(), 1);
			}
			String tempTaskPart;
			if (tempNextSplitPos == -1) {
				tempNextSplitPos = tempTaskName.length();
			}
			tempTaskPart = tempTaskName.substring(0, tempNextSplitPos);
			TimeAndSubtasks tempTime = tempTimeAndSubtasks.subtasks.get(tempTaskPart);
			if (tempTime == null) {
				tempTime = new TimeAndSubtasks();
				tempTime.taskName = aTask.getName();
				tempTime.partName = tempTaskPart;
				tempTimeAndSubtasks.subtasks.put(tempTaskPart, tempTime);
			}
			addTaskTime(tempTaskName.substring(tempNextSplitPos), aTask, tempTime, aTimeFormat);
		}
	}

	private int getFixedTaskEndPos(String aTaskName) {
		if (fixedTaskNames != null) {
			for (String tempFixed : fixedTaskNames) {
				if (aTaskName.startsWith(tempFixed)) {
					return tempFixed.length();
				}
			}
		}
		return -1;
	}

	/**
	 * Returns the value of the instance variable 'prettyFormat'.
	 *
	 * @return Returns the prettyFormat.
	 */
	public boolean isPrettyFormat() {
		return prettyFormat;
	}

	/**
	 * Sets the instance variable 'prettyFormat'.
	 *
	 * @param aPrettyFormat
	 *            The prettyFormat to set.
	 */
	public void setPrettyFormat(boolean aPrettyFormat) {
		prettyFormat = aPrettyFormat;
	}

	/**
	 * Returns the value of the instance variable 'ignoreDontSumTasks'.
	 *
	 * @return Returns the ignoreDontSumTasks.
	 */
	public boolean isIgnoreDontSumTasks() {
		return ignoreDontSumTasks;
	}

	/**
	 * Sets the instance variable 'ignoreDontSumTasks'.
	 *
	 * @param aIgnoreDontSumTasks
	 *            The ignoreDontSumTasks to set.
	 */
	public void setIgnoreDontSumTasks(boolean aIgnoreDontSumTasks) {
		ignoreDontSumTasks = aIgnoreDontSumTasks;
	}

	/**
	 * @see #reportType
	 */
	public ReportType getReportType() {
		return reportType;
	}

	/**
	 * @see #reportType
	 */
	public void setReportType(ReportType aReportType) {
		reportType = aReportType;
	}

	/**
	 * @see #scrollToBottom
	 */
	public void setScrollToBottom(boolean aScrollToBottom) {
		scrollToBottom = aScrollToBottom;
	}
}
