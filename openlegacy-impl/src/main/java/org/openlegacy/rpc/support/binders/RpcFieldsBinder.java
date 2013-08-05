package org.openlegacy.rpc.support.binders;

import org.openlegacy.PojoFieldAccessor;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class RpcFieldsBinder implements RpcEntityBinder {

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	public void populateEntity(Object entity, RpcResult result) {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(entity.getClass());

		SimpleRpcPojoFieldAccessor fieldAccesor = new SimpleRpcPojoFieldAccessor(entity);

		Collection<RpcFieldDefinition> fieldsDefinitions = rpcDefinition.getFieldsDefinitions().values();
		int index = 0;
		List<RpcField> rpcFields = result.getRpcFields();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
			if (index >= rpcFields.size()) {
				break;
			}
			fieldAccesor.setFieldValue(rpcFieldDefinition.getName(), ((RpcFlatField)rpcFields.get(index)).getValue());
			index++;
		}

	}

	public void populateAction(RpcInvokeAction sendAction, Object entity) {
		SimpleRpcPojoFieldAccessor fieldAccesor = new SimpleRpcPojoFieldAccessor(entity);

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(entity.getClass());

		Collection<RpcFieldDefinition> fieldsDefinitions = rpcEntityDefinition.getFieldsDefinitions().values();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
			if (rpcFieldDefinition.getDirection() == Direction.OUTPUT) {
				continue;
			}
			SimpleRpcFlatField rpcField = getRpcFlatField(rpcFieldDefinition, fieldAccesor, null);
			sendAction.getFields().add(rpcField);
		}

	}

	public static SimpleRpcFlatField getRpcFlatField(RpcFieldDefinition rpcFieldDefinition, PojoFieldAccessor fieldAccesor,
			String fieldPrefix) {
		if (fieldPrefix == null) {
			fieldPrefix = "";
		}
		if (fieldPrefix.length() > 0) {
			fieldPrefix += ".";
		}
		Object value = fieldAccesor.evaluateFieldValue(fieldPrefix + rpcFieldDefinition.getName());
		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		if (value != null) {
			rpcField.setValue(value);
		} else {
			rpcField.setValue(rpcFieldDefinition.getDefaultValue());
		}

		rpcField.setName(rpcFieldDefinition.getOriginalName());

		rpcField.setLength(rpcFieldDefinition.getLength());
		rpcField.setDecimalPlaces(rpcFieldDefinition.getDecimalPlaces());
		rpcField.setDirection(rpcFieldDefinition.getDirection());
		rpcField.setOrder(rpcFieldDefinition.getOrder());
		return rpcField;
	}

}
