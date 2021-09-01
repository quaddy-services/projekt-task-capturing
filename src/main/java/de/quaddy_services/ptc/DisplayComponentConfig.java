package de.quaddy_services.ptc;

/**
 * 
 */
public class DisplayComponentConfig {
	private String title;
	private String okText = "OK";
	private String cancelText = "Cancel";

	/**
	 * @see #title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @see #title
	 */
	public void setTitle(String aTitle) {
		title = aTitle;
	}

	/**
	 * @see #okText
	 */
	public String getOkText() {
		return okText;
	}

	/**
	 * @see #okText
	 */
	public void setOkText(String aOkText) {
		okText = aOkText;
	}

	/**
	 * @see #cancelText
	 */
	public String getCancelText() {
		return cancelText;
	}

	/**
	 * @see #cancelText
	 */
	public void setCancelText(String aCancelText) {
		cancelText = aCancelText;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder tempBuilder = new StringBuilder();
		tempBuilder.append("DisplayComponentConfig [");
		if (title != null) {
			tempBuilder.append("title=");
			tempBuilder.append(title);
			tempBuilder.append(", ");
		}
		if (okText != null) {
			tempBuilder.append("okText=");
			tempBuilder.append(okText);
			tempBuilder.append(", ");
		}
		if (cancelText != null) {
			tempBuilder.append("cancelText=");
			tempBuilder.append(cancelText);
		}
		tempBuilder.append("]");
		return tempBuilder.toString();
	}
}
