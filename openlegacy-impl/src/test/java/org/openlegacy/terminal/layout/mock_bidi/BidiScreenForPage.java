package org.openlegacy.terminal.layout.mock_bidi;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;

@ScreenEntity
public class BidiScreenForPage implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 22, endColumn = 31, labelColumn = 33, editable = true, displayName = "right side label")
	private String fldCol12;

	@ScreenField(row = 7, column = 22, endColumn = 31, editable = true)
	private String fld2Col12;

	public String getFldCol12() {
		return fldCol12;
	}

	public String getFld2Col12() {
		return fld2Col12;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

}
