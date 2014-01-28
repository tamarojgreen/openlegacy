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

import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.beans.DirectFieldAccessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class RpcPartsBinder implements RpcEntityBinder {

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	public void populateEntity(Object entity, RpcResult result) {

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(entity.getClass());
		SimpleRpcPojoFieldAccessor fieldAccesor = new SimpleRpcPojoFieldAccessor(entity);
		List<RpcField> rpcFields = result.getRpcFields();
		Collection<PartEntityDefinition<RpcFieldDefinition>> partsDefinitions = rpcEntityDefinition.getPartsDefinitions().values();

		for (PartEntityDefinition<RpcFieldDefinition> rpcPartDefinition : partsDefinitions) {
			populateEntityDeep(rpcPartDefinition.getPartName(), (RpcPartEntityDefinition)rpcPartDefinition, fieldAccesor,
					rpcFields);

		}
	}

	private static void populateEntityDeep(String namespace, RpcPartEntityDefinition rpcPartEntityDefinition,
			SimpleRpcPojoFieldAccessor fieldAccesor, List<RpcField> rpcFields) {
		Map<String, RpcFieldDefinition> fieldsDefinitions = rpcPartEntityDefinition.getFieldsDefinitions();

		DirectFieldAccessor partAccesor = fieldAccesor.getPartAccessor(namespace);

		int order = rpcPartEntityDefinition.getOrder();
		int count = rpcPartEntityDefinition.getCount();
		if (count == 1) {
			RpcStructureField structureField = (RpcStructureField)rpcFields.get(order);
			List<RpcField> rpcInnerFields = structureField.getChildren();
			for (RpcFieldDefinition fieldDefinition : fieldsDefinitions.values()) {
				int inerOrder = fieldDefinition.getOrder();
				String name = StringUtil.removeNamespace(fieldDefinition.getName());
				Object value = ((RpcFlatField)rpcInnerFields.get(inerOrder)).getValue();
				partAccesor.setPropertyValue(name, value);
			}
			Map<String, RpcPartEntityDefinition> innerPartDefinition = rpcPartEntityDefinition.getInnerPartsDefinitions();

			for (RpcPartEntityDefinition innerRpcPartEntityDefinition : innerPartDefinition.values()) {
				populateEntityDeep(namespace + "." + innerRpcPartEntityDefinition.getPartName(), innerRpcPartEntityDefinition,
						fieldAccesor, rpcInnerFields);
			}
		} else {
			RpcStructureListField structureListField = (RpcStructureListField)rpcFields.get(order);
			List<List<RpcField>> rpcInnerFields = structureListField.getChildren();
			Object[] objects;
			Object currentObject = fieldAccesor.getPartFieldValue(namespace, rpcPartEntityDefinition.getPartName());
			if (currentObject.getClass().isArray()) {
				objects = (Object[])currentObject;
			} else {
				objects = ((List<?>)currentObject).toArray();
			}

			for (int i = 0; i < count; i++) {
				List<RpcField> rpcCurrentFields = rpcInnerFields.get(i);
				DirectFieldAccessor innerFieldAccessor = new DirectFieldAccessor(objects[i]);
				for (RpcFieldDefinition fieldDefinition : fieldsDefinitions.values()) {
					int inerOrder = fieldDefinition.getOrder();
					String name = StringUtil.removeNamespace(fieldDefinition.getName());
					Object value = ((RpcFlatField)rpcCurrentFields.get(inerOrder)).getValue();
					innerFieldAccessor.setPropertyValue(name, value);
				}
				Map<String, RpcPartEntityDefinition> innerPartDefinition = rpcPartEntityDefinition.getInnerPartsDefinitions();

				for (RpcPartEntityDefinition innerRpcPartEntityDefinition : innerPartDefinition.values()) {
					populateEntityDeep(namespace + "." + innerRpcPartEntityDefinition.getPartName(),
							innerRpcPartEntityDefinition, new SimpleRpcPojoFieldAccessor(objects[i]), rpcCurrentFields);
				}

			}
		}

	}

	public void populateAction(RpcInvokeAction sendAction, Object entity) {

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(entity.getClass());

		SimpleRpcPojoFieldAccessor fieldAccesor = new SimpleRpcPojoFieldAccessor(entity);
		Collection<PartEntityDefinition<RpcFieldDefinition>> partsDefinitions = rpcEntityDefinition.getPartsDefinitions().values();
		for (PartEntityDefinition<RpcFieldDefinition> rpcPartDefinition : partsDefinitions) {

			RpcField field = populateActionDeep(rpcPartDefinition.getPartName(), (RpcPartEntityDefinition)rpcPartDefinition,
					fieldAccesor);

			sendAction.getFields().add(field);
		}
	}

	private static RpcField populateActionDeep(String fullName, RpcPartEntityDefinition rpcPartEntityDefinition,
			SimpleRpcPojoFieldAccessor fieldAccesor) {
		RpcField result;
		int count = rpcPartEntityDefinition.getCount();
		if (count == 1) {
			SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
			rpcStructureField.setName(rpcPartEntityDefinition.getPartName());
			rpcStructureField.setOrder(rpcPartEntityDefinition.getOrder());
			Object curentObject = fieldAccesor.getPartFieldValue(fullName, rpcPartEntityDefinition.getPartName());
			if (curentObject == null) {
				curentObject = ReflectionUtil.newInstance(rpcPartEntityDefinition.getPartClass());
				fieldAccesor.setPartFieldValue(fullName, rpcPartEntityDefinition.getPartName(), curentObject);
			}

			List<RpcField> fieldsInLevel = populateFields(fullName, rpcPartEntityDefinition, fieldAccesor);
			rpcStructureField.getChildren().addAll(fieldsInLevel);
			result = rpcStructureField;
		} else {
			SimpleRpcStructureListField rpcStructureListField = new SimpleRpcStructureListField();
			rpcStructureListField.setName(rpcPartEntityDefinition.getPartName());
			rpcStructureListField.setOrder(rpcPartEntityDefinition.getOrder());
			result = rpcStructureListField;
			Object currentObject = fieldAccesor.getPartFieldValue(fullName, rpcPartEntityDefinition.getPartName());
			if (currentObject == null) {
				currentObject = ReflectionUtil.newListInstance(rpcPartEntityDefinition.getPartClass(), count);
				fieldAccesor.setPartFieldValue(fullName, rpcPartEntityDefinition.getPartName(), currentObject);
			}
			Object[] objects;
			if (currentObject.getClass().isArray()) {
				objects = (Object[])currentObject;
			} else {
				objects = ((List<?>)currentObject).toArray();
			}

			for (int i = 0; i < count; i++) {
				SimpleRpcPojoFieldAccessor innerAccessor = new SimpleRpcPojoFieldAccessor(objects[i]);
				List<RpcField> fieldsInLevel = populateFields("", rpcPartEntityDefinition, innerAccessor);
				rpcStructureListField.getChildren().add(fieldsInLevel);
			}

		}
		return result;
	}

	private static List<RpcField> populateFields(String fullName, RpcPartEntityDefinition rpcPartEntityDefinition,
			SimpleRpcPojoFieldAccessor fieldAccesor) {
		List<RpcField> fieldsInLevel = new ArrayList<RpcField>();

		Collection<RpcFieldDefinition> fieldsDefinitions = rpcPartEntityDefinition.getFieldsDefinitions().values();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
			RpcFlatField rpcFlatField = RpcFieldsBinder.getRpcFlatField(rpcFieldDefinition, fieldAccesor, fullName);
			fieldsInLevel.add(rpcFlatField);
		}
		Collection<RpcPartEntityDefinition> innerParts = rpcPartEntityDefinition.getInnerPartsDefinitions().values();
		for (RpcPartEntityDefinition innerPart : innerParts) {
			fieldsInLevel.add(populateActionDeep(fullName + "." + innerPart.getPartName(), innerPart, fieldAccesor));
		}

		Collections.sort(fieldsInLevel, new Comparator<RpcField>() {

			public int compare(RpcField arg0, RpcField arg1) {
				return arg0.getOrder() - arg1.getOrder();
			}
		});
		return fieldsInLevel;
	}
}
