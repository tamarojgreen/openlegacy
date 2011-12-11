package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreenIdentifier;

/**
 * A simple implementation for a screen identifier
 * 
 */
public class SimpleScreenIdentifier implements ScreenIdentifier, TerminalPositionContainer {

	private TerminalPosition position;
	private String text;

	public SimpleScreenIdentifier(TerminalPosition position, String text) {
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

	public TerminalPosition getPosition() {
		return position;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return SnapshotUtils.positionTextToString(position, text);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
