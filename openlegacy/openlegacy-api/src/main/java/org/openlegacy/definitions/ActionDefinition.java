package org.openlegacy.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenPositionContainer;

/**
 * An action definition. Translated from @ScreenAction and store within a screen entity in the registry
 * 
 */
public interface ActionDefinition extends ScreenPositionContainer {

	Class<? extends SessionAction<Session>> getAction();

	String getDisplayName();

	ScreenPosition getPosition();
}
