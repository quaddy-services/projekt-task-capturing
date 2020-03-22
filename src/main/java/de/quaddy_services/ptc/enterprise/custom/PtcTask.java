package de.quaddy_services.ptc.enterprise.custom;

import java.io.Serializable;
import java.util.Date;

public class PtcTask implements Serializable {
	private Date day;
	private long durationInMillis;
	private String bookableTaskName;
	private String comment = "";

	/**
	 * @return the day
	 */
	public Date getDay() {
		return day;
	}

	/**
	 * @param aDay
	 *            the day to set
	 */
	public void setDay(Date aDay) {
		day = aDay;
	}

	/**
	 * @return the durationInMillis
	 */
	public long getDurationInMillis() {
		return durationInMillis;
	}

	/**
	 * @param aDurationInMillis
	 *            the durationInMillis to set
	 */
	public void setDurationInMillis(long aDurationInMillis) {
		durationInMillis = aDurationInMillis;
	}

	/**
	 * @return the bookableTaskName
	 */
	public String getBookableTaskName() {
		return bookableTaskName;
	}

	/**
	 * @param aBookableTaskName
	 *            the bookableTaskName to set
	 */
	public void setBookableTaskName(String aBookableTaskName) {
		bookableTaskName = aBookableTaskName;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param aComment
	 *            the comment to set
	 */
	public void setComment(String aComment) {
		comment = aComment;
	}

	@Override
	public String toString() {
		String tempString = "";
		tempString += " name=" + bookableTaskName;
		tempString += " comment=" + comment;
		tempString += " day=" + day;
		tempString += " ms=" + durationInMillis;
		return tempString + " " + super.toString();
	}
}
