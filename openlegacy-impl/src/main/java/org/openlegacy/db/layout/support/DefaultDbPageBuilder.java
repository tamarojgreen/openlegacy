package org.openlegacy.db.layout.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.layout.DbPageBuilder;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.layout.PageDefinition;

public class DefaultDbPageBuilder implements DbPageBuilder {

	private final static Log logger = LogFactory.getLog(DefaultDbPageBuilder.class);

	@Override
	public PageDefinition build(DbEntityDefinition entityDefinition) {
		PageDefinition pageDefinition = new SimplePageDefinition(entityDefinition);
		pageDefinition.getActions().addAll(entityDefinition.getActions());
		return pageDefinition;
	}

}
