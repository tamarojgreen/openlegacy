package org.openlegacy.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;

/**
 * An action definition. Translated from @ScreenAction and store within a screen entity in the registry
 * 
 */
public interface ActionDefinition {

	Class<? extends SessionAction<Session>> getAction();

	String getDisplayName();
}
