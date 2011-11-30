package org.openlegacy.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;

public interface ActionDefinition {

	Class<? extends SessionAction<Session>> getAction();

	String getDisplayName();
}
