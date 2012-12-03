package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Multy line title") })
public class MultyLineFieldScreen implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 3, endRow = 4, column = 15, endColumn = 20, rectangle = true, editable = true)
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

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
