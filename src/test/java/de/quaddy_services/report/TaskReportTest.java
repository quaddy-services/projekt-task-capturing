package de.quaddy_services.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.Action;

import org.junit.jupiter.api.Test;

import de.quaddy_services.ptc.preferences.DontSumCharList;
import de.quaddy_services.ptc.preferences.TaskDelimiterList;
import de.quaddy_services.ptc.store.TaskHistory;
import de.quaddy_services.report.format.ReportTypeList;
import de.quaddy_services.report.format.TimeFormatList;
import de.quaddy_services.report.groupby.GroupBy;
import de.quaddy_services.report.groupby.GroupByList;

public class TaskReportTest {

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test
	public void testReportCreation() throws IOException, ParseException {
		Locale.setDefault(Locale.GERMANY);
		TaskHistory tempTaskHistory = new TaskHistory() {
			@Override
			protected Reader createReader() {
				return new StringReader(
				// @formatter:off
						"SWD-SV0036-O4-Upload\t01.10.2007 09:13:55\t01.10.2007 09:16:50\n"
						+ "SWD-CR-O4 DownloadSelection\t01.10.2007 09:16:50\t01.10.2007 10:04:40\n"
						+ "SWD-Support-OMS MSC\t01.10.2007 10:04:40\t01.10.2007 10:13:11\n"
						+ "SWD-SV0036-O4-Upload\t01.10.2007 10:13:11\t01.10.2007 12:56:05\n"
						+ "SWD-other TISA\t01.10.2007 12:56:05\t01.10.2007 13:07:55\n"
						+ "SWD-SV0036-O4-Upload ErrorQueue\t01.10.2007 13:07:55\t01.10.2007 14:33:26\n"
						+ "SWD-other TISA\t01.10.2007 14:33:26\t01.10.2007 15:40:09\n"
						+ "SWD-SV0036-O4-Upload\t01.10.2007 15:40:09\t01.10.2007 16:26:41\n"
						+ "SWD-Support-OMS CareInstructions\t01.10.2007 16:26:41\t01.10.2007 17:06:01\n"
						+ "SWD-Support-OMS OMS-Errors\t01.10.2007 17:06:01\t01.10.2007 18:12:30\n"
						// @formatter:on
				);
			}
		};
		StringBuffer tempReportString = new StringBuffer();

		TaskReport tempTaskReport = createTestTaskReport(tempTaskHistory, tempReportString);

		long tempFrom = new SimpleDateFormat("dd.MM.yyyy").parse("01.10.2007").getTime();
		long tempTo = new SimpleDateFormat("dd.MM.yyyy").parse("02.10.2007").getTime();
		tempTaskReport.showReport(tempFrom, tempTo, new GroupBy[] { GroupByList.getGroupBy(GroupByList.NONE) }, TimeFormatList.getDefault(), null);
		System.out.println(tempReportString);
		String tempExpected =
		// @formatter:off
				"01.10.07 00:00 - 02.10.07 00:00 Format: Hour\n" + 
				"--- Total\n" + 
				"      00,80 SWD-CR-O4 DownloadSelection\n" + 
				"            01,43 SWD-SV0036-O4-Upload ErrorQueue\n" + 
				"      04,98 SWD-SV0036-O4-Upload\n" + 
				"            00,66 SWD-Support-OMS CareInstructions\n" + 
				"            00,14 SWD-Support-OMS MSC\n" + 
				"            01,11 SWD-Support-OMS OMS-Errors\n" + 
				"      01,91 SWD-Support-OMS\n" + 
				"      01,31 SWD-other TISA\n" + 
				"09,00 Sum\n" + 
				"\n" + 
				"-------------\n" + 
				"";
				// @formatter:on
		assertEquals(tempExpected, tempReportString.toString());
	}

	@Test
	public void testWorkingTimesReportCreation() throws IOException, ParseException {

		Locale.setDefault(Locale.GERMANY);
		TaskHistory tempTaskHistory = new TaskHistory() {
			@Override
			protected Reader createReader() {
				// @formatter:off
				return new StringReader(
						"-suspended	17.03.2020 23:47:54	17.03.2020 23:59:59\n" +
						"-suspended	18.03.2020 00:00:00	18.03.2020 08:47:20\n" +
						"-Pause	18.03.2020 08:47:20	18.03.2020 08:47:42\n" +
						"IET Meeting	18.03.2020 08:47:42	18.03.2020 09:57:02\n" +
						"T4S PO2ERP	18.03.2020 09:57:02	18.03.2020 10:53:42\n" +
						"IET DirectMemory	18.03.2020 10:53:42	18.03.2020 14:10:15\n" +
						"T4S Material	18.03.2020 14:10:15	18.03.2020 15:18:26\n" +
						"-Pause	18.03.2020 15:18:26	18.03.2020 15:28:06\n" +
						"Mail	18.03.2020 15:28:06	18.03.2020 16:35:58\n" +
						"IET Runtimes	18.03.2020 16:35:58	18.03.2020 16:58:29\n" +
						"IET Cases	18.03.2020 16:58:29	18.03.2020 18:47:23\n" +
						"-Pause	18.03.2020 18:47:23	18.03.2020 23:59:59\n" +
						"-Pause	19.03.2020 00:00:00	19.03.2020 08:42:12\n" +
						"");
				// @formatter:on
			}
		};
		// Normally today -30 days but take date as testdata above
		long tempFrom = new SimpleDateFormat("dd.MM.yyyy").parse("01.03.2020").getTime();
		long tempTo = new SimpleDateFormat("dd.MM.yyyy").parse("20.03.2020").getTime();

		final StringBuffer tempReportString = new StringBuffer();
		TaskReport tempTaskReport = createTestTaskReport(tempTaskHistory, tempReportString);

		StringBuilder tempResultReport = new StringBuilder();
		tempTaskReport.setReportType(ReportTypeList.WORKING_TIMES);
		tempTaskReport.createReport(tempResultReport, tempFrom, tempTo, null, TimeFormatList.getDefault());
		System.out.println(tempReportString);

		// @formatter:off
		String tempExpected=
				"01.03.20 00:00 - 20.03.20 00:00 Format: Hour\n" +
				"18.03.20: 08:47 - 15:18  /  15:28 - 18:47\n" +
				"";
		// @formatter:on
		assertEquals(tempExpected, tempResultReport.toString());
	}

