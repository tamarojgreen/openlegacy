package org.openlegacy.modules.table.drilldown;

/**
 * A table stop condition definition. Determine if to stop based on the given entity, or by comparing the entity before to the
 * entity acter the scroll
 * 
 */
public interface TableStopCondition {

	boolean shouldStop(Object entity);

	boolean shouldStop(Object beforeScrollEntity, Object afterScrollEntity);
}
