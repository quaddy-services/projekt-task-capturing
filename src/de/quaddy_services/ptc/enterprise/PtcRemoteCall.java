/**
 * 
 */
package de.quaddy_services.ptc.enterprise;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author user
 * 
 */
public class PtcRemoteCall {
	private String socketText;
	private Socket socket;
	static final String COMMAND_GET_BOOKABLE_TASK_NAMES = "COMMAND_GET_BOOKABLE_TASK_NAMES";
	static final String COMMAND_SAVE_REPORT = "COMMAND_SAVE_REPORT";

	/**
	 * 
	 */
	public PtcRemoteCall(String aSocketText) {
		socketText = aSocketText;
	}

	public void close() throws IOException {
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getBookableTaskNames(String aUserName) {
		Object tempExecuteResult = execute(COMMAND_GET_BOOKABLE_TASK_NAMES
				+ "\t" + aUserName);
		if (tempExecuteResult instanceof RuntimeException) {
			throw (RuntimeException) tempExecuteResult;
		}
		return (List<String>) tempExecuteResult;
	}

	private Object execute(String aCommand) {
		try {
			OutputStream tempOutputStream = getOutputStream();
			ObjectOutputStream tempObjectOutputStream = new ObjectOutputStream(
					tempOutputStream);
			tempObjectOutputStream.writeObject(aCommand);
			tempObjectOutputStream.flush();
			ObjectInputStream tempOI = new ObjectInputStream(getInputStream());
			Object tempBytes = tempOI.readObject();
			tempOI.close();
			return tempBytes;
		} catch (Exception e) {
			throw new RuntimeException("Communication failure", e);
		}
	}

	private InputStream getInputStream() throws IOException {
		try {
			Socket tempSocket = getSocket();
			tempSocket.setSoTimeout(5 * 60000); // 5 minutes
			return tempSocket.getInputStream();
		} catch (IOException e) {
			socket = null;
			Socket tempSocket = getSocket();
			tempSocket.setSoTimeout(5000);
			return tempSocket.getInputStream();
		}
	}

	private OutputStream getOutputStream() throws IOException {
		try {
			return getSocket().getOutputStream();
		} catch (IOException e) {
			socket = null;
			return getSocket().getOutputStream();
		}
	}

	private Socket getSocket() throws IOException {
		if (socket == null) {
			StringTokenizer tempTokens = new StringTokenizer(socketText, ":");
			socket = new Socket(tempTokens.nextToken(), new Integer(tempTokens
					.nextToken()).intValue());
		}
		return socket;
	}

	public String saveReport(String aUserName, String aReport) {
		Object tempExecuteResult = execute(COMMAND_SAVE_REPORT + "\t"
				+ aUserName + "\t" + aReport);
		if (tempExecuteResult instanceof RuntimeException) {
			throw (RuntimeException) tempExecuteResult;
		}
		return (String)tempExecuteResult;
	}
}
