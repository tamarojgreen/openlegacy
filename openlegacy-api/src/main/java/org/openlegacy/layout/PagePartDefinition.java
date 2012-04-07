package org.openlegacy.layout;

import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.definitions.TableDefinition.ColumnDefinition;

import java.util.List;

public interface PagePartDefinition {

	int getColumns();

	List<PagePartRowDefinition> getPartRows();

	int getWidth();

	TableDefinition<ColumnDefinition> getTableDefinition();
}
