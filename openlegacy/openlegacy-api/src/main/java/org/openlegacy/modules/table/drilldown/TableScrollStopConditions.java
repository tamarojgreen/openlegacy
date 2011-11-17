package org.openlegacy.modules.table.drilldown;

/**
 * A table stop conditions definition. Determine if to stop based on the given entity, or by comparing the entity before to the
 * entity after the scroll
 * 
 */
public interface TableScrollStopConditions {

	boolean shouldStop(Object entity);

	boolean shouldStop(Object beforeScrollEntity, Object afterScrollEntity);
}
