package de.quaddy_services.report;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.Action;

import org.junit.jupiter.api.Test;

import de.quaddy_services.ptc.preferences.DontSumCharList;
import de.quaddy_services.ptc.preferences.TaskDelimiterList;
import de.quaddy_services.ptc.store.TaskHistory;
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
				return new StringReader("SWD-SV0036-O4-Upload\t01.10.2007 09:13:55\t01.10.2007 09:16:50\r\n"
						+ "SWD-CR-O4 DownloadSelection\t01.10.2007 09:16:50\t01.10.2007 10:04:40\r\n"
						+ "SWD-Support-OMS MSC\t01.10.2007 10:04:40\t01.10.2007 10:13:11\r\n"
						+ "SWD-SV0036-O4-Upload\t01.10.2007 10:13:11\t01.10.2007 12:56:05\r\n" + "SWD-other TISA\t01.10.2007 12:56:05\t01.10.2007 13:07:55\r\n"
						+ "SWD-SV0036-O4-Upload ErrorQueue\t01.10.2007 13:07:55\t01.10.2007 14:33:26\r\n"
						+ "SWD-other TISA\t01.10.2007 14:33:26\t01.10.2007 15:40:09\r\n" + "SWD-SV0036-O4-Upload\t01.10.2007 15:40:09\t01.10.2007 16:26:41\r\n"
						+ "SWD-Support-OMS CareInstructions\t01.10.2007 16:26:41\t01.10.2007 17:06:01\r\n"
						+ "SWD-Support-OMS OMS-Errors\t01.10.2007 17:06:01\t01.10.2007 18:12:30\r\n");
			}
		};
		final StringBuffer tempReportString = new StringBuffer();
		TaskReport tempReport = new TaskReport(tempTaskHistory, null, TaskDelimiterList.getDefault(), DontSumCharList.getDefault(), null) {
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
		tempReport.showReport(new SimpleDateFormat("dd.MM.yyyy").parse("01.10.2007").getTime(),
				new SimpleDateFormat("dd.MM.yyyy").parse("02.10.2007").getTime(), new GroupBy[] { GroupByList.getGroupBy(GroupByList.NONE) },
				TimeFormatList.getDefault(), null);
		System.out.println(tempReportString);
		String tempExpected = "01.10.07 00:00 - 02.10.07 00:00 Format: Hour\r\n" + "--- Total\r\n" + "      00,79 SWD-CR-O4 DownloadSelection\r\n"
				+ "            01,42 SWD-SV0036-O4-Upload ErrorQueue\r\n" + "      04,94 SWD-SV0036-O4-Upload\r\n"
				+ "            00,65 SWD-Support-OMS CareInstructions\r\n" + "            00,14 SWD-Support-OMS MSC\r\n"
				+ "            01,10 SWD-Support-OMS OMS-Errors\r\n" + "      01,89 SWD-Support-OMS\r\n" + "      01,30 SWD-other TISA\r\n" + "08,92 Sum\r\n"
				+ "\r\n" + "-------------\r\n" + "";
		StringTokenizer tempT1 = new StringTokenizer(tempExpected, "\r\n");
		StringTokenizer tempT2 = new StringTokenizer(tempReportString.toString(), "\r\n");
		while (tempT1.hasMoreTokens() && tempT2.hasMoreTokens()) {
			String tempS1 = tempT1.nextToken();
			String tempS2 = tempT2.nextToken();
			assertEquals(tempS1, tempS2);
		}
	}
}
