package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXIScreen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreensRecognizer;

import javax.inject.Inject;

public class ApxScreensRecognizer implements ScreensRecognizer {

	private static final Object UNKNOWN = "UNKNOWN";

	@Inject
	private ScreenEntitiesRegistry screensEntitiesRegistry;

	private final static Log logger = LogFactory.getLog(ApxScreensRecognizer.class);

	public Class<?> match(TerminalSnapshot terminalSnapshot) {
		GXIScreen apxScreen = (GXIScreen)terminalSnapshot.getDelegate();
		if (apxScreen.getName().equals(UNKNOWN)) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Screen matched by ApplinX:" + apxScreen.getName());
		}
		return screensEntitiesRegistry.getEntityClass(apxScreen.getName());
	}

}
