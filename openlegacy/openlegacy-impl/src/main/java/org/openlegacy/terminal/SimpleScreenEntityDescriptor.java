package org.openlegacy.terminal;

import org.openlegacy.SimpleHostEntityDescriptor;

public class SimpleScreenEntityDescriptor extends SimpleHostEntityDescriptor implements ScreenEntityDescriptor {

	private boolean current;

	public SimpleScreenEntityDescriptor(Class<?> entityClass, String entityName, String displayName, boolean current) {
		super(entityClass, entityName, displayName);
		this.current = current;
	}

	public boolean isCurrent() {
		return current;
	}

}
