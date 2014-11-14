package org.openlegacy.rpc.support.binders;

import org.openlegacy.definitions.BooleanFieldTypeDefinition;
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

public class RpcBooleanFieldsBinder implements RpcEntityBinder, Serializable {

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
			if (!(rpcFieldDefinition.getFieldTypeDefinition() instanceof BooleanFieldTypeDefinition)) {
				continue;
			}
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
			}

			RpcFlatField booleanField = (RpcFlatField)rpcFields.get(rpcFieldDefinition.getOrder());

			Boolean apiValue = (Boolean)toApi(rpcFieldDefinition, booleanField.getValue());
			if (apiValue != null) {
				fieldAccessor.setFieldValue(rpcFieldDefinition.getShortName(), apiValue);

			}
		}

	}

	@Override
	public boolean fieldMatch(RpcFieldDefinition rpcFieldDefinition) {
		if (rpcFieldDefinition.getJavaType() != Boolean.class) {
			return false;
		}
		return true;
	}

	@Override
	public void populateAction(RpcInvokeAction sendAction, Object entity) {

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

			RpcFlatField booleanField = RpcFieldsBinder.getRpcFlatField(rpcFieldDefinition, fieldAccessor, null);
			Object fieldApiValue = fieldAccessor.evaluateFieldValue(rpcFieldDefinition.getShortName());
			Object fieldlegacyValue = toLegacy(rpcFieldDefinition, fieldApiValue, booleanField);
			booleanField.setValue(fieldlegacyValue);
			sendAction.getFields().add(booleanField);
		}
	}

	@Override
	public Object toLegacy(RpcFieldDefinition rpcFieldDefinition, Object apiFieldValue, RpcFlatField booleanField) {
		Object legacyFieldValue = null;
		BooleanFieldTypeDefinition fieldTypeDefinition = (BooleanFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
		Assert.notNull(fieldTypeDefinition, "A field of type Boolean is defined without @RpcBooleanField annotation");

		if (Boolean.TRUE.equals(apiFieldValue)) {
			if (!booleanField.getValue().equals(fieldTypeDefinition.getTrueValue())) {
				legacyFieldValue = fieldTypeDefinition.getTrueValue();
			}
		} else {
			if (apiFieldValue == null && fieldTypeDefinition.isTreatNullAsEmpty()) {
				legacyFieldValue = "";
			} else {
				if (!booleanField.getValue().equals(fieldTypeDefinition.getFalseValue())) {
					legacyFieldValue = fieldTypeDefinition.getFalseValue();
				}
			}
		}
		return legacyFieldValue;
	}

	@Override
	public Object toApi(RpcFieldDefinition rpcFieldDefinition, Object fiieldValue) {

		BooleanFieldTypeDefinition fieldTypeDefinition = (BooleanFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
		Assert.notNull(fieldTypeDefinition, "A field of type Boolean is defined without @RpcBooleanField annotation");

		if (fiieldValue != null) {
			if (fiieldValue.equals(fieldTypeDefinition.getTrueValue())) {
				return Boolean.TRUE;
			}
			if (fiieldValue.equals(fieldTypeDefinition.getFalseValue())) {
				return Boolean.FALSE;
			}
		}
		return null;
	}
}
