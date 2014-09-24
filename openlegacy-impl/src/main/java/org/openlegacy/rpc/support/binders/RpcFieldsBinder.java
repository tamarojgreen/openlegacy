/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
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
import org.openlegacy.utils.StringUtil;

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

		List<RpcField> rpcFields = result.getRpcFields();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {

			if (!fieldMatch(rpcFieldDefinition)) {
				continue;
			}
			fieldAccesor.setFieldValue(rpcFieldDefinition.getShortName(),
					((RpcFlatField)rpcFields.get(rpcFieldDefinition.getOrder())).getValue());
		}

	}

	public void populateAction(RpcInvokeAction sendAction, Object entity) {
		SimpleRpcPojoFieldAccessor fieldAccesor = new SimpleRpcPojoFieldAccessor(entity);

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(entity.getClass());

		Collection<RpcFieldDefinition> fieldsDefinitions = rpcEntityDefinition.getFieldsDefinitions().values();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {

			if (!fieldMatch(rpcFieldDefinition)) {
				continue;
			}

			RpcFlatField rpcField = getRpcFlatField(rpcFieldDefinition, fieldAccesor, null);
			sendAction.getFields().add(rpcField);
		}

	}

	public static RpcFlatField getRpcFlatField(RpcFieldDefinition rpcFieldDefinition, PojoFieldAccessor fieldAccesor,
			String fieldPrefix) {
		if (fieldPrefix == null) {
			fieldPrefix = "";
		}
		if (fieldPrefix.length() > 0) {
			fieldPrefix += ".";
		}
		Object value = fieldAccesor.evaluateFieldValue(fieldPrefix + StringUtil.removeNamespace(rpcFieldDefinition.getName()));
		SimpleRpcFlatField rpcField = new SimpleRpcFlatField();
		if (value != null) {
			rpcField.setValue(value);
		} else {
			rpcField.setDefaultValue(rpcFieldDefinition.getDefaultValue(), rpcFieldDefinition.getJavaType());

		}

		rpcField.setName(rpcFieldDefinition.getOriginalName());

		rpcField.setLength(rpcFieldDefinition.getLength());
		rpcField.setDecimalPlaces(rpcFieldDefinition.getDecimalPlaces());
		rpcField.setDirection(rpcFieldDefinition.getDirection());
		rpcField.setOrder(rpcFieldDefinition.getOrder());
		return rpcField;
	}

	public boolean fieldMatch(RpcFieldDefinition rpcFieldDefinition) {
		if (rpcFieldDefinition.getJavaType() == Boolean.class || rpcFieldDefinition.getJavaType() == java.util.Date.class
				|| rpcFieldDefinition.getJavaType().isEnum()) {
			return false;
		}
		if (rpcFieldDefinition.getDirection() == Direction.NONE) {
			return false;
		}
		return true;
	}

	public Object toApi(RpcFieldDefinition rpcFieldDefinition, Object legacyFieldValue) {
		// no conversion
		return legacyFieldValue;
	}

	public Object toLegacy(RpcFieldDefinition rpcFieldDefinition, Object apiFieldValue, RpcFlatField booleanField) {
		// no conversion
		return apiFieldValue;
	}

}
