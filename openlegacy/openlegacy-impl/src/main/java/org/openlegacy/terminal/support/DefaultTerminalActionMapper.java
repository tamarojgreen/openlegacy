package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.actions.TerminalAction;

import java.util.HashMap;
import java.util.Map;

public class DefaultTerminalActionMapper implements TerminalActionMapper {

	private Map<Class<? extends TerminalAction>, Object> actionMappings = new HashMap<Class<? extends TerminalAction>, Object>();

	public Object getCommand(Class<? extends TerminalAction> terminalAction) {
		Object command = actionMappings.get(terminalAction);
		if (command != null) {
			return command;
		}
		// support super class as well. DrillDownAction for example
		return actionMappings.get(terminalAction.getSuperclass());
	}

	public void setActionMappings(Map<Class<? extends TerminalAction>, Object> actionMappings) {
		this.actionMappings = actionMappings;
	}

}