	@Test
	public void testWorkingTimesReportOneDayWithBreaks() throws IOException, ParseException {

		Locale.setDefault(Locale.GERMANY);
		TaskHistory tempTaskHistory = new TaskHistory() {
			@Override
			protected Reader createReader() {
				// @formatter:off
				return new StringReader(
						"IET Meeting	19.03.2020 08:57:13	19.03.2020 09:02:23\n" +
						"IET Runtimes	19.03.2020 09:02:23	19.03.2020 12:20:59\n" +
						"SDI Sonar	19.03.2020 12:20:59	19.03.2020 12:58:20\n" +
						"T4S Architektur	19.03.2020 12:58:20	19.03.2020 13:57:50\n" +
						"-Pause	19.03.2020 13:57:50	19.03.2020 14:32:12\n" +
						"Mail	19.03.2020 14:32:12	19.03.2020 15:32:34\n" +
						"T4S Meeting	19.03.2020 15:32:34	19.03.2020 15:50:24\n"+
						"");
				// @formatter:on
			}
		};
		// Normally today -30 days but take date as testdata above
		long tempFrom = new SimpleDateFormat("dd.MM.yyyy").parse("01.03.2020").getTime();
		long tempTo = new SimpleDateFormat("dd.MM.yyyy").parse("20.03.2020").getTime();

		final StringBuffer tempReportString = new StringBuffer();
		TaskReport tempTaskReport = createTestTaskReport(tempTaskHistory, tempReportString);

		StringBuilder tempResultReport = new StringBuilder();
		tempTaskReport.setReportType(ReportTypeList.WORKING_TIMES);
		tempTaskReport.createReport(tempResultReport, tempFrom, tempTo, null, TimeFormatList.getDefault());
		System.out.println(tempReportString);

		// @formatter:off
		String tempExpected=
				"01.03.20 00:00 - 20.03.20 00:00 Format: Hour\n" +
				"19.03.20: 08:57 - 13:57  /  14:32 - 15:50\n" +
				"";
		// @formatter:on
		assertEquals(tempExpected, tempResultReport.toString());
	}

	@Test
	public void testWorkingTimesReportWhenRecordingIsStoppedNightlyNoBreaks() throws IOException, ParseException {

		Locale.setDefault(Locale.GERMANY);
		TaskHistory tempTaskHistory = new TaskHistory() {
			@Override
			protected Reader createReader() {
				// @formatter:off
				return new StringReader(
						"IET Meeting	18.03.2020 08:47:42	18.03.2020 09:57:02\n" +
						"T4S PO2ERP	18.03.2020 09:57:02	18.03.2020 10:53:42\n" +
						"IET DirectMemory	18.03.2020 10:53:42	18.03.2020 14:10:15\n" +
						"T4S Material	18.03.2020 14:10:15	18.03.2020 15:18:26\n");
				// @formatter:on
			}
		};
		// Normally today -30 days but take date as testdata above
		long tempFrom = new SimpleDateFormat("dd.MM.yyyy").parse("01.03.2020").getTime();
		long tempTo = new SimpleDateFormat("dd.MM.yyyy").parse("20.03.2020").getTime();

		final StringBuffer tempReportString = new StringBuffer();
		TaskReport tempTaskReport = createTestTaskReport(tempTaskHistory, tempReportString);

		StringBuilder tempResultReport = new StringBuilder();
		tempTaskReport.setReportType(ReportTypeList.WORKING_TIMES);
		tempTaskReport.createReport(tempResultReport, tempFrom, tempTo, null, TimeFormatList.getDefault());
		System.out.println(tempReportString);

		// @formatter:off
		String tempExpected=
				"01.03.20 00:00 - 20.03.20 00:00 Format: Hour\n" +
				"18.03.20: 08:47 - 15:18\n" +
				"";
		// @formatter:on
		assertEquals(tempExpected, tempResultReport.toString());
	}

	/**
	 *
	 */
	private TaskReport createTestTaskReport(TaskHistory tempTaskHistory, final StringBuffer tempReportString) {
		TaskReport tempTaskReport = new TaskReport(tempTaskHistory, null, TaskDelimiterList.getDefault(), DontSumCharList.getDefault(), null) {
			@Override
			protected void displayText(String aTempReport, List<Action> aAnActions) {
				tempReportString.append(aTempReport);
			}

			/**
			 * Avoid creating format via DateFormat.getDateTimeInstance as JDK11 may add a delimiter between date and time
			 * even locale is fixed.
			 */
			@Override
			String formatDateTime(long aFrom, long aTo) {
				return simpleFormat(aFrom) + " - " + simpleFormat(aTo);
			}

			/**
			 * Fix the locale for the JUnit test.
			 */
			private String simpleFormat(long aDate) {
				return new SimpleDateFormat("dd.MM.yy HH:mm").format(aDate);
			}
		};
		// for easier asserts ignore System.lineSeparator()
		tempTaskReport.CR = "\n";
		return tempTaskReport;
	}
}
