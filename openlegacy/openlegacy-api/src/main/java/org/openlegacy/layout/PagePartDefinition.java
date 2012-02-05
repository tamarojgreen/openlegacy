package org.openlegacy.layout;

import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;

import java.util.List;

public interface PagePartDefinition {

	int getColumns();

	List<PagePartRowDefinition> getPartRows();

	int getWidth();

	TableDefinition<ColumnDefinition> getTableDefinition();
}
