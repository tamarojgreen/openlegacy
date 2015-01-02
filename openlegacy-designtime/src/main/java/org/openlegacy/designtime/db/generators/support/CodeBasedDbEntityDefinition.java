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
	private List<ActionDefinition> actions;

	public CodeBasedDbEntityDefinition(DbPojoCodeModel codeModel, File packageDir) {
		super(codeModel, packageDir);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#getAllFieldsDefinitions()
	 */
	@Override
	public Map<String, DbFieldDefinition> getAllFieldsDefinitions() {
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#getAllChildEntitiesDefinitions()
	 */
	@Override
	public Set<EntityDefinition<?>> getAllChildEntitiesDefinitions() {
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#isWindow()
	 */
	@Override
	public boolean isWindow() {
		return getCodeModel().isWindow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.EntityDefinition#isValidateKeys()
	 */
	@Override
	public boolean isValidateKeys() {
		throwNotImplemented();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.terminal.generators.support. AbstractCodeBasedEntityDefinition#getFieldsDefinitions()
	 */
	@Override
	public Map<String, DbFieldDefinition> getFieldsDefinitions() {
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.terminal.generators.support. AbstractCodeBasedEntityDefinition#getActions()
	 */
	@Override
	public List<ActionDefinition> getActions() {
		if (actions == null) {
			actions = DbCodeBasedDefinitionUtils.getActionsFromCodeModel(getCodeModel(), getPackageDir());
		}
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.terminal.generators.support. AbstractCodeBasedEntityDefinition#getChildEntitiesDefinitions()
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

	@Override
	public Map<String, DbFieldDefinition> getColumnFieldsDefinitions() {
		if (columnFields == null) {
			columnFields = DbCodeBasedDefinitionUtils.getColumnFieldsFromCodeModel(getCodeModel());
		}
		return columnFields;
	}

	@Override
	public DbNavigationDefinition getNavigationDefinition() {
		return getCodeModel().getNavigationDefinition();
	}

	@Override
	public boolean isChild() {
		return getCodeModel().isChild();
	}

	@Override
	public String getPluralName() {
		throwNotImplemented();
		return null;
	}

}
