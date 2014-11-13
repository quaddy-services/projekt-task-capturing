package de.quaddy_services.ptc.enterprise;

import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTextField;

import de.quaddy_services.ptc.MainController;
import de.quaddy_services.ptc.MainModel;

public interface EnterpriseUtilRemote {
	public void initTaskNames(JFrame aFrame, MainModel aModel, String tempServer);

	/**
	 * @return the fixedTaskNames
	 */
	public List<String> getFixedTaskNames();

	public void filterWithFixedTasks(MainModel model, List<String> aLastTasks);

	public void addFixedDocumentFilter(JTextField tempEditor);

	public Action createShowReportAction(MainController aController,
			long aFrom, long aTo);
	public Action createShowBookingSystemAction(MainController aController);

}
