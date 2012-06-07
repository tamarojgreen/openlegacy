package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimplePasswordFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScreenFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenField.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, Field field, Annotation annotation,
			Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenField fieldAnnotation = (ScreenField)annotation;

		SimpleTerminalPosition position = SimpleTerminalPosition.newInstance(fieldAnnotation.row(), fieldAnnotation.column());
		String fieldName = field.getName();
		SimpleScreenFieldDefinition screenFieldDefinition = new SimpleScreenFieldDefinition(fieldName,
				fieldAnnotation.fieldType());
		screenFieldDefinition.setPosition(position);

		if (fieldAnnotation.endColumn() == 0) {
			screenFieldDefinition.setLength(0);
		} else {
			screenFieldDefinition.setLength(fieldAnnotation.endColumn() - fieldAnnotation.column() + 1);
		}

		if (fieldAnnotation.labelColumn() > 0) {
			SimpleTerminalPosition labelPosition = SimpleTerminalPosition.newInstance(fieldAnnotation.row(),
					fieldAnnotation.labelColumn());
			screenFieldDefinition.setLabelPosition(labelPosition);
		}

		screenFieldDefinition.setEditable(fieldAnnotation.editable());
		screenFieldDefinition.setPassword(fieldAnnotation.password());

		if (fieldAnnotation.displayName().length() > 0) {
			screenFieldDefinition.setDisplayName(fieldAnnotation.displayName());
		} else {
			screenFieldDefinition.setDisplayName(StringUtil.toDisplayName(fieldName));
		}

		screenFieldDefinition.setSampleValue(fieldAnnotation.sampleValue());
		screenFieldDefinition.setJavaType(field.getType());

		screenFieldDefinition.setKey(fieldAnnotation.key());

		setupFieldType(field, screenFieldDefinition);

		EntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		// look in screen entities
		if (screenEntityDefinition != null) {
			screenEntityDefinition.getFieldsDefinitions().put(fieldName, screenFieldDefinition);
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				screenFieldDefinition.setName(fieldName);
				screenPart.getFieldsDefinitions().put(fieldName, screenFieldDefinition);
			}

		}

	}

	private static void setupFieldType(Field field, SimpleScreenFieldDefinition screenFieldDefinition) {
		// set number type definition - may be overridden by ScreenNumericFieldAnnotationLoader to fill in specific numeric
		// properties
		if (Number.class.isAssignableFrom(field.getType())) {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleNumericFieldTypeDefinition());
		}
		// set date type definition - may be overridden by ScreenDateFieldAnnotationLoader to fill in specific date properties
		else if (Date.class.isAssignableFrom(field.getType())) {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition());
		} else if (screenFieldDefinition.isPassword()) {
			screenFieldDefinition.setFieldTypeDefinition(new SimplePasswordFieldTypeDefinition());
		} else {
			// set a default type definition
			screenFieldDefinition.setFieldTypeDefinition(new SimpleTextFieldTypeDefinition());
		}
	}

}
