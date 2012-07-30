package org.openlegacy.terminal.mock_session.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.support.AbstractScreenEntity;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(column = 3, row = 1, value = "1. Screen1") })
public class MenuScreen extends AbstractScreenEntity {

	@ScreenField(row = 3, column = 3, editable = true)
	private String menuSelection;

	public String getMenuSelection() {
		return menuSelection;
	}

	public void setMenuSelection(String menuSelection) {
		this.menuSelection = menuSelection;
	}
}
