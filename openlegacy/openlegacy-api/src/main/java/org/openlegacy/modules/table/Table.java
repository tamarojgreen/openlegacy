package org.openlegacy.modules.table;

import org.openlegacy.modules.HostSessionModule;
import org.openlegacy.modules.table.drilldown.DrilldownAction;

import java.util.List;

public interface Table extends HostSessionModule {

	<T> List<T> collectAll(Class<?> screenEntityClass, Class<T> rowClass);

	<T> T drillDown(Class<?> screenEntityClass, Class<T> targetClass, DrilldownAction drilldownAction, Object... rowKeys);

}
