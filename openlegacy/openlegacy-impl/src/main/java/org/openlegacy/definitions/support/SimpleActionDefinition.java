package org.openlegacy.definitions.support;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;

public class SimpleActionDefinition implements ActionDefinition {

	private String displayName;
	private SessionAction<? extends Session> action;
	private String alias;

	public SimpleActionDefinition(SessionAction<? extends Session> action, String displayName) {
		this.action = action;
		this.displayName = displayName;
	}

	public SessionAction<? extends Session> getAction() {
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
