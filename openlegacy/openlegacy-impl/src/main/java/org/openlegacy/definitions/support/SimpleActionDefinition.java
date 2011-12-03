package org.openlegacy.definitions.support;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.ScreenPosition;

public class SimpleActionDefinition implements ActionDefinition {

	private String displayName;
	private Class<? extends SessionAction<Session>> action;
	private ScreenPosition position;

	public SimpleActionDefinition(Class<? extends SessionAction<Session>> action, ScreenPosition screenPosition,
			String displayName) {
		this.action = action;
		this.position = screenPosition;
		this.displayName = displayName;
	}

	public Class<? extends SessionAction<Session>> getAction() {
		return action;
	}

	public String getDisplayName() {
		return displayName;
	}

	public ScreenPosition getPosition() {
		return position;
	}

}
