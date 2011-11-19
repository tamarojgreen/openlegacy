package org.openlegacy.modules.table.drilldown;

import org.openlegacy.HostAction;
import org.openlegacy.HostSession;

public interface DrilldownAction<S extends HostSession> extends HostAction<S> {

	Object getActionValue();
}
