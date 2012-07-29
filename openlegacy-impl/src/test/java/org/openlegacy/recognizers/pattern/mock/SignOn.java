package org.openlegacy.recognizers.pattern.mock;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.springframework.stereotype.Component;

@ScreenEntity
public class SignOn implements org.openlegacy.terminal.ScreenEntity {

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
