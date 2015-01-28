package org.openlegacy.db.layout.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.layout.DbPageBuilder;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.layout.PageDefinition;

import java.util.List;

public class DefaultDbPageBuilder implements DbPageBuilder {

	// private final static Log logger = LogFactory.getLog(DefaultDbPageBuilder.class);

	@Override
	public PageDefinition build(DbEntityDefinition entityDefinition) {
		PageDefinition pageDefinition = new SimplePageDefinition(entityDefinition);
		pageDefinition.getActions().addAll(entityDefinition.getActions());

		List<EntityDefinition<?>> childsEntities = entityDefinition.getChildEntitiesDefinitions();

		for (EntityDefinition<?> dbEntityDefinition : childsEntities) {
			DbEntityDefinition innerEntityDefinition = (DbEntityDefinition)dbEntityDefinition;
			PageDefinition innerPageDefinition = new SimplePageDefinition(innerEntityDefinition);
			pageDefinition.getChildPagesDefinitions().add(innerPageDefinition);
		}
		return pageDefinition;
	}
}
