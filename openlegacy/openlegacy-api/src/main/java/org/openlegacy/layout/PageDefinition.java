package org.openlegacy.layout;

import org.openlegacy.EntityDefinition;

import java.util.List;

public interface PageDefinition {

	EntityDefinition<?> getEntityDefinition();

	List<PagePartDefinition> getPageParts();
}
