/**
 *
 */
package de.quaddy_services.ptc;

import java.awt.EventQueue;

import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;

/**
 * @author Stefan Cordes
 *
 */
public class PTC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					mainEventQueue();
				} catch (Exception e) {
					Logger LOG = LoggerFactory.getLogger(PTC.class);
					LOG.error("Error", e);
					throw new RuntimeException(e);
				}
			}
		});
	}

	private static void mainEventQueue() throws Exception {
		MainController tempMainController;

		// Avoid font scaling problems in windows 10 #8
		//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		tempMainController = new MainController();

		tempMainController.init();

	}

}
