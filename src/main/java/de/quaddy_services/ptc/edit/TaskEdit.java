package de.quaddy_services.ptc.edit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.quaddy_services.ptc.enterprise.EnterpriseUtilRemote;
import de.quaddy_services.ptc.store.PosAndContent;
import de.quaddy_services.ptc.store.Task;
import de.quaddy_services.ptc.store.TaskHistory;

public abstract class TaskEdit extends JPanel {
	private static final String INTERNAL_REMARK = "<html><body>This is an internal taks marking<br>" + "the end of a session<br>"
			+ "(program exit or shutdown).<br>" + "Deleting it will <b>not</b> have any impact on reports,<br>"
			+ "but you loose the information when a shutdown did take place.</body></html>";
	private Task task;
	private JFormattedTextField to;
	private EnterpriseUtilRemote enterpriseUtil;

	public TaskEdit(PosAndContent<Task> aPosAndContent, EnterpriseUtilRemote anEnterpriseUtil) {
		task = aPosAndContent.getLine();
		enterpriseUtil = anEnterpriseUtil;
		init(task);
	}

	private void init(final Task aTask) {
		setLayout(new GridBagLayout());
		GridBagConstraints tempGBC = new GridBagConstraints();
		tempGBC.gridx = 0;
		tempGBC.gridy = 0;
		tempGBC.weightx = 2.0;
		tempGBC.fill = GridBagConstraints.HORIZONTAL;

		final JTextField tempName = new JTextField();
		tempName.setText(aTask.getName());
		TaskHistory tempTaskHistory = new TaskHistory();
		if (tempTaskHistory.isInternalTask(aTask.getName())) {
			tempName.setEditable(false);
			tempName.setEnabled(false);
			tempName.setToolTipText(INTERNAL_REMARK);
		} else {
			tempName.getDocument().addDocumentListener(new ChangeListener() {
				@Override
				public void textChanged(String aNewString) {
					aTask.setName(aNewString);
				}
			});
			enterpriseUtil.addFixedDocumentFilter(tempName);
		}
		add(tempName, tempGBC);
		tempGBC.gridx++;
		tempGBC.weightx = 0.0;

		final JFormattedTextField tempFrom = new JFormattedTextField(tempTaskHistory.DATE_FORMAT);
		tempFrom.setValue(aTask.getStart());
		add(tempFrom, tempGBC);
		tempGBC.gridx++;
		tempFrom.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent aEvt) {
				if ("value".equals(aEvt.getPropertyName())) {
					aTask.setStart((Date) aEvt.getNewValue());
				}
			}
		});

		add(new JLabel("-"), tempGBC);
		tempGBC.gridx++;

		to = new JFormattedTextField(tempTaskHistory.DATE_FORMAT);
		to.setValue(aTask.getStop());
		add(to, tempGBC);
		tempGBC.gridx++;
		to.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent aEvt) {
				if ("value".equals(aEvt.getPropertyName())) {
					aTask.setStop((Date) aEvt.getNewValue());
				}
			}
		});

		final JButton tempButton = new JButton("Delete");
		tempButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent aE) {
				tempName.setEnabled(false);
				tempFrom.setEnabled(false);
				to.setEnabled(false);
				tempButton.setEnabled(false);
				fireDeleted();
			}
		});
		if (tempTaskHistory.isInternalTask(aTask.getName())) {
			tempButton.setToolTipText(INTERNAL_REMARK);
		}
		add(tempButton, tempGBC);
	}

	public abstract void fireDeleted();

	public Task updateLastTask() {
		Date tempDate = new Date();
		to.setValue(tempDate);
		task.setStop(tempDate);
		return task;
	}

}
