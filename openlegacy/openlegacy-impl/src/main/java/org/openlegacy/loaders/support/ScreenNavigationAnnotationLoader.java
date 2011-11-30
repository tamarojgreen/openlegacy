package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenNavigationDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class ScreenNavigationAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenNavigationAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenNavigation.class;
	}

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenNavigation screenNavigation = (ScreenNavigation)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		SimpleScreenNavigationDefinition navigationDefinition = new SimpleScreenNavigationDefinition();
		navigationDefinition.setAccessedFrom(screenNavigation.accessedFrom());

		navigationDefinition.setTerminalAction(ReflectionUtil.newInstance(screenNavigation.terminalAction()));
		navigationDefinition.setExitAction(ReflectionUtil.newInstance(screenNavigation.exitAction()));

		AssignedField[] assignedFields = screenNavigation.assignedFields();
		for (AssignedField assignedField : assignedFields) {
			navigationDefinition.getAssignedFields().add(
					new SimpleFieldAssignDefinition(assignedField.field(), assignedField.value()));
		}

		SimpleScreenEntityDefinition screenEntityDefinition = (SimpleScreenEntityDefinition)screenEntitiesRegistry.get(containingClass);
		screenEntityDefinition.setNavigationDefinition(navigationDefinition);
		logger.info("Added screen navigation information to the registry for:" + containingClass);
	}
}
