package de.quaddy_services.ptc.preferences;

public interface Preferences {
	public static final String TASK_DELIMITER = "taskDelimiter";
	public static final String DONT_SUM_DELIMITER = "dontSumDelimiter";
	public static final String TIME_FORMAT = "timeFormat";
	public static final String GROUP_BY = "groupBy";
	public static final String ENTERPRISE_SERVER = "enterpriseServer";
	public static final String ALWAYS_ON_TOP = "alwaysOnTop";
	public static final String REMINDER_FLASH_ON_MINUTE = "reminderFlashOnMinute";
	public static final String DATA_FOLDER = "dataFolder";
	/**
	 * @since 2020-05-05
	 */
	public static final String ALWAYS_ON_TOP_WHEN_PAUSE = "alwaysOnTopWhenPause";
	/**
	 * https://github.com/quaddy-services/projekt-task-capturing/issues/22
	 *
	 * Show average hours per working day
	 *
	 * @since 2020-07-07
	 */
	public static final String WORKING_WEEKS_AVERAGE = "workingWeeksAverage";
	/**
	 * https://github.com/quaddy-services/projekt-task-capturing/issues/22
	 *
	 * Show average hours per working day
	 *
	 * @since 2020-07-07
	 */
	public static final String WORKING_MONTHS_AVERAGE = "workingMonthsAverage";
}
