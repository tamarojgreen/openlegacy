package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;

import java.util.Date;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "The Date Screen title") })
public class DateScreen implements org.openlegacy.terminal.ScreenEntity {

	@ScreenDateField(dayColumn = 20, monthColumn = 25, yearColumn = 30)
	@ScreenField(row = 2, column = 20, labelColumn = 3, editable = true, displayName = "Date field")
	Date dateField;

	public Date getDateField() {
		return dateField;
	}

	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}

	@ScreenDateField(dayColumn = 20, monthColumn = 25, yearColumn = 30)
	@ScreenField(row = 3, column = 20, labelColumn = 3, editable = true, displayName = "Date field")
	Date dateField2;

	public Date getDateField2() {
		return dateField2;
	}

	public void setDateField2(Date dateField2) {
		this.dateField2 = dateField2;
	}

	@ScreenDateField(pattern = "dd - MM - yyyy")
	@ScreenField(row = 4, column = 20, labelColumn = 3, editable = true, displayName = "Date field")
	Date dateField3;

	public Date getDateField3() {
		return dateField3;
	}

	public void setDateField3(Date dateField3) {
		this.dateField3 = dateField3;
	}

	@ScreenDateField(pattern = "dd MMM yyyy")
	@ScreenField(row = 5, column = 20, labelColumn = 3, editable = true, displayName = "Date field")
	Date dateField4;

	public Date getDateField4() {
		return dateField4;
	}

	public void setDateField4(Date dateField4) {
		this.dateField4 = dateField4;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
