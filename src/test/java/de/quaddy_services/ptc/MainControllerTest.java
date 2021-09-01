package de.quaddy_services.ptc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class MainControllerTest {

	@Test
	void test80SecondsFormat() {
		Locale.setDefault(Locale.ENGLISH);
		String tempActual = new MainController().formatMillisHumanReadable(80000);
		assertEquals("00:01:20", tempActual);
	}

	@Test
	void testOneHourOneSecondFormat() {
		Locale.setDefault(Locale.ENGLISH);
		String tempActual = new MainController().formatMillisHumanReadable(3600 * 1000 + 1000);
		assertEquals("01:00:01", tempActual);
	}

}
