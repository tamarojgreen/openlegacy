package org.openlegacy.definitions.page.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.layout.PagePartDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimplePageDefinition implements PageDefinition {

	private List<PagePartDefinition> pageParts = new ArrayList<PagePartDefinition>();
	private EntityDefinition<?> entityDefinition;

	public SimplePageDefinition(EntityDefinition<?> entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	public List<PagePartDefinition> getPageParts() {
		return pageParts;
	}

	public EntityDefinition<?> getEntityDefinition() {
		return entityDefinition;
	}

}
