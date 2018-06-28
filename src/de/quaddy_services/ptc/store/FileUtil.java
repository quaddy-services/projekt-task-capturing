package de.quaddy_services.ptc.store;

import java.io.File;

public class FileUtil {
	private static String dataFolder = null;

	public static String getDataFolder() {
		if (dataFolder == null) {
			String tempFileName = getDefaultDataFolder();
			dataFolder = tempFileName;
		}
		return dataFolder;
	}

	/**
	 *
	 */
	public static String getDefaultDataFolder() {
		String tempFileName = System.getProperty("user.home");
		tempFileName = tempFileName.replace('\\', '/');
		if (!tempFileName.endsWith("/")) {
			tempFileName += "/";
		}
		tempFileName += "ptc";
		new File(tempFileName).mkdir();
		return tempFileName;
	}

	public static void setDataFolder(String aDataFolder) {
		dataFolder = aDataFolder;
	}

}
