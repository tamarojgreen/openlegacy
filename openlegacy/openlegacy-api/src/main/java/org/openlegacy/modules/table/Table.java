package org.openlegacy.modules.table;

import org.openlegacy.modules.SessionModule;
import org.openlegacy.modules.table.drilldown.DrilldownAction;

import java.util.List;

/**
 * Defines a table module. Performs actions on multiple entities (e.g: screens) like collecting multiple entities, drill down, etc
 * 
 */
public interface Table extends SessionModule {

	<T> List<T> collectAll(Class<?> entityClass, Class<T> rowClass);

	<T> T drillDown(Class<?> entityClass, Class<T> targetClass, DrilldownAction<?> drilldownAction, Object... rowKeys);

}
