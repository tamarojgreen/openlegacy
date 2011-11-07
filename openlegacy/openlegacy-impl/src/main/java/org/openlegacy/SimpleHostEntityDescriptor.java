package org.openlegacy;

public class SimpleHostEntityDescriptor implements HostEntityDescriptor {

	private Class<?> entityClass;
	private String entityName;
	private String displayName;

	public SimpleHostEntityDescriptor(Class<?> entityClass, String entityName, String displayName) {
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
}
