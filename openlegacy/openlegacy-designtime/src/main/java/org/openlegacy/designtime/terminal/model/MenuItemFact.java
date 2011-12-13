package org.openlegacy.designtime.terminal.model;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

public class MenuItemFact {

	private TerminalField codeField;
	private TerminalField captionField;
	private ScreenEntityDefinition screenEntityDefinition;

	public MenuItemFact(TerminalField codeField, TerminalField captionField, ScreenEntityDefinition screenEntityDefinition) {
		this.codeField = codeField;
		this.captionField = captionField;
		this.screenEntityDefinition = screenEntityDefinition;
	}

	public TerminalField getCodeField() {
		return codeField;
	}

	public TerminalField getCaptionField() {
		return captionField;
	}

	public ScreenEntityDefinition getScreenEntityDefinition() {
		return screenEntityDefinition;
	}
}
