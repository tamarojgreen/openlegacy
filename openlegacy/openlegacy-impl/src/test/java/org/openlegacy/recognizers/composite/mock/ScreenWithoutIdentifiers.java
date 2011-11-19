package org.openlegacy.recognizers.composite.mock;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.springframework.stereotype.Component;

@Component
@ScreenEntity(name = "ApplinXDemoEnvironment")
public class ScreenWithoutIdentifiers implements org.openlegacy.terminal.ScreenEntity {

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
