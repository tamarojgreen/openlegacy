package org.openlegacy;

public class SimpleEntityDescriptor implements EntityDescriptor {

	private Class<?> entityClass;
	private String entityName;
	private String displayName;
	private boolean current;

	public SimpleEntityDescriptor(Class<?> entityClass, String entityName, String displayName) {
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.displayName = displayName;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
}
