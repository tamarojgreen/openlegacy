package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@ScreenEntity(window = true)
@ScreenIdentifiers(identifiers = { @Identifier(row = 6, column = 40, value = "Win title") })
public class WindowScreen1 implements org.openlegacy.terminal.ScreenEntity {

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	public List<TerminalActionDefinition> getActions() {
		return Collections.emptyList();
	}

}
