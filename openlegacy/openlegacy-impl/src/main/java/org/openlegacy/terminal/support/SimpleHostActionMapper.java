package org.openlegacy.terminal.support;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.HostActionMapper;

import java.util.HashMap;
import java.util.Map;

public class SimpleHostActionMapper implements HostActionMapper {

	private Map<Class<? extends HostAction>, Object> actionMappings = new HashMap<Class<? extends HostAction>, Object>();

	public Object getCommand(Class<? extends HostAction> hostAction) {
		Object command = actionMappings.get(hostAction);
		if (command != null) {
			return command;
		}
		// support super class as well. DrillDownAction for example
		return actionMappings.get(hostAction.getSuperclass());
	}

	public void setActionMappings(Map<Class<? extends HostAction>, Object> actionMappings) {
		this.actionMappings = actionMappings;
	}

}
