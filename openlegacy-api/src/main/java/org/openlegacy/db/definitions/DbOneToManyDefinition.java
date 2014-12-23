package org.openlegacy.db.definitions;

/**
 * @author Ivan Bort
 * 
 */
public interface DbOneToManyDefinition {

	String[] getCascadeTypeNames();

	String getFetchTypeName();

	String getMappedBy();

	boolean isOrphanRemoval();

	Class<?> getTargetEntity();

	String getTargetEntityClassName();

	String getJoinColumnName();
}
