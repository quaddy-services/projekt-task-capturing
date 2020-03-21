package de.quaddy_services.ptc.edit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.quaddy_services.ptc.enterprise.EnterpriseUtil;
import de.quaddy_services.ptc.store.PosAndContent;
import de.quaddy_services.ptc.store.Task;
import de.quaddy_services.ptc.store.TaskHistory;

public class EditNoChangeTest {

	private static final File TEST_FILE = new File("tests/src/de/quaddy_services/ptc/edit/EditNoChangeTest.txt");

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test
	public void testNoChange() throws IOException {
		List<String> tempOrig = readFile(TEST_FILE);
		final File tempTestFile = File.createTempFile("EditNoChangeTest", ".txt");
		tempTestFile.deleteOnExit();
		copyFile(TEST_FILE, tempTestFile);
		TaskHistory tempTaskHistory = new TaskHistory() {
			@Override
			public File getActualFile() {
				return tempTestFile;
			}
		};
		TaskEditor tempTaskEdtior = new TaskEditor();
		long tempStartPos;
		List<PosAndContent<Task>> tempTasks;
		tempTasks = tempTaskHistory.getLastLinesForEdit();
		tempStartPos = tempTasks.get(0).getPosInFile();
		tempTaskEdtior.setTasks(tempTasks, new EnterpriseUtil());
		// tempTaskEdtior.show
		for (Iterator<PosAndContent<Task>> i = tempTasks.iterator(); i.hasNext();) {
			PosAndContent<Task> tempPosAndContent = i.next();
			if (tempTaskEdtior.getDeletedTasks().contains(tempPosAndContent)) {
				i.remove();
			}
		}
		tempTaskHistory.saveTasks(tempStartPos, tempTasks);
		List<String> tempSaved = readFile(tempTestFile);
		Iterator<String> tempSaveI = tempSaved.iterator();
		for (String tempString : tempOrig) {
			assertEquals(tempString, tempSaveI.next());
		}
		System.out.println("ok");
	}

	private List<String> readFile(File aTestFile) throws IOException {
		List<String> tempLines = new ArrayList<String>();
		BufferedReader tempReader = new BufferedReader(new FileReader(aTestFile));
		while (tempReader.ready()) {
			tempLines.add(tempReader.readLine());
		}
		tempReader.close();
		return tempLines;
	}

	private void copyFile(File aFrom, File aTo) throws FileNotFoundException, IOException {
		FileInputStream tempReader = new FileInputStream(aFrom);
		FileOutputStream tempWriter = new FileOutputStream(aTo);
		int tempRead;
		byte[] tempBuff = new byte[1024 * 20];
		while (0 <= (tempRead = tempReader.read(tempBuff))) {
			tempWriter.write(tempBuff, 0, tempRead);
		}
		tempReader.close();
		tempWriter.close();
	}

}
