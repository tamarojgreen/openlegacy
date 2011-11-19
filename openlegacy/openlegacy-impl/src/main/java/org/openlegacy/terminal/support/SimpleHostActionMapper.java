package org.openlegacy.terminal.support;

import org.openlegacy.terminal.HostActionMapper;
import org.openlegacy.terminal.actions.TerminalAction;

import java.util.HashMap;
import java.util.Map;

public class SimpleHostActionMapper implements HostActionMapper {

	private Map<Class<? extends TerminalAction>, Object> actionMappings = new HashMap<Class<? extends TerminalAction>, Object>();

	public Object getCommand(Class<? extends TerminalAction> hostAction) {
		Object command = actionMappings.get(hostAction);
		if (command != null) {
			return command;
		}
		// support super class as well. DrillDownAction for example
		return actionMappings.get(hostAction.getSuperclass());
	}

	public void setActionMappings(Map<Class<? extends TerminalAction>, Object> actionMappings) {
		this.actionMappings = actionMappings;
	}

}
