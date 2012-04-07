package org.openlegacy.terminal.modules.table;

import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

public class TerminalDrilldownActions {

	public static TerminalDrilldownAction enter(Object actionValue) {
		EnterDrilldownAction action = new EnterDrilldownAction();
		action.setActionValue(actionValue);
		return action;
	}

	public static class EnterDrilldownAction extends ENTER implements TerminalDrilldownAction {

		private static final long serialVersionUID = 1L;

		private Object actionValue;

		public EnterDrilldownAction() {}

		public Object getActionValue() {
			return actionValue;
		}

		public void setActionValue(Object actionValue) {
			this.actionValue = actionValue;
		}
	}
}
