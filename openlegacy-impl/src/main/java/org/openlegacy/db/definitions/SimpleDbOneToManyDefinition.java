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
	private String joinColumnName = "";

	@Override
	public String[] getCascadeTypeNames() {
		return cascadeTypeNames;
	}

	@Override
	public String getFetchTypeName() {
		return fetchTypeName;
	}

	@Override
	public String getMappedBy() {
		return mappedBy;
	}

	@Override
	public boolean isOrphanRemoval() {
		return orphanRemoval;
	}

	@Override
	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	@Override
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

	@Override
	public String getJoinColumnName() {
		return joinColumnName;
	}

	public void setJoinColumnName(String joinColumnName) {
		this.joinColumnName = joinColumnName;
	}

}
