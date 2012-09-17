package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions.EnterDrilldownAction;
import org.openlegacy.terminal.support.mock.ScreenWithKey.ScreenWithKeyDrillDown;

@SuppressWarnings("unused")
@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Screen With Key") })
@ScreenNavigation(accessedFrom = TableScreen.class, requiresParameters = true, terminalAction = ScreenWithKeyDrillDown.class)
public class ScreenWithKey implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 2, column = 20, key = true)
	Integer keyField;

	public Integer getKeyField() {
		return keyField;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	public static class ScreenWithKeyDrillDown extends EnterDrilldownAction {

		private static final long serialVersionUID = 1L;

		@Override
		public Object getActionValue() {
			return "1";
		}

	}
}
