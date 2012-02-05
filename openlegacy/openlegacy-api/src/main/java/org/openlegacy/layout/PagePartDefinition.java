package org.openlegacy.layout;

import java.util.List;

public interface PagePartDefinition {

	int getColumns();

	List<PagePartRowDefinition> getPartRows();

	int getWidth();
}
