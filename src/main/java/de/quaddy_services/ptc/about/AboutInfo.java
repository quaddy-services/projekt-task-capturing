package de.quaddy_services.ptc.about;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import de.quaddy_services.ptc.store.TaskHistory;

public class AboutInfo extends JPanel {

	public AboutInfo() {
		JTextPane tempArea = new JTextPane();
		String tempVersion = getVersionInfo();
		String tempCVS = "https://github.com/quaddy-services/projekt-task-capturing";
		String tempHome = "http://PTC.Quaddy-Services.de/";
		String tempJavaRegValue = "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Runtime Environment";
		// https:// bugs.eclipse.org/bugs/show_bug.cgi?id=199684
		tempArea.setContentType("text/html");
		StringBuffer tempHtml = new StringBuffer();
		tempHtml.append("<html>");
		tempHtml.append("<body>");
		tempHtml.append("<h3>Project Task Capturing " + tempVersion + "</h3>");
		tempHtml.append("<h3>Freeware</h3>");
		tempHtml.append("Homepage: <a href=\"" + tempHome + "\">" + tempHome + "</a>");
		tempHtml.append("<h3>Open Source</h3>");
		tempHtml.append("<a href=\"" + tempCVS + "\">" + tempCVS + "</a>");
		tempHtml.append("<br>");
		tempHtml.append("");
		tempHtml.append("<p>");
		tempHtml.append("<b>Data Directory: </b>");
		tempHtml.append(new TaskHistory().getActualFile().getAbsolutePath());
		tempHtml.append("<br>(Edit tasks via that file or with menu -&gt; Edit Tasks)");
		tempHtml.append("<p>");
		tempHtml.append("<b>System: </b>");
		tempHtml.append("<br>");
		tempHtml.append("java.runtime.name=" + System.getProperty("java.runtime.name"));
		tempHtml.append("<br>");
		tempHtml.append("java.runtime.version=" + System.getProperty("java.runtime.version"));
		tempHtml.append("<br>");
		tempHtml.append("java.home=" + System.getProperty("java.home"));
		tempHtml.append("<br>");
		tempHtml.append("(comes from " + tempJavaRegValue + ")");

		tempHtml.append("<p><font size=2>Copyright (c) 2007-2008: Stefan Cordes</font></p>");
		tempHtml.append("</body>");
		tempHtml.append("</html>");
		tempArea.setText(tempHtml.toString());
		setLayout(new BorderLayout());
		add(tempArea, BorderLayout.CENTER);
	}

	private String getVersionInfo() {
		try {
			InputStream tempIn = getClass().getResourceAsStream("/version.txt");
			BufferedReader tempRead = new BufferedReader(new InputStreamReader(tempIn));
			String tempVersion;
			tempVersion = tempRead.readLine();
			tempRead.close();
			return tempVersion;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
