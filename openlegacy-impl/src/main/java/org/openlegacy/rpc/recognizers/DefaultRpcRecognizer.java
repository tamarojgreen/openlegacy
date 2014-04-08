package org.openlegacy.rpc.recognizers;

import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class DefaultRpcRecognizer implements RpcRecognizer {

	@Inject
	private ApplicationContext applicationContext;

	public Class<?> match(RpcSnapshot rpcSnapshot) {
		RpcEntitiesRegistry rpcEntitiesRegistry = SpringUtil.getBean(applicationContext, RpcEntitiesRegistry.class);

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.match(rpcSnapshot);
		if (rpcEntityDefinition != null) {
			return rpcEntityDefinition.getEntityClass();
		}
		return null;
	}
}
