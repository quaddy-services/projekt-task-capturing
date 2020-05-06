package de.quaddy_services.ptc.edit;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.quaddy_services.ptc.logging.Logger;
import de.quaddy_services.ptc.logging.LoggerFactory;

public abstract class ChangeListener implements DocumentListener {
	private static final Logger LOG = LoggerFactory.getLogger(ChangeListener.class);

	public abstract void textChanged(String aNewString);

	@Override
	public void changedUpdate(DocumentEvent aE) {
		textChanged(aE.getDocument());
	}

	private void textChanged(Document aDocument) {
		try {
			textChanged(aDocument.getText(0, aDocument.getLength()));
		} catch (BadLocationException e) {
			LOG.error("Error on " + aDocument, e);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent aE) {
		textChanged(aE.getDocument());
	}

	@Override
	public void removeUpdate(DocumentEvent aE) {
		textChanged(aE.getDocument());
	}

}
