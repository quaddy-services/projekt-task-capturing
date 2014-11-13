/**
 * 
 */
package de.quaddy_services.ptc;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.Document;

import de.quaddy_services.ptc.enterprise.EnterpriseUtilRemote;
import de.quaddy_services.ptc.log.Log;
import de.quaddy_services.ptc.menu.SpeedLinkMenuCreator;

/**
 * @author Stefan Cordes
 * 
 */
public class MainView extends JPanel {
	private Log LOG = new Log(this.getClass());

	private MainController controller;

	private JPanel contentPanel = null;

	private JComboBox taskNameBox = null;

	private JButton speedlinkButton = null;

	private JButton menuButton = null;

	private MainModel model = null;

	/**
	 * 
	 */
	public MainView() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.anchor = GridBagConstraints.NORTH;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(204, 25));
		this.add(getContentPanel(), gridBagConstraints1);
		this.setBorder(BasicBorders.getInternalFrameBorder());
		this.add(getMenuButton(), gridBagConstraints3);
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 1;
		gridBagConstraints4.anchor = GridBagConstraints.NORTH;
		this.add(getSpeedlinkButton(), gridBagConstraints4);
	}

	private Icon loadImage(String aString) {
		URL tempURL = getClass().getResource("/" + aString);
		return new ImageIcon(new ImageIcon(tempURL).getImage().getScaledInstance(10, 10, 0));
	}

	/**
	 * This method initializes contentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.ipadx = -1;
			gridBagConstraints.insets = new java.awt.Insets(-4, 0, 0, 0);
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints.gridx = 0;
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel.add(getTaskNameBox(), gridBagConstraints);
		}
		return contentPanel;
	}

	/**
	 * @return Returns the controller.
	 */
	public MainController getController() {
		return controller;
	}

	/**
	 * @param aController
	 *            The controller to set.
	 */
	public void setController(MainController aController) {
		controller = aController;
	}

	/**
	 * This method initializes taskNameBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTaskNameBox() {
		if (taskNameBox == null) {
			taskNameBox = new JComboBox();
			Font tempFont = taskNameBox.getFont();
			taskNameBox.setFont(new Font(tempFont.getFamily(), tempFont.getStyle(), tempFont.getSize() - 2));
			taskNameBox.setEditable(true);
			taskNameBox.setMaximumRowCount(30);
		}
		return taskNameBox;
	}

	/**
	 * 
	 */
	private void initTaskNameBoxListeners() {
		taskNameBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent aE) {
				if (aE.getID() == ItemEvent.SELECTED) {
					getController().otherTaskName(String.valueOf(aE.getItem()));
				}
			}
		});
		final JTextField tempEditor = (JTextField) taskNameBox.getEditor().getEditorComponent();
		Document tempDocument = tempEditor.getDocument();
		tempDocument.addDocumentListener(new DocumentListener() {
			private void changed() {
				getController().otherTaskName(tempEditor.getText());
			}

			public void changedUpdate(DocumentEvent aE) {
				changed();
			}

			public void insertUpdate(DocumentEvent aE) {
				changed();
			}

			public void removeUpdate(DocumentEvent aE) {
				changed();
			}
		});
		controller.getEnterpriseUtil().addFixedDocumentFilter(tempEditor);
	}

	/**
	 * This method initializes exitButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getMenuButton() {
		if (menuButton == null) {
			menuButton = new JButton();
			menuButton.setText(null);
			menuButton.setIcon(loadImage("Menuitems.gif"));
			menuButton.setToolTipText("Open Menu");
			menuButton.setMargin(new Insets(0, 0, 0, 0));
			menuButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent aE) {
					JPopupMenu tempJPopupMenu = createPopUpMenu();
					tempJPopupMenu.show(getMenuButton(), 8, 8);
				}
			});
		}
		return menuButton;
	}

	/**
	 * This method initializes exitButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSpeedlinkButton() {
		if (speedlinkButton == null) {
			speedlinkButton = new JButton();
			speedlinkButton.setText(null);
			speedlinkButton.setIcon(loadImage("speedlink.gif"));
			speedlinkButton.setToolTipText("Open speed task selection");
			speedlinkButton.setMargin(new Insets(0, 0, 0, 0));
			speedlinkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent aE) {
					JPopupMenu tempJPopupMenu = createSpeedlinkMenu();
					tempJPopupMenu.show(getSpeedlinkButton(), 8, 8);
				}

			});
		}
		return speedlinkButton;
	}

	/**
	 * @return
	 */
	private JPopupMenu createPopUpMenu() {
		JPopupMenu tempMenu = new JPopupMenu();
		String tempInfo;
		if (model.isFrameDecorated()) {
			tempInfo = "Hide frame title";
		} else {
			tempInfo = "Show frame title (to move/resize)";
		}
		final MainController tempController = getController();
		tempMenu.add(new AbstractAction(tempInfo) {
			public void actionPerformed(ActionEvent aE) {
				tempController.toggleFrameDecorator();
			}
		});
		tempMenu.addSeparator();
		tempMenu.add(new AbstractAction("Show this week") {
			public void actionPerformed(ActionEvent aE) {
				tempController.showThisWeek();
			}
		});
		tempMenu.add(new AbstractAction("Show last week") {
			public void actionPerformed(ActionEvent aE) {
				tempController.showLastWeek();
			}
		});
		tempMenu.add(new AbstractAction("Custom report...") {
			public void actionPerformed(ActionEvent aE) {
				tempController.showCustomReport();
			}
		});
		tempMenu.addSeparator();
		tempMenu.add(new AbstractAction("Edit Tasks...") {
			public void actionPerformed(ActionEvent aE) {
				tempController.editTasks();
			}
		});
		tempMenu.addSeparator();
		tempMenu.add(new AbstractAction("Options...") {
			public void actionPerformed(ActionEvent aE) {
				tempController.options();
			}
		});
		EnterpriseUtilRemote tempEnterpriseUtil = tempController.getEnterpriseUtil();
		if (tempEnterpriseUtil != null) {
			final Action tempShowBookingSystemAction = tempEnterpriseUtil.createShowBookingSystemAction(tempController);
			if (tempShowBookingSystemAction != null) {
				tempMenu.addSeparator();
				tempMenu.add(tempShowBookingSystemAction);
			}
		}
		tempMenu.addSeparator();
		tempMenu.add(new AbstractAction("About...") {
			public void actionPerformed(ActionEvent aE) {
				tempController.about();
			}
		});
		tempMenu.addSeparator();
		tempMenu.add(new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent aE) {
				LOG.info("User exit");
				tempController.exitApplicationRequestedByUser();
			}
		});
		return tempMenu;
	}

	private JPopupMenu createSpeedlinkMenu() {
		List<String> tempTaskNames = new ArrayList<String>();
		for (int i = 0; i < getTaskNameBox().getModel().getSize(); i++) {
			String tempTaskName = (String) getTaskNameBox().getModel().getElementAt(i);
			tempTaskNames.add(tempTaskName);
		}
		SpeedLinkMenuCreator tempSpeedLinkMenuCreator = new SpeedLinkMenuCreator(controller.getFixedTaskNames(), model
				.getTaskDelimiter().getDelimiter(), new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				getTaskNameBox().setSelectedItem(aE.getActionCommand());
				// getController().otherTaskName(aE.getActionCommand());
			}
		});
		return tempSpeedLinkMenuCreator.createSpeedlinkMenu(tempTaskNames);
	}

	/**
	 * @return Returns the model.
	 */
	public MainModel getModel() {
		return model;
	}

	/**
	 * @param aModel
	 *            The model to set.
	 */
	public void setModel(MainModel aModel, List<String> aLastTasks) {
		model = aModel;
		DefaultComboBoxModel tempComboModel = new DefaultComboBoxModel();
		LOG.info("setModel:" + aLastTasks);
		for (Iterator<String> i = aLastTasks.iterator(); i.hasNext();) {
			String tempTask = i.next();
			tempComboModel.addElement(tempTask);
		}
		getTaskNameBox().setModel(tempComboModel);
		if (tempComboModel.getSize() > 0) {
			getTaskNameBox().setSelectedIndex(0);
			// Make initial task pending.
			getController().otherTaskName((String) getTaskNameBox().getSelectedItem());
		}
		initTaskNameBoxListeners();
	}

	public void newTaskPending() {
		Font tempFont = getTaskNameBox().getFont();
		getTaskNameBox().setFont(new Font(tempFont.getFontName(), Font.ITALIC, tempFont.getSize()));
		getTaskNameBox().setForeground(Color.BLUE);
	}

	public void newTaskAccepted(String aNewTaskName) {
		Font tempFont = getTaskNameBox().getFont();
		getTaskNameBox().setFont(new Font(tempFont.getFontName(), 0, tempFont.getSize()));
		getTaskNameBox().setForeground(Color.BLACK);
		DefaultComboBoxModel tempModel = (DefaultComboBoxModel) getTaskNameBox().getModel();
		tempModel.removeElement(aNewTaskName);
		tempModel.insertElementAt(aNewTaskName, 0);
		getTaskNameBox().setSelectedItem(aNewTaskName);

		// TODO Highlight Read Only area
		// int tempFixedPos = getReadOnlyPos(aNewTaskName);
		// if (tempFixedPos > 0) {
		// JTextField tempEditor = (JTextField) taskNameBox.getEditor()
		// .getEditorComponent();
		// tempEditor.setCaretPosition(aNewTaskName.length());
		// tempEditor.moveCaretPosition(tempFixedPos);
		// }
	}

} // @jve:decl-index=0:visual-constraint="10,10"
