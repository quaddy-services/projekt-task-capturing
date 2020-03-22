/**
 * 
 */
package de.quaddy_services.ptc;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Stefan Cordes
 *
 */
public class LoggingWindowAdapter implements WindowListener {

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent aE) {
		System.out.println("windowOpened("+aE+")");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent aE) {
		System.out.println("windowClosing("+aE+")");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent aE) {
				System.out.println("windowClosed("+aE+")");

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent aE) {
				System.out.println("windowIconified("+aE+")");

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent aE) {
				System.out.println("windowDeiconified("+aE+")");

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent aE) {
				System.out.println("windowActivated("+aE+")");

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent aE) {
				System.out.println("windowDeactivated("+aE+")");

	}

}
