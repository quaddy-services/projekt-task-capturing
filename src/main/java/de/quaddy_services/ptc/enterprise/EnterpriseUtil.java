package de.quaddy_services.ptc.enterprise;

import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTextField;

import de.quaddy_services.ptc.MainController;
import de.quaddy_services.ptc.MainModel;

public class EnterpriseUtil implements EnterpriseUtilRemote {

	private EnterpriseUtilRemote remote;

	@Override
	public void initTaskNames(JFrame aFrame, MainModel aModel, String tempServer) {
		remote = null;
		if (tempServer == null || tempServer.trim().length() == 0) {
			return;
		}
		try {
			initRemote();
			remote.initTaskNames(aFrame, aModel, tempServer);
		} catch (Exception e) {
			throw new RuntimeException("Server not reachable: " + tempServer, e);
		}
	}

	private void initRemote() {
		remote = new EnterpriseUtilImpl();
	}

	/**
	 * @return the fixedTaskNames
	 */
	@Override
	public List<String> getFixedTaskNames() {
		if (remote == null) {
			return null;
		}
		return remote.getFixedTaskNames();
	}

	@Override
	public void filterWithFixedTasks(MainModel model, List<String> aLastTasks) {
		if (remote == null) {
			return;
		}
		remote.filterWithFixedTasks(model, aLastTasks);
	}

	@Override
	public void addFixedDocumentFilter(final JTextField tempEditor) {
		if (remote == null) {
			return;
		}
		remote.addFixedDocumentFilter(tempEditor);
	}

	@Override
	public Action createShowReportAction(MainController aController, long aFrom, long aTo) {
		if (remote == null) {
			return null;
		}
		return remote.createShowReportAction(aController, aFrom, aTo);
	}

	@Override
	public Action createShowBookingSystemAction(MainController aController) {
		if (remote == null) {
			return null;
		}
		return remote.createShowBookingSystemAction(aController);
	}

}
