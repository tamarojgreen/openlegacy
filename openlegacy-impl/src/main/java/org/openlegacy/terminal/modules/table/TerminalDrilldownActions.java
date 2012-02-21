package org.openlegacy.terminal.modules.table;

import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

public class TerminalDrilldownActions {

	public static TerminalDrilldownAction enter(Object actionValue) {
		return new EnterDrilldownAction(actionValue);
	}

	public static class EnterDrilldownAction extends ENTER implements TerminalDrilldownAction {

		private static final long serialVersionUID = 1L;

		private Object actionValue;

		public EnterDrilldownAction(Object actionValue) {
			this.actionValue = actionValue;
		}

		public Object getActionValue() {
			return actionValue;
		}

	}
}
