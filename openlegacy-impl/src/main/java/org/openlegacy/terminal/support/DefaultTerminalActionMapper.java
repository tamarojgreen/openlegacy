package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The default mapper for actions. Maps a class terminal action to a command. Command implementation depends on the emulation
 * provider SendKey(command) implementation type (String, Integer, Enum etc)
 * 
 */
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

	/**
	 * Reverse lookup. Find the action which is mapped to the given command. If 2 actions are mapped to the same command, the 1st
	 * one is picked
	 */
	public TerminalAction getAction(Object command) {
		if (command == null) {
			return null;
		}

		Set<Entry<TerminalAction, Object>> entires = actionMappings.entrySet();
		for (Entry<TerminalAction, Object> entry : entires) {
			if (entry.getValue().equals(command)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
