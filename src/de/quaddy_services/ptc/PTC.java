/**
 * 
 */
package de.quaddy_services.ptc;

import java.awt.EventQueue;

import javax.swing.UIManager;

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
			public void run() {
				try {
					mainEventQueue();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}


	private static void mainEventQueue() throws Exception {
		MainController tempMainController;

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		tempMainController = new MainController();

		tempMainController.init();

	}

}
