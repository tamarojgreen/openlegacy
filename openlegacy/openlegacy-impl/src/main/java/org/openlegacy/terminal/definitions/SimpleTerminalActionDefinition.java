package org.openlegacy.terminal.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.terminal.TerminalPosition;

public class SimpleTerminalActionDefinition extends SimpleActionDefinition implements TerminalActionDefinition {

	private AdditionalKey additionalKey = AdditionalKey.NONE;
	private TerminalPosition position;

	public SimpleTerminalActionDefinition(Class<? extends SessionAction<Session>> action, AdditionalKey additionalKey,
			String displayName, TerminalPosition position) {
		super(action, displayName);
		this.additionalKey = additionalKey;
		this.position = position;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	public void setAdditionKey(AdditionalKey additionKey) {
		this.additionalKey = additionKey;
	}

	public TerminalPosition getPosition() {
		return position;
	}
}
