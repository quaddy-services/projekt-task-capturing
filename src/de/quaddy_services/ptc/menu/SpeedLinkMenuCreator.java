package de.quaddy_services.ptc.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.quaddy_services.ptc.FixedTasksTokenizer;

public class SpeedLinkMenuCreator {

	private String delimiter;
	private ActionListener actionListener;
	private List<String> fixedTaskNames;

	public SpeedLinkMenuCreator(List<String> aFixedTaskNames,
			String aDelimiter, ActionListener aActionListener) {
		fixedTaskNames = aFixedTaskNames;
		delimiter = aDelimiter;
		actionListener = aActionListener;
	}

	interface StringMap extends Map<String, StringMap> {
	}

	class StringHashMap extends HashMap<String, StringMap> implements StringMap {
	}

	public JPopupMenu createSpeedlinkMenu(List<String> aTaskNames) {
		StringMap tempTaskMap = new StringHashMap();
		for (String tempTaskName : aTaskNames) {
			addToTaskMap(tempTaskMap, tempTaskName);
		}
		JPopupMenu tempPopUp = new JPopupMenu();
		List<JMenuItem> tempMenuItems = createSpeedLinkMenu(tempTaskMap);
		for (JMenuItem tempMenuItem : tempMenuItems) {
			tempPopUp.add(tempMenuItem);
		}
		return tempPopUp;
	}

	private void addToTaskMap(StringMap aTaskMap, String aTaskName) {
		StringMap tempTaskMap = aTaskMap;
		FixedTasksTokenizer tempTokens = new FixedTasksTokenizer(
				fixedTaskNames, aTaskName, delimiter);
		String tempTaskPart = "";
		while (tempTokens.hasMoreTokens()) {
			String tempTaskToken = tempTokens.nextToken();
			if (tempTaskPart.length() > 0) {
				tempTaskPart += delimiter;
			}
			tempTaskPart += tempTaskToken;
			StringMap tempSubMap = tempTaskMap.get(tempTaskPart);
			if (tempSubMap == null) {
				tempSubMap = new StringHashMap();
				tempTaskMap.put(tempTaskPart, tempSubMap);
			}
			tempTaskMap = tempSubMap;
		}
	}

	private List<JMenuItem> createSpeedLinkMenu(StringMap tempTaskMap) {
		List<JMenuItem> tempMenuItems = new ArrayList<JMenuItem>();
		List<String> tempKeys = new ArrayList<String>(tempTaskMap.keySet());
		Collections.sort(tempKeys);
		for (final String tempTaskName : tempKeys) {
			StringMap tempSubMap = tempTaskMap.get(tempTaskName);
			if (tempSubMap.size() == 0) {
				tempMenuItems.add(new JMenuItem(createAction(tempTaskName)));
			} else {
				JMenu tempMenu = new JMenu(tempTaskName) {
					@Override
					public Icon getIcon() {
						// TODO Auto-generated method stub
						return super.getIcon();
					}
				};
				tempMenu.add(new JMenuItem(createAction(tempTaskName)));
				List<JMenuItem> tempSubMenuItems = createSpeedLinkMenu(tempSubMap);
				for (JMenuItem tempMenuItem : tempSubMenuItems) {
					tempMenu.add(tempMenuItem);
				}
				tempMenuItems.add(tempMenu);
			}
		}
		return tempMenuItems;
	}

	private AbstractAction createAction(final String tempTaskName) {
		return new AbstractAction(tempTaskName) {
			public void actionPerformed(ActionEvent aE) {
				actionListener.actionPerformed(new ActionEvent(aE.getSource(),
						0, tempTaskName));
			}
		};
	}

}
