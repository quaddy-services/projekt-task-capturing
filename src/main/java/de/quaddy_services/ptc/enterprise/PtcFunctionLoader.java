package de.quaddy_services.ptc.enterprise;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import de.quaddy_services.ptc.enterprise.custom.PtcFunction;

class PtcFunctionLoader {
	private static final String PTC_FACTORY_PROPERTIES = "PtcFactory.properties";

	static PtcFunction loadPtcFunction() {
		ClassLoader tempClassLoader = PtcFunctionLoader.class.getClassLoader();
		URL tempURL = tempClassLoader.getResource(PTC_FACTORY_PROPERTIES);
		if (tempURL == null) {
			throw new RuntimeException(PTC_FACTORY_PROPERTIES
					+ " not found in " + tempClassLoader);
		}
		System.out.println("Found " + tempURL.toExternalForm());
		InputStream tempIn;
		try {
			tempIn = tempURL.openStream();
		} catch (IOException e1) {
			throw new RuntimeException("Cannot open "
					+ tempURL.toExternalForm());
		}
		Properties tempProperties = new Properties();
		try {
			tempProperties.load(tempIn);
			tempIn.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot load "
					+ tempURL.toExternalForm(), e);
		}
		String tempFunctionName = tempProperties.getProperty("PtcFunction");
		if (tempFunctionName == null) {
			throw new RuntimeException("Key PtcFunction not found in "
					+ PTC_FACTORY_PROPERTIES + " " + tempURL.toExternalForm());
		}
		System.out.println("Load " + tempFunctionName);
		try {
			return (PtcFunction) Class.forName(tempFunctionName).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create '" + tempFunctionName
					+ "' from " + PTC_FACTORY_PROPERTIES + " "
					+ tempURL.toExternalForm());
		}
	}
}
