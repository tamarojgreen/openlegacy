package org.openlegacy.loaders.support;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.definitions.support.SimpleBooleanFieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Component
public class ScreenBooleanFieldAnnotationLoader extends AbstractFieldAnnotationLoader{

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenBooleanField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation,
			Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenBooleanField fieldAnnotation = (ScreenBooleanField)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		// look in screen entities
		if (screenEntityDefinition != null) {
			SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition) screenEntityDefinition.getFieldsDefinitions().get(fieldName);
			fieldDefinition.setFieldTypeDefinition(new SimpleBooleanFieldTypeDefinition(fieldAnnotation.trueValue(),fieldAnnotation.falseValue()));
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition) screenPart.getFieldsDefinitions().get(fieldName);
				fieldDefinition.setFieldTypeDefinition(new SimpleBooleanFieldTypeDefinition(fieldAnnotation.trueValue(),fieldAnnotation.falseValue()));
			}

		}

	}

}
