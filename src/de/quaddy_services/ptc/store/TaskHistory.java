package de.quaddy_services.ptc.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;

public class TaskHistory implements TaskUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskHistory.class);
	/**
	 *
	 */
	public static final String TASK_CLOSED = "PTCEXIT";
	public static final String TASK_STARTED = "PTCSTART";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static final long CLASS_LOADING_TIME = System.currentTimeMillis();

	private String fileName = "ptc.tasks.txt";

	public TaskHistory() {
		super();
	}

	public File getActualFile() {
		String tempFileName = FileUtil.getDataFolder();
		return new File(tempFileName + "/" + getFileName());
	}

	/**
	 * return current Task time.
	 *
	 * @param aTaskName
	 * @return
	 * @throws IOException
	 * @throws NetworkDriveNotAvailable
	 */
	@Override
	public synchronized Task updateLastTask(String aTaskName) throws IOException, NetworkDriveNotAvailable {
		Date tempNow = new Date();
		Task tempTask = new Task(aTaskName, tempNow, tempNow);
		if (aTaskName == null) {
			return tempTask;
		}
		long tempStartTime = System.currentTimeMillis();
		long tempStopTime = tempStartTime;
		File tempFile = getActualFile();
		if (!tempFile.exists()) {
			try {
				// Create the file.
				tempFile.createNewFile();
			} catch (IOException e) {
				LOGGER.error("Error creating " + tempFile.getAbsolutePath() + ":" + e);
				// maybe network drive is not available
				if (!tempFile.getParentFile().exists()) {
					LOGGER.info("Looks like it is an NetworkDrive");
					throw new NetworkDriveNotAvailable(tempFile.getAbsolutePath(), e);
				} else {
					throw e;
				}

			}
		}
		RandomAccessFile tempContent = new RandomAccessFile(tempFile, "rw");
		long tempSize = tempContent.length();
		if (tempSize == 0) {
			tempTask.setStart(tempStartTime);
			tempTask.setStop(tempStopTime);
			tempContent.write(format(tempTask));
		} else {
			// Find last line
			PosAndContent<String> tempLastLine = findLastLine(tempContent, 200);
			StringTokenizer tempTokens = new StringTokenizer(tempLastLine.getLine(), "\t");
			byte[] tempNewLine;
			if (tempTokens.countTokens() != 3) {
				tempTask.setStart(tempStartTime);
				tempTask.setStop(tempStopTime);
				tempNewLine = format(tempTask);
			} else {
				String tempLastTask = tempTokens.nextToken();
				long tempLastStart = tempStartTime;
				long tempLastStop = tempStopTime;
				if (isInternalTask(tempLastTask)) {
					// First time updateLastTask is called
					tempLastStop = CLASS_LOADING_TIME;
				} else {
					try {
						tempLastStart = DATE_FORMAT.parse(tempTokens.nextToken()).getTime();
						tempLastStop = DATE_FORMAT.parse(tempTokens.nextToken()).getTime();
					} catch (ParseException e) {
						e.printStackTrace();
						// new task
						tempLastTask = null;
					}
				}
				if (tempLastTask != null && tempLastTask.equals(aTaskName)) {
					// Same task, set eof to old string position.
					tempContent.setLength(tempLastLine.getPosInFile());
					tempTask.setStart(tempLastStart);
					tempTask.setStop(tempStopTime);
					tempNewLine = format(tempTask);
				} else {
					// new task
					tempTask.setStart(tempLastStop);
					tempTask.setStop(tempStopTime);
					tempNewLine = format(tempTask);
				}
			}
			tempContent.write(tempNewLine);
		}
		tempContent.close();
		return tempTask;
	}

	private PosAndContent<String> findLastLine(RandomAccessFile aContent, int aOffset) throws IOException {
		long tempSize = aContent.length();
		long tempPos = Math.max(0, tempSize - aOffset);
		aContent.seek(tempPos);
		int tempLen = (int) (tempSize - tempPos);
		byte[] tempBytes = new byte[tempLen];
		aContent.readFully(tempBytes, 0, tempLen);
		int tempEnd = tempLen - 1;
		char c = new String(tempBytes, tempEnd, 1).charAt(0);
		while (Character.isWhitespace(c)) {
			tempEnd--;
			c = new String(tempBytes, tempEnd, 1).charAt(0);
		}
		int tempStart = tempEnd;
		while (tempStart > 0) {
			c = new String(tempBytes, tempStart - 1, 1).charAt(0);
			if (c == '\r' || c == '\n') {
				break;
			}
			tempStart--;
		}
		String tempString = new String(tempBytes, tempStart, (tempEnd - tempStart) + 1);
		PosAndContent<String> tempPosAndContent = new PosAndContent<String>();
		tempPosAndContent.setPosInFile(tempPos + tempStart);
		tempPosAndContent.setLine(tempString);
		return tempPosAndContent;
	}

	public List<PosAndContent<Task>> getLastLinesForEdit() throws IOException {
		backupFile();
		List<PosAndContent<Task>> tempList = new ArrayList<PosAndContent<Task>>();
		RandomAccessFile aContent = new RandomAccessFile(getActualFile(), "r");
		int aOffset = 1000;
		long tempSize = aContent.length();
		long tempPos = Math.max(0, tempSize - aOffset);
		aContent.seek(tempPos);
		int tempLen = (int) (tempSize - tempPos);
		byte[] tempBytes = new byte[tempLen];
		aContent.readFully(tempBytes, 0, tempLen);
		aContent.close();
		int tempStart = 0;
		boolean tempVeryFirstLineOfFile = (tempPos == 0);
		char c = new String(tempBytes, tempStart, 1).charAt(0);
		while (tempStart < (tempLen - 1)) {
			int tempEnd = tempStart;
			while (c != '\r' && c != '\n' && tempEnd < (tempLen - 1)) {
				tempEnd++;
				c = new String(tempBytes, tempEnd, 1).charAt(0);
			}
			// Skip to next line (not, if we are in the beginning of the
			// file)
			if (tempStart > 0 || tempVeryFirstLineOfFile) {
				tempVeryFirstLineOfFile = false;
				String tempString = new String(tempBytes, tempStart, (tempEnd - tempStart) + 1);
				PosAndContent<Task> tempContent = new PosAndContent<Task>();
				tempContent.setPosInFile(tempPos + tempStart);
				StringTokenizer tempTokens = new StringTokenizer(tempString, "\t");
				Task tempTask = new Task();
				try {
					tempTask.setName(tempTokens.nextToken());
					tempTask.setStart(DATE_FORMAT.parse(tempTokens.nextToken()));
					tempTask.setStop(DATE_FORMAT.parse(tempTokens.nextToken()));
				} catch (Exception e) {
					throw new RuntimeException("Error on Line '" + tempString + "'", e);
				}
				tempContent.setLine(tempTask);
				tempList.add(tempContent);
			}
			tempStart = tempEnd;
			while (c == '\r' || c == '\n' && tempStart < (tempLen - 1)) {
				tempStart++;
				c = new String(tempBytes, tempStart, 1).charAt(0);
			}
		}
		return tempList;
	}

	private static int MAX_BACKUPS = 20;

	public void backupFile() throws IOException {
		new BackupFile().backupFile(getActualFile(), MAX_BACKUPS);
	}

	private boolean isSameDay(Date aLastStart, Date aStopTime) {
		Calendar tempCal1 = Calendar.getInstance();
		tempCal1.setTime(aLastStart);
		Calendar tempCal2 = Calendar.getInstance();
		tempCal2.setTime(aStopTime);

		return tempCal1.get(Calendar.DAY_OF_YEAR) == tempCal2.get(Calendar.DAY_OF_YEAR);
	}

	private byte[] format(Task aTask) {
		String tempString;
		if (isSameDay(aTask.getStart(), aTask.getStop())) {
			tempString = aTask.getName() + "\t" + DATE_FORMAT.format(aTask.getStart()) + "\t" + DATE_FORMAT.format(aTask.getStop()) + "\r\n";
		} else {
			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(aTask.getStart());
			tempCal.set(Calendar.HOUR_OF_DAY, 23);
			tempCal.set(Calendar.MINUTE, 59);
			tempCal.set(Calendar.SECOND, 59);
			tempCal.set(Calendar.MILLISECOND, 900);
			tempString = aTask.getName() + "\t" + DATE_FORMAT.format(aTask.getStart()) + "\t" + DATE_FORMAT.format(tempCal.getTime()) + "\r\n";

			tempCal = Calendar.getInstance();
			tempCal.setTime(aTask.getStop());
			tempCal.set(Calendar.HOUR_OF_DAY, 0);
			tempCal.set(Calendar.MINUTE, 0);
			tempCal.set(Calendar.SECOND, 0);
			tempCal.set(Calendar.MILLISECOND, 0);
			tempString += aTask.getName() + "\t" + DATE_FORMAT.format(tempCal.getTime()) + "\t" + DATE_FORMAT.format(aTask.getStop()) + "\r\n";
		}
		return tempString.getBytes();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String aFileName) {
		fileName = aFileName;
	}

	private BufferedReader iteratorReader;

	public Iterator<Task> getTaskIterator() throws IOException {
		iteratorReader = new BufferedReader(createReader());
		class TaskIterator implements Iterator<Task> {
			TaskIterator() throws IOException {
				nextTask = readNextTask();
			}

			private Task nextTask;

			@Override
			public boolean hasNext() {
				return nextTask != null;
			}

			@Override
			public Task next() {
				Task tempNextTask = nextTask;
				try {
					nextTask = readNextTask();
				} catch (Exception e) {
					closeIteratorReader();
					throw new RuntimeException(e);
				}
				if (nextTask == null) {
					closeIteratorReader();
				}
				return tempNextTask;
			}

			@Override
			public void remove() {
				throw new RuntimeException("Not supported use updateTaskList()");
			}
		}
		return new TaskIterator();
	}

	protected Reader createReader() throws FileNotFoundException {
		return new FileReader(getActualFile());
	}

	private Task readNextTask() throws IOException {
		if (!iteratorReader.ready()) {
			return null;
		}
		String tempLine = iteratorReader.readLine();
		if (tempLine == null) {
			return null;
		}
		StringTokenizer tempTokens = new StringTokenizer(tempLine, "\t");
		int tempC = tempTokens.countTokens();
		String tempTaskName = tempTokens.nextToken();
		if (isInternalTask(tempTaskName)) {
			// Skip
			tempC = 0;
		}

		while (tempC != 3 && iteratorReader.ready()) {
			tempLine = iteratorReader.readLine();
			tempTokens = new StringTokenizer(tempLine, "\t");
			tempC = tempTokens.countTokens();
			if (tempC == 3) {
				tempTaskName = tempTokens.nextToken();
				if (isInternalTask(tempTaskName)) {
					// Skip
					tempC = 0;
				}
			}
		}
		if (tempC == 3) {
			Task tempTask = new Task();
			tempTask.setName(tempTaskName);
			try {
				tempTask.setStart(DATE_FORMAT.parse(tempTokens.nextToken()));
				tempTask.setStop(DATE_FORMAT.parse(tempTokens.nextToken()));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot parse \r\n'" + tempLine + "'", e);
			}
			return tempTask;
		}
		return null;
	}

	private void closeIteratorReader() {
		if (iteratorReader != null) {
			try {
				iteratorReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			iteratorReader = null;
		}
	}

	/**
	 *
	 * @return Last tasks ordered by usage.
	 * @throws IOException
	 */
	public List<String> getLastTasks() throws IOException {
		List<String> tempLastTasks = new ArrayList<String>();
		File tempFile = getActualFile();
		if (!tempFile.exists()) {
			return tempLastTasks;
		}
		RandomAccessFile tempContent = new RandomAccessFile(tempFile, "r");
		long tempSize = tempContent.length();
		if (tempSize > 0) {
			// Find last line
			long tempPos = Math.max(0, tempSize - 50000);
			tempContent.seek(tempPos);
			int tempLen = (int) (tempSize - tempPos);
			byte[] tempBytes = new byte[tempLen];
			tempContent.readFully(tempBytes, 0, tempLen);
			String tempLastLines = new String(tempBytes);
			StringTokenizer tempTokens = new StringTokenizer(tempLastLines, "\r\n");
			if (tempTokens.hasMoreElements()) {
				// Skip first token.
				tempTokens.nextToken();
			}
			while (tempTokens.hasMoreElements()) {
				StringTokenizer tempTaskTokens = new StringTokenizer(tempTokens.nextToken(), "\t");
				if (tempTaskTokens.countTokens() == 3) {
					String tempTaskName = tempTaskTokens.nextToken();
					if (!isInternalTask(tempTaskName) && tempTaskName.trim().length() > 0) {
						tempLastTasks.remove(tempTaskName);
						tempLastTasks.add(0, tempTaskName);
					}
				}
			}
		}
		tempContent.close();
		return tempLastTasks;
	}

	public static boolean isInternalTask(String tempTaskName) {
		return TASK_CLOSED.equals(tempTaskName) || TASK_STARTED.equals(tempTaskName);
	}

	public void saveTasks(long aStartPos, List<PosAndContent<Task>> aTasks) throws IOException {
		File tempFile = getActualFile();
		RandomAccessFile tempContent = new RandomAccessFile(tempFile, "rw");
		tempContent.setLength(aStartPos);
		tempContent.seek(aStartPos);
		for (PosAndContent<Task> tempPosAndContent : aTasks) {
			tempContent.write(format(tempPosAndContent.getLine()));
		}
		tempContent.close();

	}
}
