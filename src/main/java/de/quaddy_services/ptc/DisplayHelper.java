package de.quaddy_services.ptc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import de.quaddy_services.ptc.about.AboutInfo;

public class DisplayHelper {

	public void displayException(JFrame aFrame, Throwable aE) {
		if (aFrame == null) {
			JFrame tempFrame = new JFrame();
			tempFrame.setTitle(aE.getMessage());
			tempFrame.setVisible(true);
			displayExceptionPrivate(tempFrame, aE);
			tempFrame.dispose();
		} else {
			displayExceptionPrivate(aFrame, aE);
		}
	}

	private void displayExceptionPrivate(JFrame aFrame, Throwable aE) {
		StringWriter tempStringWriter = new StringWriter();
		PrintWriter tempPrintWriter = new PrintWriter(tempStringWriter);
		aE.printStackTrace(tempPrintWriter);
		tempPrintWriter.close();
		final JDialog tempDialog = new JDialog(aFrame, "Error: " + aE.getMessage());
		tempDialog.setModal(true);
		Dimension tempScreen = Toolkit.getDefaultToolkit().getScreenSize();
		tempDialog.setSize(tempScreen.width / 2, tempScreen.height / 2);
		tempDialog.setLocation(tempScreen.width / 4, tempScreen.height / 4);
		tempDialog.getContentPane().setLayout(new BorderLayout());
		ScrollPane tempScrollPane = new ScrollPane();
		JEditorPane tempEditorPane = new JEditorPane();
		// TODO Replace by Font.MONOSPACED with JDK 6
		tempEditorPane.setFont(new Font("Monospaced", 0, aFrame.getFont().getSize()));
		tempEditorPane.setText(tempStringWriter.toString());
		tempScrollPane.add(tempEditorPane);
		tempDialog.getContentPane().add(tempScrollPane, BorderLayout.CENTER);
		Action tempActionListener = new AbstractAction("OK") {
			@Override
			public void actionPerformed(ActionEvent aE) {
				tempDialog.dispose();
			}
		};
		tempDialog.getContentPane().add(createButtons(new Action[] { tempActionListener }), BorderLayout.SOUTH);
		tempDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tempDialog.setVisible(true);
	}

	public void displayText(JFrame aFrame, String aTitle, String aText, boolean aModalFlag, List<Action> anActions, boolean aScrollToBottom) {
		if (aFrame == null) {
			JFrame tempFrame = new JFrame();
			tempFrame.setTitle(aTitle);
			tempFrame.setVisible(true);
			displayTextPrivate(tempFrame, aTitle, aText, aModalFlag, anActions, aScrollToBottom);
			tempFrame.dispose();
		} else {
			displayTextPrivate(aFrame, aTitle, aText, aModalFlag, anActions, aScrollToBottom);
		}
	}

	public void displayText(JFrame aFrame, String aTitle, String aText, boolean aModalFlag) {
		displayText(aFrame, aTitle, aText, aModalFlag, null, false);
	}

	private void displayTextPrivate(JFrame aFrame, String aTitle, String aText, boolean aModalFlag, List<Action> anActions, boolean aScrollToBottom) {
		final JDialog tempDialog = new JDialog(aFrame, aTitle);
		tempDialog.setModal(aModalFlag);
		Dimension tempScreen = Toolkit.getDefaultToolkit().getScreenSize();
		tempDialog.setSize(tempScreen.width * 4 / 10, tempScreen.height * 4 / 5);
		tempDialog.setLocation(tempScreen.width / 4, tempScreen.height / 10);
		tempDialog.getContentPane().setLayout(new BorderLayout());
		JEditorPane tempEditorPane = new JEditorPane();
		JScrollPane tempScrollPane = new JScrollPane(tempEditorPane);
		tempEditorPane.setFont(new Font(Font.MONOSPACED, 0, aFrame.getFont().getSize()));
		tempEditorPane.setText(aText);
		tempDialog.getContentPane().add(tempScrollPane, BorderLayout.CENTER);
		if (aScrollToBottom) {
			tempEditorPane.setCaretPosition(tempEditorPane.getDocument().getLength() - 1);
		} else {
			tempEditorPane.setCaretPosition(0);
		}
		Action tempActionListener = new AbstractAction("OK") {
			@Override
			public void actionPerformed(ActionEvent aE) {
				tempDialog.dispose();
			}
		};
		List<Action> tempActions = new ArrayList<Action>();
		tempActions.add(tempActionListener);
		if (anActions != null) {
			tempActions.addAll(anActions);
		}
		tempDialog.getContentPane().add(createButtons(tempActions.toArray(new Action[tempActions.size()])), BorderLayout.SOUTH);
		tempDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tempDialog.setAlwaysOnTop(false);
		tempDialog.setVisible(true);
	}

