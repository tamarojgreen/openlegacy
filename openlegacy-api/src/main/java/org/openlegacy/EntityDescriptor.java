package org.openlegacy;

public interface EntityDescriptor {

	Class<?> getEntityClass();

	String getEntityName();

	String getDisplayName();

	boolean isCurrent();
}
