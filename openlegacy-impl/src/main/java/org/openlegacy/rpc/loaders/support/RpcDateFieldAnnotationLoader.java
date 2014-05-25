package org.openlegacy.rpc.loaders.support;

import org.openlegacy.annotations.rpc.RpcDateField;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractDateFieldAnnotationLoader;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.definitions.support.SimpleRpcDateFieldTypeDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Date;

@Component
public class RpcDateFieldAnnotationLoader extends AbstractDateFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcDateField.class;
	}

	@Override
	protected void fillTypeDefinition(Annotation annotation, FieldDefinition fieldDefinition, String fieldName) {

		RpcDateField fieldAnnotation = (RpcDateField)annotation;
		SimpleRpcFieldDefinition rpcFieldDefinition = (SimpleRpcFieldDefinition)fieldDefinition;
		Assert.notNull(fieldDefinition, MessageFormat.format(
				"Field definition for field {0} not found. Verify @RpcDateField is defined along @ScreenField annotation",
				fieldName));

		if (fieldDefinition.getJavaType() != Date.class) {
			throw (new RegistryException(MessageFormat.format(
					"Field {0} marked with @RpcDateField must be of type java.util.Date", fieldName)));
		}

		String pattern = fieldAnnotation.pattern();

		rpcFieldDefinition.setFieldTypeDefinition(new SimpleRpcDateFieldTypeDefinition(pattern, fieldAnnotation.locale()));

		rpcFieldDefinition.setJavaType(Date.class);
	}
}
