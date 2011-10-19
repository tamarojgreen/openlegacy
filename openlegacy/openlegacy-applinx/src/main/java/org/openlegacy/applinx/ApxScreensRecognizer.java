package org.openlegacy.applinx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.springframework.beans.factory.annotation.Autowired;

import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;

public class ApxScreensRecognizer implements ScreensRecognizer {

	private static final Object UNKNOWN = "UNKNOWN";

	@Autowired
	private HostEntitiesRegistry screensRegistry;

	private final static Log logger = LogFactory
			.getLog(ApxScreensRecognizer.class);

	public Class<?> match(TerminalScreen terminalScreen) {
		GXRuntimeScreen apxScreen = (GXRuntimeScreen) terminalScreen.getDelegate();
		if (apxScreen.getName().equals(UNKNOWN)) {
			return null;
		}
		if (logger.isDebugEnabled()){
			logger.debug("Screen matched by ApplinX:" + apxScreen.getName());
		}
		return screensRegistry.get(apxScreen.getName());
	}

}
