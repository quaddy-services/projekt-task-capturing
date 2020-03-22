package de.quaddy_services.ptc.logging;

import java.io.File;

/**
 *
 */
public class LoggerFactory {

	private static LoggerFactory instance;
	private FileLogger fileLogger;

	/**
	 *
	 */
	public static Logger getLogger(Class<?> aClass) {
		return getInstance().getLoggerFor(aClass);
	}

	/**
	 *
	 */
	private Logger getLoggerFor(Class<?> aClass) {
		String aName = aClass.getName();

		return new Logger(aName.substring(aName.lastIndexOf(".") + 1), fileLogger);
	}

	/**
	 *
	 */
	private static synchronized LoggerFactory getInstance() {
		if (instance == null) {
			String tempDir = System.getProperty("java.io.tmpdir");
			File temp3 = new File(tempDir + "/ptc-3.log");
			temp3.delete();
			File temp2 = new File(tempDir + "/ptc-2.log");
			temp2.renameTo(temp3);
			File temp1 = new File(tempDir + "/ptc-1.log");
			temp1.renameTo(temp2);
			File temp0 = new File(tempDir + "/ptc.log");
			temp0.renameTo(temp1);

			instance = new LoggerFactory();
			instance.fileLogger = new FileLogger(temp0);
		}
		return instance;
	}

}
