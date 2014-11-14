package org.openlegacy.terminal.layout.mock_bidi;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

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
