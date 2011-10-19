package org.openlegacy.recognizers;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreensRecognizer;

import java.util.List;

/**
 * A composite pattern screen recognizer which accept multiple recognizers and activates them one after the other
 * 
 */
public class CompositeScreenRecognizer implements ScreensRecognizer {

	private List<ScreensRecognizer> screensRecognizers;

	public Class<?> match(TerminalScreen terminalScreen) {
		for (ScreensRecognizer screensRecognizer : screensRecognizers) {
			Class<?> matchedScreen = screensRecognizer.match(terminalScreen);
			if (matchedScreen != null) {
				return matchedScreen;
			}
		}
		return null;
	}

	public void setScreensRecognizers(List<ScreensRecognizer> screensRecognizers) {
		this.screensRecognizers = screensRecognizers;
	}

}
