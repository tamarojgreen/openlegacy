package org.openlegacy.terminal.definitions;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;

public interface TerminalActionDefinition extends ActionDefinition, TerminalPositionContainer {

	public enum AdditionalKey {
		NONE,
		SHIFT,
		CTRL,
		ALT
	}

	TerminalPosition getPosition();

	AdditionalKey getAdditionalKey();

}
