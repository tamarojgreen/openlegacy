package org.openlegacy.terminal.definitions;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

public interface TerminalActionDefinition extends ActionDefinition, TerminalPositionContainer {

	TerminalPosition getPosition();

	AdditionalKey getAdditionalKey();

}
