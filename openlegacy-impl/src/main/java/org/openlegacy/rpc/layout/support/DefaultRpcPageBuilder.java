package org.openlegacy.rpc.layout.support;

import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.layout.RpcPageBuilder;

public class DefaultRpcPageBuilder implements RpcPageBuilder {

	public PageDefinition build(RpcEntityDefinition entityDefinition) {
		return new SimplePageDefinition(entityDefinition);
	}

}
