package de.quaddy_services.ptc.enterprise;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Manual test. Can only run if server is running.
 *
 * @author user
 */
public class PtcRemoteCallTestManual {
	@Test
	@Disabled
	public void testGetResource() throws Exception {
		PtcRemoteCall tempRemoteCall = new PtcRemoteCall("localhost:334");
		List<String> tempTasks = tempRemoteCall.getBookableTaskNames("test");
		System.out.println(tempTasks);
		assertEquals(3, tempTasks.size());
	}
}
