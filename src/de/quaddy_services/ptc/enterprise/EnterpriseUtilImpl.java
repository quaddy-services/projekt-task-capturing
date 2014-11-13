package de.quaddy_services.ptc.enterprise;

import java.util.List;

import javax.swing.Action;

import de.quaddy_services.ptc.MainController;
import de.quaddy_services.ptc.enterprise.custom.AbstractEnterpriseUtilRemote;
import de.quaddy_services.ptc.log.Log;

public class EnterpriseUtilImpl extends AbstractEnterpriseUtilRemote {
	private Log LOG = new Log(EnterpriseUtilImpl.class);

	public Action createShowBookingSystemAction(final MainController aController) {
		return null;
	}

	@Override
	protected void saveReport(final MainController aController, final StringBuilder aReport) {
		Thread tempSaveThread = new EnterpriseUtilSaveReportThread(getServerName(), aReport, aController);
		tempSaveThread.setName("Save-" + tempSaveThread.getName());
		tempSaveThread.start();
	}

	@Override
	protected List<String> getBookableTaskNames() {
		String tempServer = getServerName();
		if (tempServer == null || tempServer.trim().length() == 0) {
			return null;
		}
		PtcRemoteCall tempRemote = new PtcRemoteCall(tempServer);
		String tempUserName = System.getProperty("user.name");
		LOG.info("Load BookableTasks from " + tempServer + " for " + tempUserName);
		List<String> tempBookableTaskNames;
		try {
			tempBookableTaskNames = tempRemote.getBookableTaskNames(tempUserName);
		} catch (Exception e) {
			throw new RuntimeException("Server '" + tempServer + "' not reachable", e);
		}
		return tempBookableTaskNames;
	}
}
