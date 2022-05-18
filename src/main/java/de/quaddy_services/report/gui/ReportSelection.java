package de.quaddy_services.report.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.quaddy_services.report.format.ReportType;
import de.quaddy_services.report.format.ReportTypeList;
import de.quaddy_services.report.format.TimeFormat;
import de.quaddy_services.report.format.TimeFormatList;
import de.quaddy_services.report.groupby.GroupBy;
import de.quaddy_services.report.groupby.GroupByList;

public class ReportSelection extends JPanel {
	private JFormattedTextField from = new JFormattedTextField();
	private JFormattedTextField to = new JFormattedTextField();
	private JComboBox reportType = new JComboBox();
	private JComboBox groupby1 = new JComboBox();
	private JComboBox groupby2 = new JComboBox();
	private JComboBox timeformat = new JComboBox();

	public ReportSelection() {
		setOpaque(false);
		Calendar tempFirstDayOfCurrentMonth = Calendar.getInstance();
		tempFirstDayOfCurrentMonth.set(Calendar.DAY_OF_MONTH, 1);
		from.setValue(tempFirstDayOfCurrentMonth.getTime());
		to.setValue(new Date());
		setLayout(new GridBagLayout());
		int x = 0;
		int y = 0;
		add(new JLabel("From:"), createGrid(x, y));
		x++;
		add(from, createGrid(x, y));
		x = 0;
		y++;
		add(new JLabel("To:"), createGrid(x, y));
		x++;
		add(to, createGrid(x, y));
		x = 0;
		y++;

		reportType.setModel(new DefaultComboBoxModel(ReportTypeList.getReportTypetNames().toArray()));
		add(new JLabel("Report Type:"), createGrid(x, y));
		x++;
		add(reportType, createGrid(x, y));
		x = 0;
		y++;
		reportType.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent aE) {
				ReportType tempSelectedReportType = getReportType();
				if (ReportTypeList.DEFAULT.equals(tempSelectedReportType)) {
					groupby1.setEnabled(true);
					groupby2.setEnabled(true);
					timeformat.setEnabled(true);
				} else {
					groupby1.setEnabled(false);
					groupby2.setEnabled(false);
					timeformat.setEnabled(false);
				}
			}
		});

		add(new JLabel(""), createGrid(x, y));
		y++;

		timeformat.setModel(new DefaultComboBoxModel(TimeFormatList.getTimeFormatNames().toArray()));
		add(new JLabel("Time format:"), createGrid(x, y));
		x++;
		add(timeformat, createGrid(x, y));
		x = 0;
		y++;

		groupby1.setModel(new DefaultComboBoxModel(GroupByList.getGroupByNames().toArray()));
		add(new JLabel("GroupBy 1:"), createGrid(x, y));
		x++;
		add(groupby1, createGrid(x, y));
		x = 0;
		y++;

		groupby2.setModel(new DefaultComboBoxModel(GroupByList.getGroupByNames().toArray()));
		add(new JLabel("GroupBy 2:"), createGrid(x, y));
		x++;
		add(groupby2, createGrid(x, y));
		x = 0;
		y++;

		add(new JLabel(""), createGrid(x, y));
		y++;
		add(new JLabel(""), createGrid(x, y));
		y++;
		add(new JLabel(""), createGrid(x, y));
		y++;
		add(new JLabel(""), createGrid(x, y));
		y++;
	}

	private GridBagConstraints createGrid(int aI, int aY) {
		GridBagConstraints tempGBC = new GridBagConstraints();
		tempGBC.weightx = 1;
		tempGBC.weighty = 1;
		tempGBC.fill = GridBagConstraints.HORIZONTAL;
		tempGBC.gridx = aI;
		tempGBC.gridy = aY;
		tempGBC.insets = new Insets(2, 2, 2, 2);
		return tempGBC;
	}

	private GroupBy getGroupBy1() {
		return GroupByList.getGroupBy(groupby1.getSelectedItem().toString());
	}

	private GroupBy getGroupBy2() {
		return GroupByList.getGroupBy(groupby2.getSelectedItem().toString());
	}

	public TimeFormat getTimeFormat() {
		return TimeFormatList.getTimeFormat(timeformat.getSelectedItem().toString());
	}

	public ReportType getReportType() {
		return ReportTypeList.getReportType(reportType.getSelectedItem().toString());
	}

	public long getFrom() {
		Calendar tempCal = Calendar.getInstance();
		tempCal.setTime((Date) from.getValue());
		tempCal.set(Calendar.HOUR_OF_DAY, 0);
		tempCal.set(Calendar.MINUTE, 0);
		tempCal.set(Calendar.SECOND, 0);
		tempCal.set(Calendar.MILLISECOND, 0);
		return tempCal.getTimeInMillis();
	}

	public long getTo() {
		Calendar tempCal = Calendar.getInstance();
		tempCal.setTime((Date) to.getValue());
		tempCal.set(Calendar.HOUR_OF_DAY, 23);
		tempCal.set(Calendar.MINUTE, 59);
		tempCal.set(Calendar.SECOND, 59);
		tempCal.set(Calendar.MILLISECOND, 900);
		return tempCal.getTimeInMillis();
	}

	public GroupBy[] getGroupBys() {
		if (getGroupBy1() == getGroupBy2()) {
			return new GroupBy[] { getGroupBy1() };
		}
		return new GroupBy[] { getGroupBy1(), getGroupBy2() };
	}

	public void setGroupBy(GroupBy aGroupBy) {
		groupby1.setSelectedItem(aGroupBy.getName());
	}

	public void setTimeFormat(TimeFormat aTimeFormat) {
		timeformat.setSelectedItem(aTimeFormat.getName());
	}

	public void setReportType(ReportType aReportType) {
		reportType.setSelectedItem(aReportType.getName());
	}
}
