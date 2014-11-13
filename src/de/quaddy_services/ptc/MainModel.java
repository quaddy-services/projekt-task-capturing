/**
 * 
 */
package de.quaddy_services.ptc;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Properties;
import java.util.StringTokenizer;

import de.quaddy_services.ptc.preferences.DontSumChar;
import de.quaddy_services.ptc.preferences.DontSumCharList;
import de.quaddy_services.ptc.preferences.Preferences;
import de.quaddy_services.ptc.preferences.TaskDelimiter;
import de.quaddy_services.ptc.preferences.TaskDelimiterList;
import de.quaddy_services.report.format.TimeFormat;
import de.quaddy_services.report.format.TimeFormatList;
import de.quaddy_services.report.groupby.GroupBy;
import de.quaddy_services.report.groupby.GroupByList;

/**
 * @author Stefan Cordes
 * 
 */
public class MainModel {
	private static final String FRAME_BOUNDS = "frameBounds";
	private static final String FRAME_CONTENT_BOUNDS = "frameContentBounds";
	private Properties properties;

	/**
	 * @return Returns the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param aProperties
	 *            The properties to set.
	 */
	public void setProperties(Properties aProperties) {
		properties = aProperties;
	}

	/**
	 * @return Returns the isFrameDecorated.
	 */
	public boolean isFrameDecorated() {
		return Boolean.TRUE.toString().equals(properties.getProperty("frameDecorated", Boolean.TRUE.toString()));
	}

	/**
	 * @param aIsFrameDecorated
	 *            The isFrameDecorated to set.
	 */
	public void setFrameDecorated(boolean aIsFrameDecorated) {
		properties.setProperty("frameDecorated", new Boolean(aIsFrameDecorated).toString());
	}

	/**
	 * @return Returns the frameBounds.
	 */
	public Rectangle getFrameBounds() {
		String tempDefaultBounds;
		if (Toolkit.getDefaultToolkit().getScreenSize().width > 900) {
			tempDefaultBounds = "600,-15,260,54";
		} else {
			tempDefaultBounds = "550,-15,200,54";
		}
		return stringToRectangle(properties.getProperty(FRAME_BOUNDS, tempDefaultBounds));
	}

	/**
	 * @param aFrameBounds
	 *            The frameBounds to set.
	 */
	public void setFrameBounds(Rectangle aFrameBounds) {
		properties.setProperty(FRAME_BOUNDS, rectangleToString(aFrameBounds));
	}

	private String rectangleToString(Rectangle aFrameBounds) {
		return "" + aFrameBounds.x + "," + aFrameBounds.y + "," + aFrameBounds.width + "," + aFrameBounds.height;
	}

	/**
	 * @return Returns the frameContentBounds.
	 */
	public Rectangle getFrameContentBounds() {
		return stringToRectangle(properties.getProperty(FRAME_CONTENT_BOUNDS));
	}

	private Rectangle stringToRectangle(String aProperty) {
		if (aProperty == null) {
			return null;
		}
		StringTokenizer tempNumbers = new StringTokenizer(aProperty, ",");
		Rectangle tempRectangle = new Rectangle();
		tempRectangle.setBounds(new Integer(tempNumbers.nextToken()).intValue(),
				new Integer(tempNumbers.nextToken()).intValue(), new Integer(tempNumbers.nextToken()).intValue(),
				new Integer(tempNumbers.nextToken()).intValue());
		return tempRectangle;
	}

	/**
	 * @param aFrameContentBounds
	 *            The frameContentBounds to set.
	 */
	public void setFrameContentBounds(Rectangle aFrameContentBounds) {
		properties.setProperty(FRAME_CONTENT_BOUNDS, rectangleToString(aFrameContentBounds));
	}

	/**
	 * @return Returns the currentTask.
	 */
	public String getCurrentTask() {
		return properties.getProperty("currentTask");
	}

	public void setCurrentTask(String aCurrentTask) {
		if (aCurrentTask == null) {
			return;
		}
		properties.setProperty("currentTask", aCurrentTask);
	}

	public TaskDelimiter getTaskDelimiter() {
		TaskDelimiter tempTaskDelimiter = TaskDelimiterList.getTaskDelimiter(getProperties().getProperty(
				Preferences.TASK_DELIMITER));
		if (tempTaskDelimiter == null) {
			tempTaskDelimiter = TaskDelimiterList.getDefault();
			getProperties().setProperty(Preferences.TASK_DELIMITER, tempTaskDelimiter.getName());
		}
		return tempTaskDelimiter;
	}

	public DontSumChar getDontSumChar() {
		DontSumChar tempDontSumChar = DontSumCharList.getDontSumChar(getProperties().getProperty(
				Preferences.DONT_SUM_DELIMITER));
		if (tempDontSumChar == null) {
			tempDontSumChar = DontSumCharList.getDefault();
			getProperties().setProperty(Preferences.DONT_SUM_DELIMITER, tempDontSumChar.getName());
		}
		return tempDontSumChar;
	}

	public GroupBy getGroupBy() {
		GroupBy tempGroupBy = GroupByList.getGroupBy(getProperties().getProperty(Preferences.GROUP_BY));
		if (tempGroupBy == null) {
			tempGroupBy = GroupByList.getDefault();
			getProperties().setProperty(Preferences.GROUP_BY, tempGroupBy.getName());
		}
		return tempGroupBy;
	}

	public String getEnterpriseServer() {
		return getProperties().getProperty(Preferences.ENTERPRISE_SERVER, "");
	}

	public Short getReminderFlashOnMinute() {
		try {
			return new Short(getProperties().getProperty(Preferences.REMINDER_FLASH_ON_MINUTE, "57"));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public TimeFormat getTimeFormat() {
		TimeFormat tempTimeFormat = TimeFormatList.getTimeFormat(getProperties().getProperty(Preferences.TIME_FORMAT));
		if (tempTimeFormat == null) {
			tempTimeFormat = TimeFormatList.getDefault();
			getProperties().setProperty(Preferences.TIME_FORMAT, tempTimeFormat.getName());
		}
		return tempTimeFormat;
	}

	public boolean isAlwaysOnTop() {
		return Boolean.valueOf(getProperties().getProperty(Preferences.ALWAYS_ON_TOP, Boolean.FALSE.toString()));
	}

}
