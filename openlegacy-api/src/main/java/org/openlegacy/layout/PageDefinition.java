package org.openlegacy.layout;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.ActionDefinition;

import java.util.List;

public interface PageDefinition {

	EntityDefinition<?> getEntityDefinition();

	List<PagePartDefinition> getPageParts();

	List<ActionDefinition> getActions();
}
