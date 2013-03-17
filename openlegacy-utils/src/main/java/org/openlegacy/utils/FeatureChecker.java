package org.openlegacy.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeatureChecker {

	private final static Log logger = LogFactory.getLog(FeatureChecker.class);

	public static boolean isSupportBidi() {
		try {
			Class.forName("com.ibm.icu.text.Bidi");
			logger.debug("Found com.ibm.icu library in the classpath. activating bidi support");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
