package de.quaddy_services.ptc.enterprise.custom;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import de.quaddy_services.ptc.DisplayHelper;
import de.quaddy_services.ptc.MainController;
import de.quaddy_services.ptc.MainModel;
import de.quaddy_services.ptc.enterprise.EnterpriseUtilRemote;
import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;
import de.quaddy_services.report.TaskReport;
import de.quaddy_services.report.format.TimeFormatList;
import de.quaddy_services.report.groupby.GroupByList;

public abstract class AbstractEnterpriseUtilRemote implements EnterpriseUtilRemote {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractEnterpriseUtilRemote.class);
	private List<String> fixedTaskNames;
	private MainModel model;
	private String serverName;

	@Override
	public void initTaskNames(JFrame aFrame, MainModel aModel, String tempServer) {
		LOG.info("initTaskNames");
		serverName = tempServer;
		fixedTaskNames = null;
		model = aModel;
		List<String> tempBookableTaskNames = getBookableTaskNames();
		if (tempBookableTaskNames == null) {
			return;
		}
		fixedTaskNames = new ArrayList<String>();
		String tempDelimiter = aModel.getTaskDelimiter().getDelimiter();
		for (String tempBookable : tempBookableTaskNames) {
			String tempTask = tempBookable;
			if (!tempTask.endsWith(tempDelimiter)) {
				tempTask = tempTask + tempDelimiter;
			}
			fixedTaskNames.add(tempTask);
		}
	}

	@Override
	public Action createShowReportAction(final MainController aController, final long aFrom, final long aTo) {
		return new AbstractAction("Send to bookingsystem") {
			@Override
			public void actionPerformed(ActionEvent aE) {
				createReport(aController, aFrom, aTo);
			}
		};
	}

	private void createReport(final MainController aController, final long aFrom, final long aTo) {
		try {
			TaskReport tempTaskReport = new TaskReport(aController.getTaskHistory(), aController.getFrame(), model.getTaskDelimiter(), model.getDontSumChar(),
					getFixedTaskNames());
			tempTaskReport.setPrettyFormat(false);
			tempTaskReport.setIgnoreDontSumTasks(true);
			StringBuilder tempReport = new StringBuilder();
			tempTaskReport.createReport(tempReport, aFrom, aTo, GroupByList.getGroupBy(GroupByList.DAY), TimeFormatList.MILLIS);

			LOG.info(tempReport.toString());

			saveReport(aController, tempReport);

		} catch (Exception e) {
			LOG.exception(e);
			DisplayHelper.displayException(aController.getFrame(), e);
		}
	}

	protected abstract List<String> getBookableTaskNames();

	protected abstract void saveReport(final MainController aController, final StringBuilder aReport);

	protected String getServerName() {
		return serverName;
	}

	/**
	 * @return the fixedTaskNames
	 */
	@Override
	public List<String> getFixedTaskNames() {
		return fixedTaskNames;
	}

	@Override
	public void filterWithFixedTasks(MainModel model, List<String> aLastTasks) {
		if (fixedTaskNames == null || fixedTaskNames.size() == 0) {
			return;
		}
		for (Iterator<String> i = aLastTasks.iterator(); i.hasNext();) {
			String tempString = i.next();
			if (tempString.startsWith(model.getDontSumChar().getChar())) {
				continue;
			}
			boolean tempIsFixedTask = false;
			for (String tempFixed : getFixedTaskNames()) {
				if (tempString.startsWith(tempFixed)) {
					tempIsFixedTask = true;
					break;
				}
			}
			if (!tempIsFixedTask) {
				LOG.info("Remove '" + tempString + "' as it is not in the fixed tasks");
				i.remove();
			}
		}
		for (String tempFixed : getFixedTaskNames()) {
			boolean tempFixedAvailable = false;
			for (String tempTask : aLastTasks) {
				if (tempTask.startsWith(tempFixed)) {
					tempFixedAvailable = true;
				}
			}
			if (!tempFixedAvailable) {
				aLastTasks.add(tempFixed);
			}
		}
	}

	@Override
	public void addFixedDocumentFilter(final JTextField tempEditor) {
		List<String> tempFixedTaskNames = getFixedTaskNames();
		if (tempFixedTaskNames == null || tempFixedTaskNames.size() == 0) {
			return;
		}
		Document tempDocument = tempEditor.getDocument();
		if (tempDocument instanceof AbstractDocument) {
			AbstractDocument tempAbstractDocument = (AbstractDocument) tempDocument;
			tempAbstractDocument.setDocumentFilter(new DocumentFilter() {
				@Override
				public void insertString(FilterBypass aFb, int aOffset, String aString, AttributeSet aAttr) throws BadLocationException {
					int tempReadOnlyPos = getReadOnlyPos(tempEditor);
					if (tempReadOnlyPos > 0 && aOffset < tempReadOnlyPos) {
						LOG.info("Do not insert '" + aString + "' because it is fixed area");
						return;
					}
					if (aOffset == 0) {
						if (!isValidTask(aString)) {
							LOG.info("Do not insert '" + aString + "' because it is not a valid task");
							return;
						}
					}
					super.insertString(aFb, aOffset, aString, aAttr);
				}

				private int getReadOnlyPos(JTextField aEditor) {
					String tempCurrentText = aEditor.getText();
					return getReadOnlyPosInText(tempCurrentText);
				}

				@Override
				public void remove(FilterBypass aFb, int aOffset, int aLength) throws BadLocationException {
					int tempReadOnlyPos = getReadOnlyPos(tempEditor);
					if (tempReadOnlyPos > 0 && aOffset < tempReadOnlyPos) {
						LOG.info("Do not remove pos " + aOffset + " because it is fixed area");
						return;
					}
					if (aOffset == 0) {
						String tempCurrentText = tempEditor.getText();
						if (aLength <= tempCurrentText.length()) {
							String tempNewText = tempCurrentText.substring(aLength);
							if (!isValidTask(tempNewText)) {
								LOG.info("Do not remove " + aLength + " chars because '" + tempNewText + "' is not a valid task.");
								return;
							}
						}
					}
					super.remove(aFb, aOffset, aLength);
				}

				@Override
				public void replace(FilterBypass aFb, int aOffset, int aLength, String aText, AttributeSet aAttrs) throws BadLocationException {
					if (aOffset == 0) {
						if (!isValidTask(aText)) {
							LOG.info("Do not replace '" + aText + "' because it is not a valid task");
							return;
						}
					}
					if (aOffset != 0) {
						int tempReadOnlyPos = getReadOnlyPos(tempEditor);
						if (tempReadOnlyPos > 0 && aOffset < tempReadOnlyPos) {
							LOG.info("Do not replace '" + aText + "' because it is fixed area");
							return;
						}
					}
					super.replace(aFb, aOffset, aLength, aText, aAttrs);
				}

				private boolean isValidTask(String aText) {
					if (aText.startsWith(model.getDontSumChar().getChar())) {
						return true;
					}
					if (aText.trim().length() == 0) {
						return true;
					}
					for (String tempFixed : getFixedTaskNames()) {
						if (aText.startsWith(tempFixed)) {
							return true;
						}
					}
					return false;
				}
			});
		} else {
			LOG.info("Cannot add documentfilter to " + tempDocument);
		}
	}

	private int getReadOnlyPosInText(String aText) {
		List<String> tempFixedTaskNames = getFixedTaskNames();
		for (String tempFixed : tempFixedTaskNames) {
			if (aText.startsWith(tempFixed)) {
				return tempFixed.length();
			}
		}
		return -1;
	}
}
