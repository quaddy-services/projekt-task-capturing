package de.quaddy_services.ptc.enterprise.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import de.quaddy_services.ptc.enterprise.CommentDuration;
import de.quaddy_services.ptc.enterprise.custom.PtcTask;
import de.quaddy_services.report.TaskReport;
import de.quaddy_services.report.format.TimeFormatList;

public class EnterpriseReportParser {

	public List<PtcTask> parseReport(String aReport) {
		List<PtcTask> tempTasks = new ArrayList<PtcTask>();
		// Parse the report
		StringTokenizer tempLines = new StringTokenizer(aReport, "\r\n");
		// Ignore first line
		tempLines.nextToken();
		Date tempCurrentDay = null;
		List<CommentDuration> tempComments = new ArrayList<CommentDuration>();
		String tempSpaces = "";
		for (int i = 0; i < TimeFormatList.MILLIS.maxLength(); i++) {
			tempSpaces += " ";
		}
		while (tempLines.hasMoreTokens()) {
			String tempLine = tempLines.nextToken();
			if (tempLine.startsWith(TaskReport.GROUP_INDICATOR)) {
				tempCurrentDay = new Date(new Long(tempLine.substring(TaskReport.GROUP_INDICATOR.length())));
			} else {
				if (tempLine.startsWith(tempSpaces)) {
					String tempRest = tempLine.substring(tempSpaces.length());
					if (tempRest.startsWith(tempSpaces)) {
						// Comment
						tempRest = tempRest.trim();
						int tempPos = tempRest.indexOf(" ");
						Long tempDuration = new Long(tempRest.substring(0, tempPos));
						tempComments.add(new CommentDuration(tempDuration, tempRest.substring(tempPos + 1)));
					} else {
						// New task
						tempRest = tempRest.trim();
						int tempPos = tempRest.indexOf(" ");
						PtcTask tempCurrentTask = null;
						tempCurrentTask = new PtcTask();
						tempTasks.add(tempCurrentTask);
						tempCurrentTask.setDay(tempCurrentDay);
						tempCurrentTask.setDurationInMillis(new Long(tempRest.substring(0, tempPos)));
						tempCurrentTask.setBookableTaskName(tempRest.substring(tempPos + 1));
						String tempAllComments = "";
						String tempDelimiter = "";
						Collections.sort(tempComments);
						for (CommentDuration tempCommentDuration : tempComments) {
							String tempComment = tempCommentDuration.getComment();
							tempAllComments += tempDelimiter;
							String tempTaskEnd = tempComment.substring(tempCurrentTask.getBookableTaskName().length());
							tempTaskEnd = tempTaskEnd.trim();
							tempAllComments += tempTaskEnd;
							tempDelimiter = " /";
						}
						tempComments.clear();
						tempCurrentTask.setComment(tempAllComments);
					}
				}
			}
		}
		return tempTasks;
	}

}
