package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalScreen;

/**
 * Screen recognizer is returns a matching screenEntity class for the given terminal screen 
 *
 */
public interface ScreensRecognizer {

	Class<?> match(TerminalScreen terminalScreen);
}
