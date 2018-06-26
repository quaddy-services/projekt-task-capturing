package de.quaddy_services.ptc.store;

import java.io.File;

public class FileUtil {
	private static String dataFolder = null;

	public static String getDataFolder() {
		if (dataFolder == null) {
			String tempFileName = System.getProperty("user.home");
			tempFileName = tempFileName.replace('\\', '/');
			if (!tempFileName.endsWith("/")) {
				tempFileName += "/";
			}
			tempFileName += "ptc";
			new File(tempFileName).mkdir();
			dataFolder = tempFileName;
		}
		return dataFolder;
	}

	public static void setDataFolder(String aDataFolder) {
		dataFolder = aDataFolder;
	}

}
