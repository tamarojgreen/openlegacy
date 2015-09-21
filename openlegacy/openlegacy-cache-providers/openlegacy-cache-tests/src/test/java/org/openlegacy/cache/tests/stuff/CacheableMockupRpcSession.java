package org.openlegacy.cache.tests.stuff;

import org.openlegacy.cache.modules.CacheModule;
import org.openlegacy.cache.modules.CacheModule.ObtainEntityCallback;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcPojoFieldAccessor;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.support.MockupRpcSession;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.utils.HierarchyRpcPojoFieldAccessor;
import org.openlegacy.rpc.utils.SimpleHierarchyRpcPojoFieldAccessor;
import org.openlegacy.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class CacheableMockupRpcSession extends MockupRpcSession {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public RpcEntity doRpcAction(RpcAction action, RpcEntity rpcEntity) {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());

		SimpleRpcInvokeAction rpcAction = new SimpleRpcInvokeAction();
		rpcAction.setAction(action.getClass().getSimpleName());
		RpcActionDefinition actionDefinition = (RpcActionDefinition)rpcDefinition.getAction(action.getClass());
		if (actionDefinition != null) {
			rpcAction.setRpcPath(actionDefinition.getProgramPath());
			rpcAction.setProperties(actionDefinition.getProperties());
		}
		populateRpcFields(rpcEntity, rpcDefinition, rpcAction);
		converToLegacyFields(rpcAction);
		RpcResult rpcResult = invoke(rpcAction, rpcEntity.getClass().getSimpleName());
		RpcResult rpcResultCopy = rpcResult.clone();

		converToApiFields(rpcResultCopy.getRpcFields());
		populateEntity(rpcEntity, rpcDefinition, rpcResultCopy);

		return rpcEntity;

	}

	@Override
	public RpcEntity doAction(final RpcAction action, final RpcEntity rpcEntity) {
		CacheModule cacheModule = getModule(CacheModule.class);

		if (cacheModule != null) {
			return (RpcEntity) cacheModule.doStuff(action.getClass(), rpcEntity.getClass(), new ObtainEntityCallback() {

				@Override
				public Object obtainEntity() {
					return doRpcAction(action, rpcEntity);
				}

				@Override
				public List<Object> getEntityKeys() {

					Class<? extends RpcEntity> entityClass = rpcEntity.getClass();

					RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(entityClass);
					List<? extends FieldDefinition> keysDefinitions = rpcDefinition.getKeys();

					List<Object> entityKeys = new ArrayList<Object>();
					HierarchyRpcPojoFieldAccessor fieldAccesor = new SimpleHierarchyRpcPojoFieldAccessor(rpcEntity);
					for (FieldDefinition fieldDefinition : keysDefinitions) {
						RpcPojoFieldAccessor f = fieldAccesor.getPartAccessor(fieldDefinition.getName());
						entityKeys.add(f.getFieldValue(StringUtil.removeNamespace(fieldDefinition.getName())));
					}
					return entityKeys;
				}
			});
		} else {
			return doRpcAction(action, rpcEntity);
		}
	}

}
