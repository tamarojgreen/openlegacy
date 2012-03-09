package org.openlegacy;

import java.util.Map;

public interface RecordsProvider<S extends Session, T> {

	Map<Object, T> getRecords(S session, Class<?> entityClass, Class<T> rowClass, boolean collectAll, String searchText);

	DisplayItem toDisplayItem(T record);
}
