package org.openlegacy.db.layout.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.layout.DbPageBuilder;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.layout.PageDefinition;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class DefaultDbPageBuilder implements DbPageBuilder {

	// private final static Log logger = LogFactory.getLog(DefaultDbPageBuilder.class);

	@SuppressWarnings("rawtypes")
	@Inject
	private EntitiesRegistry entitiesRegistry;

	@SuppressWarnings("unchecked")
	@Override
	public PageDefinition build(DbEntityDefinition entityDefinition) {
		PageDefinition pageDefinition = new SimplePageDefinition(entityDefinition);
		pageDefinition.getActions().addAll(entityDefinition.getActions());

		Map<String, DbFieldDefinition> columnFields = entityDefinition.getColumnFieldsDefinitions();
		for (DbFieldDefinition dbFieldDefinition : columnFields.values()) {
			if (dbFieldDefinition.getOneToManyDefinition() != null) {
				Set<String> fieldsKeySet = entityDefinition.getFieldsDefinitions().keySet();
				if (fieldsKeySet.contains(dbFieldDefinition.getName())) {
					DbEntityDefinition innerEntityDefinition = (DbEntityDefinition)entitiesRegistry.get(dbFieldDefinition.getJavaType());
					PageDefinition innerPageDefinition = new SimplePageDefinition(innerEntityDefinition);
					pageDefinition.getChildPagesDefinitions().add(innerPageDefinition);
				}

			}
		}
		return pageDefinition;
	}
}
