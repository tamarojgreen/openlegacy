package org.openlegacy.db.definitions;

import org.openlegacy.EntityDefinition;

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

	EntityDefinition getTargetEntityDefinition();
}
