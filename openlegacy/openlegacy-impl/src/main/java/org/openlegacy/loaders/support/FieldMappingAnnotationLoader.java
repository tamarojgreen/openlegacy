package org.openlegacy.loaders.support;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.HostEntityDefinition;
import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldMappingDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class FieldMappingAnnotationLoader implements FieldAnnotationsLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == FieldMapping.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void load(HostEntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		FieldMapping fieldAnnotation = (FieldMapping)annotation;

		SimpleScreenPosition screenPosition = SimpleScreenPosition.newInstance(fieldAnnotation.row(), fieldAnnotation.column());
		SimpleFieldMappingDefinition fieldMappingDefinition = new SimpleFieldMappingDefinition(fieldName,
				fieldAnnotation.fieldType());
		fieldMappingDefinition.setScreenPosition(screenPosition);
		fieldMappingDefinition.setLength(fieldAnnotation.length());
		fieldMappingDefinition.setEditable(fieldAnnotation.editable());

		HostEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		// look in screen entities
		if (screenEntityDefinition != null) {
			screenEntityDefinition.getFieldsDefinitions().put(fieldName, fieldMappingDefinition);
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPartEntityDefinition = screenEntitiesRegistry.getPart(containingClass);
			if (screenPartEntityDefinition != null) {
				screenPartEntityDefinition.getFieldsDefinitions().put(fieldName, fieldMappingDefinition);
			}

		}

	}

}
