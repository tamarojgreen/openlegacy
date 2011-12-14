package org.openlegacy.terminal.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

public class SimpleTerminalActionDefinition extends SimpleActionDefinition implements TerminalActionDefinition {

	private TerminalPosition position;
	private AdditionalKey additionalKey;

	public SimpleTerminalActionDefinition(SessionAction<? extends Session> action, AdditionalKey additionalKey,
			String displayName, TerminalPosition position) {
		super(action, displayName);
		this.position = position;
		this.additionalKey = additionalKey;
	}

	public TerminalPosition getPosition() {
		return position;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}
}
