package de.quaddy_services.ptc.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.quaddy_services.ptc.store.FileUtil;

public class Log {
	private String prefix;
	private static DateFormat format = new SimpleDateFormat("HH:mm:ss");
	private static File file;
	private static int currentDayOfWeek = -1;

	private static void initFile() {
		Calendar tempCal = Calendar.getInstance();
		int tempDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK);
		if (currentDayOfWeek != tempDayOfWeek) {
			synchronized (Log.class) {
				file = new File(FileUtil.getDataFolder() + "/ptc." + tempDayOfWeek + ".log");
				// Is the file from last week? Delete
				if (file.exists() && isLastWeek(file)) {
					file.delete();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Log(Class aClass) {
		int tempPos = aClass.getName().lastIndexOf(".");
		prefix = aClass.getName().substring(tempPos + 1);
	}

	private static boolean isLastWeek(File aFile) {
		Calendar tempCal = Calendar.getInstance();
		tempCal.set(Calendar.HOUR_OF_DAY, 0);
		tempCal.set(Calendar.MINUTE, 0);
		tempCal.set(Calendar.SECOND, 0);
		tempCal.set(Calendar.MILLISECOND, 0);
		return tempCal.getTimeInMillis() > aFile.lastModified();
	}

	public void info(String aString) {
		initFile();
		String tempString = format.format(new Date()) + " " + prefix + " " + aString + " ["
				+ Thread.currentThread().getName() + "]";
		log(tempString);
	}

	private void log(String aString) {
		System.out.println(aString);
		synchronized (Log.class) {
			try {
				FileWriter tempFileWriter = new FileWriter(file, true);
				tempFileWriter.write(aString + "\r\n");
				tempFileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void exception(Throwable aE) {
		initFile();
		log("Exception: " + aE.getClass());
		aE.printStackTrace();
		synchronized (Log.class) {
			try {
				FileWriter tempFileWriter = new FileWriter(file, true);
				PrintWriter tempPrintWriter = new PrintWriter(tempFileWriter);
				aE.printStackTrace(tempPrintWriter);
				tempPrintWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
