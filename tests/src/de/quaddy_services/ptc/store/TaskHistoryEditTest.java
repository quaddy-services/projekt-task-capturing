package de.quaddy_services.ptc.store;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import junit.framework.TestCase;

public class TaskHistoryEditTest extends TestCase {

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	public void testReadCorrect() throws IOException {
		TaskHistory tempTaskHistory = new TaskHistory() {
			@Override
			public File getActualFile() {
				return new File(
						"tests/src/de/quaddy_services/ptc/store/TaskHistoryEditTest.txt");
			}
		};
		List<PosAndContent<Task>> tempList = tempTaskHistory
				.getLastLinesForEdit();
		assertEquals("" + tempList, 10, tempList.size());
		assertEquals("SWD-SV0036-O4-Upload", tempList.get(0).getLine()
				.getName());
		assertEquals("SWD-Support-OMS OMS-Errors", tempList.get(9).getLine()
				.getName());
	}

}
