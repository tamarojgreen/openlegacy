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

import org.apache.commons.lang.ArrayUtils;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFields;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.support.AbstractRpcStructure;
import org.openlegacy.rpc.support.SimpleRpcFields;
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

	@Inject
	private List<RpcEntityBinder> rpcEntityBinders;

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

	private void populateEntityDeep(String namespace, RpcPartEntityDefinition rpcPartEntityDefinition,
			SimpleRpcPojoFieldAccessor fieldAccesor, List<RpcField> rpcFields) {
		Map<String, RpcFieldDefinition> fieldsDefinitions = rpcPartEntityDefinition.getFieldsDefinitions();

		DirectFieldAccessor partAccesor = fieldAccesor.getPartAccessor(namespace);

		int order = rpcPartEntityDefinition.getOrder();
		int count = rpcPartEntityDefinition.getCount();
		if (count == 1) {
			if (partAccesor == null) {
				fieldAccesor.setPartFieldValue(namespace, rpcPartEntityDefinition.getPartName(),
						ReflectionUtil.newInstance(rpcPartEntityDefinition.getPartClass()));
				partAccesor = fieldAccesor.getPartAccessor(namespace);
			}
			RpcStructureField structureField = (RpcStructureField)rpcFields.get(order);
			List<RpcField> rpcInnerFields = structureField.getChildrens();
			for (RpcFieldDefinition fieldDefinition : fieldsDefinitions.values()) {
				int inerOrder = structureField.getFieldRelativeOrder(fieldDefinition.getOrder());

				String name = StringUtil.removeNamespace(fieldDefinition.getName());
				Object value = ((RpcFlatField)rpcInnerFields.get(inerOrder)).getValue();
				if (value != null) {
					Object apiValue = bindFieldToApi(fieldDefinition, value);
					partAccesor.setPropertyValue(name, apiValue);
				}
			}
			Map<String, RpcPartEntityDefinition> innerPartDefinition = rpcPartEntityDefinition.getInnerPartsDefinitions();

			for (RpcPartEntityDefinition innerRpcPartEntityDefinition : innerPartDefinition.values()) {
				populateEntityDeep(namespace + "." + innerRpcPartEntityDefinition.getPartName(), innerRpcPartEntityDefinition,
						fieldAccesor, rpcInnerFields);
			}
		} else {
			if (partAccesor == null) {
				fieldAccesor.setPartFieldValue(namespace, rpcPartEntityDefinition.getPartName(),
						ReflectionUtil.newListInstance(rpcPartEntityDefinition.getPartClass(), count));
				partAccesor = fieldAccesor.getPartAccessor(namespace);
			}
			RpcStructureListField structureListField = (RpcStructureListField)rpcFields.get(order);

			List<RpcFields> rpcInnerFields = structureListField.getChildrens();

			Object[] objects;
			Object currentObject = fieldAccesor.getPartFieldValue(namespace, rpcPartEntityDefinition.getPartName());
			if (currentObject.getClass().isArray()) {
				objects = (Object[])currentObject;
			} else {
				objects = ((List<?>)currentObject).toArray();
			}

			List<Integer> nullObjects = new ArrayList<Integer>();
			for (Integer i = 0; i < count; i++) {
				List<RpcField> rpcCurrentFields = rpcInnerFields.get(i).getFields();
				DirectFieldAccessor innerFieldAccessor = new DirectFieldAccessor(objects[i]);
				for (RpcFieldDefinition fieldDefinition : fieldsDefinitions.values()) {
					int inerOrder = fieldDefinition.getOrder();
					String name = StringUtil.removeNamespace(fieldDefinition.getName());
					Object value = ((RpcFlatField)rpcCurrentFields.get(inerOrder)).getValue();
					if (fieldDefinition.isKey() && fieldDefinition.getNullValue().equals(value)) {
						nullObjects.add(i);
						continue;
					}

					innerFieldAccessor.setPropertyValue(name, bindFieldToApi(fieldDefinition, value));
				}
				Map<String, RpcPartEntityDefinition> innerPartDefinition = rpcPartEntityDefinition.getInnerPartsDefinitions();

				for (RpcPartEntityDefinition innerRpcPartEntityDefinition : innerPartDefinition.values()) {
					populateEntityDeep(namespace + "." + innerRpcPartEntityDefinition.getPartName(),
							innerRpcPartEntityDefinition, new SimpleRpcPojoFieldAccessor(objects[i]), rpcCurrentFields);
				}
			}
			filterNullObjects(namespace, rpcPartEntityDefinition.getPartName(), fieldAccesor, objects, nullObjects);

		}

	}

	private static void filterNullObjects(String namespace, String partName, SimpleRpcPojoFieldAccessor fieldAccesor,
			Object[] objects, List<Integer> emptyObjectsIndex) {

		Collections.sort(emptyObjectsIndex, new Comparator<Integer>() {

			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
		for (Integer index : emptyObjectsIndex) {
			objects = ArrayUtils.remove(objects, index);
		}
		fieldAccesor.setPartFieldValue(namespace, partName, objects);
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

	private RpcField populateActionDeep(String fullName, RpcPartEntityDefinition rpcPartEntityDefinition,
			SimpleRpcPojoFieldAccessor fieldAccesor) {
		AbstractRpcStructure result;
		int count = rpcPartEntityDefinition.getCount();
		if (count == 1) {
			SimpleRpcStructureField rpcStructureField = new SimpleRpcStructureField();
			rpcStructureField.setName(rpcPartEntityDefinition.getPartName());
			rpcStructureField.setVirtual(rpcPartEntityDefinition.isVirtual());
			Object curentObject = fieldAccesor.getPartFieldValue(fullName, rpcPartEntityDefinition.getPartName());
			if (curentObject == null) {
				curentObject = ReflectionUtil.newInstance(rpcPartEntityDefinition.getPartClass());
				fieldAccesor.setPartFieldValue(fullName, rpcPartEntityDefinition.getPartName(), curentObject);
			}

			RpcFields fieldsInLevel = populateFields(fullName, rpcPartEntityDefinition, fieldAccesor);
			rpcStructureField.getChildrens().addAll(fieldsInLevel.getFields());
			result = rpcStructureField;
		} else {
			SimpleRpcStructureListField rpcStructureListField = new SimpleRpcStructureListField();
			rpcStructureListField.setName(rpcPartEntityDefinition.getPartName());

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
				RpcFields fieldsInLevel = populateFields("", rpcPartEntityDefinition, innerAccessor);
				rpcStructureListField.getChildrens().add(fieldsInLevel);
			}

		}

		result.setOrder(rpcPartEntityDefinition.getOrder());
		result.setLegacyContainerName(rpcPartEntityDefinition.getLegacyContainerName());

		return (RpcField)result;
	}

	private RpcFields populateFields(String fullName, RpcPartEntityDefinition rpcPartEntityDefinition,
			SimpleRpcPojoFieldAccessor fieldAccesor) {
		RpcFields fieldsInLevel = new SimpleRpcFields();

		Collection<RpcFieldDefinition> fieldsDefinitions = rpcPartEntityDefinition.getFieldsDefinitions().values();
		for (RpcFieldDefinition rpcFieldDefinition : fieldsDefinitions) {
			RpcFlatField rpcFlatField = RpcFieldsBinder.getRpcFlatField(rpcFieldDefinition, fieldAccesor, fullName);
			Object legacyFeldValue = bindFieldToLegacy(rpcFieldDefinition, rpcFlatField, rpcFlatField.getValue());
			rpcFlatField.setValue(legacyFeldValue);
			fieldsInLevel.add(rpcFlatField);
		}
		Collection<RpcPartEntityDefinition> innerParts = rpcPartEntityDefinition.getInnerPartsDefinitions().values();
		for (RpcPartEntityDefinition innerPart : innerParts) {
			fieldsInLevel.add(populateActionDeep(fullName + "." + innerPart.getPartName(), innerPart, fieldAccesor));
		}

		fieldsInLevel.sort();
		return fieldsInLevel;
	}

	public boolean fieldMatch(RpcFieldDefinition rpcFieldDefinition) {
		// only flat fields
		return false;
	}

	public Object bindFieldToLegacy(RpcFieldDefinition rpcFieldDefinition, RpcFlatField rpcFlatField, Object fieldValue) {
		for (RpcEntityBinder rpcEntityBinder : rpcEntityBinders) {
			if (rpcEntityBinder.fieldMatch(rpcFieldDefinition)) {
				return rpcEntityBinder.toLegacy(rpcFieldDefinition, fieldValue, rpcFlatField);

			}
		}
		return null;
	}

	public Object bindFieldToApi(RpcFieldDefinition rpcFieldDefinition, Object fieldValue) {
		for (RpcEntityBinder rpcEntityBinder : rpcEntityBinders) {
			if (rpcEntityBinder.fieldMatch(rpcFieldDefinition)) {
				return rpcEntityBinder.toApi(rpcFieldDefinition, fieldValue);

			}
		}
		return null;
	}

	public Object toApi(RpcFieldDefinition rpcFieldDefinition, Object fiieldValue) {

		throw (new IllegalStateException("Should not arrive"));
	}

	public Object toLegacy(RpcFieldDefinition rpcFieldDefinition, Object apiFieldValue, RpcFlatField booleanField) {
		throw (new IllegalStateException("Should not arrive"));
	}

}
