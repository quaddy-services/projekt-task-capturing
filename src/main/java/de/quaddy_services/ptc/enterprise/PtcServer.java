/**
 *
 */
package de.quaddy_services.ptc.enterprise;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.quaddy_services.ptc.enterprise.custom.PtcFunction;
import de.quaddy_services.ptc.enterprise.custom.PtcTask;
import de.quaddy_services.ptc.enterprise.report.EnterpriseReportParser;
import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;

/**
 * Run a server component on Port 334.
 *
 * Specify your own Booking-Adapter via
 * PtcFactory.properties
 * PtcFunction=<ClassNameImplementingPtcFunctino>
 * @see PtcFunction
 */
public class PtcServer {
	private static final Logger LOG = LoggerFactory.getLogger(PtcServer.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		int tempPort = 334;
		if (args != null && args.length > 0) {
			tempPort = new Integer(args[0]);
		}
		try {
			PtcServer tempPtcServer = new PtcServer();
			tempPtcServer.run(tempPort);
		} catch (BindException e) {
			LOG.exception(e);
			System.exit(2);
		} catch (Exception e) {
			LOG.exception(e);
			System.exit(1);
		}
		System.exit(0);
	}

	public PtcServer() {
		executorService = Executors.newFixedThreadPool(5);
	}

	private ExecutorService executorService;

	private void run(int aPort) throws IOException {
		try (ServerSocket tempServerSocket = new ServerSocket(aPort)) {
			LOG.info("Running on " + tempServerSocket);
			LOG.info("PtcFunction=" + getPtcFunction());
			while (true) {
				final Socket tempClient = tempServerSocket.accept();
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
							runForClient(tempClient);
						} catch (Exception e) {
							LOG.exception(e);
						}
					}
				});
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					LOG.exception(e);
					// Restore interrupted state...
					Thread.currentThread().interrupt();
				}
				if (Thread.interrupted()) {
					break;
				}
			}
		}
	}

	private void runForClient(Socket aClient) throws Exception {
		InputStream tempIn = aClient.getInputStream();
		OutputStream tempOut = aClient.getOutputStream();
		ObjectInputStream tempObjectInputStream = new ObjectInputStream(tempIn);
		String tempObject = (String) tempObjectInputStream.readObject();
		LOG.info(tempObject);
		StringTokenizer tempTokens = new StringTokenizer(tempObject, "\t");
		String tempCommand = tempTokens.nextToken();
		ObjectOutputStream tempOO = new ObjectOutputStream(tempOut);
		if (tempCommand.equals(PtcRemoteCall.COMMAND_GET_BOOKABLE_TASK_NAMES)) {
			try {
				Object tempBookableTasks = getBookableTasks(getSession(aClient), tempTokens.nextToken());
				tempOO.writeObject(tempBookableTasks);
			} catch (RuntimeException e) {
				LOG.exception(e);
				tempOO.writeObject(e);
			}
		} else if (tempCommand.equals(PtcRemoteCall.COMMAND_SAVE_REPORT)) {
			try {
				String tempUserName = tempTokens.nextToken();
				String tempReport = tempTokens.nextToken();
				String tempInfo = saveTasks(getSession(aClient), tempUserName, tempReport);
				LOG.info("Result for " + tempUserName + "=" + tempInfo);
				tempOO.writeObject(tempInfo);
			} catch (RuntimeException e) {
				LOG.exception(e);
				tempOO.writeObject(e);
			}
		} else {
			RuntimeException e = new RuntimeException("Unknownn function '" + tempCommand + "'");
			LOG.exception(e);
			tempOO.writeObject(e);
		}
		tempOO.flush();
		tempOut.close();
		tempIn.close();
	}

	private String saveTasks(SocketAddress aSession, String aUserId, String aReport) {
		LOG.info("saveTasks for " + aUserId);
		PtcFunction tempPtcFunction = getPtcFunction();
		List<PtcTask> tempTasks = parseReport(aReport);
		return tempPtcFunction.saveReport(aUserId, tempTasks);
	}

	private List<PtcTask> parseReport(String aReport) {
		return new EnterpriseReportParser().parseReport(aReport);
	}

	private SocketAddress getSession(Socket aClient) {
		return aClient.getRemoteSocketAddress();
	}

	private Object getBookableTasks(SocketAddress aSession, String aUserId) {
		LOG.info("getBookableTasks for " + aUserId);
		PtcFunction tempPtcFunction = getPtcFunction();
		List<String> tempBookableTaskNames = tempPtcFunction.getBookableTaskNames(aUserId);
		return tempBookableTaskNames;
	}

	private PtcFunction ptcFunction;

	private synchronized PtcFunction getPtcFunction() {
		if (ptcFunction == null) {
			ptcFunction = PtcFunctionLoader.loadPtcFunction();
		}
		return ptcFunction;
	}

}
