package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Multy line title") })
public class MultyLineFieldScreen implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 3, endRow = 4, column = 15, endColumn = 19, rectangle = true, editable = true)
	String multlyLineField;

	@ScreenField(row = 6, endRow = 7, column = 71, endColumn = 21, rectangle = false, editable = true)
	String multlyLineBreakingField;

	public String getMultlyLineField() {
		return multlyLineField;
	}

	public void setMultlyLineField(String multlyLineField) {
		this.multlyLineField = multlyLineField;
	}

	public void setMultlyLineBreakingField(String multlyLineBreakingField) {
		this.multlyLineBreakingField = multlyLineBreakingField;
	}

	public String getMultlyLineBreakingField() {
		return multlyLineBreakingField;
	}

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
