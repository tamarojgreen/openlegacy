package org.openlegacy.rpc.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.definitions.support.SimpleBooleanFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

@Component
public class RpcBooleanFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcBooleanField.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {

		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;
		RpcBooleanField fieldAnnotation = (RpcBooleanField)annotation;
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

	private static void fillTypeDefinition(RpcBooleanField fieldAnnotation, SimpleRpcFieldDefinition fieldDefinition,
			String fieldName) {

		Assert.notNull(fieldDefinition, MessageFormat.format(
				"Field definition for field {0} not found. Verify @RpcBooleanField is defined along @RpcField annotation",
				fieldName));

		if (fieldDefinition.getJavaType() != Boolean.class) {
			throw (new RegistryException(MessageFormat.format(
					"Field {0} marked with @RpcBooleanField must be of type java.lang.Boolean", fieldName)));
		}

		fieldDefinition.setFieldTypeDefinition(new SimpleBooleanFieldTypeDefinition(fieldAnnotation.trueValue(),
				fieldAnnotation.falseValue(), fieldAnnotation.treatEmptyAsNull()));
	}
}
