package org.openlegacy.designtime.db.generators.support;

import org.apache.commons.lang.NotImplementedException;
import org.openlegacy.EntityDefinition;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.DbNavigationDefinition;
import org.openlegacy.db.definitions.DbTableDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.db.generators.DbPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.AbstractCodeBasedEntityDefinition;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Bort
 * 
 */
public class CodeBasedDbEntityDefinition extends AbstractCodeBasedEntityDefinition<DbFieldDefinition, DbPojoCodeModel> implements DbEntityDefinition {

	private Map<String, DbFieldDefinition> columnFields;

	public CodeBasedDbEntityDefinition(DbPojoCodeModel codeModel, File packageDir) {
		super(codeModel, packageDir);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#getAllFieldsDefinitions()
	 */
	public Map<String, DbFieldDefinition> getAllFieldsDefinitions() {
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#getAllChildEntitiesDefinitions()
	 */
	public Set<EntityDefinition<?>> getAllChildEntitiesDefinitions() {
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#isWindow()
	 */
	public boolean isWindow() {
		throwNotImplemented();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#isValidateKeys()
	 */
	public boolean isValidateKeys() {
		throwNotImplemented();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.terminal.generators.support.AbstractCodeBasedEntityDefinition#getFieldsDefinitions()
	 */
	@Override
	public Map<String, DbFieldDefinition> getFieldsDefinitions() {
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.terminal.generators.support.AbstractCodeBasedEntityDefinition#getActions()
	 */
	@Override
	public List<ActionDefinition> getActions() {
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.terminal.generators.support.AbstractCodeBasedEntityDefinition#getChildEntitiesDefinitions()
	 */
	@Override
	public List<EntityDefinition<?>> getChildEntitiesDefinitions() {
		throwNotImplemented();
		return null;
	}

	private static void throwNotImplemented() throws UnsupportedOperationException {
		throw (new NotImplementedException("Code based screen entity has not implemented this method"));
	}

	public String getName() {
		return getCodeModel().getName();
	}

	public DbTableDefinition getTableDefinition() {
		return getCodeModel().getTableDefinition();
	}

	public Map<String, DbFieldDefinition> getColumnFieldsDefinitions() {
		if (columnFields == null) {
			columnFields = DbCodeBasedDefinitionUtils.getColumnFieldsFromCodeModel(getCodeModel());
		}
		return columnFields;
	}

	public DbNavigationDefinition getNavigationDefinition() {
		return getCodeModel().getNavigationDefinition();
	}

}
