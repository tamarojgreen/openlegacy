package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Screen title") })
public class BooleanScreen implements org.openlegacy.terminal.ScreenEntity {

	@ScreenBooleanField(falseValue = "N", trueValue = "Y")
	@ScreenField(row = 3, column = 20, labelColumn = 3, editable = true, displayName = "Boolean field")
	Boolean booleanTrue;

	@ScreenBooleanField(falseValue = "N", trueValue = "Y")
	@ScreenField(row = 4, column = 20, labelColumn = 3, editable = true, displayName = "Boolean field2")
	Boolean booleanFalse;

	@ScreenBooleanField(falseValue = "N", trueValue = "Y")
	@ScreenField(row = 5, column = 20, labelColumn = 3, editable = true, displayName = "Boolean field2")
	Boolean booleanEmpty;

	public Boolean getBooleanTrue() {
		return booleanTrue;
	}

	public void setBooleanTrue(Boolean booleanTrue) {
		this.booleanTrue = booleanTrue;
	}

	public Boolean getBooleanFalse() {
		return booleanFalse;
	}

	public void setBooleanFalse(Boolean booleanFalse) {
		this.booleanFalse = booleanFalse;
	}

	public Boolean getBooleanEmpty() {
		return booleanEmpty;
	}

	public void setBooleanEmpty(Boolean booleanEmpty) {
		this.booleanEmpty = booleanEmpty;
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
