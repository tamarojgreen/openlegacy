package org.openlegacy.definitions.support;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.TerminalPosition;

public class SimpleActionDefinition implements ActionDefinition {

	private String displayName;
	private Class<? extends SessionAction<Session>> action;
	private TerminalPosition position;

	public SimpleActionDefinition(Class<? extends SessionAction<Session>> action, TerminalPosition position,
			String displayName) {
		this.action = action;
		this.position = position;
		this.displayName = displayName;
	}

	public Class<? extends SessionAction<Session>> getAction() {
		return action;
	}

	public String getDisplayName() {
		return displayName;
	}

	public TerminalPosition getPosition() {
		return position;
	}

}
