package de.quaddy_services.ptc.store;

public class PosAndContent<E> {
	private long posInFile;
	private E line;

	/**
	 * @return the posInFile
	 */
	public long getPosInFile() {
		return posInFile;
	}

	/**
	 * @param aPosInFile
	 *            the posInFile to set
	 */
	public void setPosInFile(long aPosInFile) {
		posInFile = aPosInFile;
	}

	/**
	 * @return the line
	 */
	public E getLine() {
		return line;
	}

	/**
	 * @param aLine
	 *            the line to set
	 */
	public void setLine(E aLine) {
		line = aLine;
	}

	@Override
	public String toString() {
		return posInFile + ":" + line + " " + super.toString();
	}
}
