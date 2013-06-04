package org.openlegacy.rpc.definitions;

import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.support.AbstractEntityDefinition;

public class SimpleRpcEntityDefinition extends AbstractEntityDefinition<RpcFieldDefinition> implements RpcEntityDefinition {

	private Languages language;

	public SimpleRpcEntityDefinition(String entityName, Class<?> entityClass) {
		super(entityName, entityClass);
	}

	public Languages getLanguage() {
		return language;
	}

	public void setLanguage(Languages language) {
		this.language = language;
	}

}
