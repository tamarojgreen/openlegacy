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
package org.openlegacy.rpc.loaders.support;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.definitions.support.SimplePasswordFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcListFieldTypeDefinition;
import org.openlegacy.rpc.definitions.support.SimpleRpcDateFieldTypeDefinition;
import org.openlegacy.rpc.definitions.support.SimpleRpcNumericFieldTypeDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RpcFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		RpcField fieldAnnotation = (RpcField)annotation;

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(containingClass);

		String fieldName = field.getName();
		SimpleRpcFieldDefinition rpcFieldDefinition = new SimpleRpcFieldDefinition(fieldName, fieldAnnotation.fieldType());

		if (fieldAnnotation.order() == AnnotationConstants.AUTOMATICALY) {
			rpcFieldDefinition.setOrder(fieldOrder);
		} else {
			rpcFieldDefinition.setOrder(fieldAnnotation.order());
		}
		if (fieldAnnotation.displayName().equals(AnnotationConstants.NULL)) {
			rpcFieldDefinition.setDisplayName(StringUtil.toDisplayName(fieldName));
		} else {
			rpcFieldDefinition.setDisplayName(fieldAnnotation.displayName());
		}

		rpcFieldDefinition.setKeyIndex(fieldAnnotation.keyIndex());
		if (fieldAnnotation.length() % 1 > 0 && field.getType() == String.class) {
			throw (new RegistryException(MessageFormat.format(
					"Length with floating point cannot be set for String field {0}.{1}", containingClass.getSimpleName(),
					field.getName())));
		}
		rpcFieldDefinition.setLength(fieldAnnotation.length());
		rpcFieldDefinition.setDirection(fieldAnnotation.direction());
		if (fieldAnnotation.originalName().length() > 0) {
			rpcFieldDefinition.setOriginalName(fieldAnnotation.originalName());
		} else {
			rpcFieldDefinition.setOriginalName(field.getName());
		}
		rpcFieldDefinition.setKey(fieldAnnotation.key());
		rpcFieldDefinition.setSampleValue(fieldAnnotation.sampleValue());
		rpcFieldDefinition.setJavaType(field.getType());
		rpcFieldDefinition.setEditable(fieldAnnotation.editable());
		rpcFieldDefinition.setDefaultValue(fieldAnnotation.defaultValue());

		if (fieldAnnotation.helpText().length() > 0) {
			rpcFieldDefinition.setHelpText(fieldAnnotation.helpText());
		}
		setupFieldType(field, rpcFieldDefinition);
		if (!fieldAnnotation.nullValue().equals(AnnotationConstants.NULL)) {
			rpcFieldDefinition.setNullValue(fieldAnnotation.nullValue());
		}

		if (rpcEntityDefinition != null) {
			rpcEntityDefinition.getFieldsDefinitions().put(fieldName, rpcFieldDefinition);
		} else {
			// look in RPC entities parts
			RpcPartEntityDefinition rpcPart = rpcEntitiesRegistry.getPart(containingClass);
			if (rpcPart != null) {
				rpcFieldDefinition.setName(MessageFormat.format("{0}.{1}", rpcPart.getPartFullName(), fieldName));
				fieldName = MessageFormat.format("{0}.{1}", rpcPart.getPartName(), fieldName);
				rpcFieldDefinition.setShortName(fieldName);

				rpcPart.getFieldsDefinitions().put(fieldName, rpcFieldDefinition);
				if (fieldAnnotation.key() == true) {
					rpcPart.getKeys().add(rpcFieldDefinition);
				}
			}

		}
		rpcFieldDefinition.setExpression(fieldAnnotation.expression());
	}

	private static void setupFieldType(Field field, SimpleRpcFieldDefinition rpcFieldDefinition) {
		// set number type definition - may be overridden by ScreenNumericFieldAnnotationLoader to fill in specific numeric
		// properties

		rpcFieldDefinition.setNullValue("");
		if (Number.class.isAssignableFrom(field.getType())) {

			rpcFieldDefinition.setNullValue(StringUtils.repeat("0", "", rpcFieldDefinition.getLength()));
			rpcFieldDefinition.setFieldTypeDefinition(new SimpleRpcNumericFieldTypeDefinition());
		}
		// set date type definition - may be overridden by ScreenDateFieldAnnotationLoader to fill in specific date properties
		else if (Date.class.isAssignableFrom(field.getType())) {
			rpcFieldDefinition.setNullValue(AnnotationConstants.NULL);
			rpcFieldDefinition.setFieldTypeDefinition(new SimpleRpcDateFieldTypeDefinition());
		} else if (rpcFieldDefinition.isPassword()) {
			rpcFieldDefinition.setFieldTypeDefinition(new SimplePasswordFieldTypeDefinition());
		} else if (java.util.List.class == field.getType()) {
			rpcFieldDefinition.setFieldTypeDefinition(new SimpleRpcListFieldTypeDefinition());
		} else if (field.getType().isEnum()) {
			rpcFieldDefinition.setFieldTypeDefinition(new SimpleEnumFieldTypeDefinition());
		} else {
			rpcFieldDefinition.setFieldTypeDefinition(new SimpleTextFieldTypeDefinition());
		}
	}

}
