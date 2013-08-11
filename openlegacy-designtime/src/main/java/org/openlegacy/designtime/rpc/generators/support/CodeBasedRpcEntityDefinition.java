package org.openlegacy.designtime.rpc.generators.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.AbstractCodeBasedEntityDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CodeBasedRpcEntityDefinition extends AbstractCodeBasedEntityDefinition<RpcFieldDefinition, RpcPojoCodeModel> implements RpcEntityDefinition {

	private List<ActionDefinition> actions;
	private Map<String, RpcFieldDefinition> fields;

	public CodeBasedRpcEntityDefinition(RpcPojoCodeModel codeModel, File packageDir) {
		super(codeModel, packageDir);
	}

	public Set<EntityDefinition<?>> getAllChildEntitiesDefinitions() {
		return new HashSet<EntityDefinition<?>>();
	}

	@Override
	public List<ActionDefinition> getActions() {
		if (actions == null) {
			actions = RpcCodeBasedDefinitionUtils.getActionsFromCodeModel(getCodeModel());
		}
		return actions;
	}

	@Override
	public List<EntityDefinition<?>> getChildEntitiesDefinitions() {
		return new ArrayList<EntityDefinition<?>>();
	}

	@Override
	public Map<String, RpcFieldDefinition> getFieldsDefinitions() {
		if (fields == null) {
			fields = RpcCodeBasedDefinitionUtils.getFieldsFromCodeModel(getCodeModel(), null);
		}
		return fields;
	}

	public boolean isWindow() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.rpc.definitions.RpcEntityDefinition#getLanguage()
	 */
	public Languages getLanguage() {
		return getCodeModel().getLanguage();
	}
}
