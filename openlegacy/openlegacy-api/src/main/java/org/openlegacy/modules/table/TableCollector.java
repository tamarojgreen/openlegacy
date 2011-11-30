package org.openlegacy.modules.table;

import org.openlegacy.Session;

import java.util.List;

/**
 * Define a table collector over multiple entities
 * 
 * @param <S>
 *            The session type
 * @param <T>
 *            The row class type
 */
public interface TableCollector<S extends Session, T> {

	public List<T> collectAll(S session, Class<?> entityClass, Class<T> rowClass);
}
