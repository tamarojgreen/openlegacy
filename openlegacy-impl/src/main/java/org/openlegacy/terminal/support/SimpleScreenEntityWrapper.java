package org.openlegacy.terminal.support;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityWrapper;

import java.util.List;

public class SimpleScreenEntityWrapper implements ScreenEntityWrapper {

	private ScreenEntity screenEntity;

	private String screenEntityName;

	private List<EntityDescriptor> paths;

	private List<ActionDefinition> actions;

	public SimpleScreenEntityWrapper() {
		// for de-serialization
	}

	public SimpleScreenEntityWrapper(ScreenEntity screenEntity, List<EntityDescriptor> paths, List<ActionDefinition> actions) {
		this.screenEntity = screenEntity;

		this.screenEntityName = screenEntity.getClass().getSimpleName();
		this.actions = actions;
		this.paths = paths;
	}

	public ScreenEntity getScreenEntity() {
		return screenEntity;
	}

	public String getScreenEntityName() {
		return screenEntityName;
	}

	public List<EntityDescriptor> getPaths() {
		return paths;
	}

	public List<ActionDefinition> getActions() {
		return actions;
	}
}
