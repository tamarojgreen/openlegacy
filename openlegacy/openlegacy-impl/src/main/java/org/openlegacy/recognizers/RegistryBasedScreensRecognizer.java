package org.openlegacy.recognizers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.support.DefaultScreenEntitiesRegistry;

import javax.inject.Inject;

/**
 * Registry based recognizer. Based on @ScreenEntity (identifier=...) definitions
 * 
 */
public class RegistryBasedScreensRecognizer implements ScreensRecognizer {

	@Inject
	private DefaultScreenEntitiesRegistry screensRegistry;

	private final static Log logger = LogFactory.getLog(RegistryBasedScreensRecognizer.class);

	public Class<?> match(TerminalSnapshot terminalSnapshot) {
		ScreenEntityDefinition screenEntityDefinition = screensRegistry.match(terminalSnapshot);
		if (screenEntityDefinition != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Screen matched by registry:" + screenEntityDefinition.getEntityClass());
			}
			return screenEntityDefinition.getEntityClass();
		}
		return null;
	}

}
