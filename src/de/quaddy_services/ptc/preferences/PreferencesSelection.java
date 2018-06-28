package de.quaddy_services.ptc.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;
import de.quaddy_services.ptc.store.FileUtil;
import de.quaddy_services.report.format.TimeFormatList;
import de.quaddy_services.report.groupby.GroupByList;

public class PreferencesSelection extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesSelection.class);

	private JComboBox taskDelimiter = new JComboBox();
	private JComboBox dontSumChar = new JComboBox();
	private JComboBox groupBy = new JComboBox();
	private JComboBox timeFormat = new JComboBox();
	private JTextField enterpriseServer = new JTextField();
	private JCheckBox alwaysOnTop = new JCheckBox();
	private JTextField reminderFlashOnMinute = new JTextField();
	private JTextField dataFolder = new JTextField();
	private JButton dataFolderSelection = new JButton("...");

	public PreferencesSelection() {
		setOpaque(false);
		setLayout(new GridBagLayout());
		int x = 0;
		int y = 0;

		add(new JLabel("All preferences can be changed at any time."), createGrid(x, y, 2));
		y++;
		add(new JLabel("They do not influence the data captured while working."), createGrid(x, y, 2));
		y++;
		add(new JLabel(""), createGrid(x, y, 2));
		y++;

		taskDelimiter.setMaximumRowCount(20);
		taskDelimiter.setModel(new DefaultComboBoxModel(TaskDelimiterList.getTaskDelimiterNames().toArray()));
		add(new JLabel("Task Delimiter:"), createGrid(x, y));
		x++;
		add(taskDelimiter, createGrid(x, y));
		x = 0;
		y++;
		add(new JLabel(" (On this delimiter char the report " + "will be breaked down (grouped) in reports)"), createGrid(x, y, 2));

		y++;
		add(new JLabel(""), createGrid(x, y));
		y++;

		dontSumChar.setMaximumRowCount(20);
		dontSumChar.setModel(new DefaultComboBoxModel(DontSumCharList.getDontSumCharNames().toArray()));
		add(new JLabel("Don't Sum Character:"), createGrid(x, y));
		x++;
		add(dontSumChar, createGrid(x, y));
		x = 0;
		y++;
		add(new JLabel(" (Tasks starting with that char " + "wont be summed in the total)"), createGrid(x, y, 2));

		y++;
		add(new JLabel(""), createGrid(x, y));
		y++;

		groupBy.setModel(new DefaultComboBoxModel(GroupByList.getGroupByNames().toArray()));
		add(new JLabel("Group By:"), createGrid(x, y));
		x++;
		add(groupBy, createGrid(x, y));
		x = 0;
		y++;
		add(new JLabel(" (The default 'group by' for tasks)"), createGrid(x, y, 2));

		y++;
		add(new JLabel(""), createGrid(x, y));
		y++;

		timeFormat.setModel(new DefaultComboBoxModel(TimeFormatList.getTimeFormatNames().toArray()));
		add(new JLabel("Time Format:"), createGrid(x, y));
		x++;
		add(timeFormat, createGrid(x, y));
		x = 0;
		y++;
		add(new JLabel(" (Format of the task times)"), createGrid(x, y, 2));

		x = 0;
		y++;
		add(new JLabel("Enterprise Server:"), createGrid(x, y));
		x++;
		add(enterpriseServer, createGrid(x, y));

		x = 0;
		y++;
		add(new JLabel("Always on top:"), createGrid(x, y));
		x++;
		add(alwaysOnTop, createGrid(x, y));

		x = 0;
		y++;
		add(new JLabel("Reminderflash on Minute:"), createGrid(x, y));
		x++;
		add(reminderFlashOnMinute, createGrid(x, y));
		reminderFlashOnMinute.setToolTipText("Flash every hour on specified minute. Default=57. To disable clear field.");

		x = 0;
		y++;
		add(new JLabel("Datafolder:"), createGrid(x, y));
		x++;
		JPanel tempDataFolderPanel = createDataFolderPanel(dataFolder, dataFolderSelection);
		add(tempDataFolderPanel, createGrid(x, y));

		dataFolder.setToolTipText("Datafolder (change will NOT copy history)");
		dataFolderSelection.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent aE) {
				selectDataFolder();
			}
		});

		x = 0;
		y++;
		add(new JLabel(""), createGrid(x, y));

		y++;
		JButton tempRestore = new JButton("Defaults");
		tempRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				restoreDefaults();
			}

		});
		add(tempRestore, createGrid(x, y));
	}

	/**
	 *
	 */
	private JPanel createDataFolderPanel(JTextField aDataFolder, JButton aDataFolderSelection) {
		JPanel tempJPanel = new JPanel();
		tempJPanel.setLayout(new GridBagLayout());
		GridBagConstraints tempGridText = createGrid(0, 0);
		tempGridText.weightx = 1.0;
		tempJPanel.add(aDataFolder, tempGridText);
		GridBagConstraints tempGridButton = createGrid(1, 0);
		tempGridButton.insets = new Insets(0, 0, 0, 0);
		tempJPanel.add(aDataFolderSelection, tempGridButton);
		return tempJPanel;
	}

	/**
	 *
	 */
	protected void selectDataFolder() {
		JFileChooser tempJFileChooser = new JFileChooser(dataFolder.getText());
		tempJFileChooser.setDialogTitle("Change data folder (task history will be lost!)");
		tempJFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		tempJFileChooser.setAcceptAllFileFilterUsed(false);
		int tempResult = tempJFileChooser.showOpenDialog(this);
		if (tempResult == JFileChooser.APPROVE_OPTION) {
			dataFolder.setText(tempJFileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void restoreDefaults() {
		taskDelimiter.setSelectedItem(TaskDelimiterList.DEFAULT);
		dontSumChar.setSelectedItem(DontSumCharList.DEFAULT);
		groupBy.setSelectedItem(GroupByList.DEFAULT);
		timeFormat.setSelectedItem(TimeFormatList.DEFAULT);
		enterpriseServer.setText("");
		alwaysOnTop.setSelected(false);
		reminderFlashOnMinute.setText("57");
		dataFolder.setText(FileUtil.getDefaultDataFolder());
	}

	private GridBagConstraints createGrid(int aI, int aY) {
		return createGrid(aI, aY, 1);
	}

	private GridBagConstraints createGrid(int aI, int aY, int aSpanX) {
		GridBagConstraints tempGBC = new GridBagConstraints();
		tempGBC.fill = GridBagConstraints.HORIZONTAL;
		tempGBC.gridx = aI;
		tempGBC.gridy = aY;
		tempGBC.gridwidth = aSpanX;
		tempGBC.insets = new Insets(2, 2, 2, 2);
		return tempGBC;
	}

	public void setValues(Properties aProperties) {
		taskDelimiter.setSelectedItem(aProperties.getProperty(Preferences.TASK_DELIMITER, TaskDelimiterList.DEFAULT));
		dontSumChar.setSelectedItem(aProperties.getProperty(Preferences.DONT_SUM_DELIMITER, DontSumCharList.DEFAULT));
		timeFormat.setSelectedItem(aProperties.getProperty(Preferences.TIME_FORMAT, TimeFormatList.DEFAULT));
		groupBy.setSelectedItem(aProperties.getProperty(Preferences.GROUP_BY, GroupByList.DEFAULT));
		enterpriseServer.setText(aProperties.getProperty(Preferences.ENTERPRISE_SERVER, ""));
		alwaysOnTop.setSelected(Boolean.valueOf(aProperties.getProperty(Preferences.ALWAYS_ON_TOP, "" + Boolean.FALSE)));
		reminderFlashOnMinute.setText(aProperties.getProperty(Preferences.REMINDER_FLASH_ON_MINUTE, "57"));
		dataFolder.setText(aProperties.getProperty(Preferences.DATA_FOLDER, FileUtil.getDefaultDataFolder()));
	}

	public Properties getValues() {
		Properties tempProperties = new Properties();
		tempProperties.setProperty(Preferences.TASK_DELIMITER, (String) taskDelimiter.getSelectedItem());
		tempProperties.setProperty(Preferences.DONT_SUM_DELIMITER, (String) dontSumChar.getSelectedItem());
		tempProperties.setProperty(Preferences.TIME_FORMAT, (String) timeFormat.getSelectedItem());
		tempProperties.setProperty(Preferences.GROUP_BY, (String) groupBy.getSelectedItem());
		tempProperties.setProperty(Preferences.ENTERPRISE_SERVER, enterpriseServer.getText());
		tempProperties.setProperty(Preferences.ALWAYS_ON_TOP, "" + alwaysOnTop.isSelected());
		tempProperties.setProperty(Preferences.REMINDER_FLASH_ON_MINUTE, reminderFlashOnMinute.getText());
		return tempProperties;
	}
}
