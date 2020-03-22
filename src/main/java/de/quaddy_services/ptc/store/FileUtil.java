package de.quaddy_services.ptc.store;

import java.io.File;

import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;

public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

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
		LOGGER.info("New Datafolder: " + aDataFolder + " (replaces " + dataFolder + ")");
		dataFolder = aDataFolder;
		if (!new File(dataFolder).mkdirs()) {
			LOGGER.error("Datafolder could not be created: " + dataFolder);
		}

	}

}
