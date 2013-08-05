package org.openlegacy.rpc.support.binders;

import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;

import java.util.Collection;

import javax.inject.Inject;

public class RpcPartsBinder implements RpcEntityBinder {

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	public void populateEntity(Object entity, RpcResult result) {
		// TODO implement
	}

	public void populateAction(RpcInvokeAction sendAction, Object entity) {
		SimpleRpcPojoFieldAccessor fieldAccesor = new SimpleRpcPojoFieldAccessor(entity);

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(entity.getClass());

		Collection<PartEntityDefinition<RpcFieldDefinition>> partsDefinitions = rpcEntityDefinition.getPartsDefinitions().values();
		for (PartEntityDefinition<RpcFieldDefinition> rpcPartDefinition : partsDefinitions) {
			SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();

			Collection<RpcFieldDefinition> fieldsDefinitions = rpcPartDefinition.getFieldsDefinitions().values();
			for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
				RpcFlatField rpcFlatField = RpcFieldsBinder.getRpcFlatField(rpcFieldDefinition, fieldAccesor,
						rpcPartDefinition.getPartName());
				rpcStructureField.getChildren().add(rpcFlatField);
			}
			sendAction.getFields().add(rpcStructureField);

		}
	}
}
