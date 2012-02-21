package org.openlegacy.modules.table.drilldown;

import org.openlegacy.Session;

/**
 * Row selector performs a row selection on the given entity and session
 * 
 */
public interface RowSelector<S extends Session, T> {

	<D extends DrilldownAction<?>> void selectRow(S session, T entity, D drilldownAction, int row);
}
