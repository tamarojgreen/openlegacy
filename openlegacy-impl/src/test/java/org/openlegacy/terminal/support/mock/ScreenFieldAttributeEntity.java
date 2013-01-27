package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.FieldAttributeType;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 23, value = "Attributes") })
public class ScreenFieldAttributeEntity implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 8, column = 10, attribute = FieldAttributeType.Editable)
	private Boolean inEditModeFalse;

	public Boolean getInEditModeFalse() {
		return inEditModeFalse;
	}

	public void setInEditModeFalse(Boolean inEditModeFalse) {
		this.inEditModeFalse = inEditModeFalse;
	}

	@ScreenField(row = 9, column = 10, attribute = FieldAttributeType.Editable)
	private Boolean inEditModeTrue;

	public Boolean getInEditModeTrue() {
		return inEditModeTrue;
	}

	public void setInEditModeTrue(Boolean inEditModeTrue) {
		this.inEditModeTrue = inEditModeTrue;
	}

	@ScreenField(row = 10, column = 10, attribute = FieldAttributeType.Color)
	Color redField;

	public Color getRedField() {
		return redField;
	}

	public void setRedField(Color redField) {
		this.redField = redField;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {

	}

}
