package de.quaddy_services.ptc.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;

public class BackupFile {
	private static final Logger LOG = LoggerFactory.getLogger(BackupFile.class);

	public void backupFile(File aFileName, int aMaxBackups) throws IOException {
		if (aFileName.exists()) {
			createBackupFile(aFileName, aMaxBackups).delete();
			for (int i = aMaxBackups; i >= 0; i--) {
				createBackupFile(aFileName, i - 1).renameTo(createBackupFile(aFileName, i));
			}
			File tempBackup = createBackupFile(aFileName, 0);
			copyFile(aFileName, tempBackup);
			LOG.info("Backuped to " + tempBackup.getAbsolutePath());
		}
	}

	private void copyFile(File aFrom, File aTo) throws FileNotFoundException, IOException {
		FileInputStream tempReader = new FileInputStream(aFrom);
		FileOutputStream tempWriter = new FileOutputStream(aTo);
		int tempRead;
		byte[] tempBuff = new byte[1024 * 20];
		while (0 <= (tempRead = tempReader.read(tempBuff))) {
			tempWriter.write(tempBuff, 0, tempRead);
		}
		tempReader.close();
		tempWriter.close();
	}

	private File createBackupFile(File aFile, int i) {
		return new File(aFile.getAbsoluteFile() + "." + i + ".bak");
	}

}