	private Component createButtons(Action[] aActions) {
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new GridBagLayout());
		GridBagConstraints tempGBC = new GridBagConstraints();
		tempGBC.weightx = 1.0;
		tempGBC.fill = GridBagConstraints.HORIZONTAL;
		tempGBC.gridx = 0;
		tempPanel.add(new JLabel(" "), tempGBC);
		for (int i = 0; i < aActions.length; i++) {
			Action tempAction = aActions[i];
			if (tempAction != null) {
				tempGBC.gridx++;
				JButton tempButton = new JButton(tempAction);
				tempPanel.add(tempButton, tempGBC);
			}
		}
		tempGBC.gridx++;
		tempPanel.add(new JLabel(" "), tempGBC);

		return tempPanel;
	}

	public void displayAbout(JFrame aFrame) {
		final JDialog tempDialog = new JDialog(aFrame);
		tempDialog.setModal(true);
		Dimension tempScreen = Toolkit.getDefaultToolkit().getScreenSize();
		int tempHeight = tempScreen.height * 3 / 4;
		tempDialog.setSize(tempScreen.width / 2, tempHeight);
		tempDialog.setLocation(tempScreen.width / 4, (tempScreen.height - tempHeight) / 2);
		tempDialog.getContentPane().setLayout(new BorderLayout());
		ScrollPane tempScrollPane = new ScrollPane();
		tempScrollPane.add(new AboutInfo());
		tempDialog.getContentPane().add(tempScrollPane, BorderLayout.CENTER);
		Action tempActionListener = new AbstractAction("OK") {
			@Override
			public void actionPerformed(ActionEvent aE) {
				tempDialog.dispose();
			}
		};
		tempDialog.getContentPane().add(createButtons(new Action[] { tempActionListener }), BorderLayout.SOUTH);
		tempDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tempDialog.setVisible(true);
	}

	public boolean displayComponent(JFrame aFrame, String aTitle, final Component aComponent) {
		DisplayComponentConfig tempDisplayComponentConfig = new DisplayComponentConfig();
		tempDisplayComponentConfig.setTitle(aTitle);
		return displayComponent(aFrame, tempDisplayComponentConfig, aComponent);
	}

	public boolean displayComponent(JFrame aFrame, DisplayComponentConfig aDisplayComponentConfig, final Component aComponent) {
		final JDialog tempDialog = new JDialog(aFrame, aDisplayComponentConfig.getTitle());
		final List<Boolean> tempResult = new LinkedList<Boolean>();
		Dimension tempScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension tempPref = aComponent.getPreferredSize();
		tempDialog.setSize(Math.max(500, Math.min(tempPref.width + 150, tempScreen.width * 8 / 10)),
				Math.max(200, Math.min(tempPref.height + 100, tempScreen.height * 17 / 20)));
		tempDialog.setLocation((tempScreen.width - tempDialog.getWidth()) / 2, (tempScreen.height - tempDialog.getHeight()) / 2);
		tempDialog.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints tempGBC = new GridBagConstraints();
		tempGBC.fill = GridBagConstraints.BOTH;
		tempGBC.weightx = 1.0;
		tempGBC.weighty = 1.0;
		JScrollPane tempScroll = new JScrollPane(aComponent);
		tempScroll.setSize(tempDialog.getSize());
		tempDialog.getContentPane().add(tempScroll, tempGBC);
		tempGBC = new GridBagConstraints();
		tempGBC.gridy++;
		tempDialog.getContentPane().add(new JLabel(" "), tempGBC);
		Action tempActionListener = new AbstractAction(aDisplayComponentConfig.getOkText()) {
			@Override
			public void actionPerformed(ActionEvent aE) {
				tempResult.add(Boolean.TRUE);
				tempDialog.dispose();
			}
		};
		Action tempCancelActionListener = new AbstractAction(aDisplayComponentConfig.getCancelText()) {
			@Override
			public void actionPerformed(ActionEvent aE) {
				tempDialog.dispose();
			}
		};
		tempGBC.gridy++;
		tempDialog.getContentPane().add(createButtons(new Action[] { tempActionListener, tempCancelActionListener }), tempGBC);
		tempDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tempDialog.setAlwaysOnTop(false);
		tempDialog.setModal(true);
		tempDialog.setVisible(true);
		return tempResult.size() > 0;
	}

}
