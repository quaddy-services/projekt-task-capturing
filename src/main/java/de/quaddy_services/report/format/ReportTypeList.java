package de.quaddy_services.report.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ReportTypeList {
	public static final ReportType DEFAULT;
	public static final ReportType WORKING_TIMES;
	private static Map<String, ReportType> reportTypes = new HashMap<>();
	static {
		DEFAULT = new ReportType("Tasks");
		add(DEFAULT);
		WORKING_TIMES = new ReportType("Working Times");
		add(WORKING_TIMES);
	}

	public static ReportType getReportType(String anId) {
		return reportTypes.get(anId);
	}

	private static void add(ReportType aReportType) {
		reportTypes.put(aReportType.getName(), aReportType);
	}

	public static List<String> getReportTypetNames() {
		ArrayList<String> tempArrayList = new ArrayList<String>(reportTypes.keySet());
		Collections.sort(tempArrayList);
		return tempArrayList;
	}

}
