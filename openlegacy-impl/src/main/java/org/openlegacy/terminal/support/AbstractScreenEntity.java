package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenEntity;

public class AbstractScreenEntity implements ScreenEntity {

	private String focusField;

	public String getFocusField() {
		return focusField;
	}

	public void setFocusField(String focusField) {
		this.focusField = focusField;
	}
}
