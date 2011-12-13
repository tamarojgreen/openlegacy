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

	/**
	 * Allow to refer an action by a logical name, and not "technical" action name ("F3", etc)
	 */
	String getAlias();

}
