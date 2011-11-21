package org.openlegacy.modules.table;

import org.openlegacy.Session;

import java.util.List;

public interface TableCollector<S extends Session, T> {

	public List<T> collectAll(S session, Class<?> entityClass, Class<T> rowClass);
}
