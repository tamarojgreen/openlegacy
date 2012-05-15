package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenNumericField;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

@Component
public class ScreenNumericFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenNumericField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, Field field, Annotation annotation,
			Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenNumericField fieldAnnotation = (ScreenNumericField)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		String fieldName = field.getName();

		// look in screen entities
		if (screenEntityDefinition != null) {
			SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			fillTypeDefinition(fieldAnnotation, fieldDefinition);
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenPart.getFieldsDefinitions().get(
						fieldName);
				fillTypeDefinition(fieldAnnotation, fieldDefinition);
			}

		}

	}

	private static void fillTypeDefinition(ScreenNumericField fieldAnnotation, SimpleScreenFieldDefinition fieldDefinition) {
		fieldDefinition.setFieldTypeDefinition(new SimpleNumericFieldTypeDefinition(fieldAnnotation.minimumValue(),
				fieldAnnotation.maximumValue()));
	}

}
