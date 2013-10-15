package org.openlegacy.rpc.layout.support;

import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.layout.PageBuilder;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

public class DefaultRpcPageBuilder implements PageBuilder<RpcEntityDefinition, RpcFieldDefinition> {

	public PageDefinition build(RpcEntityDefinition entityDefinition) {
		return new SimplePageDefinition(entityDefinition);
	}

}
