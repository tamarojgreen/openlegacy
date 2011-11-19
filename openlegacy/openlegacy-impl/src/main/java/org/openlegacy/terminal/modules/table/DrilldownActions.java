package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;

public class DrilldownActions {

	public static DrilldownAction enter(Object actionValue) {
		return new EnterDrilldownAction(actionValue);
	}

	public static class EnterDrilldownAction extends ENTER implements DrilldownAction {

		private Object actionValue;

		public EnterDrilldownAction(Object actionValue) {
			this.actionValue = actionValue;
		}

		public Object getActionValue() {
			return actionValue;
		}

	}
}
