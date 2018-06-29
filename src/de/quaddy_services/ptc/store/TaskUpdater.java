package de.quaddy_services.ptc.store;

import java.io.IOException;

public interface TaskUpdater {
	public Task updateLastTask(String aTaskName) throws IOException, NetworkDriveNotAvailable;

}
