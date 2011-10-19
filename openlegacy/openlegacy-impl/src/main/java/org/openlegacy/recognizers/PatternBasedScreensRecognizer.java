package org.openlegacy.recognizers;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.springframework.beans.factory.annotation.Autowired;

/***
 * Pattern based screen recognizer. Looks for fields in the given screen in the
 * given positions. If one of the fields content matches a screen name in the
 * Screens Registry, then a matching screen class is returned The found content
 * is
 * 
 * @author RoiM
 * 
 */
public class PatternBasedScreensRecognizer implements ScreensRecognizer {

	@Autowired
	private HostEntitiesRegistry screensRegistry;

	private List<ScreenPosition> positions;

	private final static Log logger = LogFactory
			.getLog(PatternBasedScreensRecognizer.class);

	private char[] ignoreChars = new char[] { ' ' };

	public Class<?> match(TerminalScreen terminalScreen) {
		if (positions == null){
			return null;
		}
		for (ScreenPosition position : positions) {
			TerminalField field = terminalScreen.getField(position);
			String patternFromScreen = handleIgnoreChars(field.getValue());
			if (patternFromScreen.length() > 0) {
				Class<?> screenModel = screensRegistry.get(patternFromScreen);
				if (screenModel != null) {
					logger.debug(MessageFormat
							.format("Found matched screen. Found pattern '{0}' in position {1}:",
									patternFromScreen, position));
					return screenModel;
				}
			}
		}
		logger.debug("Didn't found any matched screen");
		return null;
	}

	private String handleIgnoreChars(String value) {
		char[] chars = ignoreChars;
		for (char c : chars) {
			value = value.replaceAll("\\" + Character.toString(c), "");
		}
		return value;
	}

	public void setPositions(List<ScreenPosition> positions) {
		this.positions = positions;
	}

	public void setIgnoreChars(char[] ignoreChars) {
		this.ignoreChars = ignoreChars;
	}

}
