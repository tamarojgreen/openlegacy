package org.openlegacy.modules.table.drilldown;

import org.openlegacy.HostSession;

/**
 * Performs a drill down action on the given session from sourceEntityClass to targetEntityClass using drill down action and the
 * given row keys
 * 
 */
public interface TableDrilldownPerfomer<S extends HostSession> {

	<T> T drilldown(S session, Class<?> sourceEntityClass, Class<T> targetEntityClass, DrilldownAction drilldownAction,
			Object... rowKeys);
}
