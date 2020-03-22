package de.quaddy_services.ptc.store;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskHistoryEditTest {

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	@Test
	public void testReadCorrect() throws IOException {
		TaskHistory tempTaskHistory = new TaskHistory() {
			@Override
			public File getActualFile() {
				return new File("src/test/resources/de/quaddy_services/ptc/store/TaskHistoryEditTest.txt");
			}
		};
		List<PosAndContent<Task>> tempList = tempTaskHistory.getLastLinesForEdit();
		assertEquals(10, tempList.size(), "" + tempList);
		assertEquals("SWD-SV0036-O4-Upload", tempList.get(0).getLine().getName());
		assertEquals("SWD-Support-OMS OMS-Errors", tempList.get(9).getLine().getName());
	}

}
