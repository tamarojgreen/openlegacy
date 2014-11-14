package org.openlegacy.rpc.support.binders;

import org.openlegacy.definitions.EnumFieldTypeDefinition;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcPojoFieldAccessor;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class RpcEnumFieldsBinder implements RpcEntityBinder, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Override
	public void populateEntity(Object rpcEntity, RpcResult result) {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());
		Collection<RpcFieldDefinition> fieldsDefinitions = rpcDefinition.getFieldsDefinitions().values();

		RpcPojoFieldAccessor fieldAccessor = null;
		List<RpcField> rpcFields = result.getRpcFields();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
			if (!(fieldMatch(rpcFieldDefinition))) {
				continue;
			}
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
			}

			RpcFlatField dateField = (RpcFlatField)rpcFields.get(rpcFieldDefinition.getOrder());
			if (dateField != null) {
				Object apiFieldValue = toApi(rpcFieldDefinition, dateField.getValue());
				fieldAccessor.setFieldValue(rpcFieldDefinition.getShortName(), apiFieldValue);
			}
		}

	}

	@Override
	public void populateAction(RpcInvokeAction remoteAction, Object entity) {
		SimpleRpcPojoFieldAccessor fieldAccessor = null;

		Assert.isTrue(entity instanceof RpcEntity, "rpc entity must implement RpcEntity interface");
		RpcEntity rpcEntity = (RpcEntity)entity;
		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());

		Collection<RpcFieldDefinition> fieldsDefinitions = rpcEntityDefinition.getFieldsDefinitions().values();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {

			if (!fieldMatch(rpcFieldDefinition)) {
				continue;
			}
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
			}

			Object enumApiFieldValue = fieldAccessor.evaluateFieldValue(rpcFieldDefinition.getShortName());
			RpcFlatField enumField = RpcFieldsBinder.getRpcFlatField(rpcFieldDefinition, fieldAccessor, null);
			if (enumApiFieldValue != null) {
				enumField.setValue(toLegacy(rpcFieldDefinition, enumApiFieldValue, enumField));
			}

			remoteAction.getFields().add(enumField);
		}
	}

	@Override
	public boolean fieldMatch(RpcFieldDefinition rpcFieldDefinition) {
		if (rpcFieldDefinition.getJavaType().isEnum()) {
			return true;
		}
		return false;
	}

	@Override
	public Object toApi(RpcFieldDefinition rpcFieldDefinition, Object fieldValue) {

		EnumFieldTypeDefinition fieldTypeDefinition = (EnumFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
		Collection<Object> enums = fieldTypeDefinition.getDisplayValues().keySet();

		for (Object enumValue : enums) {
			{

				if (enumValue.toString().equals(fieldValue)) {
					return enumValue;
				}
			}
		}
		return null;
	}

	@Override
	public Object toLegacy(RpcFieldDefinition rpcFieldDefinition, Object apiFieldValue, RpcFlatField rpcFlatField) {
		EnumFieldTypeDefinition enumFieldTypeDefinition = (EnumFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
		EnumGetValue enumGetValue = (EnumGetValue)apiFieldValue;
		return enumFieldTypeDefinition.getEnums().get(enumGetValue.getValue());
	}
}
