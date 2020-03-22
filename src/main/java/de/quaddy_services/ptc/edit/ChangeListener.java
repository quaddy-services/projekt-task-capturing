package de.quaddy_services.ptc.edit;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public abstract class ChangeListener implements DocumentListener {

	public abstract void textChanged(String aNewString);

	@Override
	public void changedUpdate(DocumentEvent aE) {
		textChanged(aE.getDocument());
	}

	private void textChanged(Document aDocument) {
		try {
			textChanged(aDocument.getText(0, aDocument.getLength()));
		} catch (BadLocationException e) {
			e.printStackTrace();
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
