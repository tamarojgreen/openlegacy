package org.openlegacy.modules.table.drilldown;

import org.openlegacy.HostSession;

/**
 * Perform a scroll on the given session and the given entity class. The row keys are provided as a helper in order to help decide
 * on the scrolling direction
 * 
 */
public interface TableScroller<S extends HostSession> {

	<T> T scroll(S session, Class<T> entityClass, Object... rowKeys);
}
