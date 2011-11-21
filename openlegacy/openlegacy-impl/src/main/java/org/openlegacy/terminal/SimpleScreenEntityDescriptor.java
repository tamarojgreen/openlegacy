package org.openlegacy.terminal;

import org.openlegacy.SimpleEntityDescriptor;

public class SimpleScreenEntityDescriptor extends SimpleEntityDescriptor implements ScreenEntityDescriptor {

	private boolean current;

	public SimpleScreenEntityDescriptor(Class<?> entityClass, String entityName, String displayName, boolean current) {
		super(entityClass, entityName, displayName);
		this.current = current;
	}

	public boolean isCurrent() {
		return current;
	}

}
