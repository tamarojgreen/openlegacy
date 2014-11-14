package org.openlegacy.recognizers.pattern.mock;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@ScreenEntity
public class SignOn implements org.openlegacy.terminal.ScreenEntity {

	@Override
	public String getFocusField() {
		return null;
	}

	@Override
	public void setFocusField(String focusField) {}

	@Override
	@SuppressWarnings("unchecked")
	public List<TerminalActionDefinition> getActions() {
		return Collections.EMPTY_LIST;
	}
}
