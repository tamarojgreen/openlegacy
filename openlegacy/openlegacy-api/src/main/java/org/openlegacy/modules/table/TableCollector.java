package org.openlegacy.modules.table;

import org.openlegacy.HostSession;

import java.util.List;

public interface TableCollector<S extends HostSession, T> {

	public List<T> collectAll(S session, Class<?> entityClass, Class<T> rowClass);
}
