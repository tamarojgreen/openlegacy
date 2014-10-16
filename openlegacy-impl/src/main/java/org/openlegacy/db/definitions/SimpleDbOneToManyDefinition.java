package org.openlegacy.db.definitions;

import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbOneToManyDefinition implements DbOneToManyDefinition {

	private String[] cascadeTypeNames = new String[] {};
	private String fetchTypeName = FetchType.LAZY.toString();
	private String mappedBy = "";
	private boolean orphanRemoval = false;
	private Class<?> targetEntity = void.class;
	private String targetEntityClassName = void.class.getSimpleName();

	public String[] getCascadeTypeNames() {
		return cascadeTypeNames;
	}

	public String getFetchTypeName() {
		return fetchTypeName;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public boolean isOrphanRemoval() {
		return orphanRemoval;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setCascadeTypeNames(String[] cascadeTypeNames) {
		this.cascadeTypeNames = cascadeTypeNames;
	}

	public void setFetchTypeName(String fetchTypeName) {
		this.fetchTypeName = fetchTypeName;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	public void setOrphanRemoval(boolean orphanRemoval) {
		this.orphanRemoval = orphanRemoval;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

}
