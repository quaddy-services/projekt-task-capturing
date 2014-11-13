package de.quaddy_services.ptc.edit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.quaddy_services.ptc.enterprise.EnterpriseUtil;
import de.quaddy_services.ptc.enterprise.EnterpriseUtilRemote;
import de.quaddy_services.ptc.store.PosAndContent;
import de.quaddy_services.ptc.store.Task;
import de.quaddy_services.ptc.store.TaskUpdater;

public class TaskEditor extends javax.swing.JPanel implements TaskUpdater {

	private List<PosAndContent<Task>> deletedTasks = new ArrayList<PosAndContent<Task>>();

	public TaskEditor() {
		setLayout(new GridBagLayout());
	}

	private TaskEdit lastTask;

	public Task updateLastTask(String aTaskName) {
		return lastTask.updateLastTask();
	}

	public void setTasks(List<PosAndContent<Task>> aLastLines,
			EnterpriseUtilRemote anEnterpriseUtil) {
		GridBagConstraints tempGBC = new GridBagConstraints();
		tempGBC.fill = GridBagConstraints.HORIZONTAL;
		tempGBC.weightx = 1.0;
		tempGBC.gridx = 0;
		tempGBC.gridy = 0;
		for (final PosAndContent<Task> tempPosAndContent : aLastLines) {
			TaskEdit tempTaskEdit = new TaskEdit(tempPosAndContent,
					anEnterpriseUtil) {
				@Override
				public void fireDeleted() {
					deletedTasks.add(tempPosAndContent);
				}
			};
			add(tempTaskEdit, tempGBC);
			tempGBC.gridy++;
			lastTask = tempTaskEdit;
		}
	}

	/**
	 * @return the deletedTasks
	 */
	public List<PosAndContent<Task>> getDeletedTasks() {
		return deletedTasks;
	}

}
