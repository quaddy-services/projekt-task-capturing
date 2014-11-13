package de.quaddy_services.ptc.store;

import java.io.IOException;

public class TaskHistoryTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		TaskHistory tempTaskHistory=new TaskHistory();
		tempTaskHistory.setFileName("test.txt");
		tempTaskHistory.getActualFile().delete();
		tempTaskHistory.updateLastTask("Test1");
		Thread.sleep(2000);
		// Same task
		tempTaskHistory.updateLastTask("Test1");
		Thread.sleep(2000);
		tempTaskHistory.updateLastTask("Test2");
		Thread.sleep(2000);
		tempTaskHistory.updateLastTask("Test2");
		for (int i=0;i<1000;i++) {
			tempTaskHistory.updateLastTask("TestNr"+i);
		}
		tempTaskHistory.updateLastTask("Test1");
		Thread.sleep(2000);
		// Same task
		tempTaskHistory.updateLastTask("Test1");
		Thread.sleep(2000);
		tempTaskHistory.updateLastTask("Test2");
		Thread.sleep(2000);
		tempTaskHistory.updateLastTask("Test2");
	}

}
