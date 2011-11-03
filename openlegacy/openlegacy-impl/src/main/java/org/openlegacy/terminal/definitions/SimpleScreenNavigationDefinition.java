package org.openlegacy.terminal.definitions;

import org.openlegacy.HostAction;

import java.util.ArrayList;
import java.util.List;

public class SimpleScreenNavigationDefinition implements NavigationDefinition {

	private Class<?> accessedFrom;
	private List<FieldAssignDefinition> assignedFields = new ArrayList<FieldAssignDefinition>();
	private HostAction hostAction;
	private HostAction exitAction;

	public Class<?> getAccessedFrom() {
		return accessedFrom;
	}

	public void setAccessedFrom(Class<?> accessedFrom) {
		this.accessedFrom = accessedFrom;
	}

	public List<FieldAssignDefinition> getAssignedFields() {
		return assignedFields;
	}

	public HostAction getHostAction() {
		return hostAction;
	}

	public void setHostAction(HostAction hostAction) {
		this.hostAction = hostAction;
	}

	public HostAction getExitAction() {
		return exitAction;
	}

	public void setExitAction(HostAction exitAction) {
		this.exitAction = exitAction;
	}

}
