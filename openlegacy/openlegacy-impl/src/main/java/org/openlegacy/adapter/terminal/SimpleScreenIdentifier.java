package org.openlegacy.adapter.terminal;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreenIdentifier;

/**
 * A simple implementation for a screen identifier
 * 
 */
public class SimpleScreenIdentifier implements ScreenIdentifier {

	private ScreenPosition position;
	private String text;

	public SimpleScreenIdentifier(ScreenPosition position, String text) {
		this.position = position;
		this.text = text;
	}

	public boolean match(TerminalScreen terminalScreen) {
		String foundText = terminalScreen.getText(position, text.length());
		if (foundText.equals(text)) {
			return true;
		}
		return false;
	}

}
