package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenPositionContainer;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreenIdentifier;

/**
 * A simple implementation for a screen identifier
 * 
 */
public class SimpleScreenIdentifier implements ScreenIdentifier, ScreenPositionContainer {

	private ScreenPosition position;
	private String text;

	public SimpleScreenIdentifier(ScreenPosition position, String text) {
		this.position = position;
		this.text = text;
	}

	public boolean match(TerminalSnapshot terminalSnapshot) {
		String foundText = terminalSnapshot.getText(position, text.length());
		if (foundText.equals(text)) {
			return true;
		}
		return false;
	}

	public ScreenPosition getPosition() {
		return position;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return SnapshotUtils.positionTextToString(position, text);
	}
}
