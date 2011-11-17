package org.openlegacy.modules.table.drilldown;

import org.openlegacy.HostSession;
import org.openlegacy.terminal.definitions.TableDefinition.DrilldownDefinition;

/**
 * Performs a drill down action on the given session from sourceEntityClass to targetEntityClass using drill down action and the
 * given row keys
 * 
 */
public interface TableDrilldownPerformer<S extends HostSession> {

	<T> T drilldown(DrilldownDefinition drilldownDefinition, S session, Class<?> sourceEntityClass, Class<T> targetEntityClass,
			DrilldownAction drilldownAction, Object... rowKeys);

}
