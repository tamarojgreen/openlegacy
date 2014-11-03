package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

public abstract class AbstractDateFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	protected abstract void fillTypeDefinition(Annotation fieldAnnotation, FieldDefinition fieldDefinition, String fieldName);

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {

		EntityDefinition entityDefinition = entitiesRegistry.get(containingClass);
		String fieldName = field.getName();
		// look in screen entities
		if (entityDefinition != null) {
			FieldDefinition fieldDefinition = (FieldDefinition)entityDefinition.getFieldsDefinitions().get(fieldName);
			fillTypeDefinition(annotation, fieldDefinition, fieldName);
		} else {
			// look in screen entities parts
			PartEntityDefinition part = entitiesRegistry.getPart(containingClass);
			if (part != null) {
				fieldName = MessageFormat.format("{0}.{1}", part.getPartName(), fieldName);
				FieldDefinition fieldDefinition = (FieldDefinition)part.getFieldsDefinitions().get(fieldName);
				fillTypeDefinition(annotation, fieldDefinition, fieldName);
			}

		}

	}

}
