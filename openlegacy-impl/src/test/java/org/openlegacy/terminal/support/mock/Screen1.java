package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;

import java.util.Date;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Date Screen title") })
public class Screen1 implements org.openlegacy.terminal.ScreenEntity {

	@ScreenDateField(dayColumn = 20, monthColumn = 25, yearColumn = 30)
	@ScreenField(row = 2, column = 20, labelColumn = 3, editable = true, displayName = "Date field")
	Date dateField;

	public Date getDateField() {
		return dateField;
	}

	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
