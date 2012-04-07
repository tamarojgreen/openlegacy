package org.openlegacy.modules.table.drilldown;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;

/**
 * A drill down action represent a drill-down action on a table The row number is typically calculated by <code>RowFinder</code>
 * 
 */
public interface DrilldownAction<S extends Session> extends SessionAction<S> {

	Object getActionValue();

	void setActionValue(Object actionValue);
}
