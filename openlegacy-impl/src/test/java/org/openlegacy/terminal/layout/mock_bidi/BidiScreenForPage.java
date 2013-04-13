package org.openlegacy.terminal.layout.mock_bidi;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;

@ScreenEntity
public class BidiScreenForPage implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 52, endColumn = 61, labelColumn = 63, editable = true, displayName = "right side label")
	private String fldCol2;

	@ScreenField(row = 6, column = 22, endColumn = 31, labelColumn = 33, editable = true, displayName = "right side label")
	private String fldCol1;

	@ScreenField(row = 7, column = 22, endColumn = 31, editable = true)
	private String fld2Col2;

	public String getFldCol1() {
		return fldCol1;
	}

	public String getFld2Col2() {
		return fld2Col2;
	}

	public String getFldCol2() {
		return fldCol2;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

}
