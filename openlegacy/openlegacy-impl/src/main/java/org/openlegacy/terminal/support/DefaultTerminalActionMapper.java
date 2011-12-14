package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;

public class DefaultTerminalActionMapper implements TerminalActionMapper {

	private Map<TerminalAction, Object> actionMappings = new HashMap<TerminalAction, Object>();

	public Object getCommand(TerminalAction terminalAction) {
		Object command = null;

		// check standard mapping
		command = actionMappings.get(terminalAction);

		// check combined mapping
		if (command == null) {
			command = actionMappings.get(TerminalActions.combined(AdditionalKey.NONE, terminalAction));
		}
		if (command != null) {
			return command;
		}
		// check parent class - used by EnterDrilldownAction
		if (TerminalAction.class.isAssignableFrom(terminalAction.getClass().getSuperclass())) {
			TerminalAction newParentInstance = (TerminalAction)ReflectionUtil.newInstance(terminalAction.getClass().getSuperclass());
			return getCommand(newParentInstance);
		}

		return null;

	}

	public void setActionMappings(Map<TerminalAction, Object> actionMappings) {
		this.actionMappings = actionMappings;
	}

}
