package org.openlegacy.loaders.support;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.annotations.screen.ChildScreenEntity;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleChildScreenDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class ChildScreenAnnotationLoader implements FieldAnnotationsLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ChildScreenEntity.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(HostEntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		ChildScreenEntity childScreenEntityAnnotation = (ChildScreenEntity)annotation;

		SimpleChildScreenDefinition ChildScreenDefinition = new SimpleChildScreenDefinition(fieldName);
		ChildScreenDefinition.setFetchMode(childScreenEntityAnnotation.fetchMode());
		ChildScreenDefinition.setStepInto(childScreenEntityAnnotation.stepInto());

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		screenEntityDefinition.getChildScreenDefinitions().put(fieldName, ChildScreenDefinition);
	}

}
