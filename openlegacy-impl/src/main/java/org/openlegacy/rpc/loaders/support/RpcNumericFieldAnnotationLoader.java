package org.openlegacy.rpc.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.definitions.support.SimpleRpcNumericFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcListFieldTypeDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

@Component
public class RpcNumericFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcNumericField.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		RpcNumericField fieldAnnotation = (RpcNumericField)annotation;

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(containingClass);
		String fieldName = field.getName();

		// look in rpc entities
		if (rpcEntityDefinition != null) {
			SimpleRpcFieldDefinition fieldDefinition = (SimpleRpcFieldDefinition)rpcEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			fillTypeDefinition(fieldAnnotation, fieldDefinition, fieldName);
		} else {
			// look in rpc entities parts
			RpcPartEntityDefinition rpcPart = rpcEntitiesRegistry.getPart(containingClass);
			if (rpcPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", rpcPart.getPartName(), fieldName);
				SimpleRpcFieldDefinition fieldDefinition = (SimpleRpcFieldDefinition)rpcPart.getFieldsDefinitions().get(fieldName);
				fillTypeDefinition(fieldAnnotation, fieldDefinition, fieldName);
			}

		}
	}

	private static void fillTypeDefinition(RpcNumericField fieldAnnotation, SimpleRpcFieldDefinition fieldDefinition,
			String fieldName) {
		Assert.notNull(fieldDefinition, MessageFormat.format(
				"Field definition for field {0} not found. Verify @RpcNumericField is defined along @RpcField annotation",
				fieldName));

		if (!Number.class.isAssignableFrom(fieldDefinition.getJavaType()) && fieldDefinition.getJavaType() != List.class) {
			throw (new RegistryException("A field marked with @RpcNumericField must be of numeric or List of numeric"));
		}
		SimpleRpcNumericFieldTypeDefinition simpleRpcNumericFieldTypeDefinition = new SimpleRpcNumericFieldTypeDefinition(
				fieldAnnotation.minimumValue(), fieldAnnotation.maximumValue(), fieldAnnotation.decimalPlaces());
		if (fieldDefinition.getJavaType() != List.class) {
			fieldDefinition.setFieldTypeDefinition(simpleRpcNumericFieldTypeDefinition);
		} else {
			SimpleRpcListFieldTypeDefinition simpleRpcListFieldTypeDefinition = (SimpleRpcListFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			simpleRpcListFieldTypeDefinition.setItemTypeDefinition(simpleRpcNumericFieldTypeDefinition);

		}
	}

}
