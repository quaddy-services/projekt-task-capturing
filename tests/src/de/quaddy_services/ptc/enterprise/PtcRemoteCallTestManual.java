package de.quaddy_services.ptc.enterprise;

import java.util.List;

import junit.framework.TestCase;

/**
 * Manual test. Can only run if server is running.
 * 
 * @author user
 */
public class PtcRemoteCallTestManual extends TestCase {
	public void testGetResource() throws Exception {
		PtcRemoteCall tempRemoteCall = new PtcRemoteCall("localhost:334");
		List<String> tempTasks = tempRemoteCall
				.getBookableTaskNames("test");
		System.out.println(tempTasks);
		assertEquals(3, tempTasks.size());
	}
}
