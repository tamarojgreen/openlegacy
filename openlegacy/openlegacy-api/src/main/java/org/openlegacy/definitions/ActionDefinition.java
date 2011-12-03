package org.openlegacy.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;

/**
 * An action definition. Translated from @ScreenAction and store within a screen entity in the registry
 * 
 */
public interface ActionDefinition extends TerminalPositionContainer {

	Class<? extends SessionAction<Session>> getAction();

	String getDisplayName();

	TerminalPosition getPosition();
}
