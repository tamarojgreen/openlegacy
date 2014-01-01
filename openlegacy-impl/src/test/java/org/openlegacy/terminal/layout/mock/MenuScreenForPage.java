package org.openlegacy.terminal.layout.mock;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@ScreenEntity(screenType = Menu.MenuEntity.class)
public class MenuScreenForPage implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 22, endColumn = 31, editable = true)
	private String fld1;

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	public String getFld1() {
		return fld1;
	}

	public void setFld1(String fld1) {
		this.fld1 = fld1;
	}

	@SuppressWarnings("unchecked")
	public List<TerminalActionDefinition> getActions() {
		return Collections.EMPTY_LIST;
	}
}
