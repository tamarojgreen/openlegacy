package org.openlegacy.modules.table;

import org.openlegacy.modules.HostSessionModule;

import java.util.List;

public interface Table extends HostSessionModule {

	<T> List<T> collectAll(Class<?> screenEntityClass, Class<T> rowClass);
}
