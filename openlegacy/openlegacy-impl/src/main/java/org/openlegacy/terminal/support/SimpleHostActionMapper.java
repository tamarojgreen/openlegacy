package org.openlegacy.terminal.support;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.HostActionMapper;

import java.util.HashMap;
import java.util.Map;

public class SimpleHostActionMapper implements HostActionMapper {

	private Map<Class<? extends HostAction>, Object> actionMappings = new HashMap<Class<? extends HostAction>, Object>();

	public Object getCommand(Class<? extends HostAction> hostAction) {
		return actionMappings.get(hostAction);
	}

	public void setActionMappings(Map<Class<? extends HostAction>, Object> actionMappings) {
		this.actionMappings = actionMappings;
	}

}
