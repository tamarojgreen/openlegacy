package org.openlegacy.rpc.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.rpc.RpcListField;
import org.openlegacy.definitions.support.SimpleRpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
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
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.List;

@Component
public class RpcListFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {

		return annotation.annotationType() == RpcListField.class;

	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {

		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;
		RpcListField fieldAnnotation = (RpcListField)annotation;

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(containingClass);

		String fieldName = field.getName();
		Class<?> itemClass = null;
		try {
			// Field item = containingClass.getDeclaredField(fieldName);
			ParameterizedType itemType = (ParameterizedType)field.getGenericType();
			itemClass = (Class<?>)itemType.getActualTypeArguments()[0];

		} catch (SecurityException e) {
			throw (new RegistryException("SecurityException"));
		}

		// look in rpc entities
		if (rpcEntityDefinition != null) {
			SimpleRpcFieldDefinition fieldDefinition = (SimpleRpcFieldDefinition)rpcEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			fillTypeDefinition(fieldAnnotation, fieldDefinition, fieldName, itemClass);
		} else {
			// look in rpc entities parts
			RpcPartEntityDefinition rpcPart = rpcEntitiesRegistry.getPart(containingClass);
			if (rpcPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", rpcPart.getPartName(), fieldName);
				SimpleRpcFieldDefinition fieldDefinition = (SimpleRpcFieldDefinition)rpcPart.getFieldsDefinitions().get(fieldName);
				fillTypeDefinition(fieldAnnotation, fieldDefinition, fieldName, itemClass);
			}

		}

	}

	private static void fillTypeDefinition(RpcListField fieldAnnotation, SimpleRpcFieldDefinition fieldDefinition,
			String fieldName, Class<?> itemClass) {
		Assert.notNull(fieldDefinition,
				MessageFormat.format(
						"Field definition for field {0} not found. Verify @RpcListField is defined along @RpcField annotation",
						fieldName));

		if (fieldDefinition.getJavaType() != List.class) {

			throw (new RegistryException("A field marked with @RpcListField must be List"));
		}
		if (itemClass == String.class) {
			fieldDefinition.setFieldTypeDefinition(new SimpleRpcListFieldTypeDefinition(fieldDefinition.getLength(),
					fieldAnnotation.count(), new SimpleTextFieldTypeDefinition(), itemClass));
		} else {
			fieldDefinition.setFieldTypeDefinition(new SimpleRpcListFieldTypeDefinition(fieldDefinition.getLength(),
					fieldAnnotation.count(), new SimpleRpcNumericFieldTypeDefinition(), itemClass));
		}
	}

}
