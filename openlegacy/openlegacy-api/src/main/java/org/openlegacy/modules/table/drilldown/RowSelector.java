package org.openlegacy.modules.table.drilldown;

import org.openlegacy.HostSession;

/**
 * Row selector performs a row selection on the given entity and session
 * 
 */
public interface RowSelector<S extends HostSession> {

	<D extends DrilldownAction> void selectRow(S session, Object entity, D drilldownAction, int row);
}
