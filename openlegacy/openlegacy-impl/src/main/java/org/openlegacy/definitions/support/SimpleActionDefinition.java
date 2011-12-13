package org.openlegacy.definitions.support;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;

public class SimpleActionDefinition implements ActionDefinition {

	private String displayName;
	private Class<? extends SessionAction<Session>> action;
	private String alias;

	public SimpleActionDefinition(Class<? extends SessionAction<Session>> action, String displayName) {
		this.action = action;
		this.displayName = displayName;
	}

	public Class<? extends SessionAction<Session>> getAction() {
		return action;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
