package org.openlegacy.modules.table.drilldown;

import org.openlegacy.SessionAction;
import org.openlegacy.Session;

public interface DrilldownAction<S extends Session> extends SessionAction<S> {

	Object getActionValue();
}
