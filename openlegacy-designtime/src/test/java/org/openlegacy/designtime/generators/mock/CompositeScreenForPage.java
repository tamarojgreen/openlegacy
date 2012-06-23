package org.openlegacy.designtime.generators.mock;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;

@ScreenEntity
public class CompositeScreenForPage implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 22, endColumn = 31)
	private String fld1;

	private ScreenForPage screenForPage;

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	public String getFld1() {
		return fld1;
	}

	public ScreenForPage getScreenForPage() {
		return screenForPage;
	}
}
