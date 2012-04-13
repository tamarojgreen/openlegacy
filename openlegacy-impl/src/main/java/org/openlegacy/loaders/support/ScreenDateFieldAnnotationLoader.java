package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;

@Component
public class ScreenDateFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenDateField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, Field field, Annotation annotation,
			Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenDateField fieldAnnotation = (ScreenDateField)annotation;

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

	private static void fillTypeDefinition(ScreenDateField fieldAnnotation, SimpleScreenFieldDefinition fieldDefinition) {
		if (fieldDefinition.getJavaType() != Date.class) {
			throw (new RegistryException("A field marked with @ScreenDateField must be of type java.util.Date"));
		}

		int dayColumn = fieldAnnotation.dayColumn();
		int monthColumn = fieldAnnotation.monthColumn();
		int yearColumn = fieldAnnotation.yearColumn();
		// set to null if default value is 0. null should be used to determine which date fields are enabled/disabled (to pick
		// just month+year for example)
		fieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition(dayColumn > 0 ? dayColumn : null,
				monthColumn > 0 ? monthColumn : null, yearColumn > 0 ? yearColumn : null));
		fieldDefinition.setJavaType(Date.class);
	}

}
